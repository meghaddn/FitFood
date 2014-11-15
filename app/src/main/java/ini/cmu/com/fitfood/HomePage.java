package ini.cmu.com.fitfood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class HomePage extends Activity {

    private Button Button1;
    private Button Button2;
    private TextView TextView1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Button1 = (Button) findViewById(R.id.button1);
        Button2 = (Button) findViewById(R.id.button2);
        TextView1 = (TextView) findViewById(R.id.textView);

        String usergreet = new String();
        usergreet = "Hello ";
        usergreet += getIntent().getExtras().get("username").toString();
        usergreet += "!";
        TextView1.setText(usergreet);
        Button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                /*call SearchFood*/
                Intent intent = new Intent(getApplicationContext(), SearchFood.class);
                startActivity(intent);

            }
        });
        Button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                /*call Kitchen*/
                Intent intent = new Intent(getApplicationContext(), Kitchen.class);
                startActivity(intent);

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
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
