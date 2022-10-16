package com.mulittle.skeleton.backend.services;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mulittle.skeleton.backend.ContextAwareJsonEncoder;
import com.mulittle.skeleton.backend.context.EvidenceContext;
import com.mulittle.skeleton.backend.model.AbstractAttachment;
import com.mulittle.skeleton.backend.model.Company;
import com.mulittle.skeleton.backend.model.RequestAttachment;
import com.mulittle.skeleton.backend.model.ResponseAttachment;
import com.mulittle.skeleton.backend.model.Urls;

import reactor.core.publisher.Mono;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class CompaniesService {

  private static final String COMPANIES = "/companies";
  private static final String COMPANY = "/companies/{id}";

  @Autowired
  public CompaniesService(ContextAwareJsonEncoder encoder, EvidenceContext evidenceContext) {
    this.baseClient = WebTestClient.bindToServer().codecs(clientDefaultCodecsConfigurer -> {
      clientDefaultCodecsConfigurer.defaultCodecs().jackson2JsonEncoder(encoder);
      clientDefaultCodecsConfigurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(new ObjectMapper(), MediaType.APPLICATION_JSON));
    })
    .baseUrl(Urls.BASE_URL)
    .filter(addRequestDataToAttachment(evidenceContext))
    .filter(addResponseDataToAttachment(evidenceContext))
    .build();
  }

  private ExchangeFilterFunction addRequestDataToAttachment(EvidenceContext evidenceContext) {
    return (clientRequest, next) -> {
      List<AbstractAttachment> attachments = evidenceContext.getAttachments();
      RequestAttachment requestAttachment = new RequestAttachment();
      attachments.add(requestAttachment);
      requestAttachment.setUrl(clientRequest.url().toString());
      requestAttachment.setHeader(clientRequest.headers().toString());
      requestAttachment.setMethod(clientRequest.method().name());
      return next.exchange(clientRequest);
    };
  }

  
  private ExchangeFilterFunction addResponseDataToAttachment(EvidenceContext evidenceContext) {
    return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
      List<AbstractAttachment> attachments = evidenceContext.getAttachments();
      ResponseAttachment responseAttachment = new ResponseAttachment();
      attachments.add(responseAttachment);
      responseAttachment.setHeader(clientResponse.headers().toString());
      responseAttachment.setStatus(clientResponse.statusCode().toString());
      clientResponse.bodyToMono(String.class).subscribe(body -> responseAttachment.setBody(body));
      return Mono.just(clientResponse);
    });
  }

  private final WebTestClient baseClient;

  public ResponseSpec registerCompany(Company company) {
    return baseClient
        .post()
        .uri(COMPANIES)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(company)
        .exchange();
  }
  
}
