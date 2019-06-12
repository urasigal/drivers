package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.ZEN_ADD_FEEDER_SOURCE;
import static com.zixi.globals.Macros.ZEN_CLUSTER_ID;
import static com.zixi.globals.Macros.ZEN_GET_ACCESS_TAG;
import static com.zixi.globals.Macros.ZEN_GET_BX_ID;
import static com.zixi.globals.Macros.ZEN_GET_BX_RUNDOM_ID;
import static com.zixi.globals.Macros.ZEN_GET_RX_ID;

import org.json.JSONArray;
import org.json.JSONObject;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.ApiWorkir;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class ZenAddTranscoderProfileDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	 
	public DriverReslut addTranscoderProfiles(String zenUser, String zenPass, String zenLogin_ip, 
	String zenUiPort, String audio_bitrate, String audio_encoder_profile, String bitrate_avg, String bitrate_max, String b_frames,
	String do_audio, String do_video, String encoding_profile, String fps, String gop, String gop_closed, String gop_fixed, String hardware_accel,
	String hdr_buff_length, String height, String interlaced, String keep_audio, String keep_video, String performance, String profile,
	String ref_frames, String width)throws Exception
	{   
		JSONObject json = new JSONObject();
		json.put("username", zenUser).put("password", zenPass);
		String[] cokieValuesForLoggin = apiworker.zenLogginPost("https://" + zenLogin_ip + "/login", zenUser, zenPass, zenUiPort, zenLogin_ip, json.toString().getBytes());
		
		json = new JSONObject();
		json.put("audio_encoder_profile", audio_encoder_profile).
		put("audio_bitrate", Integer.parseInt(audio_bitrate)).
		put("bitrate_avg", bitrate_avg).
		put("bitrate_max", bitrate_max).
		put("b_frames", Integer.parseInt(b_frames)).
		put("do_audio",  Boolean.parseBoolean(do_audio)).
		put("do_video", Boolean.parseBoolean(do_video)).
		put("encoding_profile", encoding_profile).
		put("fps", fps).
		put("gop", Integer.parseInt(gop)).
		put("gop_closed", Integer.parseInt(gop_closed)).
		put("gop_fixed", Integer.parseInt(gop_fixed)).
		put("hardware_accel", Boolean.parseBoolean(hardware_accel)).
		put("hdr_buff_length", Integer.parseInt(hdr_buff_length)).
		put("height", height).
		put("interlaced", Integer.parseInt(interlaced)).
		put("keep_audio", Boolean.parseBoolean(keep_audio)).
		put("keep_video", Boolean.parseBoolean(keep_video)).
		put("performance", Integer.parseInt(performance)).
		put("profile", profile).
		put("ref_frames", Integer.parseInt(ref_frames)).
		put("width", width);
		return new DriverReslut(apiworker.zenPost( "https://" + zenLogin_ip + "/api/transcoding_profiles", zenUser, zenPass, zenUiPort, zenLogin_ip, 
		json.toString().getBytes(), ZEN_ADD_FEEDER_SOURCE, cokieValuesForLoggin));
	}	
}
