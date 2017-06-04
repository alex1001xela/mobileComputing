package com.wua.mc.webuntisapp.model;

import android.database.Cursor;
import com.wua.mc.webuntisapp.presenter.Event;

public class DatabaseManager implements iDatabaseManager {

	public DatabaseManager() {
	}


	@Override
	public Cursor getAllEventsDB() {
		return null;
	}

	@Override
	public Cursor getCourseDB(String courseID) {
		return null;
	}

	@Override
	public Cursor getEventDB(String eventID) {
		return null;
	}

	@Override
	public Cursor saveEventDB(Event event) {
		return null;
	}

	@Override
	public Cursor setEventColorDB(String eventID, String color) {
		return null;
	}

	@Override
	public int deleteCourseDB(String courseID) {
		return 0;
	}

	@Override
	public int deleteEventDB(String eventID) {
		return 0;
	}

	@Override
	public Cursor loginDB(String username, String password) {
		return null;
	}

	@Override
	public int logoutDB() {
		return 0;
	}

	@Override
	public Cursor getUserDataDB() {
		return null;
	}
}