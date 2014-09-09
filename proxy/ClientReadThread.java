package proxy;

import java.net.*;
import httpparser.HttpRequest;
import java.io.*;

public class ClientReadThread implements Runnable{

private Socket socket;
private Socket serverSocket;
private URL serverURL;
private URLConnection serverConnection;

private String serverName;

private InputStream isClient;
private OutputStream osClient;

private InputStream isServer;
private OutputStream osServer;

private String firstLine;

public static final int CLIENT_SO_TIMEOUT=1000;
public static final int SERVER_SO_TIMEOUT=15000;

private static final boolean REMOVE_TRAILING_SLASH=false;

//private String[] immutableSiteStrings={".gif",".css"};
/*
 * This boolean is set to 'true' if during the reading of data from client, 
 * a 'new line' character is read else set to 'false'
 * 
 * This is necessary because if client data ends without a 'new line' the 
 * program may still try to read data from it, so this variable is tested to 
 * see if there is more data to be read from client.
 */
//private Boolean isCompleteLineRead=true;

private ClientWriteThread clientWriteThread;

public ClientReadThread(Socket socket)
{
	this.socket=socket;
	/*try {
		this.socket.setSoTimeout(ClientReadThread.CLIENT_SO_TIMEOUT);
	} catch (SocketException e1) {
		e1.printStackTrace();
	}*/
	
	try {
		isClient=socket.getInputStream();
		osClient=socket.getOutputStream();		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
/**
 * This is the original run method which works as of v 0.3
 */
public void arun()
{
	if(parseWebsite()){
		if(connectToServer()){
			startClientWriteThread();
			bridgeData();
			/////alternateBridgeData();/////////////////////////////////////////clumsy
		}
	}
	//System.out.println("*************end transaction***************");
}
/**
 * This is alternate run thread testing HttpRequest class.
 */
public void run()
{
	System.out.println("starting htreq");
	HttpRequest htReq=new HttpRequest(isClient);
	System.out.println("htreq stopped");
	
	if(alternateConnectToServer(htReq)){
		System.out.println("connectedtoserver");
		startClientWriteThread();
		System.out.println("started write thread");
		alternateBridge(htReq);
		System.out.println("stopped alternate bridge");
	}
}

private boolean alternateConnectToServer(HttpRequest htReq){
	try{
		serverURL=new URL(htReq.getServerName());
		
	InetAddress siteInetAddress=InetAddress.getByName(serverURL.getHost());
	System.out.println("inet address:"+siteInetAddress);
	
		int serverPort=serverURL.getPort();
		if(serverPort==-1)
			serverPort=80;
	
		serverSocket=new Socket(siteInetAddress,serverPort);
		//serverSocket.setSoTimeout(ClientReadThread.SERVER_SO_TIMEOUT);

		osServer=(serverSocket.getOutputStream());
		isServer=(serverSocket.getInputStream());

	}
	catch(UnknownHostException e){
		e.printStackTrace();
		try {
			socket.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return false;
	}
	catch (IOException e) {
		e.printStackTrace();
		return false;
	}
	//System.out.println("connected to site");
	return true;	

}

private void alternateBridge(HttpRequest in){
	int temp;
	while((temp=in.read())!=-1){
		try {
			osServer.write(temp);
			osServer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	try{		
	System.out.println("CLIENT OUT OF DATA");
	
	osServer.write('\n');
	osServer.write('\n');
	osServer.flush();
	
	}catch(IOException e){		
		e.printStackTrace();
	}
	
	/*finally{

			System.out.println("temp is "+temp);
		
		try {
			serverSocket.shutdownOutput();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}*/
	
	/*try {
		osServer.write('\n');
		osServer.write('\n');
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
}

private boolean parseWebsite()
{
	readFirstLine();
	
	String[] tokens=firstLine.split(" ");
	
	try {
		serverName=tokens[1];
		addDefaultProtocol();
		//setPort();
		
		if(ClientReadThread.REMOVE_TRAILING_SLASH)
			serverName=removeSlash(serverName);
		
		//System.out.println("site name::::::::::"+serverName);
		
		serverURL=new URL(serverName);
		System.out.println("websiteURL *"+serverURL+"*");
		
	} catch (MalformedURLException e) {
		e.printStackTrace();
		return false;
	}
	return true;
}

private void addDefaultProtocol()
{
	if(!serverName.contains("//"))
		serverName="http://"+serverName;
}

/*private void setPort()
{
	String[] temp=serverName.split(":");
	
	assert(temp.length==2);
	/**
	 * Website structure is http://sitename.com  or sitename.com:portno
	 * 
	 * If this is true, we assume that it is http, ftp, https, etc....
	 
	try{
	serverPort=Integer.parseInt(temp[1]);
	serverName=temp[0];
	}
	catch(NumberFormatException e)
	{
		serverPort=80;
		serverName=temp[0]+":"+temp[1];
	}		
}*/

private String removeSlash(String sitename)
{
	System.out.println("site name   "+sitename);
	if(sitename.endsWith("/"))
		sitename=sitename.substring(0, sitename.length()-1);
	System.out.println("new site name "+sitename);
	
	return sitename;
}

private boolean connectToServer()
{
	try {
		//siteConnection=websiteURL.openConnection();
		//siteConnection.setDoOutput(true);
		InetAddress siteInetAddress=InetAddress.getByName(serverURL.getHost());
		//System.out.println("hostname "+serverURL.getHost());
		
		int serverPort=serverURL.getPort();
		if(serverPort==-1)
			serverPort=80;
		
		serverSocket=new Socket(siteInetAddress,serverPort);
		//serverSocket.setSoTimeout(ClientReadThread.SERVER_SO_TIMEOUT);

		osServer=(serverSocket.getOutputStream());
		isServer=(serverSocket.getInputStream());

	} catch (IOException e) {
		e.printStackTrace();
		return false;
	}
	//System.out.println("connected to site");
	return true;	
}

private void startClientWriteThread()
{
	clientWriteThread=new ClientWriteThread(osClient, isServer, socket, serverSocket);
	Thread th=new Thread(clientWriteThread);
	th.setDaemon(false);
	th.start();
}

private void bridgeData()
{
	//System.out.println("bridging started");
	
	try {		
		osServer.write(firstLine.getBytes());
		
			osServer.write('\n');
			
			int temp;
			
			while((temp=isClient.read())!=-1)
			{
				osServer.write(temp);
			}
			
	} 
	catch(SocketTimeoutException ex){
		try {
			System.out.println("	timed out, closing sockets");
			socket.close();
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}catch (IOException e) {
		//System.out.println("client socket closed");
	}
	finally{
	//System.out.println("bridging ended");
	}
}

private void alternateBridgeData()
{
	StringBuffer sb=new StringBuffer();
	
	try {		
		osServer.write(firstLine.getBytes());
		
			osServer.write('\n');
			
			int temp;			
			
			while((temp=isClient.read())!=-1)
			{
				sb.append((char)temp);
			}
	}catch(SocketTimeoutException e){
		try {
			
			socket.shutdownInput();
			osServer.write(sb.toString().getBytes());
			serverSocket.shutdownOutput();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	catch(IOException e)
	{
		e.printStackTrace();
	}
}

private void readFirstLine()
{
	StringBuffer buffer=new StringBuffer();
	int temp;
	
	try {
		while((temp=isClient.read())!='\n')// && temp!='\n')
		{
			buffer.append((char)temp);
		}
		
		/*if(temp==-1){
			this.isCompleteLineRead=false;
			System.out.println("not completely read");
		}*/
		
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	firstLine=buffer.toString();
	//System.out.println("first line:::"+firstLine);
}

/*private boolean immutableSite() /////////// not elegant, to be removed
{
	//if(websiteURL.getFile().endsWith(".gif"))
	//	return true;
	System.out.println("website name   "+websiteURL.getFile());
	if(websiteURL.getFile().contains("."))
	{
	String temp[]=websiteURL.getFile().split(".");
	for(String i:temp)
		System.out.println(i);
	System.out.println("length    "+temp.length);
	String extention=temp[temp.length-1];
	System.out.println("extention is "+extention);
		if(extention.length()==3)
			return true;
	}
	return false;
}*/
}
