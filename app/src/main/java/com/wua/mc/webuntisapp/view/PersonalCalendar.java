package com.wua.mc.webuntisapp.view;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.wua.mc.webuntisapp.R;
import com.wua.mc.webuntisapp.presenter.Event;

public class PersonalCalendar extends Calendar {

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
		Log.i("CLICK", "YEAH");
		Context context = getApplicationContext();
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

}