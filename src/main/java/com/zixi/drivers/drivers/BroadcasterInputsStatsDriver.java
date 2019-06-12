package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.STR_STATS;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class BroadcasterInputsStatsDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	
	public DriverReslut totalUpDownTime(String userName, String userPass, String login_ip, String uiport, String wait_time, String up_down) {
		
		try {
			responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);
		} catch (Exception cookeException) {
			// TODO Auto-generated catch block
			return new DriverReslut("Can't login");
		}

		JSONArray before =  new JSONArray(apiworker.sendGet("http://" + login_ip + ":" + uiport
							+ "/zixi/streams.json", "", STR_STATS, responseCookieContainer, login_ip, this, uiport));
		try {
			Thread.sleep(Integer.parseInt(wait_time));
		} catch (NumberFormatException | InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		JSONArray after =  new JSONArray(apiworker.sendGet("http://" + login_ip + ":" + uiport
				+ "/zixi/streams.json", "", STR_STATS, responseCookieContainer, login_ip, this, uiport));
		if(after.length() != before.length())
			try {
				throw new Exception() { public String getMessage(){	
												return "Bad lenth";
					}
				};
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		for(int i = 0; i < before.length(); i++)
		{
			JSONObject	beforeTmp 	 		= after.getJSONObject(i);
			String		beforeStreamIdTmp 	= beforeTmp.getString("strId");
			int 		beforeUpTime 		= beforeTmp.getInt("upTime");
			int 		beforeDownTime 		= beforeTmp.getInt("downTime");
			
			for(int j = 0; j <after.length(); j++)
			{
				JSONObject	afterTmp 	 		= after.getJSONObject(i);
				String		afterStreamIdTmp 	= afterTmp.getString("strId");
				int 		afterUpTime 		= afterTmp.getInt("upTime");
				int 		afterDownTime 		= afterTmp.getInt("downTime");
				
				if(Boolean.parseBoolean( up_down )) // stream up case.
				{
					if(beforeStreamIdTmp.equals(afterStreamIdTmp))
					{
						if(!( beforeDownTime == afterDownTime ) && ( afterUpTime > beforeUpTime ))
							return  driverReslut.setResult("Down time before is " + beforeDownTime + " donw time after is " + afterDownTime + 
													" Up time before " + beforeUpTime + " Up time after " + afterDownTime);
					}
				}else
				{	// Stream down case.
					if(!( beforeUpTime == afterUpTime ) && ( afterDownTime > beforeDownTime )) {
						return  driverReslut.setResult("Down time before is " + beforeDownTime + " donw time after is " + afterDownTime + 
												" Up time before " + beforeUpTime + " Up time after " + afterDownTime);
					}
				}
			}
		}
		return driverReslut.setResult("Excpected behavior");
	}
}
