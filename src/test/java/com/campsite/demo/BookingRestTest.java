package com.campsite.demo;

import org.hamcrest.core.IsEqual;
import org.junit.Test;
import static com.jayway.restassured.RestAssured.given;


public class BookingRestTest extends FunctionalTest {


    @Test
    public void basicPingTest() {
        System.out.println(" basicPingTest method");
        given().when().get("/").then().statusCode(200);
    }

    @Test
    public void basicPingTest2() {
        System.out.println(" basicPingTest method");
        given().when().get("/getAvailability").then().statusCode(200);
    }


}



