package com.zixi.tcpserver;

public class JettyZixiRunnerThread extends Thread {
	
public static void main(String[] args) throws Exception {
	JettyZixiRunnerThread jettyZixiRunnerThread = new JettyZixiRunnerThread();
	jettyZixiRunnerThread.startServers("src/main/resources");
	}
	
	private JettyZixiServ jettyZixiServ = null;
	private String fileBase;
	
	public void setJettyZixiServ(JettyZixiServ jettyZixiServ) {
		this.jettyZixiServ = jettyZixiServ;
	}
	 
	public void run() {
		try {
			jettyZixiServ.serveServers(fileBase);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Relative path to write/read files.
	public void startServers( String fileBase  ) throws Exception
	{
		this.fileBase = fileBase;
		setJettyZixiServ(new JettyZixiServ());
		start();	
	}
	
	public void stopServers() throws Exception
	{
		jettyZixiServ.server.stop();
		jettyZixiServ.fileServer.stop();
		jettyZixiServ.deleteFolder();
	}
}
