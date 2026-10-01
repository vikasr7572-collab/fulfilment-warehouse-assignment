package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import com.fulfilment.application.monolith.warehouses.domain.validators.WarehouseCreationValidator;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@ApplicationScoped
public class CreateWarehouseUseCase implements CreateWarehouseOperation {

  private final WarehouseStore warehouseStore;
  private final WarehouseCreationValidator warehouseCreationValidator;

  public CreateWarehouseUseCase(WarehouseStore warehouseStore,
      WarehouseCreationValidator warehouseCreationValidator) {
    this.warehouseStore = warehouseStore;
    this.warehouseCreationValidator = warehouseCreationValidator;
  }

  @Override
  public void create(Warehouse warehouse) {
    var location = warehouseCreationValidator.validate(warehouse);
    warehouse.location = location.identification;

    warehouse.createdAt = LocalDateTime.now();
    warehouse.archivedAt = null;

    warehouseStore.create(warehouse);
  }
}
