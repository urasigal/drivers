package com.zixi.drivers.drivers;


import com.zixi.drivers.tools.DriverReslut;
import com.zixi.entities.TestParameters;
import com.zixi.tools.BroadcasterLoggableApiWorker;

import static com.zixi.globals.Macros.*;

public class BroadcasterAddTranscoderProfileDriver extends
		BroadcasterLoggableApiWorker implements TestDriver {

	public String testIMPL(String userName, String userPass, String login_ip,String uiport, String profile_name, String enc, String bitrate,
	String gop, String fixed_gop, String closed_gop,String performance, String b_frames, String frame_type, String profile, String level, String bitrate_mode,
	String ref_frames, String idr_int, String cavlc, String brightness, String contrast, String fps, String width, String height,
	String max_bitrate) throws Exception {

		testParameters = new TestParameters("userName:" + userName, "userPass:" + userPass, "login_ip:" + login_ip, "uiport:" + uiport,
		"profile_name:" + profile_name, "enc:" + enc, "bitrate:" + bitrate, "gop:" + gop, "fixed_gop:" + fixed_gop,
		"closed_gop:" + closed_gop, "perxsdeeformance:" + performance, "b_frames:" + b_frames, "frame_type:" + frame_type, "profile:"
		+ profile, "level:" + level, "bitrate_mode:" + bitrate_mode, "ref_frames:" + ref_frames, "idr_int:" + idr_int, "cavlc:" + cavlc,
		"brightness:" + brightness, "contrast:" + contrast, "fps:"
		+ fps, "width:" + width, "height:" + height, "max_bitrate:" + max_bitrate);
	
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);
	
		return apiworker.sendGet("http://" + login_ip + ":" + uiport + "/zixi/add_h264_profile.json?profile_name="
		+ profile_name + "&enc=" + enc + "&bitrate=" + bitrate + "&gop=" + gop + "&fixed_gop=" + fixed_gop + "&closed_gop=" + closed_gop + "&performance=" + performance + 
		"&b_frames=" + b_frames + "&frame_type=" + frame_type
		+ "&profile=" + profile + "&level=" + level + "&bitrate_mode=" + bitrate_mode + "&ref_frames=" + ref_frames
		+ "&idr_int=" + idr_int + "&cavlc=" + cavlc + "&brightness=" + brightness + "&contrast=" + contrast + "&fps=" + fps + "&width=" + width + "&height=" + height + "&max_bitrate=" + max_bitrate, "",
		ADD_TRANSCODER_PROFILE, responseCookieContainer, login_ip, this, uiport);	
	}
	
	public DriverReslut  testIMPL(String userName, String userPass, String login_ip, String uiport,String mode, String profile_name,
	String enc, String bitrate, String gop, String fixed_gop, String closed_gop, String copy_gop, String performance,
	String b_frames, String frame_type, String profile, String level, String bitrate_mode,
	String ref_frames, String hrd, String idr_int, String cavlc, String brightness, String contrast, String fps,
	String width, String height, String crf, String tune, String keep_ar, String max_bitrate, String x264_two_pass,  String max_qp,  String bpp) throws Exception {

		testParameters = new TestParameters("userName:" + userName, "userPass:"+ userPass, "login_ip:" + login_ip, "uiport:" + uiport,
		"profile_name:" + profile_name, "enc:" + enc, "bitrate:" + bitrate, "gop:" + gop, "fixed_gop:" + fixed_gop,
		"closed_gop:" + closed_gop, "copy_gop:" + copy_gop,  "performance:" + performance, "b_frames:" + b_frames, "frame_type:" + frame_type, "profile:"
		 + profile, "level:" + level, "bitrate_mode:" + bitrate_mode, "ref_frames:" + ref_frames, "hrd:" + hrd, "idr_int:"
		 + idr_int, "cavlc:" + cavlc, "brightness:" + brightness, "contrast:" + contrast, "fps:" + fps, "width:" + width, "height:" + height, 
		 "max_bitrate:" + max_bitrate, "x264_two_pass" + x264_two_pass);

		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);

		
		if (mode.equals("h.264") || mode.equals("mpeg2") || mode.equals("h.265"))
		{
			return new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" + uiport + "/zixi/add_h264_profile.json?profile_name="
			+ profile_name + "&enc=" + enc + "&bitrate=" + bitrate + "&rgop=" + gop + "&fixed_gop=" + fixed_gop + "&closed_gop=" + closed_gop + "&copy_gop=" + copy_gop + "&performance=" + performance
			+ "&b_frames=" + b_frames + "&rame_type=" + frame_type + "&profile=" + profile + "&level=" + level + "&bitrate_mode=" + bitrate_mode + "&ref_frames=" + ref_frames
			+ "&hrd=" + hrd + "&idr_int=" + idr_int + "&cavlc=" + cavlc + "&brightness=" + brightness + "&contrast=" + contrast + "&fps="+ fps + "&width=" + width + "&height=" + height +
			"&crf=" + crf + "&tune=" + tune +"&keep_ar=" + keep_ar + "&max_bitrate=" + max_bitrate + "&264_two_pass=" + x264_two_pass +"&max_qp=" + max_qp + "&bpp=" + bpp,
		 "", ADD_TRANSCODER_PROFILE, responseCookieContainer, login_ip, this, uiport));
			}
		 return new DriverReslut("");
	}
	static private final String rmax_bitrate = "max_bitrate=";
}
