package com.zixi.tools;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringJoiner;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import javax.net.ssl.HttpsURLConnection;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zixi.drivers.tools.DriverReslut;

import io.burt.jmespath.Expression;
import io.burt.jmespath.JmesPath;
import io.burt.jmespath.jackson.JacksonRuntime;

import static com.zixi.drivers.tools.Macros.SYS_CPU;
import static com.zixi.globals.Macros.*;

public class ApiWorkir {
	protected 			JSONObject        			json 				= null;
	protected final 	String      	  					USER_AGENT 			= "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.124 Safari/537.36";
	private 				String            				tester 				= null;
	protected 			HttpURLConnection 	httpConnection;
	protected 			HttpsURLConnection  httpsURLConnection;
	protected int               							debugLineNumber 	= 28;
	protected 			StringBuffer 					testFlowDescriptor;
	
	// default constructor.
	public ApiWorkir() {}
	
	public ApiWorkir(StringBuffer testFlowDescriptor) { this.testFlowDescriptor = testFlowDescriptor; }
	
	// It is very specific function is used to upload a server private key to a SPECIFIC feeder server - Such strong and disturbing rule comes from a Feeder server UI implementation.
	public String inserKeyToSpecificFeeder(String url, String id, String[] responseCookieContainer, String HOST, Object caller, String uiport, byte[] keyByteArray)
	{
		StringBuffer response 			= 	new StringBuffer();
		String 		 scriptText			= 	null;
		
		try {
			URL destUrl 		= new URL(url);  
			httpConnection 		= (HttpURLConnection) destUrl.openConnection();
			httpConnection.setDoOutput( true );
			//  Setup parameters and general request properties that you may need before connecting.
			///////////////////////////////////////////////////////////////////////////////////////
			httpConnection.setReadTimeout(40000);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Accept", "*/*");
			//httpConnection.setRequestProperty("Pragma","no-cache");
			//httpConnection.setRequestProperty("Cache-Control","no-cache");
			httpConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			httpConnection.setRequestProperty("Host", HOST + ":" + uiport);
			httpConnection.setRequestProperty("Origin", "http://" + HOST + ":" + uiport);
			httpConnection.setRequestProperty("User-Agent", USER_AGENT);
			httpConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
			httpConnection.setRequestProperty("Content-Type", "multipart/form-data" + ";" + " boundary=----WebKitFormBoundaryAWGt00qual97XRDu");
			httpConnection.setRequestProperty("Referer", "http://" + HOST + ":" + uiport + "/index.html");
			httpConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.8,he;q=0.6,ru;q=0.4");
			httpConnection.setRequestProperty(StringUtils.substringBetween(responseCookieContainer[0], "=", "%"), StringUtils.substringAfter(responseCookieContainer[0], "%3D"));
			httpConnection.setRequestProperty("Cookie", responseCookieContainer[1] + "; "+ responseCookieContainer[0]);
			//////////////////////////////////////////////////////////////////////////////////////////////////
			DataOutputStream wr = new DataOutputStream( httpConnection.getOutputStream()); 
			//ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
			// Temporary buffer.
			//outputStream.write( keyByteArray );
		
			// Print a POST body to a stream.
			wr.write( keyByteArray );
			wr.flush();
			BufferedReader 	in 			= new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
			String 			inputLine 	= "";
			while ((inputLine 	= in.readLine()) != null) {
				response.append(inputLine);
				System.out.println(inputLine);
			}
			
			org.jsoup.nodes.Document 	responsePage 	= 	Jsoup.parse(response.toString());
			Elements 					scriptElements 	= 	responsePage.getElementsByTag("script");
			Element 					element  		= 	scriptElements.get(0);
			 							scriptText 		= 	element.html();
			in.close();
			wr.close();			
		} catch (Exception e) {
			String exceptionTest = e.getMessage();
			System.out.println("bug ------------->>> " + exceptionTest + "Request is "   + url );
		}
		finally {
			httpConnection.disconnect();  // Closed underlying connection - underlying socket.
		}
		return scriptText;
	}
	
	public String dashValidatorPost(String url, String host, String mpdUrl) //http://10.7.0.243/DASH-IF-Conformance/Utils/Process.php
	{
		StringBuffer response 			= 	new StringBuffer();
		double random 					= 	Math.random() * 99999 + 1;
		try {
			URL destUrl 		= new URL(url);  
			httpConnection 		= (HttpURLConnection) destUrl.openConnection();
			httpConnection.setDoOutput( true );
			//  Setup parameters and general request properties that you may need before connecting.
			///////////////////////////////////////////////////////////////////////////////////////
			httpConnection.setReadTimeout(40000);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Accept", "*/*");
			//httpConnection.setRequestProperty("Pragma","no-cache");
			//httpConnection.setRequestProperty("Cache-Control","no-cache");
			httpConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			httpConnection.setRequestProperty("Host", host);
			httpConnection.setRequestProperty("Origin", host);
			httpConnection.setRequestProperty("User-Agent", USER_AGENT);
			httpConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
			httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			httpConnection.setRequestProperty("Referer", url);
			httpConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.8,he;q=0.6,ru;q=0.4");
		
			Map<String,String> arguments = new HashMap<>();
			arguments.put("urlcode", "[\"" + mpdUrl + "\",0,\"\",0,0,0,0]");
			arguments.put("foldername", random + "");
			StringJoiner sj = new StringJoiner("&");
			for(Map.Entry<String,String> entry : arguments.entrySet())
			    sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" 
			         + URLEncoder.encode(entry.getValue(), "UTF-8"));
			byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
			int length = out.length;
			httpConnection.setFixedLengthStreamingMode(length);
			
			DataOutputStream wr = new DataOutputStream(httpConnection.getOutputStream()); 
		
			// Print a POST body to a stream.
		    wr.write(out);
			wr.flush();
			BufferedReader 	in 			= new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
			String 			inputLine 	= "";
			while ((inputLine 	= in.readLine()) != null) {
				response.append(inputLine);
				System.out.println(inputLine);
			}
			
			in.close();
			wr.close();			
		} catch (Exception e) {
			String exceptionTest = e.getMessage();
			System.out.println("bug ------------->>> " + exceptionTest + "Request is "   + url );
		}
		finally {
			httpConnection.disconnect();  // Closed underlying connection - underlying socket.
		}
		return random + "";
	}
	
	public String inserKeyToSpecificFeeder1(String url, String id, String[] responseCookieContainer, String HOST, Object caller, String uiport, byte[] keyByteArray)
	{
		List<Byte>   bytesList       	= 	new ArrayList<>();
		StringBuffer response 			= 	new StringBuffer();
		String 		 scriptText			= 	null;
		
		try {
			URL destUrl 		= new URL(url);  
			httpConnection 		= (HttpURLConnection) destUrl.openConnection();
			httpConnection.setDoOutput( true );
			//  Setup parameters and general request properties that you may need before connecting.
			///////////////////////////////////////////////////////////////////////////////////////
			httpConnection.setReadTimeout(40000);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Accept", "*/*");
			//httpConnection.setRequestProperty("Pragma","no-cache");
			//httpConnection.setRequestProperty("Cache-Control","no-cache");
			httpConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			httpConnection.setRequestProperty("Host", HOST + ":" + uiport);
			httpConnection.setRequestProperty("Origin", "http://" + HOST + ":" + uiport);
			httpConnection.setRequestProperty("User-Agent", USER_AGENT);
			httpConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
			httpConnection.setRequestProperty("Content-Type", "multipart/form-data" + ";" + " boundary=----WebKitFormBoundary4uIxlrzNgZt3P0q9");
			httpConnection.setRequestProperty("Referer", "http://" + HOST + ":" + uiport + "/index.html");
			httpConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.8,he;q=0.6,ru;q=0.4");
			httpConnection.setRequestProperty(StringUtils.substringBetween(responseCookieContainer[0], "=", "%"), StringUtils.substringAfter(responseCookieContainer[0], "%3D"));
			httpConnection.setRequestProperty("Cookie", responseCookieContainer[1] + "; "+ responseCookieContainer[0]);
			//////////////////////////////////////////////////////////////////////////////////////////////////
			DataOutputStream wr = new DataOutputStream( httpConnection.getOutputStream()); 
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
			// Temporary buffer.
			outputStream.write( keyByteArray );
	
			
			// Print a POST body to a stream.
			wr.write( keyByteArray );
			wr.flush();
			
			BufferedReader 	in 			= new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
			String 			inputLine 	= "";
			while ((inputLine 	= in.readLine()) != null) {
				response.append(inputLine);
				System.out.println(inputLine);
			}
			
			org.jsoup.nodes.Document 	responsePage 	= 	Jsoup.parse(response.toString());
			Elements 					scriptElements 	= 	responsePage.getElementsByTag("script");
			Element 					element  		= 	scriptElements.get(0);
			 							scriptText 		= 	element.html();
			in.close();
			wr.close();			
		} catch (Exception e) {
			String exceptionTest = e.getMessage();
			System.out.println("bug ------------->>> " + exceptionTest + "Request is "   + url );
		}
		finally {
			httpConnection.disconnect();  // Closed underlying connection - underlying socket.
		}
		return scriptText;
	}
	
	public String uploadCertificate(String url, String id, String[] responseCookieContainer, String HOST, Object caller, String uiport, byte[] keyByteArray, String boundary)
	{
		List<Byte>   bytesList       	= 	new ArrayList<>();
		StringBuffer response 			= 	new StringBuffer();
		String 		 scriptText			= 	null;
		
		try {
			URL destUrl 		= new URL(url);  
			httpConnection 		= (HttpURLConnection) destUrl.openConnection();
			httpConnection.setDoOutput( true );
			//  Setup parameters and general request properties that you may need before connecting.
			///////////////////////////////////////////////////////////////////////////////////////
			httpConnection.setReadTimeout(40000);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Accept", "*/*");
			//httpConnection.setRequestProperty("Pragma","no-cache");
			//httpConnection.setRequestProperty("Cache-Control","no-cache");
			httpConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			httpConnection.setRequestProperty("Host", HOST + ":" + uiport);
			httpConnection.setRequestProperty("Origin", "http://" + HOST + ":" + uiport);
			httpConnection.setRequestProperty("User-Agent", USER_AGENT);
			httpConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
			httpConnection.setRequestProperty("Content-Type", "multipart/form-data" + ";" + "  + boundary=----" + boundary);
			httpConnection.setRequestProperty("Referer", "http://" + HOST + ":" + uiport + "/index.html");
			httpConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.8,he;q=0.6,ru;q=0.4");
			httpConnection.setRequestProperty(StringUtils.substringBetween(responseCookieContainer[0], "=", "%"), StringUtils.substringAfter(responseCookieContainer[0], "%3D"));
			httpConnection.setRequestProperty("Cookie", responseCookieContainer[1] + "; "+ responseCookieContainer[0]);
			//////////////////////////////////////////////////////////////////////////////////////////////////
			DataOutputStream wr = new DataOutputStream( httpConnection.getOutputStream()); 
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
			// Temporary buffer.
			outputStream.write( keyByteArray );
	
			
			// Print a POST body to a stream.
			wr.write( keyByteArray );
			wr.flush();
			
			BufferedReader 	in 			= new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
			String 			inputLine 	= "";
			while ((inputLine 	= in.readLine()) != null) {
				response.append(inputLine);
				System.out.println(inputLine);
			}
			
			org.jsoup.nodes.Document 	responsePage 	= 	Jsoup.parse(response.toString());
			Elements 					scriptElements 	= 	responsePage.getElementsByTag("script");
			Element 					element  		= 	scriptElements.get(0);
			 							scriptText 		= 	element.html();
			in.close();
			wr.close();			
		} catch (Exception e) {
			String exceptionTest = e.getMessage();
			System.out.println("bug ------------->>> " + exceptionTest + "Request is "   + url );
		}
		finally {
			httpConnection.disconnect();  // Closed underlying connection - underlying socket.
		}
		return scriptText;
	}
		public String inserKeyToSpecificBroadcaster(String url, String id, String[] responseCookieContainer, String HOST, Object caller, String uiport, byte[] keyByteArray)
		{
			List<Byte>   bytesList       	= 	new ArrayList<>();
			StringBuffer response 			= 	new StringBuffer();
			String 		 scriptText			= 	null;
			
			try {
				URL destUrl 		= new URL(url);  
				httpConnection 		= (HttpURLConnection) destUrl.openConnection();
				httpConnection.setDoOutput( true );
				//  Setup parameters and general request properties that you may need before connecting.
				///////////////////////////////////////////////////////////////////////////////////////
				httpConnection.setReadTimeout(40000);
				httpConnection.setRequestMethod("POST");
				httpConnection.setRequestProperty("Accept", "*/*");
				//httpConnection.setRequestProperty("Pragma","no-cache");
				//httpConnection.setRequestProperty("Cache-Control","no-cache");
				httpConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
				httpConnection.setRequestProperty("Host", HOST + ":" + uiport);
				httpConnection.setRequestProperty("Origin", "http://" + HOST + ":" + uiport);
				httpConnection.setRequestProperty("User-Agent", USER_AGENT);
				httpConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
				httpConnection.setRequestProperty("Content-Type", "multipart/form-data" + ";" + " boundary=----WebKitFormBoundary6KCILhDMBQpE7Hys");
				httpConnection.setRequestProperty("Referer", "http://" + HOST + ":" + uiport + "/");
				httpConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.8,he;q=0.6,ru;q=0.4");
				httpConnection.setRequestProperty(StringUtils.substringBetween(responseCookieContainer[0], "=", "%"), StringUtils.substringAfter(responseCookieContainer[0], "%3D"));
				httpConnection.setRequestProperty("Cookie", responseCookieContainer[1] + "; "+ responseCookieContainer[0]);
				//////////////////////////////////////////////////////////////////////////////////////////////////
				DataOutputStream wr = new DataOutputStream( httpConnection.getOutputStream()); 
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
				// Temporary buffer.
				outputStream.write( keyByteArray );
				//byte c[] = outputStream.toByteArray( );
				
				// Print a POST body to a stream.
				wr.write( keyByteArray );
				wr.flush();
				
				BufferedReader 	in 			= new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
				String 			inputLine 	= "";
				while ((inputLine 	= in.readLine()) != null) {
					response.append(inputLine);
					System.out.println(inputLine);
				}
				
				org.jsoup.nodes.Document 	responsePage 	= 	Jsoup.parse(response.toString());
				Elements 					scriptElements 	= 	responsePage.getElementsByTag("script");
				Element 					element  		= 	scriptElements.get(0);
				 							scriptText 		= 	element.html();
				in.close();
				wr.close();			
			} catch (Exception e) {
				String exceptionTest = e.getMessage();
				System.out.println("bug ------------->>> " + exceptionTest + "Request is "   + url );
			}
			finally {
				httpConnection.disconnect();  // Closed underlying connection - underlying socket.
			}
			return scriptText;
		}
	
		
		public String uploadPkToFeeder(String url, String id, String[] responseCookieContainer, String HOST, Object caller, String uiport, byte[] keyByteArray)
		{
			List<Byte>   bytesList       	= 	new ArrayList<>();
			StringBuffer response 			= 	new StringBuffer();
			String 		 scriptText			= 	null;
			
			try {
				URL destUrl 		= new URL(url);  
				httpConnection 		= (HttpURLConnection) destUrl.openConnection();
				httpConnection.setDoOutput( true );
				//  Setup parameters and general request properties that you may need before connecting.
				///////////////////////////////////////////////////////////////////////////////////////
				httpConnection.setReadTimeout(40000);
				httpConnection.setRequestMethod("POST");
				httpConnection.setRequestProperty("Accept", "*/*");
				//httpConnection.setRequestProperty("Pragma","no-cache");
				//httpConnection.setRequestProperty("Cache-Control","no-cache");
				httpConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
				httpConnection.setRequestProperty("Host", HOST + ":" + uiport);
				httpConnection.setRequestProperty("Origin", "http://" + HOST + ":" + uiport);
				httpConnection.setRequestProperty("User-Agent", USER_AGENT);
				httpConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
				httpConnection.setRequestProperty("Content-Type", "multipart/form-data" + ";" + " boundary=----WebKitFormBoundaryIsFztoeBqQxla1HJ");
				httpConnection.setRequestProperty("Referer", "http://" + HOST + ":" + uiport + "/index.html");
				httpConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.8,he;q=0.6,ru;q=0.4");
				httpConnection.setRequestProperty(StringUtils.substringBetween(responseCookieContainer[0], "=", "%"), StringUtils.substringAfter(responseCookieContainer[0], "%3D"));
				httpConnection.setRequestProperty("Cookie", responseCookieContainer[1] + "; "+ responseCookieContainer[0]);
				//////////////////////////////////////////////////////////////////////////////////////////////////
				DataOutputStream wr = new DataOutputStream( httpConnection.getOutputStream()); 
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
				// Temporary buffer.
				outputStream.write( keyByteArray );
				//byte c[] = outputStream.toByteArray( );
				
				// Print a POST body to a stream.
				wr.write( keyByteArray );
				wr.flush();
				
				BufferedReader 	in 			= new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
				String 			inputLine 	= "";
				while ((inputLine 	= in.readLine()) != null) {
					response.append(inputLine);
					System.out.println(inputLine);
				}
				
				org.jsoup.nodes.Document 	responsePage 	= 	Jsoup.parse(response.toString());
				Elements 					scriptElements 	= 	responsePage.getElementsByTag("script");
				Element 					element  		= 	scriptElements.get(0);
				 							scriptText 		= 	element.html();
				in.close();
				wr.close();			
			} catch (Exception e) {
				String exceptionTest = e.getMessage();
				System.out.println("bug ------------->>> " + exceptionTest + "Request is "   + url );
			}
			finally {
				httpConnection.disconnect();  // Closed underlying connection - underlying socket.
			}
			return scriptText;
		}
		
		public String inserKeyToSpecificBroadcaster1(String url, String id, String[] responseCookieContainer, String HOST, Object caller, String uiport, byte[] keyByteArray)
		{
			StringBuffer response 			= 	new StringBuffer();
			String 		 scriptText			= 	null;
			
			try {
				URL destUrl 		= new URL(url);  
				httpConnection 		= (HttpURLConnection) destUrl.openConnection();
				httpConnection.setDoOutput( true );
				//  Setup parameters and general request properties that you may need before connecting.
				///////////////////////////////////////////////////////////////////////////////////////
				httpConnection.setReadTimeout(40000);
				httpConnection.setRequestMethod("POST");
				httpConnection.setRequestProperty("Accept", "*/*");
				//httpConnection.setRequestProperty("Pragma","no-cache");
				//httpConnection.setRequestProperty("Cache-Control","no-cache");
				httpConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
				httpConnection.setRequestProperty("Host", HOST + ":" + uiport);
				httpConnection.setRequestProperty("Origin", "http://" + HOST + ":" + uiport);
				httpConnection.setRequestProperty("User-Agent", USER_AGENT);
				httpConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
				httpConnection.setRequestProperty("Content-Type", "multipart/form-data" + ";" + " boundary=----WebKitFormBoundary4uIxlrzNgZt3P0q9");
				httpConnection.setRequestProperty("Referer", "http://" + HOST + ":" + uiport + "/");
				httpConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.8,he;q=0.6,ru;q=0.4");
				httpConnection.setRequestProperty(StringUtils.substringBetween(responseCookieContainer[0], "=", "%"), StringUtils.substringAfter(responseCookieContainer[0], "%3D"));
				httpConnection.setRequestProperty("Cookie", responseCookieContainer[1] + "; "+ responseCookieContainer[0]);
				//////////////////////////////////////////////////////////////////////////////////////////////////
				DataOutputStream wr = new DataOutputStream( httpConnection.getOutputStream()); 
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
				// Temporary buffer.
				outputStream.write( keyByteArray );
				//byte c[] = outputStream.toByteArray( );
				
				// Print a POST body to a stream.
				wr.write( keyByteArray );
				wr.flush();
				
				BufferedReader 	in 			= new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
				String 			inputLine 	= "";
				while ((inputLine 	= in.readLine()) != null) {
					response.append(inputLine);
					System.out.println(inputLine);
				}
				
				org.jsoup.nodes.Document 	responsePage 	= 	Jsoup.parse(response.toString());
				Elements 					scriptElements 	= 	responsePage.getElementsByTag("script");
				Element 					element  		= 	scriptElements.get(0);
				 							scriptText 		= 	element.html();
				in.close();
				wr.close();			
			} catch (Exception e) {
				String exceptionTest = e.getMessage();
				System.out.println("bug ------------->>> " + exceptionTest + "Request is "   + url );
			}
			finally {
				httpConnection.disconnect();  // Closed underlying connection - underlying socket.
			}
			return scriptText;
		}
		
		
		public String inserKeyToSpecificBroadcasterZenCase(String url, String id, String[] responseCookieContainer, String HOST, Object caller, String uiport, byte[] keyByteArray)
		{
			StringBuffer response 			= 	new StringBuffer();
			String 		 scriptText			= 	null;
			
			try {
				URL destUrl 		= new URL(url);  
				httpConnection 		= (HttpURLConnection) destUrl.openConnection();
				httpConnection.setDoOutput( true );
				//  Setup parameters and general request properties that you may need before connecting.
				///////////////////////////////////////////////////////////////////////////////////////
				httpConnection.setReadTimeout(40000);
				httpConnection.setRequestMethod("POST");
				httpConnection.setRequestProperty("Accept", "*/*");
				//httpConnection.setRequestProperty("Pragma","no-cache");
				//httpConnection.setRequestProperty("Cache-Control","no-cache");
				httpConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
				httpConnection.setRequestProperty("Host", HOST + ":" + uiport);
				httpConnection.setRequestProperty("Origin", "http://" + HOST + ":" + uiport);
				httpConnection.setRequestProperty("User-Agent", USER_AGENT);
				httpConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
				httpConnection.setRequestProperty("Content-Type", "multipart/form-data" + ";" + " boundary=----WebKitFormBoundarymFHoqCWIpOv4psCt");
				httpConnection.setRequestProperty("Referer", "http://" + HOST + ":" + uiport + "/");
				httpConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.8,he;q=0.6,ru;q=0.4");
				httpConnection.setRequestProperty(StringUtils.substringBetween(responseCookieContainer[0], "=", "%"), StringUtils.substringAfter(responseCookieContainer[0], "%3D"));
				httpConnection.setRequestProperty("Cookie", responseCookieContainer[1] + "; "+ responseCookieContainer[0]);
				//////////////////////////////////////////////////////////////////////////////////////////////////
				DataOutputStream wr = new DataOutputStream( httpConnection.getOutputStream()); 
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
				// Temporary buffer.
				outputStream.write( keyByteArray );
				//byte c[] = outputStream.toByteArray( );
				
				// Print a POST body to a stream.
				wr.write( keyByteArray );
				wr.flush();
				
				BufferedReader 	in 			= new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
				String 			inputLine 	= "";
				while ((inputLine 	= in.readLine()) != null) {
					response.append(inputLine);
					System.out.println(inputLine);
				}
				
				org.jsoup.nodes.Document 	responsePage 	= 	Jsoup.parse(response.toString());
				Elements 					scriptElements 	= 	responsePage.getElementsByTag("script");
				Element 					element  		= 	scriptElements.get(0);
				 							scriptText 		= 	element.html();
				in.close();
				wr.close();			
			} catch (Exception e) {
				String exceptionTest = e.getMessage();
				System.out.println("bug ------------->>> " + exceptionTest + "Request is "   + url );
			}
			finally {
				httpConnection.disconnect();  // Closed underlying connection - underlying socket.
			}
			return scriptText;
		}
		
	public String sendGet(String url, String id, int mode, String[] responseCookieContainer, String HOST, Object caller, String uiport) {
		StringBuffer response = new StringBuffer();
		try {
			URL destUrl 		= new URL(url);
			httpConnection 		= (HttpURLConnection) destUrl.openConnection();
			// Setup parameters and general request properties that you may need before connecting.
			///////////////////////////////////////////////////////////////////////////////////////
			httpConnection.setReadTimeout(80000);
			httpConnection.setRequestMethod("GET");
			httpConnection.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
			httpConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			httpConnection.setRequestProperty("TR101Host", HOST + ":" + uiport);
			httpConnection.setRequestProperty("User-Agent", USER_AGENT);
			httpConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
			httpConnection.setRequestProperty("Referer", "http://" + HOST + ":" + uiport + "/index.html");
			if(responseCookieContainer.length == 2)
				httpConnection.setRequestProperty(StringUtils.substringBetween(responseCookieContainer[0], "=", "%"), StringUtils.substringAfter(responseCookieContainer[0], "%3D"));
			httpConnection.setRequestProperty("Cookie", responseCookieContainer[1] + "; " + responseCookieContainer[0]);
			///////////////////////////////////////////////////////////////////////////////////////
			
			InputStream inputStream = null;
			try {
				if ("gzip".equals(httpConnection.getContentEncoding())) {
					inputStream = new GZIPInputStream(httpConnection.getInputStream());
				}else
				if("deflate".equals(httpConnection.getContentEncoding()))
				{
					inputStream = new InflaterInputStream(httpConnection.getInputStream(), new Inflater());
				}
				else {
					inputStream = httpConnection.getInputStream();
				}
			} catch (IOException e) {
				
					InputStream es;
					if ("gzip".equals(httpConnection.getContentEncoding())) {
						es = new BufferedInputStream(new GZIPInputStream(httpConnection.getErrorStream()));
					}
					else {
						es = new BufferedInputStream(httpConnection.getErrorStream());
					}

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					try {
						// read the response body
						byte[] buf = new byte[1024];
						int read = -1;
						while ((read = es.read(buf)) > 0) {
							baos.write(buf, 0, read);
						}
					} catch (IOException e1) {
						System.out.println( "IOException when reading the error stream. Igored");
					}

					// close the errorstream
					es.close();

					throw new IOException("Error while reading from " + httpConnection.getRequestMethod() + ": [" + httpConnection.getResponseCode() + "] "
							+ httpConnection.getResponseMessage() + "\n" + new String(baos.toByteArray(), "UTF-8"), e);
				
			}
			/////////////////////////////////////////////////////////////////////////////////////
			if(inputStream == null)
				return null;
			
			BufferedReader 		in 					= 	new BufferedReader(new InputStreamReader(inputStream));
			String 				inputLine 			= 	null;
			while ((inputLine = in.readLine()) != null){ response.append(inputLine); }
			in.close();
			
			// Local variables.
			JmesPath<JsonNode> jmespath 	= new JacksonRuntime();
			Expression<JsonNode> expression = null;
			ObjectMapper mapper 			= new ObjectMapper();
		    JsonNode actualObj 				= null;
		    JsonNode result 				= null;
		   
			switch(mode)
			{
			
			  case MSG: 						inputLine = response.toString();
												json = new JSONObject(inputLine);
												return json.get("msg").toString();
			 
			  case RECEIVERIDMODE:		 		inputLine = response.toString();
				  								json = new JSONObject(inputLine);
												JSONArray streams = json.getJSONArray("streams");
												for (int i = 0; i < streams.length(); i++) 
												{
													json = streams.getJSONObject(i);
								
													if (json.get("name").toString().equals(id)) 
													{
														System.out.println(json.get("name").toString());
														return json.get("id").toString();
													}
												}
												System.out.println("no such a stream");
												return "no such a stream";
									
				case FEEDER_SSH_SERVER_STATUS:  inputLine = response.toString();
				   							    json = new JSONObject(inputLine);
				   							    return json.getString("host");
				
				case RECEIVER_ACTIVE_OUT_MODE:  inputLine = response.toString();  		   							    
												json = new JSONObject(inputLine);
												String  activeStreamId = json.getString("active_in");
												JSONArray primaryStreamsArray = json.getJSONArray("ins");
												int arrayLength  = primaryStreamsArray.length();
												JSONObject primaryStreamDescriotion = null;
												for(int i = 0; i < arrayLength; i++)
												{
													primaryStreamDescriotion = primaryStreamsArray.getJSONObject(i);
													if(primaryStreamDescriotion.getString("id").equals(activeStreamId))
													{
														return primaryStreamDescriotion.getString("source");
													}
												}
												return null;
				
				case SYS_CPU:			inputLine = response.toString();
												json = new JSONObject(inputLine);
												return json.toString();
				
				case ADAPTIVE_REC:		inputLine = response.toString(); // Response from broadcaster
														json = new JSONObject(inputLine);
														JmesPath<JsonNode> jmespathRecRes = new JacksonRuntime();
														Expression<JsonNode> expressionRecRes = jmespathRecRes.compile("success");
														ObjectMapper mapperRecRes = new ObjectMapper();
													    JsonNode actualObjRecRes = mapperRecRes.readTree(inputLine);
													    JsonNode resultRecRes = expressionRecRes.search(actualObjRecRes);
													    return resultRecRes.toString();

				
													
				case TRANSSTAT:	inputLine = response.toString(); // Response from broadcaster
												json = new JSONObject(inputLine);
												JmesPath<JsonNode> jmespathTransStat = new JacksonRuntime();
												Expression<JsonNode> expressionTransStat =
												jmespathTransStat.compile("{connections:connections, trans_stats:trans.[muxer_resets, decoder_resets, encoder_resets,muxer_drops,muxer_resets,raw_frame_drops,restarts,smoother_drops,src_cmp_frame_demux_drop,src_cmp_frame_overflows]}");
												ObjectMapper mapperTransStat = new ObjectMapper();
											    JsonNode actualObjTransStat = mapperTransStat.readTree(inputLine);
											    JsonNode resultTransStat = expressionTransStat.search(actualObjTransStat);
											    // An example of the output Json:
//											    {
//										    	  "disconnetions": 0,
//										    	  "trans_stats": [
//										    	    0,
//										    	    0,
//										    	    0,
//										    	    0,
//										    	    0,
//										    	    0,
//										    	    0,
//										    	    0,
//										    	    0,
//										    	    0
//										    	  ]
//											    }
											    return resultTransStat.toString();
				
				case VALIDATOR:	inputLine = response.toString(); // Response from broadcaster
												json = new JSONObject(inputLine);
												jmespath = new JacksonRuntime();
												expression = jmespath.compile("messages[*].errorComment");
												mapper = new ObjectMapper();
											    actualObj = mapper.readTree(inputLine);
											    result = expression.search(actualObj);
											    return result.toString();
				
				case STR_STATS:	inputLine = response.toString(); // Response from broadcaster
												json = new JSONObject(inputLine);
												jmespath = new JacksonRuntime();
												expression = jmespath.compile("streams[*].{strId: id, upTime: total_uptime, downTime: total_downtime}");
												mapper = new ObjectMapper();
											    actualObj = mapper.readTree(inputLine);
											    result = expression.search(actualObj);
											    return result.toString();				
				
				case GET_STREAM_PROGRAMS_IDS:   inputLine = response.toString(); // SUT response.
																				json = new JSONObject(inputLine);
																				jmespath = new JacksonRuntime();
																				expression = jmespath.compile("inputs[?name=='" + id + "'].programs[*].prog[]"); // Get stream's programs identifiers by stream name: [1,2,..,n]
																				mapper = new ObjectMapper();
																			    actualObj = mapper.readTree(inputLine);
																			    result = expression.search(actualObj);
																			    return result.toString();
											    
				case GET_ELEM_PIDS_OF_PROGR: 	inputLine = response.toString(); // SUT response.
																			json = new JSONObject(inputLine);
																			jmespath = new JacksonRuntime();
																			expression = jmespath.compile("pids[*].pid"); // Get elementary stream IDs by program identifier.
																			mapper = new ObjectMapper();
																		    actualObj = mapper.readTree(inputLine);
																		    result = expression.search(actualObj);
																		    return result.toString();
											    
				case GET_FAILOVER_COMPONENTS: 	inputLine = response.toString(); // Response from broadcaster
																				json = new JSONObject(inputLine);
																				JmesPath<JsonNode> jmespathBackUp = new JacksonRuntime();
																				Expression<JsonNode> expressionBackUp =
																				jmespathBackUp.compile("streams[?id=='" + id + "'].compound_components[*].[id, packets][]");
																				ObjectMapper mapperBackUp = new ObjectMapper();
																			    JsonNode actualObjBackUp = mapperBackUp.readTree(inputLine);
																			    JsonNode resultBackUp = expressionBackUp.search(actualObjBackUp);
																			    return resultBackUp.toString();
				
				case TR101:						inputLine = response.toString(); // Response from broadcaster
												jmespath = new JacksonRuntime();
												expression = jmespath.compile("tr101[*][*][] | [?name == 'Low_video_quality' || name == 'Audio_clipping' || name == 'Silent_audio' || name =='Blank_picture' || name == 'Frozen_video' || name == 'Continuity_count_error'].[name, count]");
												mapper = new ObjectMapper();
											    actualObj = mapper.readTree(inputLine);
											     result = expression.search(actualObj);
											    return result.toString();
//											    [
//											     [
//											       "Frozen_video",
//											       0
//											     ],
//											     [
//											       "Blank_picture",
//											       0
//											     ],
//											     [
//											       "Silent_audio",
//											       0
//											     ],
//											     [
//											       "Low_video_quality",
//											       0
//											     ],
//											     [
//											       "Audio_clipping",
//											       0
//											     ]
//											   ]
											    
												
				case ANALIZE_MODE:				inputLine = response.toString();
												json = new JSONObject(inputLine);
												JSONArray streamim = json.getJSONArray("streams");
												for (int i = 0; i < streamim.length(); i++) 
												{
													json = streamim.getJSONObject(i);
								
													if ( json.getString("id").equals(id) )
													{
														if( json.getInt("analyze") == 1 )
														{
															return "analyzed";
														}
													}

												}
												return "not analyzed";
				
				case FEEDER_SSH_KEY_STATUS:     inputLine = response.toString();   
												json = new JSONObject(inputLine);
											    json = json.getJSONObject("server");
											    return json.getInt("key_present") + "";
				
				case PUSHMODE:					inputLine = response.toString();
												int indx = inputLine.indexOf("(");
												inputLine = (inputLine.substring(indx + 1,inputLine.indexOf(");")));
												json = new JSONObject(inputLine);
												return json.get("msg").toString();
				
				case ADD_TRANSCODER_PROFILE: 	inputLine = response.toString();
												json = new JSONObject(inputLine);
												return json.get("msg").toString();
												
				case FEEDER_ADD_FAILOVER_GROUP: inputLine = response.toString();
												json = new JSONObject(inputLine);
												return json.get("msg").toString();
												
				case TUNNEL_ADDED_MODE: 		inputLine = response.toString();
																		json = new JSONObject(inputLine);
																		return json.get("msg").toString();
				
				case PUSHINMODE:				inputLine = response.toString();
																json = new JSONObject(inputLine);
																return json.get("msg").toString();
				
				case RECEIVER_UDP_OUT_MODE: 	inputLine = response.toString();
																			json = new JSONObject(inputLine);
																			return json.get("message").toString();
				
				case PULLMODE: 					inputLine = response.toString();
																int ind = inputLine.indexOf("(");
																inputLine = (inputLine.substring(ind + 1, inputLine.indexOf(");")));
																json = new JSONObject(inputLine);
																return json.get("msg").toString();
				
				case UDPMODE: 	inputLine = response.toString();
												json = new JSONObject(inputLine);
												return json.get("msg").toString();
												
				case FEEDER_GET_STREAMS:		inputLine = response.toString();
																		json = new JSONObject(inputLine);
																		JSONArray allFeederInputs = json.getJSONArray("inputs");
																		JSONArray feederStreamsNames = new JSONArray();
																		for(int i = 0; i < allFeederInputs.length(); i++)
																		{
																			feederStreamsNames.put(allFeederInputs.getJSONObject(i).get("name"));
																		}
																		return feederStreamsNames.toString();
												
				case FEEDER_GET_INPUTS_DATA:	inputLine = response.toString();
																			json = new JSONObject(inputLine);
																			JSONArray allFeederInputsData = json.getJSONArray("inputs");
																			return allFeederInputsData.toString();
												
				case FEEDER_GET_STREAMS_IPS: 	inputLine = response.toString();
																			json = new JSONObject(inputLine);
																			JSONArray allFeederInputsStreamsArr = json.getJSONArray("inputs");
																			JSONArray feederStreamsNamesIPs = new JSONArray();
																			for(int i = 0; i < allFeederInputsStreamsArr.length(); i++)
																			{
																				feederStreamsNamesIPs.put(allFeederInputsStreamsArr.getJSONObject(i).get("mip"));
																				feederStreamsNamesIPs.put(allFeederInputsStreamsArr.getJSONObject(i).get("ip"));
																				feederStreamsNamesIPs.put(allFeederInputsStreamsArr.getJSONObject(i).get("port"));
																			}
																			return feederStreamsNamesIPs.toString();
												
				case FEEDER_GET_STREAMS_BY_ID:			inputLine = response.toString();
																					json = new JSONObject(inputLine);
																					JSONArray allFeederInputsStreams = json.getJSONArray("inputs");
																					String [] namesById = id.split("@");
																					ArrayList<String> namesByIdArrayList = new ArrayList<>(Arrays.asList(namesById));
																					ArrayList<Integer> IDs = new ArrayList<>();
																					for(int i = 0; i < allFeederInputsStreams.length(); i++)
																					{
																						String inName = allFeederInputsStreams.getJSONObject(i).getString("name");
																						if(namesByIdArrayList.contains(inName))
																						{
																							IDs.add(allFeederInputsStreams.getJSONObject(i).getInt("id"));
																						}
																					}
																					return IDs.toString(); 
				
				case RECEIVERMODE: 				inputLine = response.toString();
																	json = new JSONObject(inputLine);
																	return json.get("message").toString();
				
				case RECEIVERDELETIONMODE:		inputLine = response.toString();
												json = new JSONObject(inputLine);
												return json.get("message").toString();
												
				case RECEIVERSTATISTICMODE: 	inputLine = response.toString();
												json = new JSONObject(inputLine);
												return json.getJSONObject("data").get("bitrate").toString();
												
				case RECEIVERSTATDATA: 			inputLine = response.toString();
												json = new JSONObject(inputLine);
												return json.getJSONObject("data").get("bitrate").toString().toString() + "#" + 
												json.getJSONObject("data").get("dropped").toString()  + "#" + 
												json.getJSONObject("data").get("recovered").toString();
				
				case UDPOUTMODE:				inputLine = response.toString();
												json = new JSONObject(inputLine);
												tester = json.getString("msg");
												if (tester.endsWith("Output " + id + " added."))
												{
													return tester = "Output " + id + " added.";
												}
				
				case JSONMODE:					ArrayList<String> inputsStreamNames = new ArrayList();
												inputLine = response.toString();
												json = new JSONObject(inputLine);
												JSONArray inputStreamsJsonArrayObj = json.getJSONArray("streams");
												int numberOfElementsInInputStreamsJsonArrayObj = inputStreamsJsonArrayObj.length();
												for (int i = 0; i < numberOfElementsInInputStreamsJsonArrayObj; i++) {
													json = inputStreamsJsonArrayObj.getJSONObject(i);
													inputsStreamNames.add(json.get("id").toString());
												}
												tester = "good";
												
				case RECORDING:					inputLine = response.toString();
												json = new JSONObject(inputLine);
												JSONArray input_streams = json.getJSONArray("streams");
												int streamsCnt = input_streams.length();

												for (int i = 0; i < streamsCnt; i++) {
													json = input_streams.getJSONObject(i);
													if(json.getString("id").equals(id))
													{
														json = json.getJSONObject("live_recording_status");
														return json.getInt("active") + "";
													}
												}
												return "bad_sstate";
				
				case SET_RTMMP_AUTO_REMOTE: 	inputLine = response.toString();
												json = new JSONObject(inputLine);
												return json.getJSONObject("http_outs").getInt("rtmp_auto_out") + "";
				
				case LINE_MODE: 				return inputLine = response.toString();
				
				case PUSHOUTMODE: 			    String wholeResult;
												String[] splittedResults;
												inputLine = response.toString();
												json = new JSONObject(inputLine);
												//System.out.println("Debug printing   -- " + json.get("msg").toString());
												wholeResult = json.get("msg").toString();
												splittedResults = wholeResult.split(",");
												return splittedResults[0];
												
				case CNANGE_SETTINGS_MODE:      inputLine = response.toString();
												json      = new JSONObject(inputLine);
												if(json != null) return GOOD;
												else throw new Exception("No new settings was applayed");
												
				case EDIT_STREAM_MODE: 			inputLine = response.toString();
																		json      = new JSONObject(inputLine);
																		if(json.getString("msg").equals("Applied new configuration to " + id))
																			return GOOD;
																		else throw new Exception("No new settings was applayed");  
				
												
				case UPTIME:      				inputLine = response.toString();
															json      = new JSONObject(inputLine);
															String upTime = json.getString("up_time");
															return upTime;
												
												
				case MULTICAST_IP_MODE:     	inputLine 			= response.toString();  
																	json      			= new JSONObject(inputLine);
																	JSONArray statuses 	= json.getJSONArray("status");
																	for (int i = 0; i < statuses.length(); i++) 
																	{
																		json = statuses.getJSONObject(i);
													
																		if (json.getString("source_id").equals(id)) 
																		{
																			System.out.println(json.get("source_id"));
																			return json.getString("multicast_ip");
																		}
																	}
				case FILE_FROM_FOLDER:     	    inputLine 					= response.toString();  
												json      			        = new JSONObject(inputLine);
												JSONArray filesInFolder 	= json.getJSONArray("files");
												String fielName             = null;
												for (int i = 0; i < filesInFolder.length(); i++) 
												{
													json = filesInFolder.getJSONObject(i);
													if ((fielName  = json.getString("name")).startsWith(id)  ) 
													{
														return fielName;
													}
												}
												return fielName;
			}
		} catch (Exception e) {
			String exceptionTest = e.getMessage();
			System.out.println("bug ------------->>> " + exceptionTest + " Request is "   + url + " \nLine Number is " +  debugLineNumber);
			System.out.println("Exception type is " + e.getClass());
		}
		finally {
			httpConnection.disconnect();
		}
		return response.toString();
	}
	
	public String sendGetAnalyzer(String url, String id, int mode, String HOST, String uiport) {
		StringBuffer response = new StringBuffer();
		try {
			URL destUrl 		= new URL(url);
			httpConnection 		= (HttpURLConnection) destUrl.openConnection();
			// Setup parameters and general request properties that you may need before connecting.
			///////////////////////////////////////////////////////////////////////////////////////
			httpConnection.setReadTimeout(80000);
			httpConnection.setRequestMethod("GET");
			httpConnection.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
			httpConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			httpConnection.setRequestProperty("TR101Host", HOST + ":" + uiport);
			httpConnection.setRequestProperty("User-Agent", USER_AGENT);
			httpConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
			httpConnection.setRequestProperty("Referer", "http://" + HOST + ":" + uiport + "/index.html");
			
			InputStream inputStream = null;
			try {
				if ("gzip".equals(httpConnection.getContentEncoding())) {
					inputStream = new GZIPInputStream(httpConnection.getInputStream());
				}else
				if("deflate".equals(httpConnection.getContentEncoding()))
				{
					inputStream = new InflaterInputStream(httpConnection.getInputStream(), new Inflater());
				}
				else {
					inputStream = httpConnection.getInputStream();
				}
			} catch (IOException e) {
				
					InputStream es;
					if ("gzip".equals(httpConnection.getContentEncoding())) {
						es = new BufferedInputStream(new GZIPInputStream(httpConnection.getErrorStream()));
					}
					else {
						es = new BufferedInputStream(httpConnection.getErrorStream());
					}

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					try {
						// read the response body
						byte[] buf = new byte[1024];
						int read = -1;
						while ((read = es.read(buf)) > 0) {
							baos.write(buf, 0, read);
						}
					} catch (IOException e1) {
						System.out.println( "IOException when reading the error stream. Igored");
					}

					// close the errorstream
					es.close();

					throw new IOException("Error while reading from " + httpConnection.getRequestMethod() + ": [" + httpConnection.getResponseCode() + "] "
							+ httpConnection.getResponseMessage() + "\n" + new String(baos.toByteArray(), "UTF-8"), e);
				
			}
			/////////////////////////////////////////////////////////////////////////////////////
			if(inputStream == null)
				return null;
			
			BufferedReader 		in 					= 	new BufferedReader(new InputStreamReader(inputStream));
			String 				        inputLine 			= 	null;
			while ((inputLine = in.readLine()) != null){ response.append(inputLine); }
			in.close();
			
			switch(mode)
			{
			  
				
				case VALIDATOR:	inputLine = response.toString(); // Response from broadcaster
												json = new JSONObject(inputLine);
												
												JmesPath<JsonNode> jmespathVALIDATOR = new JacksonRuntime();
												Expression<JsonNode> expressionVALIDATOR_Errors 		= jmespathVALIDATOR.compile("messages[*].errorComment");
												Expression<JsonNode> expressionVALIDATOR_Segmentcounts 	= jmespathVALIDATOR.compile("variants[*].processedSegmentsCount");
												
												Expression<JsonNode> expressionVALIDATOR_SegmentsErrors = jmespathVALIDATOR.compile("variants[].discontinuities[].segments[].messages[].errorComment");
												
												
												ObjectMapper mapperVALIDATOR_Segmentcounts = new ObjectMapper();
											    JsonNode actualObjVALIDATOR_Segmentcounts = mapperVALIDATOR_Segmentcounts.readTree(inputLine);
											   
											    JsonNode resultVALIDATOR_Segmentcounts = expressionVALIDATOR_Segmentcounts.search(actualObjVALIDATOR_Segmentcounts);
											    
											    JSONArray segmentCntJsonArray = new JSONArray(resultVALIDATOR_Segmentcounts.toString());
											    String segmentCnt = "";
											    for(int i = 0; i< segmentCntJsonArray.length(); i++)
											    {
											    	if( segmentCntJsonArray.getInt(i) == 0 )
											    	{
											    		segmentCnt = " No segments detested";
											    		break;
											    	}
											    		 
											    }
											    
												ObjectMapper mapperVALIDATOR = new ObjectMapper();
											    JsonNode actualObjVALIDATOR  = mapperVALIDATOR.readTree(inputLine);
											    JsonNode resultVALIDATOR     = expressionVALIDATOR_Errors.search(actualObjVALIDATOR);
											    
											    ObjectMapper mapperVALIDATORSegmentsErrors = new ObjectMapper();
											    JsonNode lObjVALIDATORSegmentsErrors = mapperVALIDATORSegmentsErrors.readTree(inputLine);
											    JsonNode VALIDATORSegmentsErrors = expressionVALIDATOR_SegmentsErrors.search(lObjVALIDATORSegmentsErrors);
											    JSONArray segmentsErrorsJsonArray = new JSONArray(VALIDATORSegmentsErrors.toString());
											    String segmentErrorMsg = "";
											    if(segmentsErrorsJsonArray.length() > 0) 
											    	segmentErrorMsg = segmentsErrorsJsonArray.toString();
											    
											    return resultVALIDATOR.toString() + segmentCnt + segmentErrorMsg;			
			}
		} catch (Exception e) {
			String exceptionTest = e.getMessage();
			System.out.println("bug ------------->>> " + exceptionTest + " Request is "   + url + " \nLine Number is " +  debugLineNumber);
			System.out.println("Exception type is " + e.getClass());
		}
		finally {
			httpConnection.disconnect();
		}
		return response.toString();
	}
	
	public String[] zenLogginPost(String url, String userName, String userPass, String uiport, String host, byte[] body)
	{
		StringBuffer response 			= 	new StringBuffer();
		String[] cookiesValues 			= new String[2];
		try {
			URL destUrl 		= new URL(url);  
			httpsURLConnection 		= (HttpsURLConnection) destUrl.openConnection();
			httpsURLConnection.setDoOutput( true );
			httpsURLConnection.setReadTimeout(40000);
			httpsURLConnection.setRequestMethod("POST");
			//httpsURLConnection.setRequestProperty(":authority", host);
			httpsURLConnection.setRequestProperty("Accept", "*/*");
			httpsURLConnection.setRequestProperty("Connection","keep-alive");
			httpsURLConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			httpsURLConnection.setRequestProperty("Host", host );
			httpsURLConnection.setRequestProperty("Origin", "https://" + host );
			httpsURLConnection.setRequestProperty("User-Agent", USER_AGENT);
			httpsURLConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
			httpsURLConnection.setRequestProperty("Content-Type", "application/json" + ";" + "charset=UTF-8");
			httpsURLConnection.setRequestProperty("Referer", "https://" + host + "/");
			httpsURLConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.8,he;q=0.6,ru;q=0.4");
			//////////////////////////////////////////////////////////////////////////////////////////////////
			DataOutputStream wr = new DataOutputStream( httpsURLConnection.getOutputStream());
			// Print a POST body to a stream.
			wr.write( body );
			wr.flush();
			BufferedReader 	in 			= new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
			String 			inputLine 	= "";
			while ((inputLine 	= in.readLine()) != null) {
				response.append(inputLine);
				System.out.println(inputLine);
			}
			// get all headers
			Map<String, List<String>> map = httpsURLConnection.getHeaderFields();
			for (Map.Entry<String, List<String>> entry : map.entrySet()) {
				if (entry.getKey() != null) {
					if (entry.getKey().equals("set-cookie")) {
						String value = entry.getValue().get(0);
						String[] cookies;
						cookies = value.split(";");
						for(int i = 0; i < cookies.length; i++)
						{
							cookiesValues [i] = cookies[i].split("=")[1];
						}
						break;
					}
				}	
			}
			in.close();
			wr.close();			
		} catch (Exception e) {
			String exceptionTest = e.getMessage();
			System.out.println("bug ------------->>> " + exceptionTest + "Request is "   + url );
		}
		finally {
			httpsURLConnection.disconnect();  // Closed underlying connection - underlying socket.
		}
		return cookiesValues;
	}
	
	
	public String zenPost(String url, String userName, String userPass, String uiport, String HOST, byte[] body, int mode, String[] responseCookieContainer)
	{
		StringBuffer response 			= 	new StringBuffer();
		String[] cookiesValues 			= 	new String[2];
		String nagativeResult			=	"false";
		try {
			URL destUrl 		= new URL(url);  
			httpConnection 		= (HttpURLConnection) destUrl.openConnection();
			httpConnection.setDoOutput( true );
			//  Setup parameters and general request properties that you may need before connecting.
			///////////////////////////////////////////////////////////////////////////////////////
			httpConnection.setReadTimeout(40000);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Accept", "application/json, text/plain, */*");
			httpConnection.setRequestProperty("Accept-Language", "en-US, he; q=0.8, en-GB; q=0.5, en; q=0.3");
			httpConnection.setRequestProperty("Connection", "Keep-Alive");
			httpConnection.setRequestProperty("Host", HOST);
			httpConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			httpConnection.setRequestProperty("User-Agent", USER_AGENT);
			httpConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
			httpConnection.setRequestProperty("Origin", "http://" + HOST );
			httpConnection.setRequestProperty("Referer", "http://" + HOST + "/");
			httpConnection.setRequestProperty("Cookie", "NG_TRANSLATE_LANG_KEY=en; valid_user=true; dns_prefix=zixi; tunnelers_host=.stagingio.devcloud.zixi.com; application_host=zixi.stagingio.devcloud.zixi.com; zixi__sid=" 
											+ responseCookieContainer[0]);
			
			//////////////////////////////////////////////////////////////////////////////////////////////////
			DataOutputStream wr = new DataOutputStream( httpConnection.getOutputStream());
			// Print a POST body to a stream.
			wr.write( body );
			wr.flush();
			
			InputStream inputStream = null;
			try {
				if ("gzip".equals(httpConnection.getContentEncoding())) {
					inputStream = new GZIPInputStream(httpConnection.getInputStream());
				}else
				if("deflate".equals(httpConnection.getContentEncoding()))
				{
					inputStream = new InflaterInputStream(httpConnection.getInputStream(), new Inflater());
				}
				else {
					inputStream = httpConnection.getInputStream();
				}
			} catch (IOException e) {
				
					InputStream es;
					if ("gzip".equals(httpConnection.getContentEncoding())) {
						es = new BufferedInputStream(new GZIPInputStream(httpConnection.getErrorStream()));
					}
					else {
						es = new BufferedInputStream(httpConnection.getErrorStream());
					}

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					try {
						// read the response body
						byte[] buf = new byte[1024];
						int read = -1;
						while ((read = es.read(buf)) > 0) {
							baos.write(buf, 0, read);
						}
					} catch (IOException e1) {
						System.out.println( "IOException when reading the error stream. Igored");
					}

					// close the errorstream
					es.close();

					throw new IOException("Error while reading from " + httpConnection.getRequestMethod() + ": [" + httpConnection.getResponseCode() + "] "
							+ httpConnection.getResponseMessage() + "\n" + new String(baos.toByteArray(), "UTF-8"), e);
				
			}
			/////////////////////////////////////////////////////////////////////////////////////
			if(inputStream == null)
				return null;
			
			BufferedReader 	in 			= new BufferedReader(new InputStreamReader(inputStream));
			String 			inputLine 	= "";
			while ((inputLine 	= in.readLine()) != null) {
				response.append(inputLine);
				System.out.println(inputLine);
			}
			
			switch(mode)
			{
					case ZEN_ADD_FEEDER: 		inputLine = response.toString();
												json = new JSONObject(inputLine);
												return json.getBoolean("success") + "";
												
					case ZEN_ADD_RECEIVVER: 	inputLine = response.toString();
												json = new JSONObject(inputLine);
												return json.getBoolean("success") + "";
					
					case ZEN_ADD_CLUSTER: 		inputLine = response.toString();
												json = new JSONObject(inputLine);
												return json.getBoolean("success") + "";
					
					case ZEN_ADD_BROADCASTER: 	inputLine = response.toString();
												json = new JSONObject(inputLine);
												return json.getBoolean("success") + "";
												
					case ZEN_ADD_FEEDER_SOURCE: 	inputLine = response.toString();
													json = new JSONObject(inputLine);
													return json.getBoolean("success") + "";
					
					case ZEN_ADD_USER: 				inputLine = response.toString();
													json = new JSONObject(inputLine);
													return json.getBoolean("success") + "";
			}
			
			in.close();
			wr.close();			
		} catch (Exception e) {
			String exceptionTest = e.getMessage();
			nagativeResult = exceptionTest;
			System.out.println("bug ------------->>> " + exceptionTest + " Request is "   + url );
		}
		finally {
			httpConnection.disconnect();  // Closed underlying connection - underlying socket.
		}
		return nagativeResult;
	}

	public String zenSendDelete(String url, int mode, String[] responseCookieContainer, String HOST, String uiport, String id) {
		StringBuffer response = new StringBuffer();
		try {
			URL destUrl 		= new URL(url);
			httpsURLConnection 		= (HttpsURLConnection) destUrl.openConnection();
			// Setup parameters and general request properties that you may need before connecting.
			///////////////////////////////////////////////////////////////////////////////////////
			httpsURLConnection.setReadTimeout(80000);
			httpsURLConnection.setRequestMethod("DELETE");
			httpsURLConnection.setRequestProperty("Accept", "application/json, text/plain, */*");
			httpsURLConnection.setRequestProperty("Accept-Language", "en-US, he; q=0.8, en-GB; q=0.5, en; q=0.3");
			httpsURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpsURLConnection.setRequestProperty("Host", HOST);
			httpsURLConnection.setRequestProperty("If-None-Match", "W/\"1dd5-yjSBx29sTLhhC6r8ItNdMw\"");
			httpsURLConnection.setRequestProperty("User-Agent", USER_AGENT);
			httpsURLConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
			httpsURLConnection.setRequestProperty("Referer", "https://" + HOST );
			httpsURLConnection.setRequestProperty("Cookie", "NG_TRANSLATE_LANG_KEY=en; valid_user=true; dns_prefix=zixi; tunnelers_host=.stagingio.devcloud.zixi.com; application_host=zixi.stagingio.devcloud.zixi.com; zixi__sid=" 
											+ responseCookieContainer[0]);
			///////////////////////////////////////////////////////////////////////////////////////
			
			InputStream inputStream = null;
			
			try {
				if ("gzip".equals(httpsURLConnection.getContentEncoding())) {
					inputStream = new GZIPInputStream(httpsURLConnection.getInputStream());
				}else
				if("deflate".equals(httpsURLConnection.getContentEncoding()))
				{
					inputStream = new InflaterInputStream(httpsURLConnection.getInputStream(), new Inflater());
				}
				else {
					inputStream = httpsURLConnection.getInputStream();
				}
			} catch (IOException e) {
				
					InputStream es;
					if ("gzip".equals(httpsURLConnection.getContentEncoding())) {
						es = new BufferedInputStream(new GZIPInputStream(httpsURLConnection.getErrorStream()));
					}
					else {
						es = new BufferedInputStream(httpsURLConnection.getErrorStream());
					}

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					try {
						// read the response body
						byte[] buf = new byte[1024];
						int read = -1;
						while ((read = es.read(buf)) > 0) {
							baos.write(buf, 0, read);
						}
						if(baos.size() !=0)
						{
							response.append(baos.toString());
						}
					} catch (IOException e1) {
						System.out.println( "IOException when reading the error stream. Igored");
					}

					// close the errorstream
					es.close();

					throw new IOException("Error while reading from " + httpsURLConnection.getRequestMethod() + ": [" + httpsURLConnection.getResponseCode() + "] "
							+ httpsURLConnection.getResponseMessage() + "\n" + new String(baos.toByteArray(), "UTF-8"), e);
				
			}
			/////////////////////////////////////////////////////////////////////////////////////
			if(inputStream == null)
				return null;
			
			BufferedReader 		in 					= 	new BufferedReader(new InputStreamReader(inputStream));
			String 				inputLine 			= 	null;
			while ((inputLine = in.readLine()) != null){ response.append(inputLine); }
			in.close();
			
			switch(mode)
			{
				case ZEN_DELETE_CLUSTER:		inputLine = response.toString();
												json = new JSONObject(inputLine);
												if(json.getBoolean("success") == true)
												{
													if(json.getInt("result") == Integer.parseInt(id))
														return "true";
												} return "failed"; 
												
				case ZEN_DELETE_CHANNEL:		inputLine = response.toString();
												json = new JSONObject(inputLine);
												if(json.getBoolean("success") == true)
												{
													return "true";
												} return "failed";				
			}
		} catch (Exception e) {
			String exceptionTest = e.getMessage();
			System.out.println("bug ------------->>> " + exceptionTest + " Request is "   + url + " \nLine Number is " +  debugLineNumber);
			System.out.println("Exception type is " + e.getClass());
		}
		finally {
			httpsURLConnection.disconnect(); 
		}
		return response.toString();
	}
	
	public String zenSendGet(String url, int mode, String[] responseCookieContainer, String HOST, String uiport, String id) {
		StringBuffer response = new StringBuffer();
		try {
			URL destUrl 		= new URL(url);
			httpsURLConnection 		= (HttpsURLConnection) destUrl.openConnection();
			// Setup parameters and general request properties that you may need before connecting.
			///////////////////////////////////////////////////////////////////////////////////////
			httpsURLConnection.setReadTimeout(80000);
			httpsURLConnection.setRequestMethod("GET");
			httpsURLConnection.setRequestProperty("Accept", "application/json, text/plain, */*");
			httpsURLConnection.setRequestProperty("Accept-Language", "en-US, he; q=0.8, en-GB; q=0.5, en; q=0.3");
			httpsURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpsURLConnection.setRequestProperty("Host", HOST);
			httpsURLConnection.setRequestProperty("If-None-Match", "W/\"1dd5-yjSBx29sTLhhC6r8ItNdMw\"");
			httpsURLConnection.setRequestProperty("User-Agent", USER_AGENT);
			httpsURLConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
			httpsURLConnection.setRequestProperty("Referer", "https://" + HOST );
			httpsURLConnection.setRequestProperty("Cookie", "NG_TRANSLATE_LANG_KEY=en; valid_user=true; dns_prefix=zixi; tunnelers_host=.stagingio.devcloud.zixi.com; application_host=zixi.stagingio.devcloud.zixi.com; zixi__sid=" 
											+ responseCookieContainer[0]);
			///////////////////////////////////////////////////////////////////////////////////////
			
			InputStream inputStream = null;
			
			try {
				if ("gzip".equals(httpsURLConnection.getContentEncoding())) {
					inputStream = new GZIPInputStream(httpsURLConnection.getInputStream());
				}else
				if("deflate".equals(httpsURLConnection.getContentEncoding()))
				{
					inputStream = new InflaterInputStream(httpsURLConnection.getInputStream(), new Inflater());
				}
				else {
					inputStream = httpsURLConnection.getInputStream();
				}
			} catch (IOException e) {
				
					InputStream es;
					if ("gzip".equals(httpsURLConnection.getContentEncoding())) {
						es = new BufferedInputStream(new GZIPInputStream(httpsURLConnection.getErrorStream()));
					}
					else {
						es = new BufferedInputStream(httpsURLConnection.getErrorStream());
					}

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					try {
						// read the response body
						byte[] buf = new byte[1024];
						int read = -1;
						while ((read = es.read(buf)) > 0) {
							baos.write(buf, 0, read);
						}
					} catch (IOException e1) {
						System.out.println( "IOException when reading the error stream. Igored");
					}

					// close the errorstream
					es.close();

					throw new IOException("Error while reading from " + httpsURLConnection.getRequestMethod() + ": [" + httpsURLConnection.getResponseCode() + "] "
							+ httpsURLConnection.getResponseMessage() + "\n" + new String(baos.toByteArray(), "UTF-8"), e);
				
			}
			/////////////////////////////////////////////////////////////////////////////////////
			if(inputStream == null)
				return null;
			
			BufferedReader 		in 					= 	new BufferedReader(new InputStreamReader(inputStream));
			String 				inputLine 			= 	null;
			while ((inputLine = in.readLine()) != null){ response.append(inputLine); }
			in.close();
			
			// Local variables.
			JmesPath<JsonNode> jmespath 	= new JacksonRuntime();
			Expression<JsonNode> expression = null;
			ObjectMapper mapper 			= new ObjectMapper();
		    JsonNode actualObj 				= null;
		    JsonNode result 				= null;
			
			switch(mode)
			{
			
				case ZEN_GET_KEYS:				inputLine = response.toString();
												json = new JSONObject(inputLine);
												JSONArray inputJsonArray = json.getJSONArray("result");
												for(int i = 0; i < inputJsonArray.length(); i++)
												{
													JSONObject obj = inputJsonArray.getJSONObject(i); 
													if(obj.getString("name").equals(id)) return obj.getInt("id") + ""; 
												}
												return "key name not found";
												
												
				case ZEN_GET_SOURCE_OBJ:		inputLine = response.toString();
												json = new JSONObject(inputLine);
												JSONArray sourceArr = json.getJSONArray("result");
												for(int i = 0; i < sourceArr.length(); i++)
												{
													JSONObject obj = sourceArr.getJSONObject(i); 
													if(obj.getString("name").equals(id)) return obj.toString(); 
												}
												return "source name not found";
												
				
				case ZEN_GET_TRANS_PROFS:		inputLine = response.toString();
												String[] transcodingProfilesNames =  id.split("@");
												StringBuilder logicExpression = new StringBuilder();
												for(int i = 0; i < transcodingProfilesNames.length; i++)
												{
													if(i == 0)
														logicExpression.append("?profile=='" + transcodingProfilesNames[i] + "'" );
													else 
														logicExpression.append(" || profile=='" + transcodingProfilesNames[i] + "'" );
												}
												jmespath = new JacksonRuntime();
												// Will looks like this: result[*] | [?profile == '1280x720' || profile == '1920x1080'].[profile, id]
												expression = jmespath.compile("result[" + logicExpression.toString() + "].[profile, id]");
												mapper = new ObjectMapper();
											    actualObj = mapper.readTree(inputLine);
												result = expression.search(actualObj);
												if(result!=null)
												{
													return result.toString();
												}
												return "source name not found";								
												
				
				case ZEN_GET_FEEDER_ID:			inputLine = response.toString();
												json = new JSONObject(inputLine);
												JSONArray feederIdJsonArray = json.getJSONArray("result");
												for(int i = 0; i < feederIdJsonArray.length(); i++)
												{
													JSONObject obj = feederIdJsonArray.getJSONObject(i); 
													if(obj.getString("name").equals(id)) return obj.getInt("id") + ""; 
												}
												return "key name not found";

												
				case ZEN_GET_ACCESS_TAG:		inputLine = response.toString();
												json = new JSONObject(inputLine);
												JSONArray jsonArray = json.getJSONArray("result");
												for(int i = 0; i < jsonArray.length(); i++)
												{
													JSONObject obj = jsonArray.getJSONObject(i); 
													if(obj.getString("name").equals(id)) return obj.getInt("id") + ""; 
												}
												return "access tag name not found";
												
				case ZEN_GET_CHANNEL_ID:		inputLine = response.toString();
												json = new JSONObject(inputLine);
												JSONArray channelArray = json.getJSONArray("result");
												for(int i = 0; i < channelArray.length(); i++)
												{
													JSONObject obj = channelArray.getJSONObject(i); 
													if(obj.getString("name").equals(id)) return obj.getInt("id") + ""; 
												}
												return null;
												
				
				case ZEN_FEEDER_DATA:			inputLine = response.toString();
												json = new JSONObject(inputLine);
												JSONArray feederDatajsonArray = json.getJSONArray("result");
												for(int i = 0; i < feederDatajsonArray.length(); i++)
												{
													JSONObject obj = feederDatajsonArray.getJSONObject(i); 
													if(obj.getString("name").equals(id)) return obj.toString(); 
												}
												return "feeder name not found";
				
				case ZEN_GET_BX_VERSION_ID: 	inputLine = response.toString();
												json = new JSONObject(inputLine);
												JSONArray versionArray = json.getJSONArray("result");
												for(int i = 0; i < versionArray.length(); i++)
												{
													JSONObject obj = versionArray.getJSONObject(i); 
													if(obj.getString("version").equals(id)) return obj.getInt("id") + ""; 
												}
												return "bx version not found";	
				
				case AWS_ACCOUNT_ID:			inputLine = response.toString();
												json = new JSONObject(inputLine);
												JSONArray awsArray = json.getJSONArray("result");
												for(int i = 0; i < awsArray.length(); i++)
												{
													JSONObject obj = awsArray.getJSONObject(i); 
													if(obj.getString("name").equals(id)) return obj.getInt("id") + ""; 
												}
												return "aws name not found";	
												
				
				case AWS_VPC:					inputLine = response.toString(); // Response from broadcaster
												json = new JSONObject(inputLine);
												jmespath = new JacksonRuntime();
												expression = jmespath.compile("result.{keys:keys, vpcs:vpcs}");
												mapper = new ObjectMapper();
											    actualObj = mapper.readTree(inputLine);
											    result = expression.search(actualObj);
											    // The returned format structure:
//											    {
//											    	"keys": ["x", "y", ...],
//											    	"vpcs": [{m}, {n}, ...]
//											    }
											    return result.toString(); 
				
				case ZEN_CLUSTER_ID: 			inputLine = response.toString();
												json = new JSONObject(inputLine);
												JSONArray clustersArray = json.getJSONArray("result");
												for(int i = 0; i < clustersArray.length(); i++)
												{
													JSONObject obj = clustersArray.getJSONObject(i); 
													if(obj.getString("name").equals(id)) return obj.getInt("id") + ""; 
												}
												return "cluster name not found";
												
				case ZEN_GET_BX_REV_PORT: 		inputLine = response.toString();
												json = new JSONObject(inputLine);
												JSONArray broadcastersArray = json.getJSONArray("result");
												for(int i = 0; i < broadcastersArray.length(); i++)
												{
													JSONObject obj = broadcastersArray.getJSONObject(i); 
													if(obj.getString("name").equals(id)) 
													{
														return obj.getJSONObject("tunnel").getInt("port") + "";
													}
												}
												return "broadcaster name not found";
												
												
				case ZEN_CLUSTER_STATUS:		inputLine = response.toString();
												
												JmesPath<JsonNode> jmespathClstr 		= new JacksonRuntime();
												Expression<JsonNode> expressionClstr 	= jmespathClstr.compile("@.result[].generalStatus");
												ObjectMapper mapperClstr = new ObjectMapper();
											    JsonNode nodeClstr = mapperClstr.readTree(inputLine);
											    nodeClstr = expressionClstr.search(nodeClstr);
											
												JSONArray jsonClstrStatusArray = new JSONArray(nodeClstr.toString());
												return 	  jsonClstrStatusArray.getString(0);
				
				case ZEN_CHANNEL_ID: 			inputLine = response.toString();
												JmesPath<JsonNode> jmespathCHNLID 	= new JacksonRuntime();
												Expression<JsonNode> expressionCHNLID 	= jmespathCHNLID.compile("result[?name=='"  + id + "'].id");
												ObjectMapper mapperCHNLID = new ObjectMapper();
											    JsonNode nodeCHNLID = mapperCHNLID.readTree(inputLine);
											    nodeCHNLID = expressionCHNLID.search(nodeCHNLID);
											    
											    if((nodeCHNLID == null) || nodeCHNLID.toString().equals(""))
											    	return "bad result";
											    String res = nodeCHNLID.toString();
											    String CHNLID = new JSONArray(res).getInt(0) + "";
												return CHNLID;
			
				case ZEN_PRFID_ID: 				inputLine = response.toString();
												JmesPath<JsonNode> jmespathPRFID 	= new JacksonRuntime();
												Expression<JsonNode> expressionPRFID 	= jmespathPRFID.compile("result[?profile=='"  + id + "'].id");
												ObjectMapper mapperPRFID = new ObjectMapper();
											    JsonNode nodePRFID = mapperPRFID.readTree(inputLine);
											    nodePRFID = expressionPRFID.search(nodePRFID);
											    
											    if((nodePRFID == null) || nodePRFID.toString().equals(""))
											    	return "bad result";
											    String PRFIDres = nodePRFID.toString();
											    String PRFID = new JSONArray(PRFIDres).getInt(0) + "";
												return PRFID;
												
					
				case ZEN_GET_BX_ID: 			inputLine = response.toString();
												json = new JSONObject(inputLine);
												JSONArray broadcastersArrayForId = json.getJSONArray("result");
												for(int i = 0; i < broadcastersArrayForId.length(); i++)
												{
													JSONObject obj = broadcastersArrayForId.getJSONObject(i); 
													if(obj.getString("name").equals(id)) 
													{
														return obj.getInt("id") + "";
													}
												}
												return "broadcaster name not found";
				
				case ZEN_GET_RX_ID: 			inputLine = response.toString();
												json = new JSONObject(inputLine);
												JSONArray receoverArrayForId = json.getJSONArray("result");
												for(int i = 0; i < receoverArrayForId.length(); i++)
												{
													JSONObject obj = receoverArrayForId.getJSONObject(i); 
													if(obj.getString("name").equals(id)) 
													{
														return obj.getInt("id") + "";
													}
												}
												return "broadcaster name not found";
				
				case ZEN_GET_BX_RUNDOM_ID: 		inputLine = response.toString();
												json = new JSONObject(inputLine);
												JSONArray broadcastersRundomArrayForId = json.getJSONArray("result");
												ArrayList<String> ids = new ArrayList<>();
												for(int i = 0; i < broadcastersRundomArrayForId.length(); i++)
												{
													JSONObject obj = broadcastersRundomArrayForId.getJSONObject(i); 
													
													ids.add( obj.getInt("id") + "");
												}
												
												int idSize = ids.size();
												if(idSize == 0)  return "broadcaster name not found";
												Random randomNum = new Random();
												int indexOfIdInArray = randomNum.nextInt(idSize);
												
												return ids.get(indexOfIdInArray);
				
												
				case ZEN_GET_RECEIVER_REV_PORT: inputLine = response.toString();
												json = new JSONObject(inputLine);
												JSONArray receiversArray = json.getJSONArray("result");
												for(int i = 0; i < receiversArray.length(); i++)
												{
													JSONObject obj = receiversArray.getJSONObject(i); 
													if(obj.getString("name").equals(id)) 
													{
														return obj.getJSONObject("tunnel").getInt("port") + "";
													}
												}
												return "receiver name not found";
			}
		} catch (Exception e) {
			String exceptionTest = e.getMessage();
			System.out.println("bug ------------->>> " + exceptionTest + " Request is "   + url + " \nLine Number is " +  debugLineNumber);
			System.out.println("Exception type is " + e.getClass());
		}
		finally {
			httpsURLConnection.disconnect(); 
		}
		return response.toString();
	}
}