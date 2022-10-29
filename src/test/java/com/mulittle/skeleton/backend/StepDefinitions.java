package com.mulittle.skeleton.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import com.mulittle.skeleton.backend.context.StoryContext;
import com.mulittle.skeleton.backend.model.Company;
import com.mulittle.skeleton.backend.services.CompaniesService;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class StepDefinitions {

    private final CompaniesService companiesService;

    private final StoryContext requestContext;

    @Autowired
    public StepDefinitions(StoryContext requestContext, CompaniesService companiesService) {
        this.requestContext = requestContext;
        this.companiesService = companiesService;
    }

    @ParameterType(".*")
    public Company company(String name) {
        return Company.builder().name(name).build();
    }

    @When("I retrieve all companies")
    public void retrieveCompanies() {
        ResponseSpec response = companiesService.retrieveCompanies();
        requestContext.response = response;
    }

    @When("I register a company whose name starts with {company}")
    public void registerCompanyStartingWith(Company company) {
        ResponseSpec response = companiesService.registerCompany(company);
        requestContext.requestBody = company;
        requestContext.response = response;
    }

    @When("I register the company whose name starts with {string} again")
    public void I_register_the_company_whose_name_starts_with_again(String s) {
        ResponseSpec response = companiesService.registerCompany(requestContext.getRequestBodyAs(Company.class));
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
