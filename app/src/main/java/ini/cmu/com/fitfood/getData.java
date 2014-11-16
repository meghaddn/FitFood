package ini.cmu.com.fitfood;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class getData {

    public static List<JSONObject> result = new ArrayList<JSONObject>();//List of recipes


    public static void main(String args[]) throws Exception {

        getRecipes("fish");

    }

    public static List<JSONObject> getRecipes(String params) throws Exception {
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
        //System.out.println(jarray);
        for (int i = 0; i < 10; i++) {
            JSONObject subset = new JSONObject();
            JSONObject recipe = new JSONObject();

            subset = jarray.getJSONObject(i);

            recipe.put("name", subset.get("recipeName"));


            ArrayList<String> list = new ArrayList<String>();
            JSONArray jsonArray = (JSONArray)subset.get("smallImageUrls");
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int m=0;m<len;m++){
                    list.add(jsonArray.get(m).toString());
                }
            }
            recipe.put("imageUrl", list.get(0));


            String id = (String) subset.get("id");
            recipe.put("recipeUrl", "http://www.yummly.com/recipe/external/"+id);

            //int time = (Integer) subset.get("totalTimeInSeconds");
            if(subset.get("totalTimeInSeconds").equals(null)){
                recipe.put("timeInMinutes", "NA");
            }
            else{
                int time = (Integer) subset.get("totalTimeInSeconds");

                System.out.println(time);

                //if(time!=null){
                recipe.put("timeInMinutes", time/60);

                //}
            }

            //recipe.put("timeInMinutes", time/60);
            recipe.put("rating", subset.get("rating"));



            //System.out.println(recipe.toString());

            result.add(recipe);

        }
        in.close();

        return result;
    }
}