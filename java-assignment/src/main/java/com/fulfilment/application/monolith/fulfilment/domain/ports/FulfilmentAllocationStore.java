package com.fulfilment.application.monolith.fulfilment.domain.ports;

import com.fulfilment.application.monolith.fulfilment.domain.model.FulfilmentAllocation;

/** Persistence boundary for allocation business rules. */
public interface FulfilmentAllocationStore {
  long warehousesForProductAtStore(Long productId, Long storeId);
  long warehousesForStore(Long storeId);
  long productsAtWarehouse(Long warehouseId);
  boolean exists(Long storeId, Long productId, Long warehouseId);
  void create(FulfilmentAllocation allocation);
}
