package com.fulfilment.application.monolith.fulfilment.domain.ports;

/** Lookup boundary for Store, Product, and active Warehouse existence checks. */
public interface FulfilmentParticipantLookup {
  boolean storeExists(Long storeId);
  boolean productExists(Long productId);
  boolean activeWarehouseExists(Long warehouseId);
}
