package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.PULLMODE;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.entities.TestParameters;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class BroadcasterSinglePullInStreamCreationDriver extends BroadcasterLoggableApiWorker implements TestDriver {	
	// Perform a test.
	public DriverReslut addPull(String userName, String userPass, String Host, String loin_ip, String id, String source, String uiport,
	String pull_port, String latency, String fec_latency, String fec_overhead, String mcast_force, String time_shift,
	String nic, String max_outputs, String type, String password, String mcast_port, String complete, String mcast_ip,
	String fec_adaptive, String mcast_ttl, String on, String func, String fec_force, String mcast_out, String propertiesFile, String mtu) throws Exception { 
		
		testParameters = new TestParameters("userName:" + userName, "userPass:" + userPass, "Host:" + Host, "loin_ip:" + loin_ip, "id:" + id,
		"source:" + source, "uiport:" + uiport, "pull_port:" + pull_port, "latency:" + latency, "fec_latency:" + fec_latency, "fec_overhead:" + fec_overhead,
		"mcast_force:" + mcast_force, "time_shift:" + time_shift, "nic:" + nic, "max_outputs:" + max_outputs, "type:" + type, "password:" + password,
		"mcast_port:" + mcast_port, "complete:" + complete, "mcast_ip:" + mcast_ip, "fec_adaptive:" + fec_adaptive, "mcast_ttl:" + mcast_ttl, "on:" + on,
		"func:" + func, "fec_force:" + fec_force, "mcast_out:" + mcast_out, "propertiesFile:" + propertiesFile);

		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + loin_ip + ":" + uiport + "/login.htm", userName, userPass, loin_ip, uiport);
		// this function creates a single pull stream
		
		driverReslut = new DriverReslut(createPullInWithRandomName(userName, userPass, Host, loin_ip, id, source, uiport, pull_port, latency, fec_latency,
		fec_overhead, mcast_force, time_shift, nic, max_outputs, type, password, mcast_port, complete, mcast_ip, fec_adaptive, mcast_ttl, on, func, fec_force, mcast_out, mtu));
		
		return driverReslut;
	}

	// Perform a test.
	public String testIMPL(String userName, String userPass, String Host, String loin_ip, String id, String source, String uiport,
	String pull_port, String latency, String fec_latency, String fec_overhead, String mcast_force, String time_shift,
	String nic, String max_outputs, String type, String password, String mcast_port, String complete, String mcast_ip,
	String fec_adaptive, String mcast_ttl, String on, String func, String fec_force, String mcast_out, String propertiesFile, String dec_type, 
	String dec_key) throws Exception { 
			
		testParameters = new TestParameters("userName:" + userName, "userPass:" + userPass, "Host:" + Host, "loin_ip:" + loin_ip, "id:" + id,
		"source:" + source, "uiport:" + uiport, "pull_port:" + pull_port, "latency:" + latency, "fec_latency:" + fec_latency, "fec_overhead:" + fec_overhead,
		"mcast_force:" + mcast_force, "time_shift:" + time_shift, "nic:" + nic, "max_outputs:" + max_outputs, "type:" + type, "password:" + password,
		"mcast_port:" + mcast_port, "complete:" + complete, "mcast_ip:" + mcast_ip, "fec_adaptive:" + fec_adaptive, "mcast_ttl:" + mcast_ttl, "on:" + on,
		"func:" + func, "fec_force:" + fec_force, "mcast_out:" + mcast_out, "propertiesFile:" + propertiesFile, "dec_type:" + dec_type, "dec_key:" + dec_key);

		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + loin_ip + ":" + uiport + "/login.htm", userName, userPass, loin_ip, uiport);
		// this function creates a single pull stream
		return createPullInWithRandomName(userName, userPass, Host, loin_ip, id, source, uiport, pull_port, latency, fec_latency,
		fec_overhead, mcast_force, time_shift, nic, max_outputs, type, password, mcast_port, complete, mcast_ip, fec_adaptive, mcast_ttl, on, func, fec_force, mcast_out, dec_type, dec_key);
	}
	
	public String testIMPLWithMtu(String userName, String userPass, String Host, String loin_ip, String id, String source, String uiport,
			String pull_port, String latency, String fec_latency, String fec_overhead, String mcast_force, String time_shift,
			String nic, String max_outputs, String type, String password, String mcast_port, String complete, String mcast_ip,
			String fec_adaptive, String mcast_ttl, String on, String func, String fec_force, String mcast_out, String propertiesFile, String dec_type, 
			String dec_key, String mtu) throws Exception { 
					
				testParameters = new TestParameters("userName:" + userName, "userPass:" + userPass, "Host:" + Host, "loin_ip:" + loin_ip, "id:" + id,
				"source:" + source, "uiport:" + uiport, "pull_port:" + pull_port, "latency:" + latency, "fec_latency:" + fec_latency, "fec_overhead:" + fec_overhead,
				"mcast_force:" + mcast_force, "time_shift:" + time_shift, "nic:" + nic, "max_outputs:" + max_outputs, "type:" + type, "password:" + password,
				"mcast_port:" + mcast_port, "complete:" + complete, "mcast_ip:" + mcast_ip, "fec_adaptive:" + fec_adaptive, "mcast_ttl:" + mcast_ttl, "on:" + on,
				"func:" + func, "fec_force:" + fec_force, "mcast_out:" + mcast_out, "propertiesFile:" + propertiesFile, "dec_type:" + dec_type, "dec_key:" + dec_key, "mtu:" + mtu);

				responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + loin_ip + ":" + uiport + "/login.htm", userName, userPass, loin_ip, uiport);
				// this function creates a single pull stream
				return createPullInWithRandomNameWithMtu(userName, userPass, Host, loin_ip, id, source, uiport, pull_port, latency, fec_latency,
				fec_overhead, mcast_force, time_shift, nic, max_outputs, type, password, mcast_port, complete, mcast_ip, fec_adaptive, mcast_ttl,
				on, func, fec_force, mcast_out, dec_type, dec_key, mtu); 
			}
	
	public String createPullInWithRandomName(String userName, String userPass, String Host, String loin_ip, String id, String source,
	String uiport, String pull_port, String latency, String fec_latency, String fec_overhead, String mcast_force,
	String time_shift, String nic, String max_outputs, String type, String password, String mcast_port, String complete,
	String mcast_ip, String fec_adaptive, String mcast_ttl, String on,String func, String fec_force, String mcast_out) {
		
		String request = "http://" + loin_ip + ":" + uiport + "/zixi/add_stream.json?" + "func" + "=" + func + "&" + "type" + "=" + type + "&" + "id" + "=" + id + "&" + "max_outputs"
		+ "=" + max_outputs + "&" + "mcast_out" + "=" + mcast_out + "&" + "mcast_force" + "=" + mcast_force + "&" + "mcast_ip" + "=" + mcast_ip + "&"
		+ "mcast_port" + "=" + mcast_port + "&" + "mcast_ttl" + "=" + mcast_ttl + "&" + "time_shift" + "="
		+ time_shift + "&" + "host0" + "=" + Host + "&" + "pull-port" + "=" + pull_port + "&" + "source" + "=" + source + "&" + "password" + "=" + password
		+ "&" + "latency" + "=" + latency + "&" + "nic" + "=" + nic + "&" + "fec_overhead" + "=" + fec_overhead + "&" + "fec_latency" + "=" + fec_latency
		+ "&" + "fec_aware" + "=" + "" + "&" + "fec_adaptive" + "=" + fec_adaptive + "&" + "fec_force" + "=" + fec_force + "&" + "complete" + "=" + complete + "&" + "on" + "=" + on;
		//System.out.println(request);
		return apiworker.sendGet(request, id, PULLMODE, responseCookieContainer, loin_ip, this, uiport);
	}
	
	public String createPullInWithRandomName(String userName, String userPass, String Host, String loin_ip, String id, String source,
			String uiport, String pull_port, String latency, String fec_latency, String fec_overhead, String mcast_force,
			String time_shift, String nic, String max_outputs, String type, String password, String mcast_port, String complete,
			String mcast_ip, String fec_adaptive, String mcast_ttl, String on,String func, String fec_force, String mcast_out, String mtu) {
				
				String request = "http://" + loin_ip + ":" + uiport + "/zixi/add_stream.json?" + "func" + "=" + func + "&" + "type" + "=" + type + "&" + "id" + "=" + id + "&" + "max_outputs"
				+ "=" + max_outputs + "&" + "mcast_out" + "=" + mcast_out + "&" + "mcast_force" + "=" + mcast_force + "&" + "mcast_ip" + "=" + mcast_ip + "&"
				+ "mcast_port" + "=" + mcast_port + "&" + "mcast_ttl" + "=" + mcast_ttl + "&" + "time_shift" + "="
				+ time_shift + "&" + "host0" + "=" + Host + "&" + "pull-port" + "=" + pull_port + "&" + "source" + "=" + source + "&" + "password" + "=" + password
				+ "&" + "latency" + "=" + latency + "&" + "nic" + "=" + nic + "&" + "fec_overhead" + "=" + fec_overhead + "&" + "fec_latency" + "=" + fec_latency
				+ "&" + "fec_aware" + "=" + "" + "&" + "fec_adaptive" + "=" + fec_adaptive +
				"&" + "fec_force" + "=" + fec_force + "&" + "complete" + "=" + complete + "&" + "on" + "=" + on + "&mtu=" + mtu;
				//System.out.println(request);
				return apiworker.sendGet(request, id, PULLMODE, responseCookieContainer, loin_ip, this, uiport);
			}
	 
	
	
	public String createPullInWithRandomName(String userName, String userPass, String Host, String loin_ip, String id, String source,
			String uiport, String pull_port, String latency, String fec_latency, String fec_overhead, String mcast_force,
			String time_shift, String nic, String max_outputs, String type, String password, String mcast_port, String complete,
			String mcast_ip, String fec_adaptive, String mcast_ttl, String on,String func, String fec_force, String mcast_out, String dec_type, 
			String dec_key) {
						
				String request = "http://" + loin_ip + ":" + uiport + "/zixi/add_stream.json?" + "func" + "=" + func + "&" + "type" + "=" + type + "&" + "id" + "=" + id + "&" + "max_outputs"
				+ "=" + max_outputs + "&" + "mcast_out" + "=" + mcast_out + "&" + "mcast_force" + "=" + mcast_force + "&" + "mcast_ip" + "=" + mcast_ip + "&"
				+ "mcast_port" + "=" + mcast_port + "&" + "mcast_ttl" + "=" + mcast_ttl + "&" + "time_shift" + "="
				+ time_shift + "&" + "host0" + "=" + Host + "&" + "pull-port" + "=" + pull_port + "&" + "source" + "=" + source + "&" + "password" + "=" + password
				+ "&" + "latency" + "=" + latency + "&" + "nic" + "=" + nic + "&" + "fec_overhead" + "=" + fec_overhead + "&" + "fec_latency" + "=" + fec_latency
				+ "&" + "fec_aware" + "=" + "" + "&" + "fec_adaptive" + "=" + fec_adaptive + "&" + "fec_force" + "=" + fec_force + "&" + "complete" + "=" + complete + "&on=" + on
				+ "&dec_type=" + dec_type + "&dec_key=" + dec_key  ;
				
				return apiworker.sendGet(request, id, PULLMODE, responseCookieContainer, loin_ip, this, uiport);
			}
	
	public String createPullInWithRandomNameWithMtu(String userName, String userPass, String Host, String loin_ip, String id, String source,
			String uiport, String pull_port, String latency, String fec_latency, String fec_overhead, String mcast_force,
			String time_shift, String nic, String max_outputs, String type, String password, String mcast_port, String complete,
			String mcast_ip, String fec_adaptive, String mcast_ttl, String on,String func, String fec_force, String mcast_out, String dec_type, 
			String dec_key, String mtu) {
						
				String request = "http://" + loin_ip + ":" + uiport + "/zixi/add_stream.json?" + "func" + "=" + func + "&" + "type" + "=" + type + "&" + "id" + "=" + id + "&" + "max_outputs"
				+ "=" + max_outputs + "&" + "mcast_out" + "=" + mcast_out + "&" + "mcast_force" + "=" + mcast_force + "&" + "mcast_ip" + "=" + mcast_ip + "&"
				+ "mcast_port" + "=" + mcast_port + "&" + "mcast_ttl" + "=" + mcast_ttl + "&" + "time_shift" + "="
				+ time_shift + "&" + "host0" + "=" + Host + "&" + "pull-port" + "=" + pull_port + "&" + "source" + "=" + source + "&" + "password" + "=" + password
				+ "&" + "latency" + "=" + latency + "&" + "nic" + "=" + nic + "&" + "fec_overhead" + "=" + fec_overhead + "&" + "fec_latency" + "=" + fec_latency
				+ "&" + "fec_aware" + "=" + "" + "&" + "fec_adaptive" + "=" + fec_adaptive + "&" + "fec_force" + "=" + fec_force + "&" + "complete" + "=" + complete + "&on=" + on
				+ "&dec_type=" + dec_type + "&dec_key=" + dec_key + "&mtu=" + mtu ;
				
				return apiworker.sendGet(request, id, PULLMODE, responseCookieContainer, loin_ip, this, uiport);
			}
}
