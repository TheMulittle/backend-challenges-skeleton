package com.mulittle.skeleton.backend.services;

import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import com.mulittle.skeleton.backend.model.Company;

public class CompaniesService {

  public ResponseSpec registerCompany(Company company) {
   return WebTestClient.bindToServer()
        .baseUrl("http://localhost:8001")
        .build()
        .post()
        .uri("/v1/companies")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(company)
        .exchange();
  }
  
}
