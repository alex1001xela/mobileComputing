package com.wua.mc.webuntisapp.view;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.wua.mc.webuntisapp.presenter.Event;
import com.wua.mc.webuntisapp.presenter.EventType;

import java.util.Date;

public class GlobalCalendar extends Calendar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_global_calendar);
        createEventBox(new Event("1", "Blablabla", "Important Event", new Date(), 10, 20, EventType.DEADLINE));
    }

    @Override
    public void showEventsOnCalendar(Event[] events) {

    }

    @Override
    public void showToast(String text) {
        Context context = getApplicationContext();
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}