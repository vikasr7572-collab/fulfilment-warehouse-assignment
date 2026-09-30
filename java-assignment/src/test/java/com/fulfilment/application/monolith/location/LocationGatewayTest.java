package com.fulfilment.application.monolith.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class LocationGatewayTest {

  @Test
  public void testWhenResolveExistingLocationShouldReturn() {
    // given
    LocationGateway locationGateway = new LocationGateway();

    // when
    var location = locationGateway.resolveByIdentifier("zwolle-001");

    // then
    assertEquals("ZWOLLE-001", location.identification);
  }

  @Test
  public void unknownOrBlankLocationReturnsNull() {
    LocationGateway locationGateway = new LocationGateway();
    assertNull(locationGateway.resolveByIdentifier("UNKNOWN"));
    assertNull(locationGateway.resolveByIdentifier(" "));
  }
}
