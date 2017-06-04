package com.wua.mc.webuntisapp.view;

import android.app.Activity;
import android.os.Bundle;

import com.wua.mc.webuntisapp.R;
import com.wua.mc.webuntisapp.presenter.Event;

public class GlobalCalendar extends Activity implements iCalendarView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_calendar);
    }

    @Override
    public void showEventsOnCalendar(Event[] events) {

    }

    @Override
    public void showToast(String text) {

    }
}