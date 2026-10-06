package com.fulfilment.application.monolith.fulfilment.domain.validators;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fulfilment.application.monolith.fulfilment.domain.model.FulfilmentAllocation;
import com.fulfilment.application.monolith.fulfilment.domain.ports.FulfilmentAllocationStore;
import com.fulfilment.application.monolith.fulfilment.domain.ports.FulfilmentParticipantLookup;
import jakarta.ws.rs.BadRequestException;
import org.junit.jupiter.api.Test;

class FulfilmentAllocationValidatorTest {

  @Test
  void acceptsAValidAllocation() {
    assertDoesNotThrow(() -> validator(new Store(), new Participants()).validate(allocation()));
  }

  @Test
  void rejectsMissingOrUnknownParticipantsAndDuplicates() {
    assertThrows(BadRequestException.class, () -> validator(new Store(), new Participants()).validate(null));
    Participants missingStore = new Participants();
    missingStore.storeExists = false;
    assertThrows(BadRequestException.class, () -> validator(new Store(), missingStore).validate(allocation()));
    Participants missingProduct = new Participants();
    missingProduct.productExists = false;
    assertThrows(BadRequestException.class, () -> validator(new Store(), missingProduct).validate(allocation()));
    Participants missingWarehouse = new Participants();
    missingWarehouse.warehouseExists = false;
    assertThrows(BadRequestException.class, () -> validator(new Store(), missingWarehouse).validate(allocation()));
    Store duplicate = new Store();
    duplicate.exists = true;
    assertThrows(BadRequestException.class, () -> validator(duplicate, new Participants()).validate(allocation()));
  }

  @Test
  void enforcesAllBonusLimits() {
    Store perProductLimit = new Store();
    perProductLimit.warehousesForProductAtStore = 2;
    assertThrows(BadRequestException.class, () -> validator(perProductLimit, new Participants()).validate(allocation()));

    Store perStoreLimit = new Store();
    perStoreLimit.warehousesForStore = 3;
    assertThrows(BadRequestException.class, () -> validator(perStoreLimit, new Participants()).validate(allocation()));

    Store warehouseLimit = new Store();
    warehouseLimit.productsAtWarehouse = 5;
    assertThrows(BadRequestException.class, () -> validator(warehouseLimit, new Participants()).validate(allocation()));
  }

  private static FulfilmentAllocationValidator validator(Store store, Participants participants) {
    return new FulfilmentAllocationValidator(store, participants);
  }

  private static FulfilmentAllocation allocation() {
    return new FulfilmentAllocation(1L, 2L, 3L);
  }

  private static final class Participants implements FulfilmentParticipantLookup {
    boolean storeExists = true;
    boolean productExists = true;
    boolean warehouseExists = true;
    public boolean storeExists(Long storeId) { return storeExists; }
    public boolean productExists(Long productId) { return productExists; }
    public boolean activeWarehouseExists(Long warehouseId) { return warehouseExists; }
  }

  private static final class Store implements FulfilmentAllocationStore {
    boolean exists;
    long warehousesForProductAtStore;
    long warehousesForStore;
    long productsAtWarehouse;
    public long warehousesForProductAtStore(Long productId, Long storeId) { return warehousesForProductAtStore; }
    public long warehousesForStore(Long storeId) { return warehousesForStore; }
    public long productsAtWarehouse(Long warehouseId) { return productsAtWarehouse; }
    public boolean exists(Long storeId, Long productId, Long warehouseId) { return exists; }
    public void create(FulfilmentAllocation allocation) {}
  }
}
