package httpparser;

import java.net.*;
import java.io.*;
import java.util.*;
import mymap.MyMap;
/**
 * 
 * @author chaitanya
 * 
 * This class takes in an <code>InputStream</code> (assuming that it is
 * a http request which has not been read already) and reads all data till
 * a <code>SocketTimeoutException</code> occurs (whose value has to be
 * set appropriately) and parses all the headers.
 *
 */
public class HttpRequest {

private InputStream inputStream;

public static final String[] allHeaders={
	"Accept",
	"Accept-Charset",
	"Accept-Encoding",
	"Accept-Language",
	"Authorization",
	"From",
	"Host",
	"If-Modified-Since",
	"If-Match",
	"If-None-Match",
	"If-Range",
	"If-Unmodified-Since",
	"Max-Forwards",
	"Proxy-Authorization",
	"Range",
	"Referer",
	"Proxy-Connection",
	"Cookie",
	"Keep-Alive",
	"User-Agent"
};

public static final String[] allMethods={
	"GET",
	"HEAD",
	"POST",
	"PUT",
	"DELETE",
	"OPTIONS",
	"TRACE",
	"CONNECT"
};

private String method;
private String hostName;
private String lineBuffer;
private MyMap<String,String> headers;
private int readCount=-1;
private String resourcePath;

public HttpRequest(InputStream inputStream)
{
	this.inputStream=inputStream;
	headers=new MyMap<String, String>(allHeaders);
	lineBuffer="";
	
	parseStream();
	setInterestingFields();
}

public void setInterestingFields()
{
	try {
		hostName=headers.get("Host");
	} catch (NoSuchFieldException e) {
		hostName=null;
	}
}

public String getHost(){
	return hostName;
}

public String getHeader(String key){
	try {
		return headers.get(key);
	} catch (NoSuchFieldException e) {
		return null;
	}
}

public String getMethod(){
	return method;
}

public void parseStream()
{
	//inputStream.mark(2000);
	
	String line=null;
	boolean requestLineFound=false;
	
	while((line=readLine())!=null)
	{
		this.lineBuffer=this.lineBuffer+"\n"+line;
		
		boolean isData=true;
		
		if(!requestLineFound){
			for(String k:allMethods){
				if(line.startsWith(k)){
					method=k;
					System.out.println("FL:  "+line);
					resourcePath=line.split(" ")[1];
					/**
					 * to be removed
					 */
					//if(!resourcePath.startsWith("http"))
					//	resourcePath="http:"+resourcePath;
					
					
					isData=false;
					break;
				}
			}
		}
		/**
		 * This test is true if the line read is a Request-Line and if it is so,
		 * there is no need to test for header-Lines.
		 */
		if(!isData)
			continue;
		
		for(String k:allHeaders){
			if(line.startsWith(k+": ")){
				headers.put(k, line.substring(k.length()+2));
				isData=false;
				break;
			}
		}
		/**
		 * If the following test passes, it means that the line is neither a Request-Line
		 * or a Header-Line, at which point, the remaining data is not to be tested for
		 * further Header-Lines.
		 */
		if(isData)
			break;
	}
	
	/*try {
		inputStream.reset();
	} catch (IOException e) {
		e.printStackTrace();
	}*/
}

public int read(){
	int temp=-1;
	
	if(this.lineBuffer.length()<=this.readCount+1){
		try{
			temp=inputStream.read();
		}catch(IOException e){
			e.printStackTrace();
		}
		return temp;
	}
	
	return this.lineBuffer.charAt(++readCount);
}

public String getServerName(){
	if(resourcePath.startsWith("www"))
		resourcePath="http://"+resourcePath;
	
	return resourcePath;
}
private String readLine() //throws IOException
{
	int temp;
	StringBuffer sb=new StringBuffer();
	
	try{
		
	while((temp=inputStream.read())!=-1 && temp!='\n')
	{
		sb.append((char)temp);
	}
	
	if(temp==-1)
		System.out.println("the client stopped sending");
	
	}catch(SocketTimeoutException e){
		System.out.println("socket timedout");
		e.printStackTrace();
		return null;
	}
	catch(IOException e){
		e.printStackTrace();
	}
	//System.out.println("line read::"+sb);
	return sb.toString();
}

public MyMap getHeaders(){
	return headers;
}
}
