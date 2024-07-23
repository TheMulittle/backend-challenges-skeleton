package com.mulittle.skeleton.backend.integration.steps;

import java.util.Map;

import org.assertj.core.api.Assertions;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mulittle.skeleton.backend.context.Context;
import com.mulittle.skeleton.backend.parser.ContextAwarePlaceholderReplacer;
import com.mulittle.skeleton.backend.parser.JsonAssertions;
import com.mulittle.skeleton.backend.parser.JsonMapper;
import com.mulittle.skeleton.backend.webclient.WebTestClientFactory;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

import com.mulittle.skeleton.backend.model.Response;

@RequiredArgsConstructor
public class GenericStepDefinitions {

    private final Context context;

    private final WebTestClientFactory webTestClientFactory;

    private final ContextAwarePlaceholderReplacer contextAwarePlaceholderReplacer;

    @When("API Consumer sends a {string} request to {string} endpoint with payload")
    public void sendRequest(String verb, String endpoint, String requestPayload) throws JsonMappingException, JsonProcessingException {
        HttpMethod httpMethod = HttpMethod.resolve(verb);
        String replacedEndpoint = contextAwarePlaceholderReplacer.replace(endpoint);
        String replaceRequestPayload = contextAwarePlaceholderReplacer.replace(requestPayload);
        FluxExchangeResult<Object> response = webTestClientFactory.getBaseWebClient()
            .method(httpMethod)
            .uri(replacedEndpoint)
            .bodyValue(replaceRequestPayload)
            .exchange()
            .returnResult(Object.class);
        
        Response responseContainer = Response.builder()
            .body(new String(response.getResponseBodyContent()))
            .statusCode(response.getStatus())
            .headers(response.getResponseHeaders())
            .build();

        context.put("lastResponse", responseContainer);
    }

    @Then("the response status code is {int}")
    public void checkStatusCode(int statusCode) {
        Response lastResponse = (Response) context.find("lastResponse");
        Assertions.assertThat(lastResponse.getStatusCode().value())
            .isEqualTo(statusCode);
    }

    @Then("response body is")
    public void matchBody(String expected) throws JsonMappingException, JsonProcessingException {
        Response lastResponse = (Response) context.find("lastResponse");
        JsonAssertions.assertJsonsMatch(lastResponse.getBody(), contextAwarePlaceholderReplacer.replace(expected));
    }

    @Then("response body contains")
    public void bodyContains(String expected) throws JsonMappingException, JsonProcessingException {
        expected.replaceAll("\\.\\.\\.", "");
        Response lastResponse = (Response) context.find("lastResponse");
        Assertions.assertThat(JsonMapper.jsonStringToObject(lastResponse.getBody()))
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(JsonMapper.jsonStringToObject(expected));
    }
}
