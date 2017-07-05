package com.wua.mc.webuntisapp.view;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.Menu;
import android.widget.Toast;

import com.wua.mc.webuntisapp.R;
import com.wua.mc.webuntisapp.presenter.Event;
import com.wua.mc.webuntisapp.presenter.FieldOfStudy;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class PersonalCalendarView extends CalendarView {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionMenu(Menu menu) {
		return false;
	}

	@Override
	protected void getWeeklyCalendar(GregorianCalendar calendar, FieldOfStudy fieldOfStudy) {
		getCalendarDataManagement().getWeeklyCalendarPersonal(this, calendar);
	}

	@Override
	protected String getEventInformation(String eventID) {
		return getCalendarDataManagement().getEventInformationPersonal(eventID);
	}

	@Override
	protected void setCalendarContentView() {
		setContentView(R.layout.activity_personal_calendar);
	}
}