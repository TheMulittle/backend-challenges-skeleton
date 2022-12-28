package com.mulittle.skeleton.backend;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mulittle.skeleton.backend.context.StoryContext;
import com.mulittle.skeleton.backend.model.ErrorPayload;
import com.mulittle.skeleton.backend.services.CompaniesService;

import io.cucumber.java.DocStringType;
import io.cucumber.java.en.Then;

public class ErrorStepDefinitions {

    private final CompaniesService companiesService;

    private final StoryContext requestContext;

    @Autowired
    public ErrorStepDefinitions(StoryContext requestContext, CompaniesService companiesService) {
        this.requestContext = requestContext;
        this.companiesService = companiesService;
    }

    @DocStringType
    public ErrorPayload json(String docString) throws JsonProcessingException {
        return new ObjectMapper().readValue(docString, ErrorPayload.class);
    }

    @Then("the error response payload is")
    public void matchFieldWithField(ErrorPayload errorPayload) {

    }
}
