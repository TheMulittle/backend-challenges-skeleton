Feature: There are two flavours of sending requests and asserting the response content that can be used

  Rule: the json request payload and json reponse body can be passed directly to generic steps

    @TC:BS-10
    Example: whole request payload and whole response body can be used

      When API Consumer sends a "POST" request to "/companies" endpoint with payload
      """ 
        {
          "name": "Not registred company"
        }
      """
      Then the response status code is 201
      And response body is
      """ 
        {
          "name": "Not registred company"
        }
      """

    @TC:BS-11
    Example: request payload and response body can have placeholders of many types

      When API Consumer sends a "POST" request to '/department' endpoint with payload
      """ 
        {
          "name": "New department ℙ%|UUID_departmentID|"
        }
      """
      Then the response status code is 201
      And response body is
      """ 
        {
          "id": ℙ=|Integer_0_2147483647|,
          "name": "New department ℙ$|departmentID|"
        }
      """

    @TC:BS-12
    Example: partial response body can be checked

      When API Consumer sends a "POST" request to '/department' endpoint with payload
      """ 
        {
          "name": "New department ℙ%|UUID_departmentID|"
        }
      """
      Then the response status code is 201
      And response body contains
      """ 
        {
          ...
          "name": "New department ℙ${departmentID}"
          ...
        }
      """

    @TC:BS-12
    Example:
    

      Given a company that is not registred
      When I register the company
      When I send request to '/𝒫${companyID}/department' with payload
      """ 
        {
          "name": "New department ℙ%|Integer_departmentID|"
        }
      """
      Then the response status code is 201
      And response body is
      """ 
        {
          "id": ℙ@|Integer_0_2147483647|
          "name": "Company with the following name was created: New department ℙ$|departmentID|"
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
        "err": "The company ℙ${company.name} already exists"
      }
      """

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
    
