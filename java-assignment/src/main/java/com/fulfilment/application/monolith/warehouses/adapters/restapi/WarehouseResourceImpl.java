package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.ports.ArchiveWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import com.warehouse.api.beans.Warehouse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import java.util.List;

@RequestScoped
@Path("/warehouse")
@Produces("application/json")
@Consumes("application/json")
public class WarehouseResourceImpl {

  @Inject private WarehouseRepository warehouseRepository;
  @Inject private CreateWarehouseOperation createWarehouse;
  @Inject private ArchiveWarehouseOperation archiveWarehouse;
  @Inject private ReplaceWarehouseOperation replaceWarehouse;

  @GET
  public List<Warehouse> listAllWarehousesUnits() {
    return warehouseRepository.getAll().stream().map(this::toWarehouseResponse).toList();
  }

  @POST
  @Transactional
  public Response createANewWarehouseUnit(@NotNull Warehouse data) {
    var warehouse = toDomain(data);
    try {
      createWarehouse.create(warehouse);
      return Response.status(Response.Status.CREATED).entity(toWarehouseResponse(warehouse)).build();
    } catch (IllegalArgumentException exception) {
      throw new BadRequestException(exception.getMessage(), exception);
    }
  }

  @GET
  @Path("/{id}")
  public Warehouse getAWarehouseUnitByID(@PathParam("id") String id) {
    var warehouse = warehouseRepository.findActiveById(parseId(id));
    if (warehouse == null) {
      throw new NotFoundException("Warehouse with id " + id + " does not exist.");
    }
    return toWarehouseResponse(warehouse);
  }

  @DELETE
  @Path("/{id}")
  @Transactional
  public void archiveAWarehouseUnitByID(@PathParam("id") String id) {
    var warehouse = warehouseRepository.findActiveById(parseId(id));
    if (warehouse == null) {
      throw new NotFoundException("Warehouse with id " + id + " does not exist.");
    }
    archiveWarehouse.archive(warehouse);
  }

  @POST
  @Path("/{businessUnitCode}/replacement")
  @Transactional
  public Warehouse replaceTheCurrentActiveWarehouse(
      @PathParam("businessUnitCode") String businessUnitCode, @NotNull Warehouse data) {
    var replacement = toDomain(data);
    replacement.businessUnitCode = businessUnitCode;
    try {
      replaceWarehouse.replace(replacement);
      return toWarehouseResponse(replacement);
    } catch (IllegalArgumentException exception) {
      if (warehouseRepository.findByBusinessUnitCode(businessUnitCode) == null) {
        throw new NotFoundException(exception.getMessage());
      }
      throw new BadRequestException(exception.getMessage(), exception);
    }
  }

  private Long parseId(String id) {
    try {
      return Long.valueOf(id);
    } catch (NumberFormatException exception) {
      throw new NotFoundException("Warehouse with id " + id + " does not exist.");
    }
  }

  private com.fulfilment.application.monolith.warehouses.domain.models.Warehouse toDomain(Warehouse data) {
    var warehouse = new com.fulfilment.application.monolith.warehouses.domain.models.Warehouse();
    warehouse.businessUnitCode = data.getBusinessUnitCode();
    warehouse.location = data.getLocation();
    warehouse.capacity = data.getCapacity();
    warehouse.stock = data.getStock();
    return warehouse;
  }

  private Warehouse toWarehouseResponse(
      com.fulfilment.application.monolith.warehouses.domain.models.Warehouse warehouse) {
    var response = new Warehouse();
    response.setId(warehouse.id == null ? null : warehouse.id.toString());
    response.setBusinessUnitCode(warehouse.businessUnitCode);
    response.setLocation(warehouse.location);
    response.setCapacity(warehouse.capacity);
    response.setStock(warehouse.stock);

    return response;
  }
}
