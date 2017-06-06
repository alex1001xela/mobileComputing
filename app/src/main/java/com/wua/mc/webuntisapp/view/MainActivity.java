package com.wua.mc.webuntisapp.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.wua.mc.webuntisapp.R;
import com.wua.mc.webuntisapp.model.WebUntisChecker;

public class MainActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent1 = new Intent(this, WebUntisChecker.class);
        startService(intent1);
    }


}
