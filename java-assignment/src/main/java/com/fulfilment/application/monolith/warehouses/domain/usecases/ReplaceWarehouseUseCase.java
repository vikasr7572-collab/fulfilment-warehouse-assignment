package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import com.fulfilment.application.monolith.warehouses.domain.ports.ArchiveWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReplaceWarehouseUseCase implements ReplaceWarehouseOperation {

  private final WarehouseStore warehouseStore;
  private final ArchiveWarehouseOperation archiveWarehouse;
  private final CreateWarehouseOperation createWarehouse;

  public ReplaceWarehouseUseCase(WarehouseStore warehouseStore,
      ArchiveWarehouseOperation archiveWarehouse, CreateWarehouseOperation createWarehouse) {
    this.warehouseStore = warehouseStore;
    this.archiveWarehouse = archiveWarehouse;
    this.createWarehouse = createWarehouse;
  }

  @Override
  public void replace(Warehouse newWarehouse) {
    if (newWarehouse == null || newWarehouse.businessUnitCode == null) {
      throw new IllegalArgumentException("A business unit code is required for replacement.");
    }
    Warehouse previous = warehouseStore.findByBusinessUnitCode(newWarehouse.businessUnitCode);
    if (previous == null) {
      throw new IllegalArgumentException("No active warehouse exists for this business unit code.");
    }
    if (!previous.location.equals(newWarehouse.location)) {
      throw new IllegalArgumentException("Replacement warehouse must be in the same location.");
    }
    if (!previous.stock.equals(newWarehouse.stock)) {
      throw new IllegalArgumentException("Replacement warehouse stock must match the existing warehouse stock.");
    }
    if (newWarehouse.capacity == null || newWarehouse.capacity < previous.stock) {
      throw new IllegalArgumentException("Replacement warehouse capacity cannot accommodate existing stock.");
    }

    archiveWarehouse.archive(previous);
    createWarehouse.create(newWarehouse);
  }
}
