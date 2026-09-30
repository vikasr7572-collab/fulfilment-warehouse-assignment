package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@ApplicationScoped
public class CreateWarehouseUseCase implements CreateWarehouseOperation {

  private final WarehouseStore warehouseStore;
  private final LocationResolver locationResolver;

  public CreateWarehouseUseCase(WarehouseStore warehouseStore, LocationResolver locationResolver) {
    this.warehouseStore = warehouseStore;
    this.locationResolver = locationResolver;
  }

  @Override
  public void create(Warehouse warehouse) {
    validateRequiredFields(warehouse);
    if (warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode) != null) {
      throw new IllegalArgumentException("Business unit code already belongs to an active warehouse.");
    }

    var location = locationResolver.resolveByIdentifier(warehouse.location);
    if (location == null) {
      throw new IllegalArgumentException("Warehouse location does not exist.");
    }
    warehouse.location = location.identification;

    var activeAtLocation = warehouseStore.getAll().stream()
        .filter(existing -> location.identification.equals(existing.location)).toList();
    if (activeAtLocation.size() >= location.maxNumberOfWarehouses) {
      throw new IllegalArgumentException("The maximum number of warehouses for this location has been reached.");
    }
    int usedCapacity = activeAtLocation.stream().mapToInt(existing -> existing.capacity).sum();
    if (usedCapacity + warehouse.capacity > location.maxCapacity) {
      throw new IllegalArgumentException("Warehouse capacity exceeds the location capacity limit.");
    }

    warehouse.createdAt = LocalDateTime.now();
    warehouse.archivedAt = null;

    warehouseStore.create(warehouse);
  }

  private void validateRequiredFields(Warehouse warehouse) {
    if (warehouse == null || warehouse.businessUnitCode == null || warehouse.businessUnitCode.isBlank()
        || warehouse.location == null || warehouse.location.isBlank()
        || warehouse.capacity == null || warehouse.stock == null) {
      throw new IllegalArgumentException("Business unit code, location, capacity and stock are required.");
    }
    if (warehouse.capacity < 0 || warehouse.stock < 0 || warehouse.stock > warehouse.capacity) {
      throw new IllegalArgumentException("Capacity and stock must be non-negative and stock cannot exceed capacity.");
    }
  }
}
