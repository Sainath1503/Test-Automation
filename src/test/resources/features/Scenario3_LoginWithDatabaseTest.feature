@Scenario3
Feature: Login Test Scenario 3 with Database Validation
  As a user
  I want to login to the application and validate database records
  So that I can verify web and database functionality

  @Scenario3
  Scenario: User performs login, validates database records and logs out
    Given I navigate to the login page
    When I enter username "student" and password "Password123"
    And I click on submit button
    Then I should be successfully logged in
    And I should see the success message
    When I connect to the database
    And I retrieve all products from database
    Then I should print products in console
    When I click on logout link
    Then I should be logged out successfully
