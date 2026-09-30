package com.fulfilment.application.monolith.fulfilment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

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
