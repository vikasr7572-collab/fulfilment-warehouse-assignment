package com.fulfilment.application.monolith.fulfilment.adapters.restapi;

import com.fulfilment.application.monolith.fulfilment.application.AllocateFulfilmentUseCase;
import com.fulfilment.application.monolith.fulfilment.domain.model.FulfilmentAllocation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

/** HTTP adapter for the bonus fulfilment allocation use case. */
@Path("fulfilment-allocation")
@ApplicationScoped
@Consumes("application/json")
@Produces("application/json")
public class FulfilmentAllocationResource {
  @Inject AllocateFulfilmentUseCase allocateFulfilmentUseCase;

  @POST
  @Transactional
  public Response create(FulfilmentAllocation allocation) {
    allocateFulfilmentUseCase.allocate(allocation);
    return Response.status(Response.Status.CREATED).entity(allocation).build();
  }
}
