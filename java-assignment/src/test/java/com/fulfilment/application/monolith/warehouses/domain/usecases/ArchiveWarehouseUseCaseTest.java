package com.fulfilment.application.monolith.warehouses.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ArchiveWarehouseUseCaseTest {
  @Test void archivesActiveWarehouse() {
    var store = new CreateWarehouseUseCaseTest.InMemoryStore();
    var warehouse = CreateWarehouseUseCaseTest.warehouse("BU-1", "A", 10, 2); warehouse.id = 1L;
    new ArchiveWarehouseUseCase(store).archive(warehouse);
    assertNotNull(warehouse.archivedAt);
  }
  @Test void rejectsMissingOrAlreadyArchivedWarehouse() {
    var useCase = new ArchiveWarehouseUseCase(new CreateWarehouseUseCaseTest.InMemoryStore());
    assertThrows(IllegalArgumentException.class, () -> useCase.archive(null));
  }
}
