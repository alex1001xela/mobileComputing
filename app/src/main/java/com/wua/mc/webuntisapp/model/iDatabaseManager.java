package com.wua.mc.webuntisapp.model;

import android.database.Cursor;
import com.wua.mc.webuntisapp.presenter.Event;

interface iDatabaseManager {

	Cursor getAllEventsDB();

	Cursor getCourseDB(String courseID);

	Cursor getEventDB(String eventID);

	Cursor saveEventDB(Event event);

	Cursor setEventColorDB(String eventID, String color);

	int deleteCourseDB(String courseID);

	int deleteEventDB(String eventID);

	Cursor loginDB(String username, String password);

	int logoutDB();

	Cursor getUserDataDB();

}