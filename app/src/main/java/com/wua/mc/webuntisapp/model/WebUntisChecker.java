package com.wua.mc.webuntisapp.model;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class WebUntisChecker extends Service {
    public WebUntisChecker() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
