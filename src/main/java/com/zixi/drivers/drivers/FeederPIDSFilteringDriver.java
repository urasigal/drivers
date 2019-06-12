package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.GET_STREAM_PROGRAMS_IDS;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.zixi.globals.Macros.GET_ELEM_PIDS_OF_PROGR;

import org.json.JSONArray;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class FeederPIDSFilteringDriver  extends BroadcasterLoggableApiWorker implements TestDriver{
	
	public DriverReslut compareToGivenParametersElementryPidsFromFeeder(String userPass, String userName, String login_ip, String uiport, 
	String streamname, String mip, String udp_ip, String pids) throws Exception 
	{
		//prog_details?mip=4000.ts&port=0&ip=file&prog=1
		// Logging to SUT.
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);
		
		//Feeder - get Json array of programs identifiers for particular input stream.
		// Note: for SPTS the size of array will be 1.
		JSONArray programPidsPerStreamName = new JSONArray((new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" + uiport + "/inputs_data", 
		streamname, GET_STREAM_PROGRAMS_IDS, responseCookieContainer, login_ip, this, uiport))).getResult());
		
		// Define a collection to store pairs of program identifiers against their particular program's elementary streams PIDs.
		// For SPTS stream there is only one program, so the size of this hash map will be just one.
		HashMap<Integer, JSONArray> pidsPerProgMap = new HashMap<>(); // { <p : [c1, c2]>, ....}
		// Store argument provided program per channels parameters.
		HashMap<Integer, String> pidsPerProgParameterMap = new HashMap<>(); // { <p : c1@c2>, ...}
		// Convert accepted programs per channels parameter to array view.
		String [] paramProvidedProgToChannels = pids.split(","); // [p1_c1@c2, ...]
		
		for(int j = 0; j < paramProvidedProgToChannels.length; j++)
		{
			String [] tmpProgPerPids = paramProvidedProgToChannels[j].split("_"); // [p, c1@c2]
			pidsPerProgParameterMap.put(Integer.parseInt(tmpProgPerPids[0]), tmpProgPerPids[1]); 
		}
		
		for(int i = 0; i < programPidsPerStreamName.length(); i++)
		{
			//prog_details?mip=4000.ts&port=0&ip=file&prog=1
			pidsPerProgMap.put(programPidsPerStreamName.getInt(i), new JSONArray(apiworker.sendGet("http://" + login_ip + ":" + uiport + "/prog_details?mip=" + mip + "&port=0&ip=" + udp_ip + "&prog=" + programPidsPerStreamName.getInt(i),
			"", GET_ELEM_PIDS_OF_PROGR, responseCookieContainer, login_ip, this, uiport)));
		}
		
		boolean checkChannelPids = false;
		
		// SPTS case.
		if( (pidsPerProgMap.size() == 1) && (pidsPerProgParameterMap.size() == 1) )
		{// SPTS case.
			// Compare { <p : c1@c2>, ...} to { <p : [c1, c2]>, ....} pidsPerProgParameterMap VS pidsPerProgMap
			String [] tmpParamChannelPidsPerOnlyProgram =  pidsPerProgParameterMap.values().toArray(new String[1])[0].split("@"); // [c1, c2, ...] as String array
			JSONArray channelsPidsPerOnlyProgramFromFeeder = pidsPerProgMap.values().toArray(new JSONArray[1])[0]; // [c1, c2, ...] as JSONArray.
			
			if(tmpParamChannelPidsPerOnlyProgram.length == channelsPidsPerOnlyProgramFromFeeder.length())
			{
				ArrayList<String> tmpParamChannelPidsPerOnlyProgramAsArrayList = new ArrayList<String>(Arrays.asList(tmpParamChannelPidsPerOnlyProgram));
				for(int k = 0; k < channelsPidsPerOnlyProgramFromFeeder.length(); k++)
				{
					checkChannelPids = false;
					if(tmpParamChannelPidsPerOnlyProgramAsArrayList.contains(channelsPidsPerOnlyProgramFromFeeder.getInt(k) + ""))
					{
						checkChannelPids = true;
					}
				}
			}	
		}
		if(checkChannelPids)
			return new DriverReslut("Pids are correct");
		else return new DriverReslut("Pids are wrong");
	}
}
