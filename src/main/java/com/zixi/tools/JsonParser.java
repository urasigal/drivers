package com.zixi.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {

	public String getSourceInputIpById(String streamsJson, String id)
	{
		JSONObject json = null;
		String tmp;
		json = new JSONObject(streamsJson);
		JSONArray inputStreamsJsonArrayObj = json.getJSONArray("streams");
		int numberOfElementsInInputStreamsJsonArrayObj = inputStreamsJsonArrayObj.length();
		for (int i = 0; i < numberOfElementsInInputStreamsJsonArrayObj; i++) 
		{
			json = inputStreamsJsonArrayObj.getJSONObject(i);
			
			tmp = json.get("id").toString();
			if(tmp.equals(id))
			{
				return json.getString("source");
			}
		}
		
		return "";
	}
	
	
	public String getSourceOutputIpById(String streamsJson, String id)
	{
		JSONObject json = null;
		String tmp;
		int bitrate;
		json = new JSONObject(streamsJson);
		JSONArray outputsStreamsJsonArrayObj = json.getJSONArray("outputs");
		
		int numberOfElementsInOutputStreamsJsonArrayObj = outputsStreamsJsonArrayObj.length();
		for (int i = 0; i < numberOfElementsInOutputStreamsJsonArrayObj; i++) 
		{
			json = outputsStreamsJsonArrayObj.getJSONObject(i);
			
			tmp = json.get("stream_id").toString();
			bitrate = json.getInt("bitrate");
			if(tmp.equals(id) && bitrate != 0) 
			{
				return json.getString("ip");
			}
		}
		
		return "";
	}
	
	public static List<String> retriveIpAddresses(String streamsJson)
	{
		List<String> ipList = new ArrayList<String>();
		
		JSONObject json = null;
		
		json = new JSONObject(streamsJson);
		
		JSONArray netDevices = json.getJSONArray("devices");
		
		int numOfDev = netDevices.length();
		
		for (int i = 0; i < numOfDev; i++) 
		{
			json = netDevices.getJSONObject(i);
			
			JSONArray netInterface = json.getJSONArray("connections");
			
			int interfaceLangth = netInterface.length();
			
			for(int j = 0 ; j < interfaceLangth; j ++ )
			{
				json = netInterface.getJSONObject(j);
				ipList.add(json.getString("ip"));
			} 
		}
		return ipList;
	}
	
	public static String getVodSettings(String streamsJson)
	{
		JSONObject json = null;
		json = new JSONObject(streamsJson);
		json = json.getJSONObject("file_transfer");
		int auto_index   = json.getInt("auto_index");
		int progressive  = json.getInt("progressive");
		
		return auto_index + "&" + progressive ;
	}
	
	public static String receiverInputGetStreamIdByname(String streamsJson, String streamName)
	{
		JSONObject json = null;
		String id = null;
		json = new JSONObject(streamsJson);
		JSONArray inputStreams = json.getJSONArray("streams");
		
		int numOfStreams = inputStreams.length();
		for (int i = 0; i < numOfStreams; i++) 
		{
			json = inputStreams.getJSONObject(i);
			if(json.getString("name").equals(streamName))
			{
				id = json.getString("id");
				break;
			}
		}
		return id;
	}
	
	
	public static String receiverOutputGetStreamIdByname(String streamsJson, String streamName)
	{
		JSONObject json = null;
		String id = null;
		json = new JSONObject(streamsJson);
		JSONArray inputStreams = json.getJSONArray("streams");
		
		int numOfStreams = inputStreams.length();
		for (int i = 0; i < numOfStreams; i++) 
		{
			json = inputStreams.getJSONObject(i);
			if(json.getString("name").equals(streamName))
			{
				id = json.getString("out_id");
				break;
			}
		}
		return id;
	}
	
	
	public static ArrayList<String> broadcasterOutputGetStreamIdByname(String streamsJson)
	{
		ArrayList<String> outputIds = new ArrayList<>();
		JSONObject json = null;
		String id = null;
		json = new JSONObject(streamsJson);
		JSONArray outpoutStreams = json.getJSONArray("outputs");
		
		int numOfStreams = outpoutStreams.length();
		for (int i = 0; i < numOfStreams; i++) 
		{
			json = outpoutStreams.getJSONObject(i);
			outputIds.add(json.getString("id"));
		}
		return outputIds;
	}
	
	public static int getInputStreamBitrate(String streamsJson, String streamName)
	{
		JSONObject json = null;
		String id = null;
		json = new JSONObject(streamsJson);
		json = json.getJSONObject("net");
		return json.getInt("bitrate");
	}
	
	public static String getActiveIpOfReceiverInput(Supplier<String> supplier, String streamName)
	{
		JSONObject json = null;
		String IP = null;
		json = new JSONObject(supplier.get()); // Convert an accepted string to Json object. supplier -  it is an instance of the anonymous inner calss
		JSONArray inputStreams = json.getJSONArray("streams"); // Get streams array
		
		int numOfStreams = inputStreams.length();
		
		for (int i = 0; i < numOfStreams; i++) 
		{
			json = inputStreams.getJSONObject(i);
			if(json.getString("name").equals(streamName))
			{
				IP = json.getString("full_address");
				IP =  StringUtils.substringBetween(IP, ": ", ":");
				break;
			}
		}
		
		return IP;
	}

	public static String getBroadcasterVersion(String streamsJson)
	{
		JSONObject json = null;
		String id = null;
		json = new JSONObject(streamsJson);
		return json.getInt("version_major") + "." + json.getInt("version_minor") + "." + json.getInt("version_minor2") 
				+ "." + json.getInt("version_build") + "." + json.getString("platform"); 
	}


	public static int getTranscoderProfiles(String outJson, String profile_name) {
		int pid = -1;
		JSONObject responseJson = new JSONObject(outJson);
		JSONArray outputStreamsArray = responseJson.getJSONArray("profiles");
		String streamName = null;
		
		for (int i = 0; i < outputStreamsArray.length(); i++) 
		{
			//System.out.println("before");
			JSONObject outputStream = outputStreamsArray.getJSONObject(i);
		   
		    //System.out.println("after");
		    //id1 = stream.getString("id");
		    streamName = outputStream.getString("profile_name");
		    if(streamName.equals(profile_name))
		    {
		    	pid = outputStream.getInt("id");
		    	//testID = profileID;
		    }
		}
		return pid;
	}
	
	public static ArrayList< String > feederOutsIds(String inputJson)
	{
		ArrayList <String> ids = new ArrayList<>();
		JSONObject responseJson = new JSONObject(inputJson);
		JSONArray outputStreamsArray = responseJson.getJSONArray("outs");
		
		for (int i = 0; i < outputStreamsArray.length(); i++) 
		{
			JSONObject outputStream = outputStreamsArray.getJSONObject(i);
			ids.add(outputStream.getString("id")); 
		}
		return ids;
	}
	
	
	public static int[] getGoodFramesNumberForInputStream(String inputJson, String streamName)
	{
		int framesAndBytes [] = new int[]{-1,-1,-1};
		int goodFramesPerStream = -1;
		JSONObject responseJson = new JSONObject(inputJson);
		JSONArray inputStreams = responseJson.getJSONArray("streams");
		JSONObject json = null;
		JSONObject wantedjJson = null;
		
		int numOfStreams = inputStreams.length();
		
		for (int i = 0; i < numOfStreams; i++) 
		{
			json = inputStreams.getJSONObject(i);
			
			if(json.getString("id").equals(streamName))
			{
				wantedjJson = json;
				break;
			}
		}
		
		if(wantedjJson != null)
		{
			
			
			int goodBytes  = 	wantedjJson.getInt("good_video_bytes");
			int goodFrames = 	wantedjJson.getInt("good_video_frames");
			int bytes	   = 	wantedjJson.getInt("bytes");
			
			framesAndBytes[0] = goodFrames;
			framesAndBytes[1] = goodBytes;
			framesAndBytes[2] = bytes;
		}
		return framesAndBytes;
	}
//	public static void main(String[] args) {
//		JsonParser.getGoodFramesNumberForInputStream("", "");
//	}

}