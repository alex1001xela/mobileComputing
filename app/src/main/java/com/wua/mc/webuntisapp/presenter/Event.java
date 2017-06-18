package com.wua.mc.webuntisapp.presenter;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Event {

	private String id = "";
	private String name = "";
	private String details = "";
	private Date startTime;
	private Date endTime;
	private EventType eventType;

	public Event(String id, String name, String details, Date startTime, Date endTime, EventType eventType) {
		this.id = id;
		this.name = name;
		this.details = details;
		this.startTime = startTime;
		this.endTime = endTime;
		this.eventType = eventType;
	}

	public Event(JSONObject jsonObject, String name){
		try {
			Date[] dates = webUntisDateToDates(jsonObject);
			this.startTime = dates[0];
			this.endTime = dates[1];
			this.name = name;
		}
		catch (JSONException e){
			Log.i("JSONException", e.toString());
		}
	}

	private Date[] webUntisDateToDates(JSONObject jsonObject) throws JSONException {
		Date[] dates = new Date[2];
		String date =  jsonObject.getString("date");

		String startTime = jsonObject.getString("startTime");
		String endTime = jsonObject.getString("endTime");

		GregorianCalendar gc = GregorianCalendarFactory.getGregorianCalendar();
		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(4, 6)) - 1;
		int dayOfMonth = Integer.parseInt(date.substring(6, 8));

		int startTimeSplitIndex = startTime.length() / 2;

		int startHour = Integer.parseInt(startTime.substring(0, startTimeSplitIndex));
		int startMinute = Integer.parseInt(startTime.substring(startTimeSplitIndex, startTime.length()));

		gc.set(year, month, dayOfMonth, startHour, startMinute);
		dates[0] = new Date(gc.getTimeInMillis());

		int endTimeSplitIndex = endTime.length() / 2;

		int endHour = Integer.parseInt(endTime.substring(0, endTimeSplitIndex));
		int endMinute = Integer.parseInt(endTime.substring(endTimeSplitIndex, endTime.length()));

		gc.set(year, month, dayOfMonth, endHour, endMinute);
		dates[1] = new Date(gc.getTimeInMillis());

		return dates;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDetails() {
		return details;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public EventType getEventType() {
		return eventType;
	}

	public boolean isEventOnThisDay (GregorianCalendar calendar){
		GregorianCalendar eventGregorianStart = GregorianCalendarFactory.getGregorianCalendar();
		eventGregorianStart.setTime(this.startTime);
		//GregorianCalendar eventGregorianEnd = GregorianCalendarFactory.getGregorianCalendar();
		//eventGregorianEnd.setTime(this.endTime);

		return calendar.get(Calendar.DAY_OF_YEAR) == eventGregorianStart.get(Calendar.DAY_OF_YEAR);
	}

	@Override
	public String toString(){
		return this.name;
	}
}