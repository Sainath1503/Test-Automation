@Scenario1
Feature: Login Test Scenario 1
  As a user
  I want to login to the application
  So that I can access the secured area

  @Scenario1
  Scenario: User performs login, validates post-login, and logs out
    Given I navigate to the login page
    When I enter username "student" and password "Password123"
    And I click on submit button
    Then I should be successfully logged in
    And I should see the success message
    When I click on logout link
    Then I should be logged out successfully
