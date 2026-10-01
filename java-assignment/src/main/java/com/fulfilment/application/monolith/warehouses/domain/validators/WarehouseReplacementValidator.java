package com.fulfilment.application.monolith.warehouses.domain.validators;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import jakarta.enterprise.context.ApplicationScoped;

/** Validates invariants that must hold when an active warehouse is replaced. */
@ApplicationScoped
public class WarehouseReplacementValidator {
  public void validate(Warehouse previous, Warehouse replacement) {
    if (!previous.location.equals(replacement.location)) {
      throw new IllegalArgumentException("Replacement warehouse must be in the same location.");
    }
    if (!previous.stock.equals(replacement.stock)) {
      throw new IllegalArgumentException("Replacement warehouse stock must match the existing warehouse stock.");
    }
    if (replacement.capacity == null || replacement.capacity < previous.stock) {
      throw new IllegalArgumentException("Replacement warehouse capacity cannot accommodate existing stock.");
    }
  }
}
