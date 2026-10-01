package com.fulfilment.application.monolith.fulfilment.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/** A warehouse selected to fulfil a product for a store. */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"storeId", "productId", "warehouseId"}))
public class FulfilmentAllocation {
  @Id @GeneratedValue public Long id;
  public Long storeId;
  public Long productId;
  public Long warehouseId;

  public FulfilmentAllocation() {}

  public FulfilmentAllocation(Long storeId, Long productId, Long warehouseId) {
    this.storeId = storeId;
    this.productId = productId;
    this.warehouseId = warehouseId;
  }
}
