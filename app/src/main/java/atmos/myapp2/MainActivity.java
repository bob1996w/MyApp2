package atmos.myapp2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Button button1;
    private Button button2;
    private Button button3;
    private TextView textview;
    private ListView listview;
    int notificationCount;
    ArrayList<String> listContent;
    ArrayAdapter<String> arrayAdapter;


    private boolean isNotificationEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /*
    //check notification access setting is enabled or not
    public static boolean checkNotificationEnabled() {
        try{
            if(Settings.Secure.getString(MainActivity.mActivity.getContentResolver(),
                    "enabled_notification_listeners").contains(App.getContext().getPackageName()))
            {
                return true;
            } else {
                return false;
            }

        }catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // all the variables are saved in R.java, so just call id from R.id.<id>
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        textview = (TextView) findViewById(R.id.textview1);
        listview = (ListView) findViewById(R.id.listview1);

        listContent = new ArrayList<>();
        listContent.add("test");
        arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, listContent);
        listview.setAdapter(arrayAdapter);
        notificationCount = 1;

        // notification Listener

        if (!NotificationManagerCompat.getEnabledListenerPackages(this).contains(getPackageName())) {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
        } else {
            Toast.makeText(this, "Notification Reading Permitted", Toast.LENGTH_LONG).show();
            MyNotificationListener myNL = new MyNotificationListener();
        }

        LocalBroadcastManager.getInstance(this).
                registerReceiver(onNotice, new IntentFilter("Msg"));

        // android studio doesn't support lambda expressions yet (java 8), so... f__k you.
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textview.setText(R.string.textview1_2);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // intent: an important object that can do a lot of things like open another activity
                // and take some values in <key, value> pairs.
                // use getIntent to get these values in next activity.
                Intent intent = new Intent();
                intent.putExtra("Name", "Shouta");
                intent.putExtra("Year", 1996);

                // or, use a bubdle to pack the data and transfer.
                Bundle bundle = new Bundle();
                bundle.putString("Name", "IDontHaveAName");
                bundle.putInt("Year", 2000);
                intent.putExtra("Bundle", bundle);
                intent.setClass(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("btn3", "button pressed");
                // set an intent to switch to this Activity
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent appIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);

                // Notification test
                // SmallIcon must be set in order to let it work.
                final Notification notification1 = new NotificationCompat.Builder(MainActivity.this)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentIntent(appIntent)
                        .setAutoCancel(true)
                        .setContentTitle(getString(R.string.notification1_header))
                        .setContentText(getString(R.string.notification1_content))
                        .setContentInfo(Integer.toString(notificationCount))
                        .setWhen(System.currentTimeMillis())
                        .setDefaults(Notification.DEFAULT_ALL)
                        .build();

                notificationCount++;
                final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                int mId = 1;
                manager.notify(mId, notification1);
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(Calendar.getInstance().getTime());
                arrayAdapter.add(timeStamp + "\n" + System.currentTimeMillis());
            }
        });

    }
    private BroadcastReceiver onNotice = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String packageName = intent.getStringExtra("Package");
            String text = intent.getStringExtra("Ticker");
            Long time = intent.getLongExtra("Time", 0L);
            arrayAdapter.add(new SimpleDateFormat("yyyy/mm/dd hh:MM:ss")
                    .format(time) + "\n" + packageName + ":" + text);
        }
    };

}
