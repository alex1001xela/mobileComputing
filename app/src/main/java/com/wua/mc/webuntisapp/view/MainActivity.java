package com.wua.mc.webuntisapp.view;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.wua.mc.webuntisapp.R;

public class MainActivity extends Activity {

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Intent intent = new Intent("com.wua.mc.webuntisapp.view.GlobalCalendar");
            startActivity(intent);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent("com.wua.mc.webuntisapp.presenter.CalendarPresenter");
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }


}
