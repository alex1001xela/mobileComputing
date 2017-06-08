package com.wua.mc.webuntisapp.presenter;

import java.util.Date;

public class UniversityEvent extends Event {

	private String untisID;

	private String courseID;

	private String[] lecturersNames;

	private String[] rooms;

	private String semester;

	public UniversityEvent(String id, String details, String name, Date date, long startTime, long endTime, EventType eventType, String untisID, String courseID, String[] lecturersNames, String[] rooms, String semester) {
		super(id, details, name, date, startTime, endTime, eventType);
		this.untisID = untisID;
		this.courseID = courseID;
		this.lecturersNames = lecturersNames;
		this.rooms = rooms;
		this.semester = semester;
	}



}