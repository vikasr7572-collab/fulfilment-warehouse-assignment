package com.fulfilment.application.monolith.warehouses.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class CreateWarehouseUseCaseTest {
  @Test void createsValidWarehouse() {
    var store = new InMemoryStore();
    var useCase = new CreateWarehouseUseCase(store, resolver(new Location("A", 2, 100)));
    assertDoesNotThrow(() -> useCase.create(warehouse("BU-1", "A", 60, 30)));
  }

  @Test void rejectsDuplicateCodeInvalidLocationAndInvalidStock() {
    var store = new InMemoryStore();
    store.warehouses.add(warehouse("BU-1", "A", 30, 10));
    var useCase = new CreateWarehouseUseCase(store, resolver(new Location("A", 2, 100)));
    assertThrows(IllegalArgumentException.class, () -> useCase.create(warehouse("BU-1", "A", 20, 10)));
    assertThrows(IllegalArgumentException.class, () -> useCase.create(warehouse("BU-2", "B", 20, 10)));
    assertThrows(IllegalArgumentException.class, () -> useCase.create(warehouse("BU-2", "A", 10, 11)));
  }

  @Test void rejectsLocationCountAndCapacityExcess() {
    var store = new InMemoryStore();
    store.warehouses.add(warehouse("BU-1", "A", 60, 10));
    var oneSlot = new CreateWarehouseUseCase(store, resolver(new Location("A", 1, 100)));
    assertThrows(IllegalArgumentException.class, () -> oneSlot.create(warehouse("BU-2", "A", 20, 10)));
    var capacity = new CreateWarehouseUseCase(store, resolver(new Location("A", 2, 70)));
    assertThrows(IllegalArgumentException.class, () -> capacity.create(warehouse("BU-2", "A", 20, 10)));
  }

  static Warehouse warehouse(String code, String location, int capacity, int stock) {
    var warehouse = new Warehouse(); warehouse.businessUnitCode = code; warehouse.location = location;
    warehouse.capacity = capacity; warehouse.stock = stock; return warehouse;
  }
  static LocationResolver resolver(Location location) { return id -> location.identification.equals(id) ? location : null; }
  static class InMemoryStore implements WarehouseStore {
    final List<Warehouse> warehouses = new ArrayList<>();
    public List<Warehouse> getAll() { return warehouses.stream().filter(w -> w.archivedAt == null).toList(); }
    public void create(Warehouse warehouse) { warehouse.id = (long) warehouses.size() + 1; warehouses.add(warehouse); }
    public void update(Warehouse warehouse) {}
    public void remove(Warehouse warehouse) { warehouses.remove(warehouse); }
    public Warehouse findByBusinessUnitCode(String code) { return warehouses.stream().filter(w -> w.businessUnitCode.equals(code) && w.archivedAt == null).findFirst().orElse(null); }
  }
}
