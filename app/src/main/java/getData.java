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
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class getData {

	public static void main(String args[]) throws Exception {

		getRecipes("apple+Cocktail+orange");

	}

	public static void getRecipes(String params) throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();

		String url = "http://api.yummly.com/v1/api/recipes?_app_id=786acad9&_app_key=e0ed83d8d6e7026ae4cdfc436af2f838&q="
				+ params;

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		int responseCode = con.getResponseCode();

		System.out.println(responseCode);

		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuilder response = new StringBuilder();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}

		System.out.println(response);
		

		JSONObject json = new JSONObject(response.toString());

		System.out.println(json);

		JSONArray jarray = json.getJSONArray("matches");
		System.out.println("*******************jsonarray************");
		System.out.println(jarray);
		String[] subset = new String[10];
		for (int i = 0; i < 10; i++) {
			subset[i] = jarray.get(i).toString();
			System.out.println(subset[i]);
		}
		in.close();


	}
}
