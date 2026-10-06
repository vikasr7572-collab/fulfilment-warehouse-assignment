package com.fulfilment.application.monolith.stores;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class StoreEndpointTest {

  @Test
  void createsAStoreAndAllowsPatchToSetStockToZero() {
    Number id = given().contentType("application/json")
        .body("{\"name\":\"TEST-STORE\",\"quantityProductsInStock\":5}")
        .when().post("store")
        .then().statusCode(201)
        .extract().path("id");

    given().contentType("application/json")
        .body("{\"name\":\"TEST-STORE\",\"quantityProductsInStock\":0}")
        .when().patch("store/" + id)
        .then().statusCode(200)
        .body("quantityProductsInStock", equalTo(0));
  }

  @Test
  void rejectsStoreWithNegativeStock() {
    given().contentType("application/json")
        .body("{\"name\":\"INVALID-STORE\",\"quantityProductsInStock\":-1}")
        .when().post("store")
        .then().statusCode(422);
  }
}
