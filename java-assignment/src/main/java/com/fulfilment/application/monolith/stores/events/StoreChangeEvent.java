package com.fulfilment.application.monolith.stores.events;

import com.fulfilment.application.monolith.stores.Store;

/** Domain event emitted inside the Store transaction. */
public record StoreChangeEvent(StoreChangeType type, Store store) {}
