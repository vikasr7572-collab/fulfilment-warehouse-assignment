package com.fulfilment.application.monolith.fulfilment;

import com.fulfilment.application.monolith.products.ProductRepository;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

/** Assigns an active warehouse to fulfill a product for a store. */
@Path("fulfilment-allocation")
@ApplicationScoped
@Consumes("application/json")
@Produces("application/json")
public class FulfilmentAllocationResource {
  @Inject FulfilmentAllocationRepository allocations;
  @Inject ProductRepository products;
  @Inject WarehouseRepository warehouses;

  @POST
  @Transactional
  public Response create(FulfilmentAllocation allocation) {
    if (allocation == null || allocation.storeId == null || allocation.productId == null || allocation.warehouseId == null) {
      throw new BadRequestException("storeId, productId and warehouseId are required.");
    }
    if (Store.findById(allocation.storeId) == null || products.findById(allocation.productId) == null
        || warehouses.findActiveById(allocation.warehouseId) == null) {
      throw new BadRequestException("Store, product and active warehouse must exist.");
    }
    if (allocations.exists(allocation.storeId, allocation.productId, allocation.warehouseId)) {
      throw new BadRequestException("This fulfilment allocation already exists.");
    }
    if (allocations.warehousesForProductAtStore(allocation.productId, allocation.storeId) >= 2) {
      throw new BadRequestException("A product can use at most two warehouses per store.");
    }
    if (allocations.warehousesForStore(allocation.storeId) >= 3) {
      throw new BadRequestException("A store can use at most three warehouses.");
    }
    if (allocations.productsAtWarehouse(allocation.warehouseId) >= 5) {
      throw new BadRequestException("A warehouse can store at most five product types.");
    }
    allocations.persist(allocation);
    return Response.status(Response.Status.CREATED).entity(allocation).build();
  }
}
