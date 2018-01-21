package com.wua.mc.webuntisapp.view;
import android.view.Menu;

import com.wua.mc.webuntisapp.presenter.Event;
import com.wua.mc.webuntisapp.presenter.WebUntisService;

import java.util.ArrayList;


public interface iCalendarView {

	void showEventsOnCalendar(ArrayList<Event> events);
	WebUntisService getWebUntisService();
	void showToast(String text);

    boolean onCreateOptionMenu(Menu menu);
}