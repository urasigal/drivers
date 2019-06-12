package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.*;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.entities.TestParameters;
import com.zixi.tools.ApiWorkir;
import com.zixi.tools.BroadcasterLoggableApiWorker;


public class ReceiverPullInputStreamCreationDriver extends BroadcasterLoggableApiWorker implements TestDriver {
	public DriverReslut testIMPL(String userName, String userPass, String login_ip, String uiport, String dec_key, String dec_type,
	String fec_adaptive, String fec_aware, String fec_force, String fec_latency, String fec_overhead, String host,
	String latency, String min_bit, String name, String nic, String port, String session, String stream) throws Exception {
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet( "http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);
		String response = apiworker.sendGet("http://" + login_ip + ":" + uiport + "/add_zixi_in.json?" + "dec-key=" + dec_key + "&dec-type=" + dec_type + 
		"&fec_adaptive="+ fec_adaptive + "&fec_aware="+ fec_aware +"&fec_force="+ fec_force +"&fec_latency="+ fec_latency +"&fec_overhead="+ fec_overhead +"&host="+ host +"&latency="+ 
		latency +"&min_bit="+  min_bit +"&name="+ name +"&nic="+ nic +"&port="+ port +"&session="+ session +
		"&stream="+ stream , "", RECEIVERMODE, responseCookieContainer , login_ip, this, uiport);
		Thread.sleep(Integer.parseInt(latency));
		return new DriverReslut(response);
	}
}
