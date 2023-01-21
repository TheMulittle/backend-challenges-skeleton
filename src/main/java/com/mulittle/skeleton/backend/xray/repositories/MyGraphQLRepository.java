package com.mulittle.skeleton.backend.xray.repositories;

import java.util.List;

import com.graphql.generated.AddEvidenceResult;
import com.graphql.generated.AttachmentDataInput;
import com.graphql.generated.FolderSearchInput;
import com.graphql.generated.TestExecutionResults;
import com.graphql.generated.TestResults;
import com.graphql.generated.TestRun;
import com.graphql.generated.TestTypeInput;
import com.graphql_java_generator.annotation.RequestType;
import com.graphql_java_generator.client.graphqlrepository.GraphQLRepository;
import com.graphql_java_generator.client.graphqlrepository.PartialRequest;
import com.graphql_java_generator.exception.GraphQLRequestExecutionException;

@GraphQLRepository
public interface MyGraphQLRepository {

	@PartialRequest(request = """
		{
			total
			results {
				issueId
			}
		}
	""")
	public TestResults getTests(String jql, List<String> issueIds, String projectId, TestTypeInput testType, String modifiedSince, Integer limit, Integer start, FolderSearchInput folder) throws GraphQLRequestExecutionException;

	@PartialRequest(request = """
		{
			total
			results {
				issueId
			}
		}
	""")
	public TestExecutionResults getTestExecutions(String jql, List<String> issueIds, String projectId, Integer limit, Integer start, String modifiedSince) throws GraphQLRequestExecutionException;

	@PartialRequest(request = """
		{
			id
		}
	""")
	public TestRun getTestRun(String testCaseInternalId, String testExecutionInternalId) throws GraphQLRequestExecutionException;

	@PartialRequest(requestName = "getTestRunById", request = """
		{
			evidence {
				filename
				downloadLink
				id
			}
		}
		""")
	public TestRun getTestRunAttachmentLinks(String testRunInternalId) throws GraphQLRequestExecutionException;

	@PartialRequest(requestType = RequestType.mutation, request = """
		{ 
			addedEvidence 
		}
	""")
	public AddEvidenceResult addEvidenceToTestRun(String testRunInternalId, List<AttachmentDataInput> attachments) throws GraphQLRequestExecutionException;

	@PartialRequest(requestType = RequestType.mutation, request = "{}")
	public String updateTestRunStatus(String testRunInternalId, String status) throws GraphQLRequestExecutionException;

	@PartialRequest(requestType = RequestType.mutation, request = "{}")
	public String updateTestRunComment(String testRunInternalId, String status) throws GraphQLRequestExecutionException;
}