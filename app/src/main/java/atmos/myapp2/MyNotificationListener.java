package atmos.myapp2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by Shouta on 2017/9/20.
 */

public class MyNotificationListener extends NotificationListenerService {

    Context context;

    public void onCreate(){
        Log.i("MyNL","MyNotificationListener Created");
        context = getApplicationContext();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        String packageName = sbn.getPackageName();
        String ticker = (sbn.getNotification().tickerText == null)?
                "": sbn.getNotification().tickerText.toString();
        Long time = sbn.getPostTime();
        Intent msg = new Intent("Msg");
        msg.putExtra("Package", packageName);
        msg.putExtra("Ticker", ticker);
        msg.putExtra("Time", time);
        Log.i("Package", packageName);
        Log.i("Ticker", ticker);
        Log.i("Time", Long.toString(time));
        LocalBroadcastManager.getInstance(context).sendBroadcast(msg);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        Log.i("Msg", "Notification Removed");
    }
}