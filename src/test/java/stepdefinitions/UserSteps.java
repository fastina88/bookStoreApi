package stepdefinitions;

import clients.UserClient;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import models.user;
import org.testng.Assert;
import utils.AuthUtils;
import utils.ScenarioContext;
import utils.commonUtilMethods;

public class UserSteps {

    private final user data = user.getInstance();
    ScenarioContext context = ScenarioContext.getInstance();

    @Given("The Bookstore service is online and accepting user account operations")
    public void the_bookstore_api_is_available() {
        UserClient.verifyApiIsAvailable();
    }

    @Given("I am a new user wanting to create an account in the Bookstore {string} {string}")
    public void new_user_wants_to_create_account_details(String fname, String lname) {
        data.setFirstName(fname);
        data.setLastName(lname);
        String phoneNumber = commonUtilMethods.generateRandomPhoneNumber();
        data.setPhoneNumber(phoneNumber);
        data.setEmail("user" + fname + System.currentTimeMillis() + "@example.com");
        data.setPassword("SecureP@ss123");
        context.setExistingUser(data.getEmail());
        context.setUserPassword(data.getPassword());

    }

    @When("I register with valid bookstore account details")
    public void register_with_valid_details() {
        Response response = UserClient.registerUser(data, 201);
        data.setSignUpResponse(response);
        data.setUserId(response.jsonPath().getString("userId"));
    }

    @Then("The registration should succeed with status code 201 and recieve a valid userId and a confirmation message")
    public void registration_should_succeed() {
        Response response = data.getSignUpResponse();
        UserClient.validateStatusCode(response, 201);
        String userid  = response.jsonPath().getString("data.userId");
        data.setUserId(userid);
        Assert.assertNotNull(userid, "no userId found");
        commonUtilMethods.assertMessageEquals(response,"Resgisteration successfull", "message");
    }

    @Given("An existing bookstore account already exists")
    public void existing_account_details() {
        data.setEmail(context.getExistingUser());
        data.setPassword("SecureP@ss123");
    }

    @When("I attempt to register a new account using the same email")
    public void attempt_register_with_existing_email() {
        Response response = UserClient.registerUser(data, 409);
        data.setSignUpResponse(response);
    }

    @Then("The registration should fail with status code 409 and indicate that the email is already in use")
    public void registration_should_fail() {
        Response response = data.getSignUpResponse();
        UserClient.validateStatusCode(response, 409);
        commonUtilMethods.assertMessageEquals(response,"Email already in use","message");

    }

    @Given("Registered bookstore user with existing username and password")
    public void registered_user_exists_details() {
        // Retrieve existing registered email and password from ScenarioContext
        String email = context.getExistingUser();
        String password = context.getUserPassword();
        // Set these back into the data object
        data.setEmail(email);
        data.setPassword(password);

    }

    @When("I log in to the Bookstore with correct email and password and get the access token")
    public void login_with_valid_credentials() {
        Response response = UserClient.loginUser(data, 200);
        data.setLoginResponse(response);
        data.setAccessToken(response.getHeader("access_token"));
        data.setRememberMeCookie(response.getHeader("remember-me"));
        context.setAuthToken(data.getAccessToken());
    }

    @Then("The login should succeed with status code 200")
    public void login_should_succeed() {
        UserClient.validateStatusCode(data.getLoginResponse(), 200);
        commonUtilMethods.assertMessageEquals(data.getLoginResponse(),"logged in successfully","message");
    }

    @When("I attempt to log in with an incorrect password")
    public void login_with_incorrect_password() {
        data.setPassword("WrongPassword123");
        Response response = UserClient.loginUser(data, 401);
        data.setLoginResponse(response);
    }

    @Then("The login should fail with status code 401 and error message should indicate invalid login credentials")
    public void login_should_fail() {
        UserClient.validateStatusCode(data.getLoginResponse(), 401);
        commonUtilMethods.assertMessageEquals(data.getLoginResponse(), "invalid credentials","message");
    }

