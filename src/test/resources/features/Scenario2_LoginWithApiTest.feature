@Scenario2
Feature: Login Test Scenario 2 with API Validation
  As a user
  I want to login to the application and validate API response
  So that I can verify both web and API functionality

  @Scenario2
  Scenario: User performs login, validates API response and logs out
    Given I navigate to the login page
    When I enter username "student" and password "Password123"
    And I click on submit button
    Then I should be successfully logged in
    And I should see the success message
    When I hit the products API endpoint
    And I compare the API response with saved response
    Then I should save comparison results to file
    When I click on logout link
    Then I should be logged out successfully
