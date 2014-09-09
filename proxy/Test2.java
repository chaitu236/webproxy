package proxy;

import java.io.*;
import java.net.*;
import httpparser.HttpRequest;

public class Test2 {
public static void main(String[] args) throws Exception
{
	ServerSocket serverSocket=new ServerSocket(23600);
	Socket socket=serverSocket.accept();
	
	InputStream is=socket.getInputStream();
	HttpRequest htReq=new HttpRequest(is);
	
	System.out.println("method:  "+htReq.getMethod());
	System.out.println("serverName   "+htReq.getServerName());
	System.out.println("Map entries \n"+htReq.getHeaders());
	
	int temp;
	while((temp=htReq.read())!=-1)
	{
		System.out.print((char)temp);
	}
	//while((temp=is.read())!=-1)
	//	System.out.print((char)temp);
}
}
