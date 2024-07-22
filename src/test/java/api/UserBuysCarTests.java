package api;

import Entity.Car;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static Utils.ConfigProvider.*;
import static Utils.Utils.getJsonData;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static spec.Specifications.*;

public class UserBuysCarTests {
    private final static ThreadLocal<String> token = new ThreadLocal<>();
    private static Car newCar;

    @BeforeTest
    public void preconditions() {
        installSpecifications(requestSpec(LOGIN), responseSpec(202));
        ValidatableResponse response = given()
                .body(getJsonData("authorization")).log().all()
                .when()
                .post()
                .then()
                .log().all();
        token.set(response.extract().path("access_token"));
    }

    @BeforeClass
    public void creatingNewCar() {
        installSpecifications(requestSpec(CREATE_CAR), responseSpec(201));
        Car car = Car.builder()
                .engineType("Diesel")
                .model("SLX")
                .mark("Car")
                .id(10)
                .price(500)
                .build();
        newCar = given()
                .header("Authorization", "Bearer " + token.get())
                .body(car).log().all()
                .when()
                .post()
                .then()
                .log().all()
                .extract().body().jsonPath().getObject(".", Car.class);
    }

    @Test
    public void buyCarTest() {
        installSpecifications(requestSpec(BUY_CAR), responseSpec(200));
        ValidatableResponse response = given()
                .header("Authorization", "Bearer " + token.get())
                .pathParam("userId", 9479)
                .pathParam("carId", newCar.getId())
                .when()
                .post()
                .then()
                .log().all();
        response.assertThat().body(matchesJsonSchemaInClasspath("userSchema.json"));
    }

    @Test
    public void notEnoughMoneyForBuyingCarTest() {
        installSpecifications(requestSpec(BUY_CAR), responseSpec(406));
        ValidatableResponse response = given()
                .header("Authorization", "Bearer " + token.get())
                .pathParam("userId", 7198)
                .pathParam("carId", newCar.getId())
                .when()
                .post()
                .then()
                .log().all();
    }

    @Test
    public void buyCarInvalidUserIdTest() {
        installSpecifications(requestSpec(BUY_CAR), responseSpec(404));
        given()
                .header("Authorization", "Bearer " + token.get())
                .pathParam("userId", 20000)
                .pathParam("carId", newCar.getId())
                .when()
                .post();
    }

    @Test
    public void buyCarInvalidCarIdTest() {
        installSpecifications(requestSpec(BUY_CAR), responseSpec(404));
        given()
                .header("Authorization", "Bearer " + token.get())
                .pathParam("userId", 9479)
                .pathParam("carId", newCar.getId() + 10000)
                .when()
                .post();
    }
}
