package com.wua.mc.webuntisapp.model;

import com.wua.mc.webuntisapp.presenter.Event;

import java.util.List;

public interface iDatabaseManager {

	List<DataBaseObject> getAllEventsDB();

	DataBaseObject getCourseDB(String courseID);

	DataBaseObject getEventDB(String eventID);

	DataBaseObject saveEventDB(Event event);

	DataBaseObject setEventColorDB(long eventID, String color);

	int deleteCourseDB(long course_id);

	int deleteEventDB(long event_id);

	boolean loginDB();

	boolean logoutDB();

	List<DataBaseObject> getUserDataDB();

}