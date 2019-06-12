package com.zixi.drivers.drivers;

import org.testng.Assert;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.entities.TestParameters;
import com.zixi.tools.BroadcasterLoggableApiWorker;
import com.zixi.tools.RemoveInputHelper;


public class BroadcasterSingleInputStreamDeletionDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	
	RemoveInputHelper removeInputHelper = new RemoveInputHelper();
		
	public DriverReslut removeInput(String login_ip, String userName, String userPassword, String streamId, String uiport) throws Exception 
	{
		testParameters 			= new TestParameters("login_ip:"+login_ip, "userName:" + userName , "userPassword:" + userPassword, "streamId:" + streamId, "uiport:" + uiport);
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName , userPassword, login_ip, uiport);
		return new DriverReslut( removeInputHelper.sendGet("http://" + login_ip + ":" + uiport +  "/zixi/remove_stream.json?" + "id=" + streamId, login_ip, responseCookieContainer));
	}
}
