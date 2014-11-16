package ini.cmu.com.fitfood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class Quantity extends Activity {
    private Button Buttonplus;
    private Button Buttonminus;
    private Button Buttonback;
    private EditText quantity;
    private TextView itemname;
    private int amount = 1;
    private String qty = "1";
    private List<String> calqty;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quantity);
        Buttonplus = (Button) findViewById(R.id.buttonplus);
        Buttonminus = (Button) findViewById(R.id.buttonminus);
        Buttonback = (Button) findViewById(R.id.backbutton);
        quantity = (EditText) findViewById(R.id.editText);
        itemname = (TextView) findViewById(R.id.textView);
        calqty = new ArrayList<String>();

        String position = getIntent().getExtras().get("Position").toString();
        System.out.println("pos" + position);
        List keys = new ArrayList(SearchFood.items.keySet());
        for (int i=0; i< keys.size();i++){
            if (i == Integer.parseInt(position)){
                 name = keys.get(i).toString();
                System.out.println(" name in key "+ name);
               calqty = SearchFood.items.get(name);
                qty = calqty.get(1);
            }
        }
        quantity.setText(qty);
        Buttonplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int amount = Integer.parseInt(qty);
                amount += 1;
                qty = Integer.toString(amount);
                quantity.setText(qty);
            }
        });
        Buttonminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int amount = Integer.parseInt(qty);
                amount -= 1;
                qty = Integer.toString(amount);
                quantity.setText(qty);
            }
        });

        Buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> qtyList = new ArrayList<String>();
                double calorie = Double.parseDouble(calqty.get(0));
                calorie = calorie * Integer.parseInt(qty);
                qtyList.add(Double.toString(calorie));
                qtyList.add(qty);
                System.out.println(" name in key outside "+ name);
                System.out.println("qty" + qty + " calorie" + calorie);
                //SearchFood.items.put(name,qtyList);
                SearchFood.userSelected.put(name,qtyList);
                System.out.println(" in qty" + SearchFood.userSelected.keySet().size());
                Intent intent = new Intent(getApplicationContext(), SearchFood.class);
                //intent.putExtra("quantity", amount);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quantity, menu);
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
