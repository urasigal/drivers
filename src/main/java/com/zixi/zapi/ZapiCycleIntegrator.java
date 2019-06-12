package com.zixi.zapi;

import static com.zixi.tools.Macros.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.json.JSONArray;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zixi.drivers.drivers.TestDriver;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;
import io.burt.jmespath.Expression;
import io.burt.jmespath.JmesPath;
import io.burt.jmespath.jackson.JacksonRuntime;
import views.html.ac.internal.main;

public class ZapiCycleIntegrator extends BroadcasterLoggableApiWorker implements TestDriver{
	
	public static String createTestCycle(String expand, String clonedCycleId, String cycleName, String build, String environment, 
	String description, String startDate, String endDate, String projectId, String versionId, String zapiUser, String zapiAccesskey,
	String zapiSecretkey) throws Exception 
	{
		String payLoadString = "{\"name\":\"" + cycleName + "\",\"build\":\"" + build + "\",\"environment\":\""
		+ environment + "\",\"description\":\"" + description + "\",\"startDate\":\"" + 
		 startDate + "\",\"endDate\":\"" + endDate + "\",\"projectId\":" + projectId +",\"versionId\":" + versionId + "}";
		
		// Create cycle
		String responseBody = CallApi.restZapiPOST(zapiUser, zapiAccesskey,
		zapiSecretkey, ZAPI_CYCLE + "?expand=" + expand + "&clonedCycleId=" + clonedCycleId, payLoadString);
		return responseBody;
	}
	
	public DriverReslut addTestCycleSetup(String build, String expand, String clonedCycleId, String cycleName, String environment, 
	String description, String startDate, String endDate, String projectId, String versionId, String zapiUser, String zapiAccesskey,
	String zapiSecretkey) throws Exception {
		String cycleID = checkExistenceOfTestCycleByName(cycleName, projectId, versionId, zapiUser, zapiAccesskey, zapiSecretkey);
		if(cycleID == null)
		{
			String responseBody = ZapiCycleIntegrator.createTestCycle(expand, clonedCycleId, cycleName, build,
			environment, description, startDate, endDate, projectId, versionId, zapiUser, zapiAccesskey, zapiSecretkey);
			
			JmesPath<JsonNode> jmespath = new JacksonRuntime();
			Expression<JsonNode> expression = jmespath.compile("id");
			ObjectMapper mapper = new ObjectMapper();
		    JsonNode actualObj = mapper.readTree(responseBody);
		    JsonNode result = expression.search(actualObj);
		    cycleID 	= result.toString();
		}
	    BroadcasterLoggableApiWorker.zapiCycleID = cycleID;
	    
	    cycleID = cycleID.replace("\"", "");
	   
		Writer fileWriter = new FileWriter("src/main/resources/cycleid");
		fileWriter.write(cycleID);
		fileWriter.close();
		driverReslut =  new DriverReslut("success");
		
		return driverReslut;
	}
	
	
	public static String getFolderIdFromCycle( String cycleId, String versionId, String projectId, String folderName, String zapiUser, 
	String zapiAccesskey, String zapiSecretkey) throws URISyntaxException, JsonProcessingException, IOException
	{
		
		String responseBody = CallApi.restZapiGET(zapiUser, zapiAccesskey,
		zapiSecretkey, ZAPI_FOLDERS + "?projectId=" + projectId + "&versionId=" + versionId + "&cycleId=" + cycleId);
		
		JmesPath<JsonNode> jmespath = new JacksonRuntime();
		Expression<JsonNode> expression = jmespath.compile("[?name=='" + folderName + "'].id");
		ObjectMapper mapper = new ObjectMapper();
	    JsonNode actualObj = mapper.readTree(responseBody);
	    JsonNode result = expression.search(actualObj);
	    String folder 	= result.toString().replace("\"", "").replace("]", "").replace("[", "");
		return folder;
	}
	
	public static String checkExistenceOfTestCycleByName(String cycleName, String projectId, String versionId, String zapiUser, String zapiAccesskey,
	String zapiSecretkey) throws Exception
	{
		String responseBody = CallApi.restZapiGET(zapiUser, zapiAccesskey,
		zapiSecretkey, ZAPI_SEARCH_CYCLE + "?expand=executionSummaries&projectId=" + projectId + "&versionId=" + versionId);
		JmesPath<JsonNode> jmespath = new JacksonRuntime();
		Expression<JsonNode> expression = jmespath.compile("[?name=='" + cycleName + "'].id");
		ObjectMapper mapper = new ObjectMapper();
	    JsonNode actualObj = mapper.readTree(responseBody);
	    JsonNode result = expression.search(actualObj);
	    JSONArray cycleIds = new JSONArray( result.toString());
	    if (cycleIds.length() > 1)
	    	throw new Exception("More than one cycle IDs is found. Please clean the setup");
	    if(cycleIds.length() == 1)
	    	return cycleIds.getString(0);
	    if(cycleIds.length() == 0)
	    	return null;
	   return null;
	}
}
