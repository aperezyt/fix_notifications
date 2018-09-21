package com.yt.alejandroperez.fixnotchnotifications;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetProviderInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Implementation of App Widget functionality.
 * Created by Alejandro Perez
 */
public class NotificationsWidget extends AppWidgetProvider {

    private final int MAX_NOTIFICATIONS= 6; //For 6 icons (check XML)

    static void updateAppWidget(AppWidgetManager appWidgetManager,
                                int appWidgetId, RemoteViews views) {
            appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        changeNotifications(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        LocalBroadcastManager.getInstance(context).registerReceiver(onNotice, new IntentFilter("Msg"));
        context.startService(new Intent(context, NotificationService.class));

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private void changeNotifications(Context context){
        StatusBarNotification[] activeNotifications = NotificationService.get().getActiveNotifications();
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.notifications_widget);
        int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, NotificationsWidget.class));
        if(activeNotifications!=null && activeNotifications.length>0) {
            ArrayList<StatusBarNotification> nonDuplicateNotifications = removeDuplicate(activeNotifications);
            for (int i = 0; i < appWidgetIds.length; i++) {
                int j, id;
                for (j = 0; j < nonDuplicateNotifications.size() && j < MAX_NOTIFICATIONS; j++) {
                    id = context.getResources().getIdentifier("imgaview_icon" + j, "id", context.getPackageName());
                    if (j != MAX_NOTIFICATIONS - 1) {
                        views.setImageViewIcon(id, nonDuplicateNotifications.get(j).getNotification().getSmallIcon());

                    } else if (j == MAX_NOTIFICATIONS - 1) {
                        views.setImageViewResource(id, R.drawable.more);
                    }
                    views.setViewVisibility(id, View.VISIBLE);

                }// for
                while (j < MAX_NOTIFICATIONS) {
                    id = context.getResources().getIdentifier("imgaview_icon" + j, "id", context.getPackageName());
                    views.setViewVisibility(id, View.GONE);
                    j++;
                }
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                updateAppWidget(appWidgetManager, appWidgetIds[i], views);
            }
        }else{
            //When there is a problem or no notifications
            for (int i = 0; i < appWidgetIds.length; i++) {
                int id= context.getResources().getIdentifier("imgaview_icon0", "id", context.getPackageName());
                views.setImageViewResource(id, R.drawable.no_notifications);
                views.setViewVisibility(id,View.VISIBLE);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                updateAppWidget(appWidgetManager, appWidgetIds[i], views);
            }// for
        }//if (activeNotifications)
    }// changeNotifications

    private ArrayList<StatusBarNotification> removeDuplicate (StatusBarNotification[] notifications){
        ArrayList<String> listPackageName = new ArrayList<String>();
        //StatusBarNotification[] newNotifications = new StatusBarNotification[notifications.length];
        ArrayList<StatusBarNotification> newNotifications = new ArrayList<StatusBarNotification>();
        for(int i=0; i<notifications.length; i++){
            if(!listPackageName.contains(notifications[i].getPackageName())){
                listPackageName.add(notifications[i].getPackageName());
                newNotifications.add(notifications[i]);

            }
        }
        return newNotifications;
    }

    private BroadcastReceiver onNotice= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
              changeNotifications(context);
        }
    };

    /**
     * Class to sort StatusBarNotifications by post time
     */
    class SortbyDate implements Comparator<StatusBarNotification>
    {

        @Override
        public int compare(StatusBarNotification t1, StatusBarNotification t2) {
            if (t1.getPostTime() < t2.getPostTime())
                return 1;
            else if (t1.getPostTime() > t2.getPostTime())
                return -1;
            else
                return 0;
        }
    }
}

