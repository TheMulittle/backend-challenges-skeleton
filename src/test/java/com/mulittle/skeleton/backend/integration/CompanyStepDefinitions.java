package com.mulittle.skeleton.backend.integration;

import java.util.Map;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mulittle.skeleton.backend.context.PlaceholderContext;
import com.mulittle.skeleton.backend.context.StoryContext;
import com.mulittle.skeleton.backend.model.Company;
import com.mulittle.skeleton.backend.parser.ContextAwarePlaceholderReplacer;
import com.mulittle.skeleton.backend.parser.JsonMapper;
import com.mulittle.skeleton.backend.services.CompaniesService;

import io.cucumber.java.DocStringType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CompanyStepDefinitions {

    private final CompaniesService companiesService;

    private final StoryContext requestContext;

    private final PlaceholderContext placeholderContext;

    @DocStringType
    public Map<String, Object> json(String docString) throws JsonMappingException, JsonProcessingException {
        String body = ContextAwarePlaceholderReplacer.replace(docString, placeholderContext);
        return JsonMapper.jsonStringToMap(body);
    }

    @Given("a company that is not registred")
    public void createCompany() {
        String companyName = "Not registred company" + UUID.randomUUID();
        Company company = Company.builder().name(companyName).build();
        placeholderContext.put("company", company);
    }

    @When("I retrieve all companies")
    public void retrieveCompanies() {
        ResponseSpec response = companiesService.retrieveCompanies();
        requestContext.response = response;
    }

    @When("I register the company")
    public void registerCompany() {
        ResponseSpec response = companiesService
                .registerCompany(placeholderContext.getObjectAs("company", Company.class));
        requestContext.response = response;
    }

    @Then("the response status code is {int}")
    public void checkStatusCode(int statusCode) {
        ResponseSpec lastResponse = (ResponseSpec) requestContext.response;
        lastResponse.expectStatus()
                .isEqualTo(statusCode);
    }

    @Then("response body is")
    public void matchBody(Map<String, Object> expected) throws JsonMappingException, JsonProcessingException {
        ResponseSpec lastResponse = (ResponseSpec) requestContext.response;
        Map<String, Object> actual = JsonMapper.jsonStringToMap(new String(lastResponse.expectBody().returnResult().getResponseBodyContent()));
        Assertions.assertThat(actual).containsAllEntriesOf(expected);
    }
}
