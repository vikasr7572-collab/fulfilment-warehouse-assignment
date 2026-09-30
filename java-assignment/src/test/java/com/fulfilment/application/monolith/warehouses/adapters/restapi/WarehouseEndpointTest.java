package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

/** Exercises the generated Warehouse HTTP contract and the bonus allocation endpoint. */
@QuarkusTest
class WarehouseEndpointTest {

  @Test
  void createsAndArchivesAWarehouseThroughTheApi() {
    String warehouse =
        "{\"businessUnitCode\":\"MWH.TEST\",\"location\":\"VETSBY-001\",\"capacity\":20,\"stock\":10}";

    String id =
        given()
            .contentType("application/json")
            .body(warehouse)
            .when()
            .post("warehouse")
            .then()
            .statusCode(201)
            .body("businessUnitCode", equalTo("MWH.TEST"))
            .extract()
            .path("id");

    given().when().delete("warehouse/" + id).then().statusCode(204);
    given().when().get("warehouse/" + id).then().statusCode(404);
  }

  @Test
  void createsBonusFulfilmentAllocationAndRejectsDuplicates() {
    String allocation = "{\"storeId\":1,\"productId\":2,\"warehouseId\":1}";

    given().contentType("application/json").body(allocation).when().post("fulfilment-allocation")
        .then().statusCode(201);
    given().contentType("application/json").body(allocation).when().post("fulfilment-allocation")
        .then().statusCode(400);
  }
}
