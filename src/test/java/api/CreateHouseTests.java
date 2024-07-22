package api;

import Entity.House;
import Entity.ParkingPlaces;
import io.restassured.response.ValidatableResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static Utils.ConfigProvider.CREATE_HOUSE;
import static Utils.ConfigProvider.LOGIN;
import static Utils.Utils.getJsonData;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static spec.Specifications.*;

public class CreateHouseTests {
    private final static ThreadLocal<String> token = new ThreadLocal<>();

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

    @Test
    public void createHouseTest() {
        installSpecifications(requestSpec(CREATE_HOUSE), responseSpec(201));
        House house = House.builder()
                .floorCount(25)
                .id(10)
                .price(23400.78)
                .parkingPlaces(getParkingPlaces(1))
                .build();
        ValidatableResponse response = given()
                .header("Authorization", "Bearer " + token.get())
                .body(house).log().all()
                .when()
                .post()
                .then()
                .log().all();
        House newHouse = response.extract().body().jsonPath().getObject(".", House.class);
        Assert.assertEquals(house, newHouse);
        response.assertThat().body(matchesJsonSchemaInClasspath("houseSchema.json"));
    }

    @Test
    public void createHouseWithoutParkingPlacesTest() {
        installSpecifications(requestSpec(CREATE_HOUSE), responseSpec(201));
        House house = House.builder()
                .floorCount(25)
                .id(10)
                .price(234009.78)
                .parkingPlaces(new ArrayList<>())
                .build();
        ValidatableResponse response = given()
                .header("Authorization", "Bearer " + token.get())
                .body(house).log().all()
                .when()
                .post()
                .then()
                .log().all();
        response.assertThat().body(matchesJsonSchemaInClasspath("houseSchema.json"));
    }

    @Test
    public void createHouseWithZeroParkingPlacesTest() {
        installSpecifications(requestSpec(CREATE_HOUSE), responseSpec(400));
        House house = House.builder()
                .floorCount(25)
                .id(10)
                .price(234100.78)
                .parkingPlaces(getParkingPlaces(0))
                .build();
        given()
                .header("Authorization", "Bearer " + token.get())
                .body(house).log().all()
                .when()
                .post();
    }

    @Test
    public void createHouseWithInvalidParkingPlacesTest() {
        installSpecifications(requestSpec(CREATE_HOUSE), responseSpec(400));
        House house = House.builder()
                .floorCount(25)
                .id(10)
                .price(234100.78)
                .parkingPlaces(getParkingPlaces(-1))
                .build();
        given()
                .header("Authorization", "Bearer " + token.get())
                .body(house).log().all()
                .when()
                .post();
    }

    @Test
    public void createHouseWithInvalidPriceTest() {
        installSpecifications(requestSpec(CREATE_HOUSE), responseSpec(400));
        House house = House.builder()
                .floorCount(25)
                .id(10)
                .price(0.0)
                .parkingPlaces(getParkingPlaces(1))
                .build();
        given()
                .header("Authorization", "Bearer " + token.get())
                .body(house).log().all()
                .when()
                .post();
    }

    @Test
    public void createHouseWithInvalidFloorCountTest() {
        installSpecifications(requestSpec(CREATE_HOUSE), responseSpec(400));
        House house = House.builder()
                .floorCount(0)
                .id(10)
                .price(44789)
                .parkingPlaces(getParkingPlaces(1))
                .build();
        given()
                .header("Authorization", "Bearer " + token.get())
                .body(house).log().all()
                .when()
                .post();
    }

    private List<ParkingPlaces> getParkingPlaces(int i) {
        ParkingPlaces parkingPlaces1;
        ParkingPlaces parkingPlaces2;
        List<ParkingPlaces> list = new ArrayList<>();
        switch (i) {
            case 0:
                parkingPlaces1 = new ParkingPlaces(10, true, false, 0);
                list.add(parkingPlaces1);
                break;
            case -1:
                parkingPlaces1 = new ParkingPlaces(10, true, false, -1);
                list.add(parkingPlaces1);
                break;
            case 1:
                parkingPlaces1 = new ParkingPlaces(10, true, false, 3);
                parkingPlaces2 = new ParkingPlaces(11, false, false, 2);
                list.add(parkingPlaces1);
                list.add(parkingPlaces2);
                break;
        }
        return list;
    }
}
