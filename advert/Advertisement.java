package advert;

import java.net.*;
import java.util.Enumeration;

public class Advertisement implements Runnable{

private InetAddress multicastAddress;
private int multicastPort;
private String multicastMessage;
private int timeperiod;

private DatagramSocket socket;
private DatagramPacket packet;

private boolean stop=false;

public static final String DEFAULT_ADDRESS="230.0.0.1";
public static final int DEFAULT_PORT=23600;
public static final int TIME_PERIOD=8000;

public Advertisement(String addr, int port, String msg, int timeperiod){
	try {
		this.multicastAddress=InetAddress.getByName(addr);
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return;
	}
	
	this.multicastPort=port;
	this.multicastMessage=msg;
	this.timeperiod=timeperiod;
	
	startBroadcast();
}

public Advertisement()
{
	this(DEFAULT_ADDRESS, DEFAULT_PORT, getDefaultMessage(), TIME_PERIOD);
}

private void startBroadcast()
{
	Thread th=new Thread(this);
	th.setDaemon(false);
	th.start();
}

@Override
public void run() {
	try {
		socket=new DatagramSocket();
	} catch (SocketException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	byte[] buf=multicastMessage.getBytes();
	
	packet=new DatagramPacket(buf, buf.length, multicastAddress, multicastPort);

	try{
	
		do{
			socket.send(packet);
			Thread.sleep(timeperiod);
		
		}while(!stop);
		
	}catch(Exception e){
		e.printStackTrace();
	}
}

public static String getDefaultMessage() 
{
	StringBuffer temp=new StringBuffer("Server on \n");
	Enumeration<NetworkInterface> netinfEn=null;
	
		try {
			netinfEn=NetworkInterface.getNetworkInterfaces();		
			
	while(netinfEn.hasMoreElements())
	{
		NetworkInterface ni=netinfEn.nextElement();
		
			if(ni.isLoopback())
				continue;
			
		Enumeration<InetAddress> inetEn=ni.getInetAddresses();
		
		while(inetEn.hasMoreElements())
		{
			InetAddress inetAddr=inetEn.nextElement();
			
			if(inetAddr instanceof Inet6Address)
				continue;
			temp.append(inetAddr+"\n");
		}
		
	}
	
	} catch (SocketException e) {
		e.printStackTrace();
	}
	
	return new String(temp);
}
public void stop()
{
	this.stop=true;
}
}
