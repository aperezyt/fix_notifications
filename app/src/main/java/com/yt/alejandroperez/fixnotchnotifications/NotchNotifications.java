package com.yt.alejandroperez.fixnotchnotifications;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Main activity
 * Created by Alejandro Pérez
 */
public class NotchNotifications extends AppCompatActivity {

    private TextView textViewInfo;
    private Button buttonNotificationSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notch_notifications);

        textViewInfo = (TextView) findViewById(R.id.textViewInfo);
        buttonNotificationSettings = (Button) findViewById(R.id.buttonAccessNotifications);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void pressButton(View view){
        if(view.getId()==R.id.buttonAccessNotifications) {
            startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
        }else if(view.getId()==R.id.buttonDetails){
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
    }
}
