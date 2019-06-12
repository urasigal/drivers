package com.zixi.tcpserver;



import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.SequenceInputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

import com.zixi.nio.WriteerToFile;
import com.zixi.threads.JettyHandlerZixiThread;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class JettyZixiServ { 
    public final  Object writeLock =  new Object();
	public static Server server;     // This server serves like a file server - writes a files to. Not returns resources.
	public static Server fileServer; // This is a resource server - returns resources from specific resource path.
	public static String prefixPath = "src/main/resources/";
	public static String deleteFolder;
	
	public  void serveServers(String fileBase) throws Exception
    {
		prefixPath = fileBase + "/";
        server = new Server(); 
        ServerConnector httpConnector = new ServerConnector(server);
        //http.setHost("localhost");
        httpConnector.setPort(5432);
        httpConnector.setIdleTimeout(30000);
   
        HttpConfiguration https = new HttpConfiguration();
       
        https.addCustomizer(new SecureRequestCustomizer());
        
        SslContextFactory sslContextFactory = new SslContextFactory();
       
        sslContextFactory.setKeyStorePath(fileBase+  "/keystore.jks");
       
        sslContextFactory.setKeyStorePassword("123456");
        sslContextFactory.setKeyManagerPassword("123456");
        ServerConnector sslConnector = new ServerConnector(server,  new SslConnectionFactory(sslContextFactory, "http/1.1"), new HttpConnectionFactory(https));
        sslConnector.setPort(9998);
        
        
        // Set the connector
        server.addConnector(httpConnector);
        
        server.setConnectors(new Connector[] { httpConnector, sslConnector });
        
        server.setHandler(new Handler(writeLock));
        
        fileServer = new Server(4321);
        new Thread(new RunnableJettyFileServer(fileServer , fileBase, writeLock)).start();
        
        server.start();
        server.join();
    }
    
    public static class Handler extends AbstractHandler
    {
        private Object writeLock;
		
        public Handler(Object writeLock) {
			this.writeLock =writeLock;
		}

		// Handle input request to write on disk.
		public void handle( String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response ) throws IOException,  ServletException
        {
			// Response output stream.
	        PrintWriter out 						= response.getWriter();
	        boolean appendFlag 				= false;
           JettyHandlerZixiThread thread 	= new JettyHandlerZixiThread(writeLock);
           response.setContentType("text/html; charset=utf-8");
           response.addHeader("Access-Control-Allow-Origin", "*");
           response.setStatus(HttpServletResponse.SC_OK);
       
           System.out.print("Request is " + baseRequest.getRequestURL() + " time ms " + System.currentTimeMillis() +  "\n");
           
           String fileToWriteName 	= baseRequest.getRequestURI().split("/")[baseRequest.getRequestURI().split("/").length-1];    
           String pathWithNoFile 		= baseRequest.getRequestURI().substring(0,  baseRequest.getRequestURI().length() - fileToWriteName.length());
          // System.out.println("Directory path  is " + pathWithNoFile + " file path is " + fileToWriteName);
           
           // Create a first directory - corresponds to adaptive group name.
           String rootPath 					= baseRequest.getRequestURI().toString().split("/")[1];
           deleteFolder 						= prefixPath + rootPath;
           String localServerPath		= prefixPath +pathWithNoFile;
    	  
           try{ Files.createDirectories(Paths.get(localServerPath)); }catch(FileAlreadyExistsException e){}
    	   thread.path = localServerPath + "/" + fileToWriteName;
           
    	   if(fileToWriteName.split("\\.")[1].equals("ts")  || fileToWriteName.split("\\.")[1].equals("m4v") )
           {
    		   appendFlag = true;
           }
           thread.appendFlag = appendFlag;
           try {
	           ServletInputStream stream 	= request.getInputStream();
	           InputStream dest 					= IOUtils.toBufferedInputStream(stream);
	           thread.stream 						= dest;
           }catch(Exception e)
           {
        	   System.out.println("Exception Error is " + e.getMessage());
        	   out.println("<h1> done </h1>");
        	   out.close();
        	   return ;
           }
           out.println("<h1> done </h1>");
           out.close();
           thread.start();
           baseRequest.setHandled(true);
        }
    }
    
    public static void deleteFolder() throws IOException
    {
    	FileUtils.deleteDirectory(new File(deleteFolder));
    }
}