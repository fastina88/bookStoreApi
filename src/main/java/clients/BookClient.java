package clients;

import config.Endpoints;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.Book;
import utils.YamlConfigReader;

import static io.restassured.RestAssured.*;

public class BookClient {

    private static final String BASE_URL = Endpoints.BASE_URL_BOOK.getBaseUrl();

    public Response createBook(Book book, String authToken, int expectedStatusCode) {
        return given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .header("Authorization", authToken != null ? "Bearer " + authToken : null)
                .body(book)
                .when()
                .post(Endpoints.CREATE_BOOK.getEndpoint())
                .then()
                .statusCode(expectedStatusCode)
                .extract()
                .response();
    }

    public Response getAllBooks() {
        return given()
                .baseUri(BASE_URL)
                .when()
                .get(Endpoints.GET_ALL_BOOKS.getEndpoint())
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    public Response updateBook(String bookId, Book book, String authToken, int expectedStatusCode) {
        return given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .header("Authorization", authToken != null ? "Bearer " + authToken : null)
                .body(book)
                .when()
                .put(Endpoints.UPDATE_BOOKID.getEndpoint() + bookId)
                .then()
                .statusCode(expectedStatusCode)
                .extract()
                .response();
    }

    public Response deleteBook(String bookId, String authToken, int expectedStatusCode) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", authToken != null ? "Bearer " + authToken : null)
                .when()
                .delete(Endpoints.DELETE_BOOK.getEndpoint() + bookId)
                .then()
                .statusCode(expectedStatusCode)
                .extract()
                .response();
    }

    public Response simulateRateLimit(String authToken, int expectedStatusCode) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", authToken != null ? "Bearer " + authToken : null)
                .when()
                .get("/simulate-rate-limit")
                .then()
                .statusCode(expectedStatusCode)
                .extract()
                .response();
    }
}