package proxy;

import java.io.IOException;
import java.net.*;

public class Main {

private int count=0;

public static void main(String[] args) throws IOException
{
	new Main().start();
}

public void start() throws IOException
{
	Thread tt=new Thread(new ThreadTracker());
	tt.setDaemon(false);
	tt.start();
	
	ServerSocket serverSocket=null;	
	serverSocket=new ServerSocket(23600);
	Socket socket;
	Thread th;
	
	while(true){
		socket=serverSocket.accept();
		count++;
		th=new Thread(new ClientReadThread(socket));
		th.start();
	}
}
}
