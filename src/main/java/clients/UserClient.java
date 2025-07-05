package clients;

import config.Endpoints;
import models.user;
import io.restassured.response.Response;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

@UtilityClass
public class UserClient {

    private static final String BASE_URL = Endpoints.BASE_URL_USER.getBaseUrl();

    public void verifyApiIsAvailable() {
        given()
                .baseUri(BASE_URL)
                .when()
                .get(Endpoints.HEALTH_CHECK.getEndpoint())
                .then()
                .statusCode(200);
    }

    public Response registerUser(user userData, int expectedStatusCode) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("fname", userData.getFirstName());
        requestBody.put("lname", userData.getLastName());
        requestBody.put("phone", userData.getPhoneNumber());
        requestBody.put("email", userData.getEmail());
        requestBody.put("password",userData.getPhoneNumber());

        return given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(Endpoints.REGISTER_USER.getEndpoint())
                .then()
                .statusCode(expectedStatusCode)
                .extract()
                .response();
    }

    public Response loginUser(user userData, int expectedStatusCode) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", userData.getEmail());
        requestBody.put("password", userData.getPassword());

        return given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(Endpoints.LOGIN_USER.getEndpoint())
                .then()
                .statusCode(expectedStatusCode)
                .extract()
                .response();
    }

    public Response loginWithCookie(String rememberMeCookie, int expectedStatusCode) {
        return given()
                .baseUri(BASE_URL)
                .cookie("remember-me", rememberMeCookie)
                .when()
                .post(Endpoints.LOGIN_WITH_COOKIE.getEndpoint())
                .then()
                .statusCode(expectedStatusCode)
                .extract()
                .response();
    }

    public Response updateUserProfile(String userId, String accessToken, user profile, int expectedStatusCode) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("fname", profile.getFirstName());
        requestBody.put("lname", profile.getLastName());
        requestBody.put("phonenumber",profile.getPhoneNumber());

        return given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .header("Authorization", "Bearer " + accessToken)
                .body(requestBody)
                .when()
                .put("/users/" + userId + "/profile")
                .then()
                .statusCode(expectedStatusCode)
                .extract()
                .response();
    }

    public Response deleteUser(String userId, String accessToken, int expectedStatusCode) {
      return given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .delete(Endpoints.DELETE_USER.getEndpoint() + userId)
                .then()
                .statusCode(expectedStatusCode)
                .extract()
                .response();
    }

    public void validateStatusCode(Response response, int expectedStatusCode) {
        response.then().statusCode(expectedStatusCode);
    }

    public void validateErrorMessage(Response response, int expectedStatusCode, String expectedMessagePart) {
        response.then()
                .statusCode(expectedStatusCode)
                .body("message", org.hamcrest.Matchers.containsStringIgnoringCase(expectedMessagePart));
    }
}

