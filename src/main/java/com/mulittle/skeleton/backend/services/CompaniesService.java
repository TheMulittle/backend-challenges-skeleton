package com.mulittle.skeleton.backend.services;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mulittle.skeleton.backend.ContextPopulatingJsonEncoder;
import com.mulittle.skeleton.backend.model.Company;
import com.mulittle.skeleton.backend.model.Urls;

@Component
public class CompaniesService {

  public static final String COMPANIES = "/companies";
  public static final String COMPANY = "/companies/{id}";

  public ContextPopulatingJsonEncoder encoder;

  @Autowired
  public CompaniesService(ContextPopulatingJsonEncoder encoder) {
    this.encoder = encoder;
    this.baseClient = WebTestClient.bindToServer().codecs(clientDefaultCodecsConfigurer -> {
      clientDefaultCodecsConfigurer.defaultCodecs().jackson2JsonEncoder(this.encoder);
      clientDefaultCodecsConfigurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(new ObjectMapper(), MediaType.APPLICATION_JSON));})
    .baseUrl(Urls.BASE_URL)
    .build();
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
