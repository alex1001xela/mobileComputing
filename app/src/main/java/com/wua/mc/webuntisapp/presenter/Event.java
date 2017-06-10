package com.wua.mc.webuntisapp.presenter;

import java.util.Date;

public class Event {

	private String id;
	private String details;
	private String name;
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

	@Override
	public String toString(){
		return this.name + " " + this.details;
	}
}