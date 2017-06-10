package com.wua.mc.webuntisapp.presenter;

import java.util.Arrays;
import java.util.Date;

public class UniversityEvent extends Event {

	private String untisID;

	private String courseID;

	private String[] lecturersNames;

	private String[] rooms;

	private String semester;

	public UniversityEvent(String id, String name, String details, Date startTime, Date endTime, EventType eventType, String untisID, String courseID, String[] lecturersNames, String[] rooms, String semester) {
		super(id, name, details, startTime, endTime, eventType);
		this.untisID = untisID;
		this.courseID = courseID;
		this.lecturersNames = lecturersNames;
		this.rooms = rooms;
		this.semester = semester;
	}

	@Override
	public String toString(){
		return getName() + " " + getDetails() + " " + Arrays.toString(lecturersNames) + " " + Arrays.toString(rooms) + " " + semester;
	}


}