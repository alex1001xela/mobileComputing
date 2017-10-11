package com.wua.mc.webuntisapp.view;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.Menu;
import android.widget.Toast;

import com.wua.mc.webuntisapp.R;
import com.wua.mc.webuntisapp.presenter.CalendarPresenter;
import com.wua.mc.webuntisapp.presenter.Event;
import com.wua.mc.webuntisapp.presenter.FieldOfStudy;
import com.wua.mc.webuntisapp.presenter.Filter;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class GlobalCalendarView extends CalendarView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        String chosenFieldOfStudy;
        String id;
        String filterID;
        String name;

        if (extras != null) {
            chosenFieldOfStudy = extras.getString("SelectedFieldOfStudy");
            id = extras.getString("id");
            filterID = extras.getString("filterID");
            name = extras.getString("name");
        } else {
            //todo temp data, getFromDatabase
            chosenFieldOfStudy = "Medien- und Kommunikationsinformatik-3MKIB6";
            id = "2129";
            filterID = "2";
            name = "3MKIB6";
        }


        final FieldOfStudy fieldOfStudy = new FieldOfStudy(id, name, chosenFieldOfStudy, true, filterID);
        calendarDataManagement.setSelectedFieldOfStudy(fieldOfStudy);
        buildWeeklyCalendar();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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
    public boolean onCreateOptionMenu(Menu menu) {
        return false;
    }

    @Override
    protected void getWeeklyCalendar(GregorianCalendar calendar, FieldOfStudy fieldOfStudy) {
        getCalendarWebUntis().getWeeklyCalendarGlobal(this, calendar, fieldOfStudy);
    }

    @Override
    protected String getEventInformation(String eventID) {
        return getCalendarWebUntis().getEventInformationGlobal(eventID);
    }

    @Override
    protected void setCalendarContentView() {
        setContentView(R.layout.activity_global_calendar);
    }
}