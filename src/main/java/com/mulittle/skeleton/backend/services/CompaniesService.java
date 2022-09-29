package com.mulittle.skeleton.backend.services;

import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import com.mulittle.skeleton.backend.model.Company;
import com.mulittle.skeleton.backend.model.Urls;

public class CompaniesService {

  public static final String COMPANIES = "/companies";
  public static final String COMPANY = "/companies/{id}";

  private static final WebTestClient baseClient = WebTestClient.bindToServer()
    .baseUrl(Urls.BASE_URL)
    .build();

  public ResponseSpec registerCompany(Company company) {
    return baseClient
        .post()
        .uri(COMPANIES)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(company)
        .exchange();
  }
  
}
