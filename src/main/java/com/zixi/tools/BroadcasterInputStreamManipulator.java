package com.zixi.tools;

import static com.zixi.globals.Macros.*;
import org.json.JSONObject;
import com.zixi.entities.StreamEntity;

public class BroadcasterInputStreamManipulator extends BroadcasterLoggableApiWorker{

	private StreamEntity streamEntity = null;
	
	/**
	 * Empty constructor
	 */
	public BroadcasterInputStreamManipulator() {
	}
	
	/**
	 * @param streamEntity
	 */
	public BroadcasterInputStreamManipulator(StreamEntity streamEntity) {
		 this.streamEntity = streamEntity;
	}
	
	// Get stream properties from the broadcaster. This is a special kind of reflectInputStream method because of it can initialize a streamEntity by 
	// streamEntity's id from broadcaster server.
	/**
	 * @param ipAddress
	 * @param port
	 * @param usrNmae
	 * @param userPassword
	 * @param streamId
	 * @throws Exception
	 */
	public void reflectInputStreamWithInit(String ipAddress, String port, String usrNmae, String userPassword, String streamId) throws Exception 
	{
		// Logging to broadcaster server.
		login_in( ipAddress, port, usrNmae, userPassword);
		if ( streamEntity == null )
		{
			streamEntity  = new StreamEntity(new JSONObject( apiworker.sendGet("http://" + ipAddress + ":" + port + "/input_stream_stats.json?id=" + streamId, 
			"", LINE_MODE, responseCookieContainer, ipAddress, this, port)));
		}
		else
		{
			streamEntity.setStreamJsonContainer( new JSONObject( apiworker.sendGet("http://" + ipAddress + ":" + port + 
			"/input_stream_stats.json?id=" + streamId, "", LINE_MODE, responseCookieContainer, ipAddress, this, port)));
		}
	}
	
	// Get stream properties from the broadcaster.
		/**
		 * @param ipAddress
		 * @param port
		 * @param usrNmae
		 * @param userPassword
		 * @param streamId
		 * @throws Exception
		 */
		public void reflectInputStream(String ipAddress, String port, String usrNmae, String userPassword) throws Exception 
		{
			// Logging to broadcaster server.
			login_in( ipAddress, port, usrNmae, userPassword);
			if ( streamEntity == null )
			{
				return;
			}
			else
			{
				streamEntity.setStreamJsonContainer( new JSONObject( apiworker.sendGet("http://" + ipAddress + ":" + port + 
				"/input_stream_stats.json?id=" + streamEntity.getStreamJsonContainer().getString("id"), "", LINE_MODE, responseCookieContainer,
				ipAddress, this, port)));
			}
		}
	
	// Stops or starts the broadcaster server's push input stream.
	public String broadcasterStopStartPushInputStream(String ipAddress, String port, String usrNmae, String userPassword, boolean onOff, String sType, 
	String sId) throws Exception{
		
		String result = "";
		
		// Logging to broadcaster server.
		login_in( ipAddress, port, usrNmae, userPassword);
		
		// Trying to initialize a StreamEntity object - sId (stream id) should be provided and StreamEntity object should point to null.
		if ( streamEntity == null && (sId != null) )
		{
			if(!(sId.equals("") ))
			{
				reflectInputStreamWithInit(ipAddress, port, usrNmae, userPassword, sId);
			}
		}
		
		if ( streamEntity == null && ( (sId == null) ) )
		{
			return "";
		}
		
		if ( streamEntity == null && ( ( (sId.equals("") ) ) ) )
		{
			return "";
		}
		
		if(sType.equals(FILE) && sId != null )
		{
			if ((!sId.equals("")))
			{
			    if(onOff)
				{
			    	result = apiworker.sendGet( "http://" + ipAddress + ":" + port + "/zixi/edit_stream.json?id=" + sId + 
			    	"&on=" + 1, "", UDPMODE, responseCookieContainer, ipAddress, this, port);
				}
				else
				{
					result = apiworker.sendGet( "http://" + ipAddress + ":" + port + "/zixi/edit_stream.json?id=" + sId + 
					"&on=" + 0, "", UDPMODE, responseCookieContainer, ipAddress, this, port);
				}
			}
		}
		else
		{
			if(onOff)
			{
				result = apiworker.sendGet( "http://" + ipAddress + ":" + port + "/zixi/edit_stream.json?id=" + streamEntity.getStreamJsonContainer().getString("id") + 
				"&on=" + 1, "", UDPMODE, responseCookieContainer, ipAddress, this, port);
			}
			else
			{
				result = apiworker.sendGet( "http://" + ipAddress + ":" + port + "/zixi/edit_stream.json?id=" + streamEntity.getStreamJsonContainer().getString("id") + 
				"&on=" + 0, "", UDPMODE, responseCookieContainer, ipAddress, this, port);
			}
		}
		
		reflectInputStream(ipAddress, port, usrNmae, userPassword);
		return result;
	}
	
	/**
	 * @return the streamEntity
	 */
	public StreamEntity getStreamEntity() {
		return streamEntity;
	}

	/**
	 * @param streamEntity the streamEntity to set
	 */
	public void setStreamEntity(StreamEntity streamEntity) {
		this.streamEntity = streamEntity;
	}
	
	// Login to the broadcaster.
	/**
	 * @param ipAddress
	 * @param port
	 * @param usrNmae
	 * @param userPassword
	 * @throws Exception
	 */
	protected void login_in(String ipAddress, String port, String usrNmae, String userPassword) throws Exception
	{
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + ipAddress + ":" + port + "/login.htm", usrNmae, userPassword, ipAddress, port);
	}
	
	public static void main(String args[]) throws Exception
	{
		BroadcasterInputStreamManipulator BroadcasterInputStreamManipulator = new BroadcasterInputStreamManipulator();
		//BroadcasterInputStreamManipulator.reflectInputStreamWithInit("10.7.0.63", "4444", "admin", "1234", "feederout");
		BroadcasterInputStreamManipulator.broadcasterStopStartPushInputStream("10.7.0.63", "4444", "admin", "1234", true, "", "feederout");
//		for(;;)
//		{
//			BroadcasterInputStreamManipulator.login_in("10.7.0.63", "4444", "admin", "1234");
//			BroadcasterInputStreamManipulator.broadcasterStopStartPushInputStream("10.7.0.63", "4444", "admin", "1234", true,  "", null);
//			BroadcasterInputStreamManipulator.broadcasterStopStartPushInputStream("10.7.0.63", "4444", "admin", "1234", false, "", null);
//		}
	}
}
