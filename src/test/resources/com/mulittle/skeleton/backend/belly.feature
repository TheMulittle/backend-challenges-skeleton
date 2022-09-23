Feature: Company registring 

  Scenario: a new company can be registred
    When I register a company whose name starts with 'New Company'
    Then the response status code is 201
    And the field 'name' of the response payload is equal to the field 'name' of the request payload

  Scenario: a new company can be registred
    When I register the company whose name starts with 'New Company' again
    Then the response status code is 400
    And the field 'err' of the response payload is equal to 'AZ Company already exists'
