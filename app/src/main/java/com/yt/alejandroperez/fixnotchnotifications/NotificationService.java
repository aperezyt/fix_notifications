package com.yt.alejandroperez.fixnotchnotifications;

import android.content.Context;
import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.support.v4.content.LocalBroadcastManager;


/**
 * Notification service
 * Created by Alejandro Perez on 20/09/18.
 */
public class NotificationService extends NotificationListenerService {

    Context context;
    static NotificationService _this;

    @Override

    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();
        _this = this;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        notice(sbn);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("Msg","Notification Removed");
        notice(sbn);

    }

    private void notice(StatusBarNotification sbn){
        String pack = sbn.getPackageName();

        Log.i("Package",pack);

        StatusBarNotification[] activeNotifications = this.getActiveNotifications();
        Log.d("num notifications", ""+activeNotifications.length);

        Intent msgrcv = new Intent("Msg");
        msgrcv.putExtra("sbn", sbn); //don't know possible or not
        msgrcv.putExtra("notifications", activeNotifications);
        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
    }

    public static NotificationService get(){
        return _this;
    }
}
