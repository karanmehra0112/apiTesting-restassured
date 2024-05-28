package com.assessment.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiUtil {
    public static Response getObjects(String baseUrl) {
        RestAssured.baseURI = baseUrl;
        return RestAssured.given().when().get("/objects");
    }
}
