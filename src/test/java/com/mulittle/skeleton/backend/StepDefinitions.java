package com.mulittle.skeleton.backend;

import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import com.mulittle.skeleton.backend.context.RequestContext;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class StepDefinitions {

    RequestContext requestContext;

    public StepDefinitions(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    @When("I register a company whose name starts with {string}")
    public void registerCompanyStartingWith(String s) {
        // Write code here that turns the phrase above into concrete actions
        ResponseSpec response = WebTestClient.bindToServer()
        .baseUrl("http://localhost:8001")
        .build()
        .post()
        .uri("/v1/companies")
        .bodyValue("{name: \"AZ Company\"}")
        .exchange();
        requestContext.response = response;
    }

    @When("I register the company whose name starts with {string} again")
    public void I_register_the_company_whose_name_starts_with_again(String s) {
        ResponseSpec response = WebTestClient.bindToServer()
        .baseUrl("http://localhost:8001")
        .build()
        .post()
        .uri("/v1/companies")
        .bodyValue("{name: \"AZ Company\"}")
        .exchange();
        requestContext.response = response;
    }
    
    @Then("the response status code is {int}")
    public void checkStatusCode(int statusCode) {
        ResponseSpec lastResponse = requestContext.response;
        lastResponse.expectStatus()
            .isEqualTo(statusCode);
    }

    @Then("the field {string} of the response payload is equal to the field {string} of the request payload")
    public void matchFieldWithField(String responseField, String valueField) {
        /*ResponseSpec lastResponse = requestContext.response;
        Companies lastRequestBody = requestContext.getRequestBodyAs(Companies.class);
        lastRequestBody.
        lastResponse.expectBody()
        .jsonPath("$.%s", responseField)
        .isEqualTo();*/
    }

    @Then("the field {string} of the response payload is equal to {string}")
    public void matchFieldWithString(String field, String value) {
        ResponseSpec lastResponse = requestContext.response;
        lastResponse.expectBody()
            .jsonPath("$.%s", field)
            .isEqualTo(value);
    }
}
