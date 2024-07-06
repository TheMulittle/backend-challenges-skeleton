package com.mulittle.skeleton.backend.integration.steps;

import java.io.IOException;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mulittle.skeleton.backend.context.StoryContext;
import com.mulittle.skeleton.backend.parser.JsonMapper;
import com.mulittle.skeleton.backend.webclient.WebTestClientFactory;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GenericStepDefinitions {

    private final StoryContext requestContext;

    private final WebTestClientFactory webTestClientFactory;

    private <T> T deserializeByteArrayTo(byte[] x, Class<T> clazz) throws StreamReadException, DatabindException, IOException {
        return new ObjectMapper().readValue(x, clazz);
    }


    @When("I send request to {endpoint} with payload")
    public void sendRequest(String endpoint, Map<String, ?> requestPayload) throws JsonMappingException, JsonProcessingException {
        webTestClientFactory.getBaseWebClient()
            .method(HttpMethod.POST)
            .uri(endpoint)
            .bodyValue(requestPayload)
            .exchange();
    }

    @Then("response body is")
    public void matchBody(Map<String, ?> expected) throws JsonMappingException, JsonProcessingException {
        ResponseSpec lastResponse = (ResponseSpec) requestContext.response;
        Map<String, Object> actual = JsonMapper.jsonStringToMap(new String(lastResponse.expectBody().returnResult().getResponseBodyContent()));
        //Assertions.assertThat(actual).usingRecursiveComparison().;
    }
}
