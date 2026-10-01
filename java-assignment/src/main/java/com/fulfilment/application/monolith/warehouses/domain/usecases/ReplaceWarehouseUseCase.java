package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import com.fulfilment.application.monolith.warehouses.domain.ports.ArchiveWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.validators.WarehouseReplacementValidator;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReplaceWarehouseUseCase implements ReplaceWarehouseOperation {

  private final WarehouseStore warehouseStore;
  private final ArchiveWarehouseOperation archiveWarehouse;
  private final CreateWarehouseOperation createWarehouse;
  private final WarehouseReplacementValidator warehouseReplacementValidator;

  public ReplaceWarehouseUseCase(WarehouseStore warehouseStore,
      ArchiveWarehouseOperation archiveWarehouse, CreateWarehouseOperation createWarehouse,
      WarehouseReplacementValidator warehouseReplacementValidator) {
    this.warehouseStore = warehouseStore;
    this.archiveWarehouse = archiveWarehouse;
    this.createWarehouse = createWarehouse;
    this.warehouseReplacementValidator = warehouseReplacementValidator;
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
    warehouseReplacementValidator.validate(previous, newWarehouse);

    archiveWarehouse.archive(previous);
    createWarehouse.create(newWarehouse);
  }
}
