package ini.cmu.com.fitfood;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EstimateCalorie extends Activity {
    private SearchView mSearchView;
    private ListView mListView;
    private HttpClient client;
    private HttpResponse response;
    private ArrayAdapter<String> listAdapter;
    public static HashMap<String,String> KitchenItems ;
    //public static HashMap<String,List<String>> userSelected ;
    private Button DoneButton;
    private Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate_calorie);
        DoneButton = (Button) findViewById(R.id.button);
        btn2 = (Button) findViewById(R.id.button2);

       // mSearchView = (SearchView) findViewById(R.id.searchView);
        KitchenItems= new HashMap<String, String>();

        client = new DefaultHttpClient();
    //    setupSearchView();


    for (int i=0; i < Kitchen.ingdt.size();i++) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.nutritionix.com")
                .appendPath("v1_1")
                .appendPath("search")
                .appendPath(Kitchen.ingdt.get(i))
                .appendQueryParameter("results", "0:6")
                .appendQueryParameter("fields", "item_name,nf_calories")
                .appendQueryParameter("appId", "1204291e")
                .appendQueryParameter("appKey", "6b1c9af9fcd19d88c850dec9411c71a0");
        String URL = builder.build().toString();
        KitchenItems.put(Kitchen.ingdt.get(i),"1");
        try {
            QueryResults qr = new QueryResults();
            qr.execute(new String[]{URL});

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

         DoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double TotalCal = 3000.00;
                double UserCal =0;
                for (Map.Entry<String,List<String>> entry: SearchFood.userSelected.entrySet()){
                    List<String> val = entry.getValue();
                    UserCal = Double.parseDouble(val.get(1)) + UserCal;
                }
                double diff = TotalCal - UserCal;
                double TotalIng=0;
                String IngName = "";
                Map<String,String> sortedMap = sortByComparator(KitchenItems);
                for (Map.Entry<String,String> entry: sortedMap.entrySet()) {
                    String val = entry.getValue();
                    TotalIng = Integer.parseInt(val) + TotalIng;
                    if (diff > TotalIng ) {


                        IngName = IngName + "+" + entry.getKey();
                    }else{
                        TotalIng =  TotalIng - Integer.parseInt(val) ;
                    }
                }
                IngName = IngName.substring(1);
                System.out.println("IngName" + IngName);
                Intent intent = new Intent(getApplicationContext(), ViewReciepeList.class);
                intent.putExtra("Ingredients", IngName);

                startActivity(intent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double UserCal =0;
                for (Map.Entry<String,List<String>> entry: SearchFood.userSelected.entrySet()){
                    List<String> val = entry.getValue();
                    UserCal = Double.parseDouble(val.get(0)) + UserCal;
                }
                System.out.println("cal "+ UserCal);
                Intent intent = new Intent(getApplicationContext(), ProgressCheck.class);
                intent.putExtra("UserCal",UserCal);

                startActivity(intent);
            }
        });


        /*
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet();
        HttpGet request = new HttpGet(URL);
        HttpResponse response;
        try {
            response = client.execute(request);
            Log.d("Response of GET request", response.toString());
        } catch (ClientProtocolException e) {
            TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            TODO Auto-generated catch block
            e.printStackTrace();
        }*/

    }

    private static Map<String, String> sortByComparator(Map<String, String> unsortMap) {

        // Convert Map to List
        List<Map.Entry<String, String>> list =
                new LinkedList<Map.Entry<String, String>>(unsortMap.entrySet());
        // Sort list with comparator, to compare the Map values
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1,
                               Map.Entry<String, String> o2) {
                return (o1.getValue()).compareTo(o2.getValue()) * (-1);
            }
        });
        // Convert sorted map back to a Map
        Map<String, String> sortedMap = new LinkedHashMap<String, String>();
        for (Iterator<Map.Entry<String, String>> it = list.iterator(); it.hasNext();) {
            Map.Entry<String, String> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    private class QueryResults extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... URL) {
            InputStream response = null;
            String res = "";
            try {
                URL obj = new URL(URL[0]);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                // optional default is GET
                con.setRequestMethod("GET");

                //add request header
                //con.setRequestProperty("User-Agent", USER_AGENT);

                int responseCode = con.getResponseCode();
                System.out.println("\nSending 'GET' request to URL : " + URL);
                System.out.println("Response Code : " + responseCode);
                response = con.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(response));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    res += s;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            result.getBytes();
            System.out.println(result);
            // HashMap<String,String> items =  new HashMap<String, String>();
            List<String> itemname = new ArrayList<String>();
            try {
                JSONObject jsobj = new JSONObject(result);
                JSONArray jsarr = jsobj.getJSONArray("hits");
                for (int i=0; i < jsarr.length();i++) {
                    JSONObject obj = jsarr.getJSONObject(i);
                    //System.out.println("values " + obj.toString());
                    try {
                        JSONObject obj1 = obj.getJSONObject("fields");
                        System.out.println("values " + obj1.toString());
                        String name = obj1.getString("item_name");
                        String calories = obj1.getString("nf_calories");
                        System.out.println(" name "+ name+" KitchenItems.containsKey(name) "+ KitchenItems.containsKey(name));
                       if (KitchenItems.containsKey(name)){
                           KitchenItems.put(name,calories);
                       }

                        System.out.println("calories" + calories);

                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_food, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
