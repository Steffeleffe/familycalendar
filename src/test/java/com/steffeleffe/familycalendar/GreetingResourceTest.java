package com.steffeleffe.familycalendar;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.not;

@QuarkusTest
public class GreetingResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/calendar")
          .then()
             .statusCode(200)
             .body(containsString(("Family calendar")));
    }

    @Test
    public void testEventsEndpoint() {
        given()
          .when().get("/calendar/events")
          .then()
             .statusCode(200)
             .body(not(containsString(("{}"))));
    }

}