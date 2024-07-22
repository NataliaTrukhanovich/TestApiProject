package api;

import Entity.Car;
import io.restassured.response.ValidatableResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static Utils.Utils.getJsonData;
import static Utils.ConfigProvider.CREATE_CAR;
import static Utils.ConfigProvider.LOGIN;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static spec.Specifications.*;

public class CreateCarTests {
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
    public void createCarTest() {
        installSpecifications(requestSpec(CREATE_CAR), responseSpec(201));
        Car car = Car.builder()
                .engineType("Diesel")
                .model("NewModel")
                .mark("S800")
                .id(10)
                .price(345.99)
                .build();
        ValidatableResponse response = given()
                .header("Authorization", "Bearer " + token.get())
                .body(car).log().all()
                .when()
                .post()
                .then()
                .log().all();
        Car newCar = response.extract().body().jsonPath().getObject(".", Car.class);
        Assert.assertEquals(car, newCar);
        response.assertThat().body(matchesJsonSchemaInClasspath("createCarSchema.json"));
    }

    @Test
    public void createCarWithoutEngineTypeTest() {
        installSpecifications(requestSpec(CREATE_CAR), responseSpec(400));
        Car car = Car.builder()
                .model("NewModel")
                .mark("S800")
                .id(10)
                .price(345.99)
                .build();
        given()
                .header("Authorization", "Bearer " + token.get())
                .body(car).log().all()
                .when()
                .post();
    }

    @Test
    public void createCarWithoutModelTest() {
        installSpecifications(requestSpec(CREATE_CAR), responseSpec(400));
        Car car = Car.builder()
                .engineType("Diesel")
                .mark("S800")
                .id(10)
                .price(345.99)
                .build();
        given()
                .header("Authorization", "Bearer " + token.get())
                .body(car).log().all()
                .when()
                .post();
    }

    @Test
    public void createCarWithoutMarkTest() {
        installSpecifications(requestSpec(CREATE_CAR), responseSpec(400));
        Car car = Car.builder()
                .engineType("Diesel")
                .model("NewModel")
                .id(10)
                .price(345.99)
                .build();
        given()
                .header("Authorization", "Bearer " + token.get())
                .body(car).log().all()
                .when()
                .post();
    }

    @Test
    public void createCarWithoutIdTest() {
        installSpecifications(requestSpec(CREATE_CAR), responseSpec(201));
        Car car = Car.builder()
                .engineType("Diesel")
                .model("NewModel")
                .mark("S800")
                .price(345.99)
                .build();
        given()
                .header("Authorization", "Bearer " + token.get())
                .body(car).log().all()
                .when()
                .post();
    }

    @Test
    public void createCarWithInvalidPriceTest() {
        installSpecifications(requestSpec(CREATE_CAR), responseSpec(400));
        Car car = Car.builder()
                .engineType("Diesel")
                .model("NewModel")
                .mark("S800")
                .id(10)
                .price(0.0)
                .build();
        given()
                .header("Authorization", "Bearer " + token.get())
                .body(car).log().all()
                .when()
                .post();
    }
}