    @Given("I have a valid remember-me cookie from a previous bookstore login")
    public void valid_remember_me_cookie_details() {
        String rememberme = context.getRemember_me();
        data.setRememberMeCookie(rememberme);
    }

    @When("I log in using the cookie")
    public void login_using_cookie() {
        Response response = UserClient.loginWithCookie(data.getRememberMeCookie(), 200);
        data.setLoginResponse(response);
    }

    @Then("The cookie-based login should succeed with status code 200")
    public void cookie_login_should_succeed() {
        UserClient.validateStatusCode(data.getLoginResponse(), 200);
    }

    @Then("I should receive the same userId and access token")
    public void receive_same_userid_and_token() {
        UserClient.validateStatusCode(data.getLoginResponse(), 200);

        String userId = data.getLoginResponse().jsonPath().getString("userId");
        String accessToken = data.getLoginResponse().jsonPath().getString("accessToken");

        data.setUserId(userId);
        data.setAccessToken(accessToken);

        Assert.assertNotNull(userId, "UserId should not be null");
        Assert.assertNotNull(accessToken, "Access token should not be null");
    }


    @Given("I am a logged-in bookstore user with a valid access token")
    public void logged_in_user_exists() {
        String token = (String) AuthUtils.ensureToken(ScenarioContext.getInstance());
        Assert.assertNotNull(token, "Access token should not be null ");
   }

    @When("I update my profile with new valid details")
    public void update_profile_with_valid_details() {
        user profile = new user("John", "Doe", "9876543210");
        Response response = UserClient.updateUserProfile(data.getUserId(), data.getAccessToken(), profile, 200);
        commonUtilMethods.assertMessageEquals(response,"user profile updated successfully", "message");
        data.setEditUserResponse(response);
    }

    @Then("The update should succeed with status code 200 and have the updated details")
    public void update_should_succeed() {
        Response response = data.getEditUserResponse();

        UserClient.validateStatusCode(response, 200);

        commonUtilMethods.assertMessageEquals(response,data.getFirstName(), "data.firstName");
        commonUtilMethods.assertMessageEquals(response,data.getLastName(), "data.lastName");
        commonUtilMethods.assertMessageEquals(response,data.getPhoneNumber(), "data.phoneNumber");

    }

    @Given("I am an unauthenticated or invalid bookstore user")
    public void invalid_user_details() {
        data.setUserId("some-user-id");
        data.setAccessToken("InvalidToken");
    }

    @When("I attempt to update the profile using an invalid access token")
    public void update_with_invalid_token() {
        user profile = new user("Hack", "User", "0000000000");
        Response response = UserClient.updateUserProfile(data.getUserId(), data.getAccessToken(), profile, 403);
        data.setEditUserResponse(response);
    }

    @Then("The update should fail with status code 403 and indicate forbidden access due to invalid credentials")
    public void update_should_fail() {
        Response response = data.getEditUserResponse();

        UserClient.validateStatusCode(response, 403);
        commonUtilMethods.assertMessageEquals(response, "forbidden", "message");

    }

    @When("I request to delete my bookstore account")
    public void delete_my_account() {
        Response response = UserClient.deleteUser(data.getUserId(), data.getAccessToken(), 204);
        data.setDeleteUserResponse(response);
    }

    @Then("The account deletion should succeed with status code 204")
    public void deletion_should_succeed() {
        UserClient.validateStatusCode(data.getDeleteUserResponse(), 204);
    }

    @Given("No bookstore account exists for userId {string}")
    public void no_account_exists(String userId) {
        data.setUserId(userId);
        data.setAccessToken(context.getAuthToken());
    }

    @When("I attempt to delete this non-existing account")
    public void delete_non_existing_account() {
        Response response = UserClient.deleteUser(data.getUserId(), data.getAccessToken(), 404);
        data.setDeleteUserResponse(response);
    }

    @Then("The deletion should fail with status code 404 and indicate that the user was not found")
    public void deletion_should_fail() {
        Response response = data.getDeleteUserResponse();
        UserClient.validateStatusCode(response, 404);
        commonUtilMethods.assertMessageEquals(response, "forbidden", "message");
    }

}
