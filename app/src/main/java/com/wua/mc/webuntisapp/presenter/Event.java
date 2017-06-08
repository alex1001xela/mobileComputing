package com.wua.mc.webuntisapp.presenter;

import java.util.Date;

public class Event {

	private String id;
	private String details;
	private String name;
	private Date date;
	private long startTime;
	private long endTime;
	private EventType eventType;

	public Event(String id, String details, String name, Date date, long startTime, long endTime, EventType eventType) {
		this.id = id;
		this.details = details;
		this.name = name;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.eventType = eventType;
	}

	public String getId() {
		return id;
	}

	public String getDetails() {
		return details;
	}

	public String getName() {
		return name;
	}

	public Date getDate() {
		return date;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public EventType getEventType() {
		return eventType;
	}
}