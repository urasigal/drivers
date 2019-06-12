package com.zixi.drivers.drivers;

import org.json.JSONArray;
import org.json.JSONObject;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.entities.TestParameters;
import com.zixi.tools.ApiWorkir;
import com.zixi.tools.BroadcasterLoggableApiWorker;

import static com.zixi.globals.Macros.*;
public class ReceiverOutputStreamDeletionDriver extends BroadcasterLoggableApiWorker implements TestDriver {

	//private ApiWorkir streamDeletor = new ApiWorkir();

	//private TestParameters testParameters;

	public DriverReslut testIMPL(String login_ip, String userName, String userPass, String uiport, String stream_name, String testid) throws Exception {

		String streamName = null;
		String streamId = null;
		
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);

		String response = apiworker.sendGet("http://" + login_ip + ":" + uiport + "/out_streams.json", "", 77, responseCookieContainer, login_ip, this, uiport);

		JSONObject responseJson = new JSONObject(response.toString());
		JSONArray outputStreamsArray = responseJson.getJSONArray("streams");

		for (int i = 0; i < outputStreamsArray.length(); i++) {
			// System.out.println("before");
			JSONObject outputStream = outputStreamsArray.getJSONObject(i);

			streamName = outputStream.getString("name");
			if (streamName.equals(stream_name)) {
				streamId = outputStream.getString("out_id");
				break;
			}
		}
		
		return new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" + uiport + "/del_output.json?" + "out_id=" + streamId, streamName, RECEIVERDELETIONMODE, responseCookieContainer, 
		login_ip, this, uiport));
	}
}
