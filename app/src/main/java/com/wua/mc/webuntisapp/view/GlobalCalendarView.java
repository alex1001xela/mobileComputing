package com.wua.mc.webuntisapp.view;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.wua.mc.webuntisapp.presenter.Event;

public class GlobalCalendarView extends CalendarView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void showEventsOnCalendar(Event[] events) {
        showEventsOnDailyPlan(events);
    }

    @Override
    public void showToast(String text) {
        Context context = getApplicationContext();
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}