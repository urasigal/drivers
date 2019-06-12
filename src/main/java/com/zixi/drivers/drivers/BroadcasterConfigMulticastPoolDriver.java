package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.*;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.entities.TestParameters;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class BroadcasterConfigMulticastPoolDriver  extends BroadcasterLoggableApiWorker implements TestDriver {
	
	public DriverReslut testIMPL( String login_ip, String userName, String userPassword, String uiport, String multicast_pool_enabled,
	String multicast_pool_address, String multicast_pool_mask, String multicast_pool_fec_overhead, String multicast_pool_port, String multicast_pool_ttl, 
	String multicast_pool_nic, String  multicast_pool_tos) throws Exception{
		
		driverReslut = new DriverReslut();
		
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPassword, login_ip, uiport);

		driverReslut.touchResutlDescription("http://" + login_ip + ":" + uiport + "/apply_settings.json?" + "multicast_pool_enabled=" + multicast_pool_enabled + 
		"&multicast_pool_address=" + multicast_pool_address + "&multicast_pool_mask=" + multicast_pool_mask + "&multicast_pool_fec_overhead=" + multicast_pool_fec_overhead + 
		"&multicast_pool_port=" +  multicast_pool_port  + "&multicast_pool_ttl=" +  1 + "&multicast_pool_nic=" + multicast_pool_nic + "&multicast_pool_tos=" +  0);
		
		if( apiworker.sendGet("http://" + login_ip + ":" + uiport + "/apply_settings.json?" + "multicast_pool_enabled=" + multicast_pool_enabled + 
		"&multicast_pool_address=" + multicast_pool_address + "&multicast_pool_mask=" + multicast_pool_mask + "&multicast_pool_fec_overhead=" + multicast_pool_fec_overhead + 
		"&multicast_pool_port=" +  multicast_pool_port  + "&multicast_pool_ttl=" +  1 + "&multicast_pool_nic=" + multicast_pool_nic + "&multicast_pool_tos=" +  0 , "",
		CNANGE_SETTINGS_MODE, responseCookieContainer, login_ip, this, uiport).equals(GOOD))
		{
			driverReslut.touchResutlDescription(" " + "http://" + login_ip + ":" + uiport + "/quit");
			apiworker.sendGet("http://" + login_ip + ":" + uiport + "/quit", "", CNANGE_SETTINGS_MODE, responseCookieContainer, login_ip, this, uiport);
			Thread.sleep(30000);
			driverReslut.setResult("GOOD");
			return driverReslut;
		}
		driverReslut.setResult("No settings were applyed");
		return driverReslut;
	}
}
