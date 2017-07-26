package com.wua.mc.webuntisapp.presenter;

import android.util.Log;

import com.wua.mc.webuntisapp.model.DataBaseObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Event {

	private String id = "";
	private String name = "";
	private String details = "";
	private GregorianCalendar startTime;
	private GregorianCalendar endTime;
	private EventType eventType = EventType.LECTURE;
	private String color = "#FF0000"; // THIS A Default color for all events on creation

	public Event(String id, String name, String details, GregorianCalendar startTime, GregorianCalendar endTime, EventType eventType) {
		this.id = id;
		this.name = name;
		this.details = details;
		this.startTime = startTime;
		this.endTime = endTime;
		this.eventType = eventType;

		if(this.getColor()==null){
			this.color="#808080";
		}else{
			this.color=getColor();
		}
       // this.color= "#808080"; // alawys has a color..defualr gray....
	}

	public Event(JSONObject jsonObject, String name){
		try {
			GregorianCalendar[] dates = webUntisDateToDates(jsonObject);
			this.startTime = dates[0];
			this.endTime = dates[1];
			this.name = name;
		}
		catch (JSONException e){
			Log.i("JSONException", e.toString());
		}
	}

	public Event(DataBaseObject dbObject){
		this.id = "" + dbObject.getEvent_id();
		this.name = dbObject.getEvent_name();

		GregorianCalendar start = GregorianCalendarFactory.getGregorianCalendar();
		GregorianCalendar end = GregorianCalendarFactory.getGregorianCalendar();
		start.setTimeInMillis(dbObject.getEvent_timestamp_start());
		end.setTimeInMillis(dbObject.getEvent_timestamp_end());

		this.startTime = start;
		this.endTime = end;
		this.eventType = EventType.valueOf(dbObject.getEvent_type());
		this.color = dbObject.getEvent_color();
	}

	private GregorianCalendar[] webUntisDateToDates(JSONObject jsonObject) throws JSONException {
		GregorianCalendar[] dates = new GregorianCalendar[2];
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
		dates[0] = GregorianCalendarFactory.createGregorianCalendarCopy(gc);

		int endTimeSplitIndex = endTime.length() / 2;

		int endHour = Integer.parseInt(endTime.substring(0, endTimeSplitIndex));
		int endMinute = Integer.parseInt(endTime.substring(endTimeSplitIndex, endTime.length()));

		gc.set(year, month, dayOfMonth, endHour, endMinute);
		dates[1] = GregorianCalendarFactory.createGregorianCalendarCopy(gc);

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

	public GregorianCalendar getStartTime() {
		return startTime;
	}

	public GregorianCalendar getEndTime() {
		return endTime;
	}

	public EventType getEventType() {
		return eventType;
	}

	public String getColor() {
		return color;
	}

    public void setID(String id) {
        this.id = id;
    }

    public boolean isEventOnThisDay (GregorianCalendar calendar){

		return calendar.get(Calendar.DAY_OF_YEAR) == this.startTime.get(Calendar.DAY_OF_YEAR);
	}
	public void setColor(String color) { // by ray ..

            this.color = color;

	}

	@Override
	public String toString(){
		return this.name;
	}


}