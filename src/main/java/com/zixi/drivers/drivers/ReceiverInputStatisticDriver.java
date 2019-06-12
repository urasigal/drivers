package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.*;

import java.util.ArrayList;

import org.testng.Reporter;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.ApiWorkir;
import com.zixi.tools.BroadcasterInitialSecuredLogin;
import com.zixi.tools.BroadcasterLoggableApiWorker;
import com.zixi.tools.StreamStatisticAnalyzer;

public class ReceiverInputStatisticDriver extends BroadcasterLoggableApiWorker implements TestDriver {

	private ArrayList<Integer> 		bitrateList             = new ArrayList();
	private StreamStatisticAnalyzer streamStatisticAnalyzer = new StreamStatisticAnalyzer();
	private boolean 				stopFlag                = true; // Used to stop statistic measurement at "continiousCheck" method.

	public DriverReslut testIMPL(String userName, String userPass, String login_ip, String uiport, String id, String testduration) throws Exception {
		
		int singleBitrateProbe = 0;
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet( "http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);

		String streamId = apiworker.sendGet("http://" + login_ip + ":" + uiport + "/in_streams.json?complete=1", id, RECEIVERIDMODE, responseCookieContainer, login_ip, this, uiport);
		
		try { Thread.currentThread().sleep(20000); } catch (InterruptedException e) {e.printStackTrace();}
		
		for (int i = 0; i < Integer.parseInt(testduration); i++) 
		{
			try { Thread.currentThread().sleep(1000);}  catch  (InterruptedException e) { e.printStackTrace(); }
			
			singleBitrateProbe =  Integer.parseInt(apiworker.sendGet("http://" + login_ip + ":" + uiport + "/in_stats.json?id=" + streamId, id, RECEIVERSTATISTICMODE,
			responseCookieContainer, login_ip, this, uiport));
			
			if (singleBitrateProbe == 0)
			{
				return new DriverReslut("zero bit rate is detected"); 
			}
		
			bitrateList.add(singleBitrateProbe);
		}
		
		int[] statArray = streamStatisticAnalyzer.getMaxMinAvg(bitrateList);
		Reporter.log("Max bitrate  is " 	+ statArray[0] 	+ 	"");
		Reporter.log("Min bitrate is " 		+ statArray[1] 	+ 	"");
		Reporter.log("Average bitrate is "	+ statArray[2] 	+ 	"");
		
		return new DriverReslut("tested");
	}
	
	
public DriverReslut continiousCheck(String userName, String userPass, String login_ip, String uiport, 
		String[] responseCookieContainer, String id) throws Exception {
		
		ArrayList<String> statList = new ArrayList<>();
		
		// Login to a receiver server.
		this.responseCookieContainer = responseCookieContainer;
		
		// Get internal stream ID.
		String streamId = apiworker.sendGet("http://" + login_ip + ":" + uiport + "/in_streams.json?complete=1", id, RECEIVERIDMODE, responseCookieContainer, login_ip, this, uiport);
		
		// Measure statistics and save it to buffer.
		while (stopFlag) 
		{
			try { Thread.currentThread().sleep(1000);}  catch  (InterruptedException e) { e.printStackTrace(); }
		
			statList.add(apiworker.sendGet("http://" + login_ip + ":" + uiport + "/in_stats.json?id=" + streamId, id, RECEIVERSTATDATA,
			responseCookieContainer, login_ip, this, uiport));
		}
		
		return new DriverReslut(statList);
	}

	public boolean isStopFlag() {
		return stopFlag;
	}
	
	public void setStopFlag(boolean stopFlag) {
		this.stopFlag = stopFlag;
	}
}
