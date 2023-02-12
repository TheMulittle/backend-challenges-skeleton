Feature: Company registration 

  Rule: Companies can be managed (registred, deleted, edited)

    Background:
      Given a company that is not registred

    @TC:BS-10
    Example: a company can be registred
      When I register the company
      Then the response status code is 201
      And response body is
      """ 
        {
          "name": "![company.name]"
        }
      """
  
    Example: a company can be registred only once
      When I register the company
      And I register the company again
      Then the response status code is 400
      And response body is 
      """
      {
        "err": "This company already exists"
      }
      """

    @wip
    Example: a company can be deleted
      When I register the company
      And I delete the company
      Then the response status code is 204

  Rule: Companies can be retrieved

    @isolated
    Example: all companies can be retrieved - zero companies registred
      Given no companies are registred
      When I retrieve all companies
      Then the response status code is 200
      And the response body is
      """
      {
        "companies": []
      }
      """
      When I register the company
      Then the response status code is 200
      And the repsonse body is
      """
      {
        "companies": [
          {
            "id": "![int]",
            "name": "![company.name]"
          }
        ]
      }
      """
    
