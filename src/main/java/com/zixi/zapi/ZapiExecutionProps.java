package com.zixi.zapi;
import static com.zixi.tools.Macros.*;

import java.net.URISyntaxException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.burt.jmespath.Expression;
import io.burt.jmespath.JmesPath;
import io.burt.jmespath.jackson.JacksonRuntime;


public class ZapiExecutionProps {
	
	public void createNewTestExecutionWithStatus() throws Exception
	{
		String payLoadString = "{\"status\":{\"id\":1},\"projectId\":10003,\"issueId\":10081,\"cycleId\":\"d665d810-e148-4cf0-a358-b0e6145500bb\",\"versionId\":-1,\"assigneeType\":\"currentUser\"}";
		
		// Create execution
		String responseBody = CallApi.restZapiPOST("uri@zixi.com", "",
		"nKw_iEzONL_AgPhMRxsrDysWj98wp0ulvApTYdcFG6U", ZAPI_EXECUTION, payLoadString);
		JmesPath<JsonNode> jmespath = new JacksonRuntime();
		Expression<JsonNode> expression = jmespath.compile("execution.id");
		ObjectMapper mapper = new ObjectMapper();
	    JsonNode actualObj = mapper.readTree(responseBody);
	    JsonNode result = expression.search(actualObj);
	    String execId 	= result.toString();
	    ///rest/zapi/latest/execution/id/quickExecute
	    execId = execId.replace("\"", "");
	    responseBody = CallApi.restZapiPUT("uri@zixi.com", ""
	    		+ "",
	    "", ZAPI_EXECUTION + "/" + execId , "{\"status\":{\"id\":1},\"projectId\":10003,\"issueId\":10081,\"cycleId\":\"d665d810-e148-4cf0-a358-b0e6145500bb\",\"versionId\":-1,\"assigneeType\":\"currentUser\"}");
	}
	
	public static void createNewTestExecutionWithStatus_TestCycle_TestFolder(String status, String projectId, String issueId, String cycleId, String folderId, 
	String versionId, String assigneeType, String zapiUser, String zapiAccesskey, String zapiSecretkey, String testFlowDescription) throws Exception
	{
		String payLoadString = "{\"status\":{\"id\":" + status + "},\"projectId\":" + projectId + 
		",\"issueId\":" + issueId + ",\"cycleId\":\"" + cycleId + "\",\"versionId\":" + versionId + ",\"assigneeType\":\"" + assigneeType +
		"\",\"folderId\":\"" + folderId + "\"}";
		
		// Create execution
		String responseBody = CallApi.restZapiPOST(zapiUser, zapiAccesskey,
		zapiSecretkey, ZAPI_EXECUTION, payLoadString);
		
		JmesPath<JsonNode> jmespath = new JacksonRuntime();
		Expression<JsonNode> expression = jmespath.compile("execution.id");
		ObjectMapper mapper = new ObjectMapper();
	    JsonNode actualObj = mapper.readTree(responseBody);
	    JsonNode result = expression.search(actualObj);
	    String execId 	= result.toString();
	    execId = execId.replace("\"", "");
	    
	    // Update execution.
	    responseBody = CallApi.restZapiPUT(zapiUser, zapiAccesskey,
	    zapiSecretkey, ZAPI_EXECUTION + "/" + execId , 
	    "{\"status\":{\"id\":" + status + "},\"projectId\":" + projectId + ",\"issueId\":" + issueId + 
	    ",\"cycleId\":\"" + cycleId + "\",\"versionId\":" +  versionId + ",\"assigneeType\":\"" +
	    assigneeType + "\",\"folderId\":\"" + folderId + "\",\"comment\":\"" + "Automation test execution" + "\"}");
	    
	    // Add attachment to execution 
	    CallApi.restZapiTextPOST(zapiUser, zapiAccesskey,
	    zapiSecretkey, ZAPI_ATTACHMENT + "?issueId=" + issueId + "&versionId=" + versionId + "&entityName=execution" + 
	    "&cycleId=" + cycleId + "&entityId=" + execId + "&comment=_" + "&projectId=" + projectId , testFlowDescription);
	}
	
	public void deleteTestExecution(String execId, String issueId) throws URISyntaxException
	{
		CallApi.restZapiDELETE("uri@zixi.com", "",
		"", ZAPI_EXECUTION + "/" + execId + "?issueId=" + issueId);
	}

	public static void main(String[] args) throws Exception {
		ZapiExecutionProps createExecution = new ZapiExecutionProps();
		//createExecution.createNewTestExecutionWithStatus();
		createExecution.deleteTestExecution("51d40639-9e90-43d6-8a26-faf6b1576dae", "10081");
	}
}
