package com.wua.mc.webuntisapp.view;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.wua.mc.webuntisapp.R;
import com.wua.mc.webuntisapp.presenter.CalendarPresenter;

public class MainActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent1 = new Intent(this, CalendarPresenter.class);
        startService(intent1);
//        Intent intent2 = new Intent(this, GlobalCalendar.class);
//        startActivity(intent2);
    }


}
