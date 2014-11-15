package ini.cmu.com.fitfood;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ini.cmu.com.fitfood.util.SystemUiHider;

//import static ini.cmu.com.fitfood.getData.*;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class ViewReciepeList extends ListActivity implements
        OnItemClickListener {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;
    private ImageView img;
    private ArrayAdapter<Bitmap> listAdapter;
     ListView listview;
    List<JSONObject> urlobj;
   // ListView listView;
    List<RowItem> rowItems;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Item " + (position + 1) + ": " + rowItems.get(position),
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
        String url = rowItems.get(position).getrecpurl();
        System.out.println("opening url intent ***** "+url);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_reciepe_list);
        System.out.println("onCreate");





        //  final View controlsView = findViewById(R.id.fullscreen_content_controls);
       // final View contentView = findViewById(R.id.fullscreen_content);
        try {
           // setListAdapter(new ArrayAdapter(
                 //   this,android.R.layout.simple_list_item_1,
                   // (java.util.List) getData.getRecipes("fish")));
        } catch (Exception e) {
            e.printStackTrace();
        }
         img = (ImageView) findViewById(R.id.img);
       // TextView txt=(TextView) findViewById(R.id.txt);
        listview = (ListView) findViewById(android.R.id.list);
        listview.setOnItemClickListener(this);
        System.out.println("after imageview");
        try {
            getURLConnection urlget = new getURLConnection();
            urlget.execute(new String[] {""});
        } catch (Exception ex) {

        }


    }

    private class getURLConnection extends AsyncTask<String, Void, List<Bitmap>>{

        @Override
        protected List<Bitmap> doInBackground(String... urls){
            String result = "";
            List<Bitmap> bitmap=new ArrayList<Bitmap>();
            try{
            urlobj = getData.getRecipes("fish");
                System.out.println("len" + urlobj.size());
                for (int i=0;i<10;i++){
                    JSONObject firstobj=urlobj.get(i);
                    String imgurl=firstobj.get("imageUrl").toString();
                    System.out.println("1st url" + imgurl);
                    result = imgurl;
                    URL url = new URL(result);
                    System.out.println("URL....." + url);
                    //get("recipeUrl"));
                    //try this url = "http://0.tqn.com/d/webclipart/1/0/5/l/4/floral-icon-5.jpg"
                    HttpGet httpRequest = null;

                    httpRequest = new HttpGet(url.toURI());

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpResponse response = (HttpResponse) httpclient
                            .execute(httpRequest);

                    HttpEntity entity = response.getEntity();
                    BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
                    InputStream input = b_entity.getContent();

                    Bitmap bitmap_new = BitmapFactory.decodeStream(input);
                    System.out.println("new bitmap*********"+bitmap_new);
                    bitmap.add(bitmap_new);
                }

            }catch(Exception e){
                e.printStackTrace();
            }
            System.out.println("final bitmap"+bitmap);
            return bitmap;
        }

        @Override
        protected void onPostExecute(List<Bitmap> result){
            System.out.println("bitmap image....." + result);
           String[] names = new String[10];
            String[] rating = new String[10];
            String[] time = new String[10];
            String[] recpurl = new String[10];
           // ArrayList<String> ar = new ArrayList<String>();
            rowItems = new ArrayList<RowItem>();
            for (int i=0;i<10;i++){
                JSONObject firstobj=urlobj.get(i);
                try {
                    String recpname=firstobj.get("name").toString();
                    String recrating=firstobj.get("rating").toString();

                    String rectime=firstobj.get("timeInMinutes").toString();
                    String recurl=firstobj.get("recipeUrl").toString();
                    names[i]=recpname;
                    rating[i]=recrating;
                    time[i]=rectime;
                    recpurl[i]=recurl;

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            for (int i = 0; i < 10; i++) {
                RowItem item = new RowItem(result.get(i), names[i], rating[i],time[i],recpurl[i]);
                rowItems.add(item);
            }


            CustomListViewAdapter custadapter = new CustomListViewAdapter(getApplicationContext(),R.layout.simplerow, rowItems);

      //  listAdapter = new ArrayAdapter<Bitmap>(getApplicationContext(),R.layout.simplerow,R.id.txt,result);
            listview.setAdapter(custadapter);

           // img.setImageBitmap(result);

        }

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
        //    mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
