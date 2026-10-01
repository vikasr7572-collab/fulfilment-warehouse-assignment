package com.fulfilment.application.monolith.fulfilment.adapters.database;

import com.fulfilment.application.monolith.fulfilment.domain.ports.FulfilmentParticipantLookup;
import com.fulfilment.application.monolith.products.ProductRepository;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/** Database adapter for allocation participant lookups. */
@ApplicationScoped
public class FulfilmentParticipantJpaAdapter implements FulfilmentParticipantLookup {
  @Inject ProductRepository products;
  @Inject WarehouseRepository warehouses;

  @Override
  public boolean storeExists(Long storeId) {
    return storeId != null && Store.findById(storeId) != null;
  }

  @Override
  public boolean productExists(Long productId) {
    return productId != null && products.findById(productId) != null;
  }

  @Override
  public boolean activeWarehouseExists(Long warehouseId) {
    return warehouseId != null && warehouses.findActiveById(warehouseId) != null;
  }
}
