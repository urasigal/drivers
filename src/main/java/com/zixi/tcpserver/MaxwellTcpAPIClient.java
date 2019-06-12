package com.zixi.tcpserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MaxwellTcpAPIClient {
	public static void maxwellconnect(String hostName, int portNumber, String command){
		try(
			    Socket clientSoscet = new Socket(hostName, portNumber);
			    PrintWriter out = new PrintWriter(clientSoscet.getOutputStream(), true);
			    BufferedReader in = new BufferedReader(new InputStreamReader(clientSoscet.getInputStream()));
			){
			out.println(command);
			String serverResponse = in.readLine();
			System.out.println("Service response " + serverResponse); 
		}catch(Exception e){
				// catch body
			}
	}
}
