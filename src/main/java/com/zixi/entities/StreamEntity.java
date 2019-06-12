package com.zixi.entities;

import org.json.JSONObject;

/**
 * @author yuri
 * Represents a broadcaster input steam.
 */
public class StreamEntity {
	
	// Physical parameters
	private  String width;
	private  String hight;
	private  String progressive;
	private  String fps;
	private  String audioRate;
	private  String videoCodec;
	private  String audioCodec;
	
	// Manageable stream's parameters.
	private String streamName;
	
	// General stream's container - Json object.
	private JSONObject streamJsonContainer = null;

	// Empty constructor.
	public StreamEntity() {}
	
	// Json constructor.
	public StreamEntity(JSONObject streamJsonContainer) {
		this.streamJsonContainer = streamJsonContainer;
	}
	
	/**
	 * @param width
	 * @param hight
	 * @param progressive
	 * @param fps
	 * @param audioRate
	 * @param videoCodec
	 * @param audioCodec
	 */
	public StreamEntity(String width, String hight, String progressive,String fps, String audioRate, String videoCodec, String audioCodec)
	{
		this.width 			= width;
		this.hight 			= hight;
		this.progressive 	= progressive;
		this.fps 			= fps;
		this.audioRate 		= audioRate;
		this.videoCodec 	= videoCodec;
		this.audioCodec 	= audioCodec;
	}	
	
	public String getWidth() {
		return width;
	}

	public String getHight() {
		return hight;
	} 

	public String getProgressive() {
		return progressive;
	}

	public String getFps() {
		return fps;
	}

	public String getAudioRate() {
		return audioRate;
	}

	public String getVideoCodec() {
		return videoCodec;
	}

	public String getAudioCodec() {
		return audioCodec;
	}
	
	/**
	 * @return the streamName
	 */
	public String getStreamName() {
		return streamName;
	}

	/**
	 * @param streamName the streamName to set
	 */
	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}
	
	/**
	 * @return the streamJsonContainer
	 */
	public JSONObject getStreamJsonContainer() {
		return streamJsonContainer;
	}

	/**
	 * @param streamJsonContainer the streamJsonContainer to set
	 */
	public void setStreamJsonContainer(JSONObject streamJsonContainer) {
		this.streamJsonContainer = streamJsonContainer;
	}
}
