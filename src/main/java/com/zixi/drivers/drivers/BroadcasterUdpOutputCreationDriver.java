package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.*;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.entities.TestParameters;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class BroadcasterUdpOutputCreationDriver extends BroadcasterLoggableApiWorker implements TestDriver {

	// Default constructor.
	public BroadcasterUdpOutputCreationDriver() {}
	
	public BroadcasterUdpOutputCreationDriver(StringBuffer 	testFlowDescriptor) {
		super(testFlowDescriptor);
	}
	
	public DriverReslut testIMPL(String userName, String userPass, String loin_ip, String port, String stream, String streamname, String host,
	String id, String rtp, String fec, String smoothing, String ttl, String remux_bitrate, String df, String local_port, String dec_key,
	String type, String rows, String remux_buff, String local_ip, String remux_restampdts, String uiport, String remux_pcr, 
	String dec_type, String cols, String max_payload, String mtu_time,  String rist ) throws Exception {
	
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + loin_ip + ":" + uiport + "/login.htm", userName , userPass, loin_ip, uiport);
		
		String request = "http://" + loin_ip + ":" + uiport + "/zixi/add_output.json?" + "type" + "=" + type + "&" + "id" + "=" + id  + "&" + "name" + "=" + streamname + 
		"&stream=" + stream + "&host=" + host + "&port=" + port + "&smoothing=" + smoothing + "&ttl=" + ttl + "&df=" + df + "&local-port=" + local_port
		+ "&local-ip=" + local_ip + "&dec-type=" + dec_type + "&dec-key=" + dec_key + "&rtp=" + rtp + "&fec=" + fec + "&rows=" + rows + "&cols=" + cols + "&remux_bitrate=" + remux_bitrate + 
		"&remux_pcr=" + remux_pcr + "&remux_buff=" + remux_buff + "&remux_restampdts=" + remux_restampdts + 
		"&max_payload=" + max_payload + "&mtu_time=" + mtu_time + "&rist=" + rist;
		
		driverReslut =  new DriverReslut(apiworker.sendGet(request, id, UDPOUTMODE, responseCookieContainer, loin_ip, this, uiport));
		
		return driverReslut;
	}
}
