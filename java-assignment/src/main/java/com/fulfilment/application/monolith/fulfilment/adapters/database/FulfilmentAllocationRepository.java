package com.fulfilment.application.monolith.fulfilment.adapters.database;

import com.fulfilment.application.monolith.fulfilment.domain.model.FulfilmentAllocation;
import com.fulfilment.application.monolith.fulfilment.domain.ports.FulfilmentAllocationStore;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/** JPA adapter implementing the allocation persistence port. */
@ApplicationScoped
public class FulfilmentAllocationRepository
    implements PanacheRepository<FulfilmentAllocation>, FulfilmentAllocationStore {

  @Override
  public long warehousesForProductAtStore(Long productId, Long storeId) {
    return count("select count(distinct warehouseId) from FulfilmentAllocation where productId = ?1 and storeId = ?2",
        productId, storeId);
  }

  @Override
  public long warehousesForStore(Long storeId) {
    return count("select count(distinct warehouseId) from FulfilmentAllocation where storeId = ?1", storeId);
  }

  @Override
  public long productsAtWarehouse(Long warehouseId) {
    return count("select count(distinct productId) from FulfilmentAllocation where warehouseId = ?1", warehouseId);
  }

  @Override
  public boolean exists(Long storeId, Long productId, Long warehouseId) {
    return count("storeId = ?1 and productId = ?2 and warehouseId = ?3", storeId, productId, warehouseId) > 0;
  }

  @Override
  public void create(FulfilmentAllocation allocation) {
    persist(allocation);
  }
}
