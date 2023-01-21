package com.mulittle.skeleton.backend.services;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import com.mulittle.skeleton.backend.model.Company;
import com.mulittle.skeleton.backend.webclient.WebTestClientFactory;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class CompaniesService {

  private static final String COMPANIES = "/companies";
  private static final String COMPANY = "/companies/{id}";

  private final WebTestClient baseClient;

  @Autowired
  public CompaniesService(WebTestClientFactory webClientFactory) {
    this.baseClient = webClientFactory.getBaseWebClient();
  }

  public ResponseSpec registerCompany(Company company) {
    return baseClient
        .post()
        .uri(COMPANIES)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(company)
        .exchange();
  }

  public ResponseSpec deleteCompany(Integer id) {
    return baseClient
        .delete()
        .uri(uriBuilder -> uriBuilder.path(COMPANY).build(id))
        .exchange();
  }

  public ResponseSpec deleteCompany(Company company) {
    return deleteCompany(company.getId());
  }

  public ResponseSpec retrieveCompanies() {
    return baseClient
    .get()
    .uri(COMPANIES)
    .exchange();
  }
}
