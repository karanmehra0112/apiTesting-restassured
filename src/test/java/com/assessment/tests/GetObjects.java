package com.assessment.tests;

import com.assessment.config.ConfigLoader;
import com.assessment.api.ApiUtil;
import com.assessment.data.DataProcessor;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertNotNull;

public class GetObjects {

    private static String baseUrl;
    private ExtentReports extent;
    private ExtentTest test;

    @BeforeClass
    public void setUp() throws IOException {
        baseUrl = ConfigLoader.loadBaseUrl("src/test/resources/config.properties");

        ExtentSparkReporter htmlReporter = new ExtentSparkReporter("extent-reports.html");
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setDocumentTitle("API Test Report");
        htmlReporter.config().setReportName("API Test Report");

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Tester", "Your Name");
    }

    @Test(description = "get all phone names  starting with work apple\n get phone with lowest cost\n validate that ID is not null")
    public void validateObjectAPI() {
        test = extent.createTest("GET /objects");
        test.log(Status.INFO, "Starting the test for sample GET request");

        Response response = ApiUtil.getObjects(baseUrl);
        response.then().statusCode(200);
        test.log(Status.PASS, "Verified the status code is 200");

        List<Object> appleIphoneNames = extractAppleIphoneNames(response);
        test.log(Status.INFO, "Apple iPhones: " + appleIphoneNames);

        String cheapestProductName = findProductWithMinimumPrice(response);
        if (cheapestProductName != null) {
            test.log(Status.INFO, "Product with minimum price: " + cheapestProductName);
        } else {
            test.log(Status.INFO, "No product found with price information.");
        }

        List<String> ids = extractIds(response);
        assertNotNull(ids);
        test.log(Status.PASS, "ID is not null: " + ids);
    }

    public List<Object> extractAppleIphoneNames(Response response) {
        return DataProcessor.extractAppleIphoneNames(response);
    }

    public String findProductWithMinimumPrice(Response response) {
        return DataProcessor.findProductWithMinimumPrice(response);
    }

    public List<String> extractIds(Response response) {
        return DataProcessor.extractIds(response);
    }

    @AfterClass
    public void tearDown() {
        extent.flush();
    }
}