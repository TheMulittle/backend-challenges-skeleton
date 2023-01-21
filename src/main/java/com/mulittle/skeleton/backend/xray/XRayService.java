package com.mulittle.skeleton.backend.xray;

import java.lang.reflect.Proxy;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.graphql.generated.AttachmentDataInput;
import com.graphql.generated.Evidence;
import com.graphql.generated.TestExecutionResults;
import com.graphql.generated.TestResults;
import com.graphql.generated.util.MutationExecutor;
import com.graphql.generated.util.QueryExecutor;
import com.graphql_java_generator.client.GraphQLConfiguration;
import com.graphql_java_generator.client.RequestExecution;
import com.graphql_java_generator.client.RequestExecutionSpringReactiveImpl;
import com.graphql_java_generator.client.graphqlrepository.GraphQLRepositoryInvocationHandler;
import com.graphql_java_generator.exception.GraphQLRequestExecutionException;
import com.graphql_java_generator.exception.GraphQLRequestPreparationException;
import com.mulittle.skeleton.backend.xray.repositories.MyGraphQLRepository;

import reactor.core.publisher.Mono;

public class XRayService {

  private final String ITEM_ID_JQL = "id = %s";

  private WebClient webClient;

  private MyGraphQLRepository myGraphQLRepo;

  public void setup(String serviceUrl) throws GraphQLRequestPreparationException {
    this.webClient = webClient.mutate().baseUrl(serviceUrl).build();
    
    RequestExecution executor = new RequestExecutionSpringReactiveImpl(serviceUrl, null,
    this.webClient, GraphQLConfiguration.getWebSocketClient(null),
				null, null);

    GraphQLConfiguration configuration = new GraphQLConfiguration(executor);

    QueryExecutor queryExecutor = new QueryExecutor(configuration);

		MutationExecutor mutationExecutor = new MutationExecutor(configuration);

    //Step 1: create the invocationHandler
		GraphQLRepositoryInvocationHandler<MyGraphQLRepository> invocationHandler = new GraphQLRepositoryInvocationHandler<MyGraphQLRepository>
    (MyGraphQLRepository.class, queryExecutor, mutationExecutor, null);
  
    //Step 2: create the dynamic proxy
    myGraphQLRepo = (MyGraphQLRepository) Proxy.newProxyInstance(this.getClass().getClassLoader(),
      new Class[] { MyGraphQLRepository.class }, invocationHandler);
  }


  public void authenticate(String authenticationEndpoint) {

    if(System.getProperty("xRayId") == null || System.getProperty("xRaySecret") == null) {
      throw new RuntimeException("Missing XRay credentials");
    }

    XRayCredentials credentials = XRayCredentials.builder()
      .clientId(System.getProperty("xRayId"))
      .clientSecret(System.getProperty("xRaySecret"))
      .build();

    this.webClient = GraphQLConfiguration.getWebClient(authenticationEndpoint, null, null, (ExchangeFilterFunction[]) null);
    String token = webClient
      .post()
      .bodyValue(credentials)
      .exchangeToMono(response -> {
        if (response.statusCode().equals(HttpStatus.OK)) {
            return response.bodyToMono(String.class);
        }
        else {
            return response.createException().flatMap(Mono::error);
        }
      })
      .block();

      this.webClient = webClient.mutate().filter(bearerTokenFilter(token.replaceAll("\"", ""))).build();
  }
  
  private ExchangeFilterFunction bearerTokenFilter(String token) {
    return new ExchangeFilterFunction() {

      @Override
      public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        ClientRequest authenticatedRequest = ClientRequest.from(request)
            .header("Authorization", "Bearer " + token)
            .build();
        return next.exchange(authenticatedRequest);
      }
    };
  }

  public String getTestCaseInternalId(String jiraId) throws GraphQLRequestExecutionException {
    TestResults tests = myGraphQLRepo.getTests(ITEM_ID_JQL.formatted(jiraId), null, null, null, null, 1, null, null);

    if(tests.getTotal() == 0) {
      return null;
    }

    return tests.getResults().get(0).getIssueId(); 
  }

  public String getTestRunInternalId(String testCaseInternalId, String testExecutionInternalId) throws GraphQLRequestExecutionException {
    return myGraphQLRepo.getTestRun(testCaseInternalId, testExecutionInternalId).getId(); 
  }

  public String getTestExecutionInternalId(String jiraId) throws GraphQLRequestExecutionException {
    TestExecutionResults testExecutions = myGraphQLRepo.getTestExecutions(ITEM_ID_JQL.formatted(jiraId), null, null, 1, null, null);

    if(testExecutions.getTotal() == 0) {
      return null;
    }

    return testExecutions.getResults().get(0).getIssueId(); 
  }

  public List<String> attach(String testRunId, List<AttachmentDataInput> xRayAttachements) throws GraphQLRequestExecutionException {
    return myGraphQLRepo.addEvidenceToTestRun(testRunId, xRayAttachements).getAddedEvidence();
  }

  public String updateTestRunStatus(String testRunId, String status) throws GraphQLRequestExecutionException {
    return myGraphQLRepo.updateTestRunStatus(testRunId, status);
  }

  public String updateTestRunComment(String testRunId, String comment) throws GraphQLRequestExecutionException {
    return myGraphQLRepo.updateTestRunComment(testRunId, comment);
  }

  public List<Evidence> getTestRunAttachmentLinks(String testRunId) throws GraphQLRequestExecutionException {
    return myGraphQLRepo.getTestRunAttachmentLinks(testRunId).getEvidence();
  }
}