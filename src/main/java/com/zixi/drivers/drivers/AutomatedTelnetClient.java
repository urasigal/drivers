package com.zixi.drivers.drivers;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class AutomatedTelnetClient {
    
	private TelnetClient 	telnet		 = new TelnetClient();
    private InputStream  	in;
    private PrintStream  	out;
    private String        	prompt 		 = "#";
    private BufferedReader 	bufferedReader;

    public AutomatedTelnetClient(String server, String user, String password) {
    	try {
    		// Connect to the specified server
    		telnet.connect(server, 23);

    		// Get input and output stream references
    		in 				 = telnet.getInputStream();
    		bufferedReader	 = new BufferedReader(new InputStreamReader(in));
    		out 			 = new PrintStream(telnet.getOutputStream());

    		// Log the user on
    		readUntil("Username: ");
    		write(user);
    		readUntil("Password: ");
    		write(password);
    		readUntil(prompt);
    		// Advance to a prompt
    		//readUntil(prompt + " ");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    public void su(String password) {
    	try {
    		write("su");
    		readUntil("Password: ");
    		write(password);
    		prompt = "#";
    		readUntil(prompt + " ");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    public String readUntil(String pattern) {
    	StringBuffer sb =  new StringBuffer();
    	try {
    		char lastChar = pattern.charAt(pattern.length() - 1);
    		StringBuffer response = new StringBuffer();
    		boolean found = false;
    		char ch = (char) in.read();
    		String inputLine = "";
    		
//    		//////////////////////////////////////////////////////////////////////
//    		while ((inputLine = bufferedReader.readLine()) != null) {
//				response.append(inputLine);
//				sb.append(inputLine);
//				System.out.println(inputLine);
//			}
    		////////////////////////////////////////////////////////////////////
    		
    		
    		while (true) {
    			System.out.print(ch);
    			sb.append(ch);
    			if (ch == lastChar) 
    			{
    				if (sb.toString().endsWith(pattern)) {
    					return sb.toString();
    				}
    			}
    			ch = (char) in.read();
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return sb.toString();
    }

    public void write(String value) {
    	try {
    		out.println(value);
    		out.flush();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    public String sendCommand(String command) {
    	try {
    		System.out.println("wont send " + command);
    		write(command);
    		return readUntil(prompt);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }

    public void disconnect() {
    	try {
    		telnet.disconnect();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    public static void mainTest(String[] args) {
    	try {
    		AutomatedTelnetClient telnet = new AutomatedTelnetClient("10.7.0.140", "cisco", "cisco");
    		System.out.println("Got Connection...");
    		//telnet.su("cisco");
    		//telnet.sendCommand("cisco ");
    		telnet.sendCommand("configure terminal\n");
    		telnet.sendCommand("interface gigabitEthernet 0/0\n");
    		telnet.sendCommand("interface gigabitEthernet 0/0\n");
    		//telnet.sendCommand("end");
    		//telnet.sendCommand("end");
    		telnet.disconnect();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
}