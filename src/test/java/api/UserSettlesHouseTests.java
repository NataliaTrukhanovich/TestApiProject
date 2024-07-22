package api;

import Entity.House;
import Entity.ParkingPlaces;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static Utils.ConfigProvider.*;
import static Utils.Utils.getJsonData;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static spec.Specifications.*;

public class UserSettlesHouseTests {
    private final static ThreadLocal<String> token = new ThreadLocal<>();
    private static House newHouse;

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
    public void creatingNewHouse() {
        installSpecifications(requestSpec(CREATE_HOUSE), responseSpec(201));
        House house = House.builder()
                .floorCount(25)
                .id(10)
                .price(3400.78)
                .parkingPlaces(getParkingPlaces(1))
                .build();
        newHouse = given()
                .header("Authorization", "Bearer " + token.get())
                .body(house).log().all()
                .when()
                .post()
                .then()
                .log().all()
                .extract().body().jsonPath().getObject(".", House.class);
    }

    @Test
    public void userSettlesHouseTest() {
        installSpecifications(requestSpec(BUY_HOUSE), responseSpec(200));
        ValidatableResponse response = given()
                .header("Authorization", "Bearer " + token.get())
                .pathParam("userId", 9479)
                .pathParam("houseId", newHouse.getId())
                .log().all()
                .when()
                .post()
                .then()
                .log().all();
        response.assertThat().body(matchesJsonSchemaInClasspath("settlesHouseSchema.json"));
    }

    @Test
    public void notEnoughMoneyForBuyingHouseTest() {
        installSpecifications(requestSpec(BUY_HOUSE), responseSpec(406));
        ValidatableResponse response = given()
                .header("Authorization", "Bearer " + token.get())
                .pathParam("userId", 7198)
                .pathParam("houseId", newHouse.getId())
                .log().all()
                .when()
                .post()
                .then()
                .log().all();
    }

    @Test
    public void buyHouseInvalidUserIdTest() {
        installSpecifications(requestSpec(BUY_HOUSE), responseSpec(404));
        ValidatableResponse response = given()
                .header("Authorization", "Bearer " + token.get())
                .pathParam("userId", 200000)
                .pathParam("houseId", newHouse.getId())
                .log().all()
                .when()
                .post()
                .then()
                .log().all();
    }

    @Test
    public void buyHouseInvalidHouseIdTest() {
        installSpecifications(requestSpec(BUY_HOUSE), responseSpec(404));
        ValidatableResponse response = given()
                .header("Authorization", "Bearer " + token.get())
                .pathParam("userId", 9479)
                .pathParam("houseId", newHouse.getId() + 10000)
                .log().all()
                .when()
                .post()
                .then()
                .log().all();
    }

    private List<ParkingPlaces> getParkingPlaces(int i) {
        ParkingPlaces parkingPlaces1;
        ParkingPlaces parkingPlaces2;
        List<ParkingPlaces> list = new ArrayList<>();
        parkingPlaces1 = new ParkingPlaces(10, true, false, 3);
        parkingPlaces2 = new ParkingPlaces(10, false, false, 2);
        list.add(parkingPlaces1);
        list.add(parkingPlaces2);
        return list;
    }
}
