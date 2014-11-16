package ini.cmu.com.fitfood;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchFood extends Activity implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {
    private SearchView mSearchView;
    private ListView mListView;
    private HttpClient client;
    private HttpResponse response;
    private ArrayAdapter<String> listAdapter;
    public static HashMap<String,List<String>> items ;
    public static HashMap<String,List<String>> userSelected ;
    private Button DoneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);
        DoneButton = (Button) findViewById(R.id.donebutton);
        mSearchView = (SearchView) findViewById(R.id.searchView);
        items= new HashMap<String, List<String>>();
        userSelected = new  HashMap<String,List<String>>();
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setOnItemClickListener(this);
        client = new DefaultHttpClient();
        setupSearchView();
        DoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double UserCal =0;
                System.out.println("in srch" + userSelected.keySet().size());
                for (Map.Entry<String,List<String>> entry: userSelected.entrySet()){
                    List<String> val = entry.getValue();
                    System.out.println("Key" + entry.getKey() + " value" + entry.getValue());
                    UserCal = Double.parseDouble(val.get(0).toString()) + UserCal;
                }
                System.out.println("cal222 "+ UserCal);
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent);
            }
        });
    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(false);
    }

    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            //mListView.clearTextFilter();
        } else {
            //mListView.setFilterText(newText.toString());
        }
        return true;
    }

    public boolean onQueryTextSubmit(String query) {
      //  String url = "http://www.google.com/search?q=mkyong";

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.nutritionix.com")
                .appendPath("v1_1")
                .appendPath("search")
                .appendPath(query)
                .appendQueryParameter("results", "0:6")
                .appendQueryParameter("fields", "item_name,nf_calories")
                .appendQueryParameter("appId","1204291e")
                .appendQueryParameter("appKey","6b1c9af9fcd19d88c850dec9411c71a0");
        String URL = builder.build().toString();
        try {
            QueryResults qr = new QueryResults();
            qr.execute(new String[] {URL});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        return false;
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
                        String itemqty ="1";
                        List<String> calqty = new ArrayList<String>();
                        calqty.add(calories);
                        calqty.add(itemqty);
                        itemname.add(name);
                        items.put(name, calqty);
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
                listAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.row, itemname);
                mListView.setAdapter(listAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public void onItemClick(AdapterView<?> a, View v, int position, long id){
        //System.out.println("id : "+ id +" position"+position);
        Intent quanIntent = new Intent(getApplicationContext(), Quantity.class);

        quanIntent.putExtra("Position",position);
        startActivity(quanIntent);
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
