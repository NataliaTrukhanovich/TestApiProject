package api;

import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;

import static Utils.Utils.getJsonData;
import static Utils.ConfigProvider.LOGIN;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.notNullValue;
import static spec.Specifications.*;

public class AuthorizationTests {
    private final static ThreadLocal<String> tokenUser = new ThreadLocal<>();

    @Test
    public void authorizationValidTest() {
        installSpecifications(requestSpec(LOGIN), responseSpec(202));
        ValidatableResponse response = given()
                .body(getJsonData("authorization")).log().all()
                .when()
                .post()
                .then()
                .body(".", hasKey("access_token"))
                .body("access_token", notNullValue());
        tokenUser.set(response.extract().path("access_token"));
    }

    @Test
    public void authorizationUsernameOnlyTest() {
        installSpecifications(requestSpec(LOGIN), responseSpec(403));

        ValidatableResponse response = given()
                .body(getJsonData("authorizationUsernameOnly"))
                .log().all()
                .when()
                .post()
                .then()
                .log().all();
    }

    @Test
    public void authorizationPasswordOnlyTest() {
        installSpecifications(requestSpec(LOGIN), responseSpec(403));

        ValidatableResponse response = given()
                .body(getJsonData("authorizationPasswordOnly"))
                .log().all()
                .when()
                .post()
                .then()
                .log().all();
    }

    @Test
    public void authorizationInvalidTest() {
        installSpecifications(requestSpec(LOGIN), responseSpec(403));

        ValidatableResponse response = given()
                .body(getJsonData("authorizationInvalid"))
                .log().all()
                .when()
                .post()
                .then()
                .log().all();
    }
}
