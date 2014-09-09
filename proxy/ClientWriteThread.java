package proxy;

import java.io.*;
import java.net.*;

public class ClientWriteThread implements Runnable{

private OutputStream osClient;
private InputStream isSite;
private Socket clientSocket;
private Socket siteSocket;

public ClientWriteThread(OutputStream bosClient, InputStream bisSite, Socket clientSocket, Socket siteSocket)
{
	this.osClient=bosClient;
	this.isSite=bisSite;
	this.clientSocket=clientSocket;
	this.siteSocket=siteSocket;
}

public void run()
{
	int temp=-2;
	/*try {
		Thread.sleep(500);
	} catch (InterruptedException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	}*/
	try {
		//int count=1;
		while((temp=isSite.read())!=-1)
		{
			osClient.write(temp);
			//if(count%1000==0)
				osClient.flush();			
			//count++;
		}
		//osClient.flush();
		
	} catch (IOException e) {
		e.printStackTrace();
	}
	finally {
		try {
				System.out.println("CWT: temp value "+temp);
				System.out.println("Testing read() is :"+isSite.read());
				clientSocket.close();
				siteSocket.close();
				
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
}
