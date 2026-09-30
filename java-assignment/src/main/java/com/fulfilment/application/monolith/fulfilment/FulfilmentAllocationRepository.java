package com.fulfilment.application.monolith.fulfilment;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FulfilmentAllocationRepository implements PanacheRepository<FulfilmentAllocation> {
  public long warehousesForProductAtStore(Long productId, Long storeId) {
    return count("select count(distinct warehouseId) from FulfilmentAllocation where productId = ?1 and storeId = ?2", productId, storeId);
  }
  public long warehousesForStore(Long storeId) {
    return count("select count(distinct warehouseId) from FulfilmentAllocation where storeId = ?1", storeId);
  }
  public long productsAtWarehouse(Long warehouseId) {
    return count("select count(distinct productId) from FulfilmentAllocation where warehouseId = ?1", warehouseId);
  }
  public boolean exists(Long storeId, Long productId, Long warehouseId) {
    return count("storeId = ?1 and productId = ?2 and warehouseId = ?3", storeId, productId, warehouseId) > 0;
  }
}
