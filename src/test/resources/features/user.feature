Feature: Bookstore User Management


  @create @positive
  Scenario: Successful bookstore user registration
    Given I am a new user wanting to create an account in the Bookstore "firstNme" "lastNme"
    When I register with valid bookstore account details
    Then The registration should succeed with status code 201 and recieve a valid userId and a confirmation message

  @create @negative
  Scenario: Attempt to register with an existing bookstore account email
    Given An existing bookstore account already exists
    When I attempt to register a new account using the same email
    Then The registration should fail with status code 409 and indicate that the email is already in use

  @read @positive
  Scenario: Successful bookstore user login and token retrieval
    Given An existing bookstore account already exists
    When I log in to the Bookstore with correct email and password and get the access token
    Then The login should succeed with status code 200

  @read @negative
  Scenario: Attempt to login to the Bookstore with incorrect password
    Given An existing bookstore account already exists
    When I attempt to log in with an incorrect password
    Then The login should fail with status code 401 and error message should indicate invalid login credentials

  @read @positive
  Scenario: Login to the Bookstore using remember-me cookie
    Given I have a valid remember-me cookie from a previous bookstore login
    When I log in using the cookie
    Then The cookie-based login should succeed with status code 200
    And I should receive the same userId and access token

  @update @positive
  Scenario: Successfully update bookstore user profile
    Given I am a logged-in bookstore user with a valid access token
    When I update my profile with new valid details
    Then The update should succeed with status code 200 and have the updated details

  @update @negative
  Scenario: Attempt to update bookstore profile with invalid token
    Given I am an unauthenticated or invalid bookstore user
    When I attempt to update the profile using an invalid access token
    Then The update should fail with status code 403 and indicate forbidden access due to invalid credentials

  @delete @positive
  Scenario: Successfully delete a bookstore user account
    Given I am a logged-in bookstore user with a valid access token
    When I request to delete my bookstore account
    Then The account deletion should succeed with status code 204

  @delete @negative
  Scenario: Attempt to delete a non-existing bookstore user account
    Given No bookstore account exists for userId "non-existing-user-id"
    When I attempt to delete this non-existing account
    Then The deletion should fail with status code 404 and indicate that the user was not found
