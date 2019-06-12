package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.ApiWorkir;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class FeederInputStreamDeletionDriver extends BroadcasterLoggableApiWorker implements TestDriver {
	
	public DriverReslut testIMPL(String userName, String userPass, String login_ip, String uiport, String mip, String port, String ip)  throws Exception 
	{	
		// Logging to SUT.
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);	
		// Perform the test...
		driverReslut = new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" +  uiport + "/del_input?mip=" + mip+ "&port=" + port + "&ip=" + ip ,  
		"", UDPMODE, responseCookieContainer, login_ip, this, uiport));
		return driverReslut;
	}
	
	public DriverReslut feederGetInputStreamsNames(String userName, String userPass, String login_ip, String uiport)  throws Exception 
	{	
		// Logging to SUT.
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);	
		
		// The driverResult will contain string representation of org.json.JSONArray content - the array data.
		driverReslut = new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" +  uiport + "/inputs_data",  
		"", FEEDER_GET_STREAMS, responseCookieContainer, login_ip, this, uiport));
		return driverReslut;
	}
	
	public DriverReslut feederGetInputStreamsNamesAndIPs(String userName, String userPass, String login_ip, String uiport)  throws Exception 
	{	
		// Logging to SUT.
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);	
		
		// The driverResult will contain string representation of org.json.JSONArray content - the array data.
		driverReslut = new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" +  uiport + "/inputs_data",  
		"", FEEDER_GET_STREAMS_IPS, responseCookieContainer, login_ip, this, uiport));
		return driverReslut;
	}

	public DriverReslut feederGetInputStreamsIds(String userName, String userPass, String login_ip, String uiport, String group_member_streams)  throws Exception 
	{	
		// Logging to SUT.
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);	
		
		// The driverResult will contain string representation of org.json.JSONArray content - the array data.
		driverReslut = new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" +  uiport + "/inputs_data",  
		group_member_streams, FEEDER_GET_STREAMS_BY_ID, responseCookieContainer, login_ip, this, uiport));
		return driverReslut;
	}
	
	public DriverReslut addFailoverGroup(String userName, String userPass, String login_ip, String uiport,
		   String group_member_streams, String group_name, String search_window, String max_bitrate) throws Exception {
		
		String idsAsString =  feederGetInputStreamsIds(userName, userPass, login_ip, uiport, group_member_streams).getResult().replace("[", "").replace("]", "");
		idsAsString = idsAsString.replaceAll("\\s+", "");
		String [] idsAsStringArray = idsAsString.split(",");
		ArrayList<String> idsAsStringList = new ArrayList<>(Arrays.asList(idsAsStringArray));
		
		StringBuilder failoverGroupComponents = new StringBuilder();
		for(int i = 0 ; i < idsAsStringList.size(); i++)
		{
			failoverGroupComponents.append("&component=").append(idsAsStringList.get(i)).append("&priority=1");
		}
		driverReslut = new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" +  uiport + "/add_failover_input?name=" + group_name + "&search_window=" + search_window + "&max_bitrate=" + max_bitrate +
		failoverGroupComponents, group_member_streams, FEEDER_ADD_FAILOVER_GROUP, responseCookieContainer, login_ip, this, uiport));
		return driverReslut;
	}
	
	public DriverReslut feederDeleteAllInputs(String userName, String userPass, String login_ip, String uiport) throws InterruptedException, ExecutionException
	{
		 ExecutorService executorService = Executors.newFixedThreadPool(5);
		 FeederInputStreamDeletionDriver feederInputStreamDeletionDriver = new FeederInputStreamDeletionDriver();
		 org.json.JSONArray streamsNames = null;
		// Retrieve input stream names from a feeder server.
		
		try { 
			streamsNames = new org.json.JSONArray(feederInputStreamDeletionDriver.feederGetInputStreamsNamesAndIPs(userName, userPass, login_ip, uiport).getResult()); 
		} 
		catch (Exception e) { e.printStackTrace(); }
		
		ArrayList<Callable<String>> callablesTasks = new ArrayList<Callable<String>>();
		int taskNumbers = streamsNames.length();
		
		for(int i = 0; i < taskNumbers; i +=3)
		{
			
			FeederExecuteTask task = new FeederExecuteTask( userName,  userPass,  login_ip,  uiport, 
			streamsNames.getString(i), streamsNames.getString(i+1), streamsNames.getInt(i+2)+ "");
			callablesTasks.add(task);
		}
		
		List<Future<String>> futures = executorService.invokeAll(callablesTasks);
		String result = "all streams are deleted";
		for(Future<String> future : futures)
		{
			if( !((String)future.get()).equals("Input deleted.") )
			{
				result = "failed to delete";
			}
		}
		return new DriverReslut(result);
	}
	
	// Inner class  - used to implement Callable interface.
	private class FeederExecuteTask  implements Callable<String> {
		String userName, userPass, login_ip, uiport, input, feedInIP, port;
		
		public FeederExecuteTask(String userName, String userPass, String login_ip, String uiport, String input, String feedInIP, String port)
		{
			this.userName = userName; this.userPass =userPass; this.login_ip = login_ip; 
			this.uiport = uiport; this.input = input; this.feedInIP = feedInIP; this.port = port;
		}
		
		public String call() throws Exception 
		{
			responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);	
			ApiWorkir aw  = new ApiWorkir();	 	
			return aw.sendGet("http://" + login_ip + ":" +  uiport + "/del_input?mip=" + input + "&port=" + port + "&ip=" + feedInIP, "", FEEDER_ADD_FAILOVER_GROUP, responseCookieContainer, login_ip, this, uiport);
		}
    }
}
