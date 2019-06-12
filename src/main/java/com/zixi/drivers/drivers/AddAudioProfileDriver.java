package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.*;

import com.zixi.tools.*;
import com.zixi.entities.TestParameters;

public class AddAudioProfileDriver extends BroadcasterLoggableApiWorker implements TestDriver
{	
	public String testIMPL(String userName, String userPass, String login_ip,
	String uiport, String profile_name, String enc, String bitrate,
	String profile, String testid) throws Exception {
		
		testParameters = new TestParameters("userName:"+ userName, "userPass:"+ userPass, "login_ip:"+ login_ip, "uiport" + uiport, "profile_name" + profile_name +
		"enc" + enc, "bitrate" + bitrate, "profile" + profile, "testid" + testid);
		// Extract session credentials parameters.
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName , userPass, login_ip, uiport);
		// http://10.7.0.47:4444/zixi/add_aac_profile.json?profile_name=audio_profile_name&enc=1&bitrate=6000&profile=0
		return apiworker.sendGet("http://" + login_ip + ":" + 4444 + "/zixi/add_aac_profile.json?profile_name=" + profile_name + "&enc=" + enc +
				 "&bitrate=" + bitrate + "&profile=" + profile, "", ADD_TRANSCODER_PROFILE, responseCookieContainer, login_ip, this, uiport);
		
	}
	
}
