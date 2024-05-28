package com.assessment.data;

import io.restassured.response.Response;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataProcessor {
    public static List<Object> extractAppleIphoneNames(Response response) {
        List<Map<String, Object>> appleIphones = response.jsonPath().getList("findAll { it.name.toLowerCase().startsWith('apple') && it.name.toLowerCase().contains('iphone') }");
        return appleIphones.stream()
                           .map(phone -> (String) phone.get("name"))
                           .collect(Collectors.toList());
    }

    public static String findProductWithMinimumPrice(Response response) {
        List<Map<String, Object>> appleIphones = response.jsonPath().getList("findAll { it.name.toLowerCase().startsWith('apple') && it.name.toLowerCase().contains('iphone') }");
        List<Map<String, Object>> appleIphonesWithPrice = appleIphones.stream()
                                                                     .filter(phone -> phone.get("data") != null && ((Map<String, Object>) phone.get("data")).containsKey("price"))
                                                                     .collect(Collectors.toList());

        Map<String, Object> cheapestProduct = appleIphonesWithPrice.stream()
                                                                   .min((phone1, phone2) -> {
                                                                       double price1 = ((Number) ((Map<String, Object>) phone1.get("data")).get("price")).doubleValue();
                                                                       double price2 = ((Number) ((Map<String, Object>) phone2.get("data")).get("price")).doubleValue();
                                                                       return Double.compare(price1, price2);
                                                                   })
                                                                   .orElse(null);

        return cheapestProduct != null ? (String) cheapestProduct.get("name") : null;
    }

    public static List<String> extractIds(Response response) {
        return response.jsonPath().getList("id");
    }
}
