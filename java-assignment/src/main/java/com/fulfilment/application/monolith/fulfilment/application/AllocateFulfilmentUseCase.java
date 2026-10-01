package com.fulfilment.application.monolith.fulfilment.application;

import com.fulfilment.application.monolith.fulfilment.domain.model.FulfilmentAllocation;
import com.fulfilment.application.monolith.fulfilment.domain.ports.FulfilmentAllocationStore;
import com.fulfilment.application.monolith.fulfilment.domain.validators.FulfilmentAllocationValidator;
import jakarta.enterprise.context.ApplicationScoped;

/** Application use case coordinating allocation validation and persistence. */
@ApplicationScoped
public class AllocateFulfilmentUseCase {
  private final FulfilmentAllocationStore allocationStore;
  private final FulfilmentAllocationValidator allocationValidator;

  public AllocateFulfilmentUseCase(FulfilmentAllocationStore allocationStore,
      FulfilmentAllocationValidator allocationValidator) {
    this.allocationStore = allocationStore;
    this.allocationValidator = allocationValidator;
  }

  public void allocate(FulfilmentAllocation allocation) {
    allocationValidator.validate(allocation);
    allocationStore.create(allocation);
  }
}
