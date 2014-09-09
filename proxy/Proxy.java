package proxy;

import java.io.IOException;
import java.net.*;
import java.io.*;

public class Proxy {
	public static void main(String[] args) throws IOException
	{
		
		ServerSocket serverSocket=new ServerSocket(23600);
		while(true)
		{
		Socket socket=serverSocket.accept();
		BufferedInputStream bis=new BufferedInputStream(socket.getInputStream());
			int temp;
			bis.mark(100);
			
			while((temp=bis.read())!=-1)
			{
				System.out.print((char)temp);
			}
		bis.close();
		System.out.println("--------------------------------------");
		}
	}
}
