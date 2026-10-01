package com.fulfilment.application.monolith.warehouses.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.validators.WarehouseReplacementValidator;
import org.junit.jupiter.api.Test;

public class ReplaceWarehouseUseCaseTest {
  @Test void archivesOldWarehouseAndCreatesReplacement() {
    var store = new CreateWarehouseUseCaseTest.InMemoryStore();
    var old = CreateWarehouseUseCaseTest.warehouse("BU-1", "A", 20, 10); old.id = 1L; store.warehouses.add(old);
    var archive = new ArchiveWarehouseUseCase(store);
    var create = new CreateWarehouseUseCase(store, CreateWarehouseUseCaseTest.validator(store, new Location("A", 1, 100)));
    var replacement = CreateWarehouseUseCaseTest.warehouse("BU-1", "A", 30, 10);
    new ReplaceWarehouseUseCase(store, archive, create, new WarehouseReplacementValidator()).replace(replacement);
    assertNotNull(old.archivedAt);
    assertNotNull(replacement.id);
  }

  @Test void rejectsMismatchedLocationStockAndMissingWarehouse() {
    var store = new CreateWarehouseUseCaseTest.InMemoryStore();
    var old = CreateWarehouseUseCaseTest.warehouse("BU-1", "A", 20, 10); old.id = 1L; store.warehouses.add(old);
    var archive = new ArchiveWarehouseUseCase(store);
    var create = new CreateWarehouseUseCase(store, CreateWarehouseUseCaseTest.validator(store, new Location("A", 2, 100)));
    var useCase = new ReplaceWarehouseUseCase(store, archive, create, new WarehouseReplacementValidator());
    assertThrows(IllegalArgumentException.class, () -> useCase.replace(CreateWarehouseUseCaseTest.warehouse("BU-1", "B", 20, 10)));
    assertThrows(IllegalArgumentException.class, () -> useCase.replace(CreateWarehouseUseCaseTest.warehouse("BU-1", "A", 20, 9)));
    assertThrows(IllegalArgumentException.class, () -> useCase.replace(CreateWarehouseUseCaseTest.warehouse("OTHER", "A", 20, 10)));
  }
}
