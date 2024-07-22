package api;

import Entity.User;
import io.restassured.response.ValidatableResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static Utils.Utils.getJsonData;
import static Utils.ConfigProvider.CREATE_USER;
import static Utils.ConfigProvider.LOGIN;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static spec.Specifications.*;

public class CreateUserTests {
    private final static ThreadLocal<String> token = new ThreadLocal<>();

    @BeforeTest
    public void preconditions() {
        installSpecifications(requestSpec(LOGIN), responseSpec(202));
        ValidatableResponse response = given()
                .body(getJsonData("authorization"))
                .log().all()
                .when()
                .post()
                .then()
                .log().all();
        token.set(response.extract().path("access_token"));
    }

    @Test
    public void createUserTest() {
        installSpecifications(requestSpec(CREATE_USER), responseSpec(201));
        User user = User.builder()
                .firstName("Sergey")
                .secondName("Sergeev")
                .age(32)
                .id(10)
                .money(213400.78)
                .sex("MALE")
                .build();
        ValidatableResponse response = given()
                .header("Authorization", "Bearer " + token.get())
                .body(user).log().all()
                .when()
                .post()
                .then()
                .log().all();
        User newUser = response.extract().body().jsonPath().getObject(".", User.class);
        Assert.assertEquals(user, newUser);
        response.assertThat().body(matchesJsonSchemaInClasspath("userSchema.json"));
    }

    @Test
    public void createUserWithoutFirstNameTest() {
        installSpecifications(requestSpec(CREATE_USER), responseSpec(400));
        User user = User.builder()
                .secondName("Testovich")
                .age(62)
                .id(10)
                .money(23400)
                .sex("MALE")
                .build();
        given()
                .header("Authorization", "Bearer " + token.get())
                .body(user).log().all()
                .post();
    }

    @Test
    public void createUserWithoutSecondNameTest() {
        installSpecifications(requestSpec(CREATE_USER), responseSpec(400));
        User user = User.builder()
                .firstName("Ivan")
                .age(62)
                .id(10)
                .money(23400)
                .sex("MALE")
                .build();
        given()
                .header("Authorization", "Bearer " + token.get())
                .body(user).log().all()
                .post();
    }

    @Test
    public void createUserWithoutAgeTest() {
        installSpecifications(requestSpec(CREATE_USER), responseSpec(400));
        User user = User.builder()
                .firstName("Ivan")
                .secondName("Testovich")
                .id(10)
                .money(23400)
                .sex("MALE")
                .build();
        given()
                .header("Authorization", "Bearer " + token.get())
                .body(user).log().all()
                .post();
    }

    @Test
    public void createUserWithoutIdTest() {
        installSpecifications(requestSpec(CREATE_USER), responseSpec(201));
        User user = User.builder()
                .firstName("Ivan")
                .secondName("Testovich")
                .age(62)
                .money(23400)
                .sex("MALE")
                .build();
        given()
                .header("Authorization", "Bearer " + token.get())
                .body(user).log().all()
                .post();
    }

    @Test
    public void createUserWithoutMoneyTest() {
        installSpecifications(requestSpec(CREATE_USER), responseSpec(201));
        User user = User.builder()
                .firstName("Ivan")
                .secondName("Testovich")
                .age(62)
                .id(10)
                .sex("MALE")
                .build();
        given()
                .header("Authorization", "Bearer " + token.get())
                .body(user).log().all()
                .post();
    }

    @Test
    public void createUserWithoutSexTest() {
        installSpecifications(requestSpec(CREATE_USER), responseSpec(201));
        User user = User.builder()
                .firstName("Ivan")
                .secondName("Testovich")
                .age(62)
                .id(10)
                .money(23400)
                .build();
        given()
                .header("Authorization", "Bearer " + token.get())
                .body(user).log().all()
                .post();
    }
}
