package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.*;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.entities.TestParameters;
import com.zixi.tools.ApiWorkir;
import com.zixi.tools.BroadcasterLoggableApiWorker;


public class BroadcasterSingleUdpInCreationDriver extends BroadcasterLoggableApiWorker implements TestDriver {
	
	public DriverReslut testIMPL(String userName, String userPass,
	String loin_ip, String ts_port, String id,String rtp_type,String multi_src, String max_bitrate,
	String time_shift,String mcast_ip,String mcast_force, String mcast_port, String nic, String type, String multicast,
	String enc_key, String kompression, String uiport,String mcast_ttl,String enc_type,String mcast_out,String complete, 
	String max_outputs,String on, String mtu, String rist, String rist_latency) throws Exception {
		
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + loin_ip + ":" + uiport + "/login.htm", userName , userPass, loin_ip, uiport);
		
		driverReslut = new DriverReslut(apiworker.sendGet("http://" + loin_ip + ":" + uiport + "/zixi/add_stream.json?type=" 
		+ type + "&id=" + id + "&max_outputs=" + max_outputs + "&mcast_out=" + mcast_out + "&mcast_force="
		+ mcast_force + "&mcast_ip=" + mcast_ip + "&mcast_port=" + mcast_port + "&mcast_ttl=" + mcast_ttl
		+ "&time_shift=" + time_shift + "&complete=" + complete + "&on=" + on + "&ts-port=" + ts_port + "&max_bitrate="
		+ max_bitrate + "&multicast=" + multicast + "&multi_src=" + multi_src + "&nic=" 
		+ nic + "&kompression=" + kompression + "&enc-type=" + enc_type + "&enc-key=" + enc_key+ "&rtp-type=" + rtp_type 
		+"&mtu=" +mtu +"&rist=" +rist + "&rist-latency=" +rist_latency,
		id, UDPMODE, responseCookieContainer, loin_ip, this, uiport));
		
		return driverReslut; 
	}
}
