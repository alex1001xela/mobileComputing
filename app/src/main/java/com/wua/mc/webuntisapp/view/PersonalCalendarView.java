package com.wua.mc.webuntisapp.view;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.wua.mc.webuntisapp.presenter.Event;
import com.wua.mc.webuntisapp.presenter.FieldOfStudy;

import java.util.GregorianCalendar;

public class PersonalCalendarView extends CalendarView {

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
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	@Override
	protected void getWeeklyCalendar(GregorianCalendar calendar, FieldOfStudy fieldOfStudy) {
		getCalendarDataManagement().getWeeklyCalendarPersonal(this, calendar);
	}
}