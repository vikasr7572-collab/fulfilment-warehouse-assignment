package com.fulfilment.application.monolith.stores.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fulfilment.application.monolith.stores.LegacyStoreManagerGateway;
import com.fulfilment.application.monolith.stores.Store;
import org.junit.jupiter.api.Test;

class LegacyStoreSynchronizationObserverTest {

  @Test
  void routesCreatedAndUpdatedEventsToTheCorrectLegacyOperation() {
    RecordingGateway gateway = new RecordingGateway();
    LegacyStoreSynchronizationObserver observer = new LegacyStoreSynchronizationObserver();
    observer.legacyStoreManagerGateway = gateway;
    Store store = new Store("EVENT-STORE");
    store.quantityProductsInStock = 1;

    observer.synchronize(new StoreChangeEvent(StoreChangeType.CREATED, store));
    observer.synchronize(new StoreChangeEvent(StoreChangeType.UPDATED, store));

    assertEquals(1, gateway.creates);
    assertEquals(1, gateway.updates);
  }

  private static final class RecordingGateway extends LegacyStoreManagerGateway {
    int creates;
    int updates;
    @Override public void createStoreOnLegacySystem(Store store) { creates++; }
    @Override public void updateStoreOnLegacySystem(Store store) { updates++; }
  }
}
