package proxy;

import java.net.*;
import java.util.Map;
import java.io.*;

public class Test {

public static final int CR=13;
public static final int LF=10;
public static final int SP=32;

public static String data="71 69 84 32 104 116 116 112 58 47 47 108 111 99 97 108 104 111 115 116 47 32 72 84 84 80 47 49 46 49 13 10 72 111 115 116 58 32 108 111 99 97 108 104 111 115 116 13 10 85 115 101 114 45 65 103 101 110 116 58 32 77 111 122 105 108 108 97 47 53 46 48 32 40 88 49 49 59 32 85 59 32 76 105 110 117 120 32 120 56 54 95 54 52 59 32 101 110 45 85 83 59 32 114 118 58 49 46 57 46 50 41 32 71 101 99 107 111 47 50 48 49 48 48 49 50 52 32 70 105 114 101 102 111 120 47 51 46 54 13 10 65 99 99 101 112 116 58 32 116 101 120 116 47 104 116 109 108 44 97 112 112 108 105 99 97 116 105 111 110 47 120 104 116 109 108 43 120 109 108 44 97 112 112 108 105 99 97 116 105 111 110 47 120 109 108 59 113 61 48 46 57 44 42 47 42 59 113 61 48 46 56 13 10 65 99 99 101 112 116 45 76 97 110 103 117 97 103 101 58 32 101 110 45 117 115 44 101 110 59 113 61 48 46 53 13 10 65 99 99 101 112 116 45 69 110 99 111 100 105 110 103 58 32 103 122 105 112 44 100 101 102 108 97 116 101 13 10 65 99 99 101 112 116 45 67 104 97 114 115 101 116 58 32 73 83 79 45 56 56 53 57 45 49 44 117 116 102 45 56 59 113 61 48 46 55 44 42 59 113 61 48 46 55 13 10 75 101 101 112 45 65 108 105 118 101 58 32 49 49 53 13 10 80 114 111 120 121 45 67 111 110 110 101 99 116 105 111 110 58 32 107 101 101 112 45 97 108 105 118 101 13 10 13 10 ";

public static void main(String[] args) throws Exception
{
	Socket socket=new Socket("www.google.com",80);
	
	OutputStream os=(socket.getOutputStream());
	InputStream is=(socket.getInputStream());
	//os.write(("GET /manual HTTP/1.1").getBytes());
	//os.write(CR);
	//os.write(LF);
	//os.write(CR);
	//os.write(LF);
	//os.flush();
	String[] strings=data.split(" ");
	for(String i:strings)
	{
		os.write(Integer.parseInt(i));;
		System.out.print((char)Integer.parseInt(i));
	}
	
	socket.shutdownOutput();
	
	int temp;
	System.out.println("written data");
	
	while((temp=is.read())!=-1)
	{
		System.out.print((char)temp);
	}
}
}
