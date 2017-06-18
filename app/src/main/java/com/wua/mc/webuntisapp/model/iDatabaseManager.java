package com.wua.mc.webuntisapp.model;

import com.wua.mc.webuntisapp.presenter.Event;

import java.util.List;

public interface iDatabaseManager {

	List<DataBaseObject> getAllEventsDB();

	DataBaseObject getCourseDB(String courseID);

	DataBaseObject getEventDB(String eventID);

	DataBaseObject saveEventDB(Event event);

	DataBaseObject setEventColorDB(String eventID, String color);

	void deleteCourseDB(String courseID);

	void deleteEventDB(String eventID);

	boolean loginDB();

	void logoutDB();

	List<DataBaseObject> getUserDataDB();

}