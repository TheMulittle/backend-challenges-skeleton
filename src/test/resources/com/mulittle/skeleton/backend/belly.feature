Feature: Company registration 

  Scenario: a company can be registred only once
    When I register a company whose name starts with 'AZ Company'
    Then the response status code is 201
    And the field 'name' of the response payload is equal to the field 'name' of the request payload
    When I register the company whose name starts with 'AZ Company' again
    Then the response status code is 400
    And the field 'err' of the response payload is equal to 'Company already exists'

  Scenario: all companies
    When I retrieve all companies
    Then the response status code is 200