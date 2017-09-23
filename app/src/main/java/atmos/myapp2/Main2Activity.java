package atmos.myapp2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    private TextView textview2;
    private TextView textview3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // get string value from the last page with key = "Name";
        String name = getIntent().getStringExtra("Name");
        // get int value from the last page with key = "Year";
        // if the key cannot be found then return 0.
        int year = getIntent().getIntExtra("Year", 0);


        // or get by bundle
        Bundle getBundle = getIntent().getBundleExtra("Bundle");
        String name2 = getBundle.getString("Name");
        int year2 = getBundle.getInt("Year");

        textview2 = (TextView) findViewById(R.id.textView2);
        textview3 = (TextView) findViewById(R.id.textView3);

        textview3.setText("{Name: " + name + ", year: " + year + "}\n{Name: " + name2 + ", year: " + year2 + "}\nEnd of list.\n");

    }
}
