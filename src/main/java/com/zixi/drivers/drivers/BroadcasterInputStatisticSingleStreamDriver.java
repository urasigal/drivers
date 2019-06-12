package com.zixi.drivers.drivers;

import java.util.ArrayList;
import org.json.JSONObject;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterInputStatisticHelper;
import com.zixi.tools.BroadcasterLoggableApiWorker;
import com.zixi.tools.StreamStatisticAnalyzer;


/**
 * @author yuri
 *
 */
public class BroadcasterInputStatisticSingleStreamDriver extends BroadcasterLoggableApiWorker implements TestDriver {

	private BroadcasterInputStatisticHelper broadcasterInputStatisticHelper = new BroadcasterInputStatisticHelper();
	private JSONObject 						statisitcJson;
	
	public DriverReslut testStatistic(String userName, String userPass, String host, String loin_ip, String uiport, String id, String testduration) throws Exception {
		
		ArrayList<Integer> bitRateList   	  =  new ArrayList<Integer>();
						   driverReslut 	  =  new DriverReslut(); 
		int 			   iteratorCount  	  =  Integer.parseInt(testduration);
		int 			   badPrecentage 	  =  0;
		final int          badPercentageRatio =  5; 
		// Logging parameters.
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + loin_ip + ":" + uiport + "/login.htm", userName, userPass, loin_ip, uiport);
		
		// Wait till connection. This test may run right after stream creation.
		try { Thread.sleep(60_000); } catch (InterruptedException e) { e.printStackTrace(); }
		
		for (int i = 0; i < iteratorCount ; i++) { 
			try {Thread.sleep(500);} catch (InterruptedException e) { e.printStackTrace(); }
			
			// Returns a JSON as a response to "/input_stream_stats.json?" request.
			statisitcJson = broadcasterInputStatisticHelper.sendGet("http://" + loin_ip + ":" + uiport + "/input_stream_stats.json?" + "id" + "=" + id, loin_ip, responseCookieContainer);
			// debug printing System.out.println(statisitcJson.toString());
			
			int bitrate = StreamStatisticAnalyzer.getStatBitrate(statisitcJson);
			if (bitrate == 0) { badPrecentage += 1; }
				else bitRateList.add(bitrate);
		}// end of for loop
		float tmpRes = iteratorCount / 100;
			  tmpRes = badPrecentage / tmpRes;
		// Enable some zero rate measurement.
		if(tmpRes > badPercentageRatio)
		{
			driverReslut.setResult( "There were more than " + badPercentageRatio + " bad statistic brobes - zero bitrate presented"); return  driverReslut;
		}
		
		
		int statResults[] = StreamStatisticAnalyzer.getMaxMinAvg(bitRateList);
		
		driverReslut.touchResutlDescription("\nMax bitrate is "  	+ statResults[0]);
		driverReslut.touchResutlDescription("\nMin bitrete is " 	+ statResults[1]);
		driverReslut.touchResutlDescription("\nAverage bitrate is " + statResults[2]);
		driverReslut.setResult("statistic was accepted");
		return driverReslut;
	}
}
