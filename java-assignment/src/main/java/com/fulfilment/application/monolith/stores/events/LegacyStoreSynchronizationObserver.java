package com.fulfilment.application.monolith.stores.events;

import com.fulfilment.application.monolith.stores.LegacyStoreManagerGateway;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.inject.Inject;

/** Sends to the legacy system only after the Store database transaction succeeds. */
@ApplicationScoped
public class LegacyStoreSynchronizationObserver {
  @Inject LegacyStoreManagerGateway legacyStoreManagerGateway;

  void synchronize(@Observes(during = TransactionPhase.AFTER_SUCCESS) StoreChangeEvent event) {
    if (event.type() == StoreChangeType.CREATED) {
      legacyStoreManagerGateway.createStoreOnLegacySystem(event.store());
    } else {
      legacyStoreManagerGateway.updateStoreOnLegacySystem(event.store());
    }
  }
}
