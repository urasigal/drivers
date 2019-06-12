package com.zixi.zapi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.sym.Name;

import javax.ws.rs.core.MediaType;
public class CallApi {
	
	//payload - POST request payload - JSON as a string representation.
	//requestURL - request URL string like for example "/public/rest/api/1.0/cycle?expand=&clonedCycleId="
	public static String restZapiPOST(String zypherUserName, String zapiAccesskey,
	String zapiSecretkey, String requestURL, String payLoadString) throws URISyntaxException {
		Client client = ClientBuilder.newClient();
		Entity<String> payload = Entity.json(payLoadString);  
		
		Response response = client.target(  ZapiConnection.zephyrBaseUrl +  requestURL)
		  .request(MediaType.APPLICATION_JSON_TYPE)
		  .header("Authorization", ZapiConnection.getJwtPost(zypherUserName, zapiAccesskey, zapiSecretkey, requestURL)) 
		  .header("zapiAccessKey", zapiAccesskey)
		  .post(payload);

		System.out.println("status: " + response.getStatus());
		System.out.println("headers: " + response.getHeaders());
		String body = response.readEntity(String.class);
		System.out.println("body:" + body);
		return body;
	}
	
	
	//payload - POST request payload - text as a string representation.
	public static String restZapiTextPOST(String zypherUserName, String zapiAccesskey,
	String zapiSecretkey, String requestURL, String payLoadString) throws URISyntaxException, ParseException, IOException {
		
		
		File test_sescription_file = File.createTempFile("test_detais", ".txt");
		FileWriter fileWriter = new FileWriter(test_sescription_file);
		fileWriter.write(payLoadString);
		fileWriter.flush();
		fileWriter.close();
		
		MultipartEntity entity = new MultipartEntity();
		entity.addPart("attach", new FileBody(test_sescription_file) );
		//Entity<String> payload = Entity.text(ent.toString());  
		
		HttpResponse response = null;
		HttpClient restClient = new DefaultHttpClient();
		URI uri = new URI(ZapiConnection.zephyrBaseUrl +  requestURL);
		HttpPost addAttachmentReq = new HttpPost(uri);
		addAttachmentReq.addHeader("Authorization", ZapiConnection.getJwtPost(zypherUserName, zapiAccesskey, zapiSecretkey, requestURL));
		addAttachmentReq.addHeader("zapiAccessKey", zapiAccesskey);
		addAttachmentReq.setEntity(entity);

		try {
			response = restClient.execute(addAttachmentReq);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		HttpEntity entity1 = response.getEntity();
		int statusCode = response.getStatusLine().getStatusCode();
		// System.out.println(statusCode);
		// System.out.println(response.toString());
		if (statusCode >= 200 && statusCode < 300) {
			System.out.println("Attachment added Successfully");
		} else {
			try {
				String string = null;
				string = EntityUtils.toString(entity1);
				System.out.println(string);
				throw new ClientProtocolException("Unexpected response status: " + statusCode);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				return "Unexpected response status: " + statusCode;
			}
		}

		
//		Response response = client.target(  ZapiConnection.zephyrBaseUrl +  requestURL)
//		  .request(MediaType.TEXT_PLAIN_TYPE)
//		  .header("Authorization", ZapiConnection.getJwtPost(zypherUserName, zapiAccesskey, zapiSecretkey, requestURL)) 
//		  .header("zapiAccessKey", zapiAccesskey)
//		  .post(payload);
//
//		System.out.println("status: " + response.getStatus());
//		System.out.println("headers: " + response.getHeaders());
//		String body = response.readEntity(String.class);
//		System.out.println("body:" + body);
//		return body;
		return "Attachment added Successfully";
	}
	
	
	public static String restZapiGET(String zypherUserName, String zapiAccesskey,
	String zapiSecretkey, String requestURL) throws URISyntaxException {
		Client client = ClientBuilder.newClient();
		
		Response response = client.target( ZapiConnection.zephyrBaseUrl + requestURL)
		  .request(MediaType.APPLICATION_JSON_TYPE)
		  .header("Authorization", ZapiConnection.getJwtGet(zypherUserName, zapiAccesskey, zapiSecretkey, requestURL)) 
		  .header("zapiAccessKey", zapiAccesskey)
		  .get();

		System.out.println("status: " + response.getStatus());
		System.out.println("headers: " + response.getHeaders());
		String body = response.readEntity(String.class);
		System.out.println("body:" + body);
		return body;
	}
	
	
	//payload - PUT request payload - JSON as a string representation.
		//requestURL - request URL string like for example "/public/rest/api/1.0/cycle?expand=&clonedCycleId="
		public static String restZapiPUT(String zypherUserName, String zapiAccesskey,
		String zapiSecretkey, String requestURL, String payLoadString) throws URISyntaxException {
			Client client = ClientBuilder.newClient();
			Entity<String> payload = Entity.json(payLoadString);  
			
			Response response = client.target(  ZapiConnection.zephyrBaseUrl +  requestURL)
			  .request(MediaType.APPLICATION_JSON_TYPE)
			  .header("Authorization", ZapiConnection.getJwtPut(zypherUserName, zapiAccesskey, zapiSecretkey, requestURL)) 
			  .header("zapiAccessKey", zapiAccesskey)
			  .put(payload);

			System.out.println("status: " + response.getStatus());
			System.out.println("headers: " + response.getHeaders());
			String body = response.readEntity(String.class);
			System.out.println("body:" + body);
			return body;
		}
	
	//requestURL - request URL string like for example "/public/rest/api/1.0/cycle?expand=&clonedCycleId="
	public static void restZapiDELETE(String zypherUserName, String zapiAccesskey,
	String zapiSecretkey, String requestURL) throws URISyntaxException {
		Client client = ClientBuilder.newClient();
		
		Response response = client.target(  ZapiConnection.zephyrBaseUrl +  requestURL)
		  .request(MediaType.APPLICATION_JSON_TYPE)
		  .header("Authorization", ZapiConnection.getJwtDelete(zypherUserName, zapiAccesskey, zapiSecretkey, requestURL)) 
		  .header("zapiAccessKey", zapiAccesskey)
		  .delete();

		System.out.println("status: " + response.getStatus());
		System.out.println("headers: " + response.getHeaders());
		System.out.println("body:" + response.readEntity(String.class));
		}
}
