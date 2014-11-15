
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



import org.apache.*;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class getData {
	
	public static void main(String args[]) throws Exception{
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		
		String params ="potato";
		String url = "http://api.yummly.com/v1/api/recipes?_app_id=786acad9&_app_key=e0ed83d8d6e7026ae4cdfc436af2f838&q="+params;
		
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		// optional default is GET
		con.setRequestMethod("GET");
		
		//con.setRequestProperty("User-Agent", USER_AGENT);
		 
		int responseCode = con.getResponseCode();
		
	System.out.println(	responseCode);
	
	System.out.println("\nSending 'GET' request to URL : " + url);
	System.out.println("Response Code : " + responseCode);

	BufferedReader in = new BufferedReader(
	        new InputStreamReader(con.getInputStream()));
	String inputLine;
	StringBuffer response = new StringBuffer();

	while ((inputLine = in.readLine()) != null) {
		response.append(inputLine);
	}
	in.close();
	
	System.out.println(response.length());
	
	System.out.println(response.toString());
 
		
		

		
	}
}
	
	 




