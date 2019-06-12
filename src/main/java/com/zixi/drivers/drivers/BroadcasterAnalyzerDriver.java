package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class BroadcasterAnalyzerDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	protected static final int NUMBER_OF_THREADS = 5 ;
	
	
	public static void main(String[] args) throws Exception {
		BroadcasterAnalyzerDriver analyzerDriver = new BroadcasterAnalyzerDriver();
		//System.out.println(analyzerDriver.getStreamTR101Statistic("admin", "1234", "10.7.0.66", "4444", "H264_25fps_640_480").getResult() );
		
		System.out.println (analyzerDriver.boadcasterCompareStatisticOnAllInputsStreams("admin", "1234", "10.7.0.75", "4444", "tg") .getResult() );
	}
	
	
	public DriverReslut boadcasterBackupIsNotChosen(String login_ip, String userName, String userPassword, 
	String uiport, String failover_group_name, String backup_stream_name) throws Exception 
	{
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPassword, login_ip, uiport);
		
		ArrayList<JSONArray> componentStats = new ArrayList<JSONArray>();
		
		// Returns Json array of failover group components against their number of contributed packets.
		componentStats.add(new JSONArray(apiworker.sendGet("http://" + login_ip + ":" +  uiport + "/zixi/streams.json", failover_group_name,
		GET_FAILOVER_COMPONENTS, responseCookieContainer, login_ip, this, uiport)));
		Thread.sleep(20_000);
		// Returns Json array of failover group components against their number of contributed packets.
		componentStats.add(new JSONArray(apiworker.sendGet("http://" + login_ip + ":" +  uiport + "/zixi/streams.json", failover_group_name,
		GET_FAILOVER_COMPONENTS, responseCookieContainer, login_ip, this, uiport)));
		
		JSONArray first = componentStats.get(0);
		JSONArray last  = componentStats.get(componentStats.size() - 1);
		
		int ref1 = -1, ref2 = -5 ;
		for(int i = 0; i < first.length(); i++)
		{
			JSONArray tmp = first.getJSONArray(i);
			if(tmp.getString(0).equals(backup_stream_name))
				ref1 = tmp.getInt(1);
		}
		
		for(int i = 0; i < last.length(); i++)
		{
			JSONArray tmp = last.getJSONArray(i);
			if(tmp.getString(0).equals(backup_stream_name))
				ref2 = tmp.getInt(1);
		}
		
		return new DriverReslut(( ref2 - ref1 ) + ""); 
	}
	
	// This method is used to turn on/off TR101 analyzer on specific stream identified by its name - broadcaster.
	public DriverReslut turnAnalyzerOnSingleStream(String userName, String userPass, String login_ip, String uiport, String input, String analyze) throws Exception 
	{
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);
		return new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" +  uiport + "/analyze_input.json?input=" + input + "&analyze=" + analyze, input,
		ANALIZE_MODE, responseCookieContainer, login_ip, this, uiport));
	}
	
	// This method is used to turn on/off TR101 analyzer on specific stream identified by its name - feeder.
	public DriverReslut feederTurnAnalyzerOnSingleStream(String userName, String userPass, String login_ip, String uiport, 
	String input, String feedInIP, String port, String analyze) throws Exception 
	{
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);
		return new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" +  uiport + "/analyze_input.json?mip=" + input +
		"&port=" + port + "&ip=" + feedInIP +"&analyze=" + analyze, input,
		FEEDER_ADD_FAILOVER_GROUP, responseCookieContainer, login_ip, this, uiport));
	}
	
	// This method is used to turn on/off TR101 analyzer on all inputs  - broadcaster.
	public DriverReslut turnAnalyzerOnAllInputs(String userName, String userPass, String login_ip, String uiport, String analyze) throws InterruptedException, ExecutionException
	{
		 ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
		 BroadcasterInputStreamDriver broadcasterInputStreamDriver = new BroadcasterInputStreamDriver();
		 String[] streamsNames = null;
		// Retrieve input stream names from a broadcaster server.
		try { streamsNames  = broadcasterInputStreamDriver.retrieveAllInputStreamsFromBroadcaser(login_ip, userName, userPass, uiport); } catch (Exception e) { e.printStackTrace(); }
		
		ArrayList<Callable<String>> callablesTasks = new ArrayList<Callable<String>>();
		int taskNumbers = streamsNames.length;
		
		for(int i = 0; i < taskNumbers; i ++)
		{
			
			ExecuteTask task = new ExecuteTask( userName,  userPass,  login_ip,  uiport,  streamsNames[i],  analyze);
			
			callablesTasks.add(task);
		}
		
		List<Future<String>> futures = executorService.invokeAll(callablesTasks);
		String result = "analyzed";
		for(Future future : futures)
		{
			
			if( !( (String)future.get() ).equals("analyzed") )
			{
				result = "not analyzed";
			}
		}
		return new DriverReslut(result);
	}
	
	// This method is used to turn on/off TR101 analyzer on all inputs  - feeder.
	public DriverReslut feederTurnAnalyzerOnAllInputs(String userName, String userPass, String login_ip, String uiport, String analyze) throws InterruptedException, ExecutionException
	{
		 ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
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
			streamsNames.getString(i), streamsNames.getString(i+1), streamsNames.getInt(i+2)+ "", analyze);
			callablesTasks.add(task);
		}
		
		List<Future<String>> futures = executorService.invokeAll(callablesTasks);
		String result = "analyzed";
		for(Future<String> future : futures)
		{
			if( !( (String)future.get() ).equals("Analysis started.") )
			{
				result = "not analyzed";
			}
		}
		return new DriverReslut(result);
	}
	
	// Inner class  - used to implement Callable interface.
	private class ExecuteTask  implements Callable<String> {
		String userName, userPass, login_ip, uiport, input, analyze;
		
		public ExecuteTask(String userName, String userPass, String login_ip, String uiport, String input, String analyze)
		{
			this.userName = userName; this.userPass =userPass; this.login_ip = login_ip; this.uiport = uiport; this.input = input; this.analyze = analyze;
		}
		
		public String call() throws Exception 
		{
			BroadcasterAnalyzerDriver broadcasterAnalyzerDriver = new BroadcasterAnalyzerDriver();
			return broadcasterAnalyzerDriver.turnAnalyzerOnSingleStream( userName,  userPass,  login_ip,  uiport,  input,  analyze).getResult();
		}
    }
	
	// Inner class  - used to implement Callable interface.
	private class FeederExecuteTask  implements Callable<String> {
		String userName, userPass, login_ip, uiport, input, analyze, feedInIP, port;
		
		public FeederExecuteTask(String userName, String userPass, String login_ip, String uiport, String input, String feedInIP, String port, String analyze)
		{
			this.userName = userName; this.userPass =userPass; this.login_ip = login_ip; 
			this.uiport = uiport; this.input = input; this.analyze = analyze; this.feedInIP = feedInIP; this.port = port;
		}
		
		public String call() throws Exception 
		{
			BroadcasterAnalyzerDriver broadcasterAnalyzerDriver = new BroadcasterAnalyzerDriver();
			return broadcasterAnalyzerDriver.feederTurnAnalyzerOnSingleStream( userName,  userPass,  login_ip,  uiport,  input, feedInIP, port, analyze).getResult();
		}
    }
	
	protected String getStreamTR101ArrayData(String userName, String userPass, String login_ip, String uiport, String input) throws Exception 
	{
		// Get UI access - login.
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);
		return  apiworker.sendGet("http://" + login_ip + ":" +  uiport + "/input_stream_stats.json?id=" + input, "", TR101, responseCookieContainer, login_ip, this, uiport);
	}
	
	public DriverReslut feederCompareStatisticCcErrors(String login_ip, String userName, String userPassword, 
	String uiport, String streams, String ref_stream) throws Exception
	{
		// Get UI access - login.
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", 
		userName, userPassword, login_ip, uiport);
		
		ArrayList<String> inputsStreamNames = new ArrayList<>(Arrays.asList(streams.split("@")));
		
		JSONArray feederAllInpusDataJsonArray = new JSONArray (apiworker.sendGet("http://" + login_ip + ":" +  uiport + "/inputs_data",
		"", FEEDER_GET_INPUTS_DATA, responseCookieContainer, login_ip, this, uiport));
		
		Hashtable<String,JSONArray> inputStreamNamesToCcErrorsMap = new Hashtable<>();
		
		for(int i = 0; i < feederAllInpusDataJsonArray.length(); i++)
		{
			JSONObject tmpStreamObj = feederAllInpusDataJsonArray.optJSONObject(i);
			if(tmpStreamObj != null)
			{
				String name = tmpStreamObj.getString("name");
				JSONArray tr101Data = tmpStreamObj.getJSONArray("tr101");
				inputStreamNamesToCcErrorsMap.put(name, tr101Data);
			}
		}
		
		JSONArray refTR101 =  inputStreamNamesToCcErrorsMap.get(ref_stream);
		int refCcErors = 0;
		for(int i = 0; i < refTR101.length(); i++)
		{
			JSONArray arrAsObj = refTR101.getJSONArray(i);
			for(int j = 0; j < arrAsObj.length(); j++ )
			{
				JSONObject tmp = arrAsObj.getJSONObject(j);
				if(tmp.getString("name").equals("Continuity_count_error"))
				{
					refCcErors = tmp.getInt("count");
				}
			}
		}
		
		String result = "passed";
		looopExit:
		for (String streamName : inputStreamNamesToCcErrorsMap.keySet()) {
		    if(inputsStreamNames.contains(streamName))
		    {
		    	JSONArray inputTR101 = inputStreamNamesToCcErrorsMap.get(streamName);
		    	
		    	for(int i = 0; i < inputTR101.length(); i++)
				{
					JSONArray arrAsObj = inputTR101.getJSONArray(i);
					for(int j = 0; j < arrAsObj.length(); j++ )
					{
						JSONObject tmp = arrAsObj.getJSONObject(j);
						if(tmp.getString("name").equals("Continuity_count_error"))
						{
							int inCcErors = tmp.getInt("count");
							if(inCcErors >= refCcErors)
							{
								result = "passed";
							}else
							{
								result = "failed";
								break looopExit;
							}
						}
					}
				}
		    }
		}
		return new DriverReslut(result);
	}
	
	public DriverReslut boadcasterCompareStatisticOnAllInputsStreams(String userName, String userPass, 
	String login_ip, String uiport, String streamReferenceName)
	{
		BroadcasterInputStreamDriver broadcasterInputStreamDriver = new BroadcasterInputStreamDriver();
		// Retrieve input streams names.
		String[] streamsNames = null;
		try {
			streamsNames = broadcasterInputStreamDriver.retrieveAllInputStreamsFromBroadcaser(login_ip, userName, userPass, uiport);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if(streamsNames == null || streamsNames.length == 0) return new DriverReslut("Bad flow");
		
		Map<String, JSONArray> streamNameStatisticJsonMap = new HashMap<String, JSONArray>();
		for(int i = 0; i < streamsNames.length; i++)
		{
			try {
				JSONArray TR101StatJsonArray = new JSONArray(getStreamTR101ArrayData( userName,  userPass, login_ip, uiport, streamsNames[i]));
				streamNameStatisticJsonMap.put(streamsNames[i], TR101StatJsonArray);
			} catch (JSONException e) {
				System.out.println("@BroadcasterAnalyzerDriver.boadcasterCompareStatisticOnAllInputsStreams: Cant get a statistic.");
			} catch (Exception e) {
				System.out.println("@BroadcasterAnalyzerDriver.boadcasterCompareStatisticOnAllInputsStreams: Cant get a statistic.");
			}
		}
		if(streamNameStatisticJsonMap.size()!=0)
		{
			if(compareStats(streamNameStatisticJsonMap, streamReferenceName ))
				return new DriverReslut("passed");
			else return new DriverReslut("failed");
		}
		return new DriverReslut("Bad flow");
	}
	
	public DriverReslut boadcasterFozenDetection(String userName, String userPass, String login_ip, String uiport, String streamNameToTestAgainst,
	String detectionCounter, String streamLenthSec) throws JSONException, Exception
	{
		try {
			Thread.sleep(1000 * Integer.parseInt(streamLenthSec));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		JSONArray TR101StatJsonArray = new JSONArray(getStreamTR101ArrayData( userName,  userPass, login_ip, uiport, streamNameToTestAgainst));
		int measuredFrozenDetectionCounter = -1;
		for(int i = 0; i < TR101StatJsonArray.length(); i++)
		{
			JSONArray tr101Param = TR101StatJsonArray.getJSONArray(i);
			if(tr101Param.getString(0).equals("Frozen_video"))
			{
				measuredFrozenDetectionCounter = tr101Param.getInt(1);
			}
		}
		
		if( ((Integer.parseInt(detectionCounter) - 1) <= measuredFrozenDetectionCounter ) & (Integer.parseInt(detectionCounter) >= measuredFrozenDetectionCounter))
			return new DriverReslut("passed");
		else return new DriverReslut("failed");
	}
	
	
	public DriverReslut boadcasterAllCqaDetection(String userName, String userPass, String login_ip, String uiport, String streamNameToTestAgainst,
	String detectionCounter, String streamLenthSec) throws JSONException, Exception
	{
		Thread.sleep(1000 * Integer.parseInt(streamLenthSec));
		JSONArray TR101StatJsonArray = new JSONArray(getStreamTR101ArrayData( userName,  userPass, login_ip, uiport, streamNameToTestAgainst));
		int measuredFrozenDetectionCounter = -1;
		int low_video_quality =	-1, audio_clipping = -1, silent_audio = -1, blank_picture = -1;
		//Low_video_quality' || name == 'Audio_clipping' || name == 'Silent_audio' || name =='Blank_picture'
		for(int i = 0; i < TR101StatJsonArray.length(); i++)
		{
			JSONArray tr101Param = TR101StatJsonArray.getJSONArray(i);
			String    cqaType    = tr101Param.getString(0);
			switch(cqaType)
			{
				case "Frozen_video"		: 	measuredFrozenDetectionCounter 	= tr101Param.getInt(1);
				case "Low_video_quality": 	low_video_quality 				= tr101Param.getInt(1);
				case "Audio_clipping"	: 	audio_clipping 					= tr101Param.getInt(1);
				case "Silent_audio"		: 	silent_audio 					= tr101Param.getInt(1);
				case "Blank_picture"	: 	blank_picture 					= tr101Param.getInt(1);
			}
		}
		if( 
			(measuredFrozenDetectionCounter >= 1 )
			&&
			(low_video_quality >= 1 )
			&&
			(audio_clipping >= 1)
			&&
			(silent_audio >= 1)
			&&
			(blank_picture >=1 )
		  )
			return new DriverReslut("passed");
		else return new DriverReslut("failed");
	}

	protected boolean compareStats(Map<String, JSONArray> streaNameStatisticJsonMap, String referenceStreamName)
	{
		// [["Continuity_count_error",0],["Frozen_video",0],["Blank_picture",0],["Silent_audio",0],["Low_video_quality",0],["Audio_clipping",0]]
		JSONArray referenceStreamStatisticArray = streaNameStatisticJsonMap.get(referenceStreamName);
		int referenceCcCount = -1;
		JSONObject tmpStatRefObj = null;
		
		for (int i = 0; i < referenceStreamStatisticArray.length(); i++) {
			JSONArray ststObjArray = referenceStreamStatisticArray.getJSONArray(i);
			if(ststObjArray.getString(0).equals("Continuity_count_error"))
			{
				referenceCcCount = ststObjArray.getInt(1);
				break;
			}
		}
		
		boolean functionResultFlag = true;
		if(referenceCcCount == -1)
			return functionResultFlag = false;
		for (String key : streaNameStatisticJsonMap.keySet())
		{
			JSONArray tmpArrayOfArrausOfStreamStattistic = streaNameStatisticJsonMap.get(key);
			
    		
    		// Walk through array.
    		for (int i = 0; i < tmpArrayOfArrausOfStreamStattistic.length(); i++) {
    			JSONArray ststObjArray = tmpArrayOfArrausOfStreamStattistic.getJSONArray(i);
    			int ccCount;
    			if(ststObjArray.getString(0).equals("Continuity_count_error"))
    			{
    				ccCount = ststObjArray.getInt(1);
    				if(ccCount < referenceCcCount)
    				{
    					System.out.println("The cause is ---------------> ccCount " + ccCount + "referenceCcCount " + referenceCcCount);
    					functionResultFlag = false;
    					break;
    				}
    			}
			}
		}
		return functionResultFlag;
	}
}
