Feature: There are two flavours of sending requests and asserting the response content that can be used

  Rule:  

    @TC:BS-10
    Example: 

      When I send request to "/company" with payload
      """ 
        {
          "name": "New company"
        }
      """
      Then the response status code is 201
      And response body is
      """ 
        {
          "name": "Company created successfully"
        }
      """

    @TC:BS-11
    Example:


      When I send request to '/department' with payload
      """ 
        {
          "name": "New department ℙ!{departmentID=ℙ%{UUID}}"
        }
      """
      Then the response status code is 201
      And response body is
      """ 
        {
          "name": "Department with the following name was created: New department 𝒫${departmentID}"
        }
      """

    @TC:BS-12
    Example:
    

      Given a company that is not registred
      When I register the company
      When I send request to '/𝒫${companyID}/department' with payload
      """ 
        {
          "name": "New department 𝒫%{Integer,departmentID}"
        }
      """
      Then the response status code is 201
      And response body is
      """ 
        {
          "name": "Company with the following name was created: New department 𝒫${departmentID}"
        }
      """
      
    @TC:BS-10
    Example: a company can be registred
      Given a company that is not registred
      When I register the company
      Then the response status code is 201
      And response body is
  
    Example: a company can be registred only once
      When I register the company
      And I register the company again
      Then the response status code is 400
      And response body is 
      """
      {
        "err": "The company 𝒫${company.name} already exists"
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
    
