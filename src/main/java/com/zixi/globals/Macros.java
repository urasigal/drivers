package com.zixi.globals;

public class Macros {
	
	// modes for the CreatePushHalper class//////////////////////////////////////
		public static final int 	UDPMODE 												= 	0;
		public static final int 	RTMPMODE 											= 	0;
		public static final int 	PUSHMODE 											= 	1;
		public static final int 	PUSHINMODE 										= 	2;
		public static final String  FILE_UPLOAD_MODE 						= 	"2";
		public static final int 	PULLMODE 											= 	3;
		public static final int 	PUSHOUTMODE 									= 	4;
		public static final int 	GENERALMODE 									= 	5;
		public static final int 	UDPOUTMODE 										= 	6;
		public static final int 	RECEIVERMODE 									= 	7;
		public static final int 	JSONMODE 											= 	8;
		public static final int 	RECEIVERIDMODE									= 	9;
		public static final int 	RECEIVERDELETIONMODE 					= 	10;
		public static final int 	RECEIVERSTATISTICMODE 					= 	11;
		public static final int 	RECEIVER_UDP_OUT_MODE 					= 	12;
		public static final int 	ADD_TRANSCODER_PROFILE 				= 	13;
		public static final int 	SET_RTMMP_AUTO_REMOTE 				= 	14;
		public static final int 	FIND_SOURCE_IP_BX 							= 	15;
		public static final int 	FEEDER_SSH_KEY_STATUS 					= 	16;
		public static final int 	FEEDER_SSH_SERVER_STATUS 				= 	17;
		public static final int 	CNANGE_SETTINGS_MODE 					= 	18;
		public static final int 	EDIT_STREAM_MODE 							= 	19;
		public static final int 	MULTICAST_IP_MODE 							= 	20;
		public static final int 	LINE_MODE 		 									= 	77;
		public static final int 	RECEIVERSTATDATA 		    				= 	21;
		public static final int 	UPTIME			 		    							= 	22;
		public static final int 	DTLS			 		   									= 	23;
		public static final int 	RECORDING			 								= 	24;
		public static final int 	FILE_FROM_FOLDER								= 	25;
		public static final int 	TUNNEL_ADDED_MODE						= 	26;
		public static final int 	RECEIVER_ACTIVE_OUT_MODE				= 	27;
		public static final int 	ANALIZE_MODE										= 	28;
		public static final int 	TR101														= 	29;
		public static final int 	ZEN_GET_KEYS										= 	30;
		public static final int 	ZEN_GET_ACCESS_TAG							= 	31;
		public static final int 	ZEN_ADD_FEEDER									= 	32;
		public static final int 	ZEN_FEEDER_DATA								= 	33;
		public static final int 	FEEDER_GET_STREAMS							= 	34;
		public static final int 	ZEN_GET_BX_VERSION_ID						= 	35;
		public static final int  	AWS_ACCOUNT_ID							=	36;
		public static final int  	AWS_VPC											=	37;
		public static final int		ZEN_ADD_CLUSTER							=	38;
		public static final int     ZEN_CLUSTER_ID								=	39;
		public static final int		ZEN_ADD_BROADCASTER				=	40;
		public static final int		ZEN_GET_BX_REV_PORT					=	41;
		public static final int		ZEN_ADD_RECEIVVER						=	42;
		public static final int		ZEN_GET_RECEIVER_REV_PORT		=	43;
		public static final int		ZEN_GET_FEEDER_ID							=	44;
		public static final int		ZEN_ADD_FEEDER_SOURCE				=	45;
		public static final int		ZEN_GET_SOURCE_OBJ						=	46;
		public static final int		ZEN_GET_BX_ID									=	47;
		public static final int		ZEN_GET_CHANNEL_ID						=	48;
		public static final int		ZEN_GET_BX_RUNDOM_ID				=	49;
		public static final int		FEEDER_GET_STREAMS_BY_ID 			= 	50;
		public static final int		FEEDER_ADD_FAILOVER_GROUP 	= 	51;
		public static final int		FEEDER_GET_STREAMS_IPS 				= 	52;
		public static final int		FEEDER_GET_INPUTS_DATA 			= 	53;
		public static final int		ZEN_DELETE_CLUSTER						=	54;
		public static final int		ZEN_GET_RX_ID									=	55;
		public static final int		ZEN_ADD_USER									=	66;
		public static final int		ZEN_GET_TRANS_PROFS					=	67;
		public static final int		ZEN_CLUSTER_STATUS						=	68;
		public static final int		ZEN_CHANNEL_ID								=	69;
		public static final int		ZEN_DELETE_CHANNEL						=	70;
		public static final int		ZEN_PRFID_ID									=	71;
		public static final int 		TRANSSTAT 										= 	72;
		public static final int 		VALIDATOR 										= 	73;
		public static final int     GET_FAILOVER_COMPONENTS			=	74;
		public static final int     GET_STREAM_PROGRAMS_IDS			=	75;
		public static final int     GET_ELEM_PIDS_OF_PROGR				=	76;
		public static final int     MSG	  													=	78;
		public static final int     STR_STATS	  										=	79;
		public static final int     ADAPTIVE_REC	  								=	81;
		//////////////////////////////////////////////////
		
		public static final String  GOOD 												= 	"GOOD";
		public static final String  FILE 													= 	"FILE";
		
		
		
		public static final String  GET_ALL_INPUTS_DATA 					= 	"/zixi/streams.json";
		public static final String  GET_ALL_OUTPUTS_DATA 				= 	"/zixi/outputs.json";

}
