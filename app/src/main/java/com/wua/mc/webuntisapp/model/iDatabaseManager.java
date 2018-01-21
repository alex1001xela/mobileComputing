package com.wua.mc.webuntisapp.model;

import com.wua.mc.webuntisapp.presenter.Event;
import com.wua.mc.webuntisapp.presenter.FieldOfStudy;
import com.wua.mc.webuntisapp.presenter.UniversityEvent;

import java.util.List;

public interface iDatabaseManager {

	List<DataBaseObject> getAllEventsDB();

	DataBaseObject getCourseDB(String courseID);

	DataBaseObject getEventDB(String eventID);

	DataBaseObject saveEventDB(Event event);

	DataBaseObject setEventColorDB(long eventID, int color);

	DataBaseObject saveCourseDB(UniversityEvent event);

	int deleteCourseDB(long course_id);

	int deleteEventDB(long event_id);

	void loginDB();

	void logoutDB();

	void setSelectedFieldOfStudyDB(FieldOfStudy fieldOfStudy);

	FieldOfStudy getSelectedFieldOfStudyDB();

	boolean isLoggedIn();



}