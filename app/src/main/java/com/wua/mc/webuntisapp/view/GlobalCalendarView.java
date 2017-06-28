package com.wua.mc.webuntisapp.view;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.wua.mc.webuntisapp.presenter.Event;
import com.wua.mc.webuntisapp.presenter.FieldOfStudy;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class GlobalCalendarView extends CalendarView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void showEventsOnCalendar(ArrayList<Event> events) {
        showEventsOnDailyPlan(events);
    }

    @Override
    public void showToast(String text) {
        Context context = getApplicationContext();
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void getWeeklyCalendar(GregorianCalendar calendar, FieldOfStudy fieldOfStudy) {
        getCalendarWebUntis().getWeeklyCalendarGlobal(this, calendar, fieldOfStudy);
    }

    @Override
    protected String getEventInformation(String eventID) {
        return getCalendarWebUntis().getEventInformationGlobal(eventID);
    }
}