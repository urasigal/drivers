package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.RECORDING;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.entities.TestParameters;
import com.zixi.tools.BroadcasterInputStreamManipulator;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class BroadcasterInputStreamManipulatorDriver  extends BroadcasterLoggableApiWorker implements TestDriver
{
	public DriverReslut testIMPL( String userName, String userPass, String login_ip, String uiport, String streamId, String stopStart, String streamType ) throws Exception
	{
		boolean onOff =  false;
		// Stream manipulator class - actually performs a stream stop start operation.
		BroadcasterInputStreamManipulator broadcasterInputStreamManipulator = new BroadcasterInputStreamManipulator();
		switch(stopStart){
			case "on"	:	 onOff = true;
			case "off"	: 	 onOff = false;
		}
		driverReslut = new DriverReslut(broadcasterInputStreamManipulator.broadcasterStopStartPushInputStream(login_ip, uiport, userName, userPass, onOff, streamType, streamId));
		return driverReslut;
	}
	
	public DriverReslut testIMPL( String userName, String userPass, String login_ip, String uiport, String id, String duration_ms ) throws Exception
	{
		// Logging to broadcaster server.
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);
		
		String firstRes =  new DriverReslut(apiworker.sendGet("http://"+ login_ip +":"+ uiport + "/set_live_recording.json?id=" + id + "&on=" + 1, id, RECORDING,
		responseCookieContainer, login_ip, this, uiport)).getResult();
		
		Thread.sleep(Integer.parseInt(duration_ms));
		
		String second =  new DriverReslut(apiworker.sendGet("http://"+ login_ip +":"+ uiport + "/set_live_recording.json?id=" + id + "&on=" + 0, id, RECORDING,
		responseCookieContainer, login_ip, this, uiport)).getResult();
		
		if( (firstRes + " " + second).equals("1 0") )
		{
			return new DriverReslut("stream was successfully recorded");
		}
		
		return new DriverReslut("stream recording is failed");
	}
	
	
	public DriverReslut startRecording( String userName, String userPass, String login_ip, String uiport, String id ) throws Exception
	{
		// Logging to broadcaster server.
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);
		
		String firstRes =  new DriverReslut(apiworker.sendGet("http://"+ login_ip +":"+ uiport + "/set_live_recording.json?id=" + id + "&on=" + 1, id, RECORDING,
		responseCookieContainer, login_ip, this, uiport)).getResult();
		
		if( (firstRes).equals("1") )
		{
			return new DriverReslut("stream was successfully recorded");
		}
		
		return new DriverReslut("stream recording is failed");
	}
}
