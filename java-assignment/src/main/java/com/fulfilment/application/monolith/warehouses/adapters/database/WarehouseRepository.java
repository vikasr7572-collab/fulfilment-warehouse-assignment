package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class WarehouseRepository implements WarehouseStore, PanacheRepository<DbWarehouse> {

  @Override
  public List<Warehouse> getAll() {
    return find("archivedAt is null").list().stream().map(DbWarehouse::toWarehouse).toList();
  }

  @Override
  public void create(Warehouse warehouse) {
    DbWarehouse entity = new DbWarehouse();
    copy(warehouse, entity);
    persist(entity);
    warehouse.id = entity.id;
  }

  @Override
  public void update(Warehouse warehouse) {
    if (warehouse.id == null) {
      throw new IllegalArgumentException("Warehouse id is required for an update.");
    }
    DbWarehouse entity = findByIdOptional(warehouse.id)
        .orElseThrow(() -> new IllegalArgumentException("Warehouse does not exist."));
    copy(warehouse, entity);
  }

  @Override
  public void remove(Warehouse warehouse) {
    if (warehouse.id != null) {
      deleteById(warehouse.id);
    }
  }

  @Override
  public Warehouse findByBusinessUnitCode(String buCode) {
    if (buCode == null) {
      return null;
    }
    return find("businessUnitCode = ?1 and archivedAt is null", buCode).firstResultOptional()
        .map(DbWarehouse::toWarehouse).orElse(null);
  }

  public Warehouse findActiveById(Long id) {
    if (id == null) return null;
    return find("id = ?1 and archivedAt is null", id).firstResultOptional()
        .map(DbWarehouse::toWarehouse).orElse(null);
  }

  public DbWarehouse findActiveEntityByBusinessUnitCode(String businessUnitCode) {
    return find("businessUnitCode = ?1 and archivedAt is null", businessUnitCode)
        .firstResult();
  }

  private void copy(Warehouse source, DbWarehouse target) {
    target.businessUnitCode = source.businessUnitCode;
    target.location = source.location;
    target.capacity = source.capacity;
    target.stock = source.stock;
    target.createdAt = source.createdAt;
    target.archivedAt = source.archivedAt;
  }
}
