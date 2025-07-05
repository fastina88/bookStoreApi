Feature: Bookstore Book Management

  @create @positive
  Scenario: Successfully create a new book
    Given User is an authorized bookstore user
    When User create a new book with valid details
    Then The operation should succeed with status code 201

  @create @negative
  Scenario: Prevent book creation without authorization
    Given User is an unauthorized bookstore user
    When User attempt to create a new book without authorization
    Then The operation should fail with status code 401

  @create @negative
  Scenario: Should return conflict when creating a duplicate book
    Given User is an authorized bookstore user
    When User create a new book with valid details
    And User create the same book again
    Then The operation should fail with status code 409

  @read @positive
  Scenario: Successfully fetch all books
    Given User is an authorized bookstore user
    When User fetch the list of books
    Then The operation should succeed with status code 200

  @update @positive
  Scenario: Successfully update an existing book
    Given User is an authorized bookstore user
    When User create a new book with valid details
    And User update the existing book details
    Then The operation should succeed with status code 200
    And The response should contain the necessary details

  @update @negative
  Scenario: Prevent book updation without authorization
    Given User is an unauthorized bookstore user
    When User attempt to update book without authorization
    Then The operation should fail with status code 401

  @update @negative
  Scenario: Prevent book updation with insufficient permissions
    Given User is an authorized bookstore user without edit permissions
    When User attempt to update book without proper rights
    Then The operation should fail with status code 403

  @delete @positive
  Scenario: Successfully delete an existing book
    Given User is an authorized bookstore user
    When User create a new book with valid details
    And User delete the existing book
    Then The operation should succeed with status code 204

  @delete @negative
  Scenario: Prevent book deletion without authorization
    Given User is an unauthorized bookstore user
    When User attempt to delete a book without authorization
    Then The operation should fail with status code 401

  @delete @negative
  Scenario: Prevent book deletion with insufficient permissions
    Given User is an authorized bookstore user without delete permissions
    When User attempt to delete book without proper rights
    Then The operation should fail with status code 403

  @rate-limit @negative
  Scenario: Should return 429 Too Many Requests when exceeding allowed request limit
    Given User is an authorized bookstore user
    When User exceed the allowed number of requests
    Then The operation should fail with status code 429
