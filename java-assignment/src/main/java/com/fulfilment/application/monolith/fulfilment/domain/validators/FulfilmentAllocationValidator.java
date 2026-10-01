package com.fulfilment.application.monolith.fulfilment.domain.validators;

import com.fulfilment.application.monolith.fulfilment.domain.model.FulfilmentAllocation;
import com.fulfilment.application.monolith.fulfilment.domain.ports.FulfilmentAllocationStore;
import com.fulfilment.application.monolith.fulfilment.domain.ports.FulfilmentParticipantLookup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;

/** Centralizes all bonus fulfilment allocation constraints. */
@ApplicationScoped
public class FulfilmentAllocationValidator {
  private final FulfilmentAllocationStore allocationStore;
  private final FulfilmentParticipantLookup participantLookup;

  public FulfilmentAllocationValidator(FulfilmentAllocationStore allocationStore,
      FulfilmentParticipantLookup participantLookup) {
    this.allocationStore = allocationStore;
    this.participantLookup = participantLookup;
  }

  public void validate(FulfilmentAllocation allocation) {
    if (allocation == null || allocation.storeId == null || allocation.productId == null
        || allocation.warehouseId == null) {
      throw new BadRequestException("storeId, productId and warehouseId are required.");
    }
    if (!participantLookup.storeExists(allocation.storeId)
        || !participantLookup.productExists(allocation.productId)
        || !participantLookup.activeWarehouseExists(allocation.warehouseId)) {
      throw new BadRequestException("Store, product and active warehouse must exist.");
    }
    if (allocationStore.exists(allocation.storeId, allocation.productId, allocation.warehouseId)) {
      throw new BadRequestException("This fulfilment allocation already exists.");
    }
    if (allocationStore.warehousesForProductAtStore(allocation.productId, allocation.storeId) >= 2) {
      throw new BadRequestException("A product can use at most two warehouses per store.");
    }
    if (allocationStore.warehousesForStore(allocation.storeId) >= 3) {
      throw new BadRequestException("A store can use at most three warehouses.");
    }
    if (allocationStore.productsAtWarehouse(allocation.warehouseId) >= 5) {
      throw new BadRequestException("A warehouse can store at most five product types.");
    }
  }
}
