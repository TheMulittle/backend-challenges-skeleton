Feature: 

  Rule: the json request payload and json reponse body can be passed directly to generic steps

    @wip
    @TC:BS-10
    Example: whole request payload and whole response body can be used

      When API Consumer sends a "POST" request to "/labels" endpoint with payload
      """ 
        {
          "label": "A label"
        }
      """
      Then the response status code is 201
      And response body is
      """ 
        {
          "label": "A label"
        }
      """

    @TC:BS-11
    Example: request payload and response body can have placeholders of various types

      When API Consumer sends a "POST" request to '/cost-centers' endpoint with payload
      """ 
        {
          "name": "Cost Center P%|UUID_ccID|",
          "label": "CC1"
        }
      """
      Then the response status code is 201
      And response body is
      """ 
        {
          "id": P@|Long_1_2147483647|,
          "name": "Cost Center P$|ccID|"
          "label": "CC1"
        }
      """

    @TC:BS-12
    Example: partial response body can be checked

      Given a cost center is registred
      When API Consumer sends a "POST" request to '/department' endpoint with payload
      """ 
        {
          "name": "New department P%|UUID_departmentID|"
          "costCenterId": "P$|costcenter.id|"
        }
      """
      Then the response status code is 201
      And response body contains
      """ 
        {
          ...
          "name": "New department P$|departmentID|"
          "costCenter": {
            ...
            label: "P$|costcenter.label|"
          }
        }
      """




      
   @TC:BS-14
    Example: a company can be registred only once
      Given a department is registred
      When API Consumer sends a "POST" request to '/departments' endpoint with payload
      """ 
        {
          "name": "New department P%|UUID_departmentID|"
        }
      """
      When API Consumer sends a "POST" request to '/departments' endpoint with payload
      """ 
        {
          "name": "New department P$|departmentID|"
        }
      """
      Then the response status code is 400
      And response body is 
      """
      {
        "err": "The department 'New department P$|departmentID|' already exists"
      }
      """

    Example: a company can be deleted
      When I register the company
      And I delete the company
      Then the response status code is 204

  Rule: Scenarios can be run in isolation

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
    
