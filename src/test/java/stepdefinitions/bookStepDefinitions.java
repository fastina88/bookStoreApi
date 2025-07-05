package stepdefinitions;

import clients.BookClient;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import models.Book;
import org.slf4j.LoggerFactory;
import utils.AuthUtils;
import utils.ScenarioContext;
import org.slf4j.Logger;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class bookStepDefinitions {

    private static final Logger logger = LoggerFactory.getLogger(bookStepDefinitions.class);

    ScenarioContext context = ScenarioContext.getInstance();
    BookClient bookClient = new BookClient();
    private Book updatedBook;

    @Given("User is an authorized bookstore user")
    public void i_am_authorized_user() {
        logger.info("User is authorized with token: {}", AuthUtils.ensureToken(ScenarioContext.getInstance()));
    }

    @Given("User is an unauthorized bookstore user")
    public void unauthorized_user() {
        context.setAuthToken(null);
        logger.info("User is unauthorized.");
    }

    @Given("User is an authorized bookstore user without edit permissions")
    public void user_without_edit_permissions() {
        context.setAuthToken("valid-auth-token-no-edit");
        logger.info("User has no edit permissions.");
    }

    @Given("User is an authorized bookstore user without delete permissions")
    public void user_without_delete_permissions() {
        context.setAuthToken("valid-auth-token-no-delete");
        logger.info("User has no delete permissions.");
    }

    @When("User create a new book with valid details")
    public void create_book() {
        Book book = new Book("The Great Gatsby", "F. Scott Fitzgerald", 500, "Classic");
        Response response = bookClient.createBook(book, context.getAuthToken(), 201);
        context.setBookId(response.jsonPath().getString("id"));
        context.setLastResponse(response);
        logger.info("Book created with ID: {}", context.getBookId());
    }

    @When("User create the same book again")
    public void create_duplicate_book() {
        Book book = new Book("The Great Gatsby", "F. Scott Fitzgerald", 500, "Classic");
        Response response = bookClient.createBook(book, context.getAuthToken(), 409);
        context.setLastResponse(response);
        logger.warn("Duplicate book creation attempt.");
    }

    @When("User attempt to create a new book without authorization")
    public void create_book_without_auth() {
        Book book = new Book("Unauthorized Book", "Unknown", 250, "Mystery");
        Response response = bookClient.createBook(book, null, 401);
        context.setLastResponse(response);
        logger.warn("Unauthorized attempt to create book.");
    }

    @When("User fetch the list of books")
    public void fetch_books() {
        Response response = bookClient.getAllBooks();
        context.setLastResponse(response);
        logger.info("Fetched books: Status code {}", response.statusCode());
    }

    @When("User update the existing book details")
    public void update_book() {
        updatedBook = new Book("Updated Title", "Updated Author", 250, "Updated Genre");
        Response response = bookClient.updateBook(context.getBookId(), updatedBook, context.getAuthToken(), 200);
        context.setLastResponse(response);
        logger.info("Book updated: {}", context.getBookId());
    }

    @When("User attempt to update book without authorization")
    public void update_book_without_auth() {
        Book updatedBook = new Book("Hack", "Unauthorized", 350, "None");
        Response response = bookClient.updateBook("invalid-book-id", updatedBook, null, 401);
        context.setLastResponse(response);
        logger.warn("Unauthorized update attempt.");
    }

    @When("User attempt to update book without proper rights")
    public void update_book_without_rights() {
        Book updatedBook = new Book("Hack", "NoRights", 450, "Forbidden");
        Response response = bookClient.updateBook("some-book-id", updatedBook, context.getAuthToken(), 403);
        context.setLastResponse(response);
        logger.warn("Forbidden update attempt.");
    }

    @When("User delete the existing book")
    public void delete_book() {
        Response response = bookClient.deleteBook(context.getBookId(), context.getAuthToken(), 204);
        context.setLastResponse(response);
        logger.info("Book deleted: {}", context.getBookId());
    }

    @When("User attempt to delete a book without authorization")
    public void delete_book_without_auth() {
        Response response = bookClient.deleteBook("non-existent-book-id", null, 401);
        context.setLastResponse(response);
        logger.warn("Unauthorized delete attempt.");
    }

    @When("User attempt to delete book without proper rights")
    public void delete_book_without_rights() {
        Response response = bookClient.deleteBook("some-book-id", context.getAuthToken(), 403);
        context.setLastResponse(response);
        logger.warn("Forbidden delete attempt.");
    }

    @When("User exceed the allowed number of requests")
    public void exceed_rate_limit() {
        Response response = bookClient.simulateRateLimit(context.getAuthToken(), 429);
        context.setLastResponse(response);
        logger.warn("Rate limit exceeded.");
    }

    @Then("The operation should succeed with status code {int}")
    public void operation_should_succeed(int expectedStatusCode) {
        assertThat(context.getLastResponse().statusCode()).isEqualTo(expectedStatusCode);
        logger.info("Verified success status code: {}", expectedStatusCode);
    }

    @Then("The operation should fail with status code {int}")
    public void operation_should_fail(int expectedStatusCode) {
        assertThat(context.getLastResponse().statusCode()).isEqualTo(expectedStatusCode);
        String actualMessage = context.getLastResponse().jsonPath().getString("message");
        assertThat(actualMessage)
                .as("Actual message not matching expected (case-insensitive)")
                .isEqualToIgnoringCase("operation failed");
        logger.info("Verified failure status code: {}", expectedStatusCode);
    }

    @And("The response should contain the necessary details")
    public void theResponseShouldContainTheNecessaryDetails() {
        Response response = context.getLastResponse();

        Map<String, Object> dataMap = response.jsonPath().getMap("data");
        assertThat(dataMap).as("'data' object should not be null").isNotNull();
        assertThat(dataMap).containsKeys("bookId","name", "author", "genre", "price");

        String actualBookId = (String) dataMap.get("bookId");
        String actualTitle = (String) dataMap.get("name");
        String actualAuthor = (String) dataMap.get("author");
        String actualGenre = (String) dataMap.get("genre");
        int actualPrice = Integer.parseInt(dataMap.get("price").toString());

        assertThat(actualTitle).as("Book title mismatch").isEqualTo(updatedBook.getTitle());
        assertThat(actualAuthor).as("Author mismatch").isEqualTo(updatedBook.getAuthor());
        assertThat(actualGenre).as("Genre mismatch").isEqualTo(updatedBook.getGenre());
        assertThat(actualPrice).as("Price mismatch").isEqualTo(updatedBook.getPrice());
        assertThat(actualBookId).as("Price mismatch").isEqualTo(context.getBookId());

        logger.info("Verified updated book details match: title={}, author={}, genre={}, price={} , bookId={}",
                actualTitle, actualAuthor, actualGenre, actualPrice,actualBookId);

    }
}
