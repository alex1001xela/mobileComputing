package com.wua.mc.webuntisapp.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.wua.mc.webuntisapp.presenter.Event;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;


public class DatabaseManager implements iDatabaseManager {

	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	private static final String LOG_TAG = DatabaseManager.class.getSimpleName();

	private String[] columns = {
			dbHelper.COLUMN_COURSE_ID,
			dbHelper.COLUMN_COURSE_NAME,
			dbHelper.COLUMN_COURSE_LECTURER,
			dbHelper.COLUMN_COURSE_COLOR,
			dbHelper.COLUMN_COURSE_UNTIS_ID,
	};

	private String[] columns2 = {
			DatabaseHelper.COLUMN_EVENT_ID,
			DatabaseHelper.COLUMN_EVENT_ROOM,
			DatabaseHelper.COLUMN_EVENT_TIMESTAMP_START,
			DatabaseHelper.COLUMN_EVENT_TIMESTAMP_END,
			DatabaseHelper.COLUMN_EVENT_NAME,
			DatabaseHelper.COLUMN_EVENT_COLOR,
			DatabaseHelper.COLUMN_EVENT_TYPE
	};

	private String[] columns3 = {
			DatabaseHelper.COLUMNN_AUTHENTICATE
	};


	public DatabaseManager(Context context) {
		dbHelper = new DatabaseHelper(context);
		//context.deleteDatabase(dbHelper.DB_NAME);
	}

	public void connectToDatabase() {
		Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
		database = dbHelper.getWritableDatabase();
		Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
	}

	public void disconnectFromDatabase() {
		dbHelper.close();
		Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
	}

	public DataBaseObject createCourse(String course_name, String course_lecturer, String course_color, int course_untis_id) {
		//Tabelle Course
		ContentValues values = new ContentValues();
		//values.put(DatabaseHelper.COLUMN_COURSE_ID, course_id);
		values.put(DatabaseHelper.COLUMN_COURSE_NAME, course_name);
		values.put(DatabaseHelper.COLUMN_COURSE_LECTURER,course_lecturer);
		values.put(DatabaseHelper.COLUMN_COURSE_COLOR, course_color);
		values.put(DatabaseHelper.COLUMN_COURSE_UNTIS_ID,course_untis_id);

		//Course Tabelle
		long insertId = database.insert(DatabaseHelper.TABLE_COURSE, null, values);
		DataBaseObject databaseObject;
		Cursor cursor = null;
		cursor = database.query(DatabaseHelper.TABLE_COURSE, columns, DatabaseHelper.COLUMN_COURSE_ID + "=" + insertId, null, null, null, null);
		cursor.moveToFirst();
		databaseObject = cursorTodatabaseObject(cursor);
		cursor.close();

		return databaseObject;

	}

	public DataBaseObject createEvent(String event_room, int event_timestamp_start, int event_timestamp_end, String event_name, String event_type, int course_id,
									  String event_color) {

		//Tabelle Event
		ContentValues values = new ContentValues();
		//values.put(DatabaseHelper.COLUMN_EVENT_ID, event_id);
		values.put(DatabaseHelper.COLUMN_EVENT_ROOM, event_room);
		values.put(DatabaseHelper.COLUMN_EVENT_TIMESTAMP_START,event_timestamp_start);
		values.put(DatabaseHelper.COLUMN_EVENT_TIMESTAMP_END, event_timestamp_end);
		values.put(DatabaseHelper.COLUMN_EVENT_NAME, event_name);
		values.put(DatabaseHelper.COLUMN_EVENT_TYPE, event_type);
		values.put(DatabaseHelper.COLUMN_COURSE_ID, course_id);
		values.put(DatabaseHelper.COLUMN_EVENT_COLOR, event_color);


		//Event

		long insertId = database.insert(DatabaseHelper.TABLE_EVENT, null, values);

		Cursor cursor = database.query(DatabaseHelper.TABLE_EVENT, columns2, DatabaseHelper.COLUMN_EVENT_ID + "=" + insertId, null, null, null, null);

		cursor.moveToFirst();
		DataBaseObject databaseObject = cursorTodatabaseObject2(cursor);
		cursor.close();

		return databaseObject;

	}

	public DataBaseObject createPersonalInformation() {

		//Tabelle Personal_Information
		ContentValues values = new ContentValues();
		//values.put(DatabaseHelper.COLUMNN_AUTHENTICATE, authenticate);

		//Personal information Tabelle
	//	long insertId = database.insert(DatabaseHelper.TABLE_PERSONAL_INFORMATION, null, values);

		Cursor cursor = database.query(DatabaseHelper.TABLE_PERSONAL_INFORMATION, columns3, DatabaseHelper.COLUMNN_AUTHENTICATE , null, null, null, null);

		cursor.moveToFirst();
		DataBaseObject databaseObject = cursorTodatabaseObject3(cursor);
		cursor.close();

		return databaseObject;

	}

	private DataBaseObject cursorTodatabaseObject(Cursor cursor) {

			int idIndex_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE_ID);
			int courseName_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE_NAME);
			int courseLecturer_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE_LECTURER);
			int courseColor_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE_COLOR);
			int courseUntisId_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE_UNTIS_ID);

			//int courseId = cursor.getInt(courseId_);
			String courseName = cursor.getString(courseName_);
			String courseLecturer = cursor.getString(courseLecturer_);
			String courseColor = cursor.getString(courseColor_);
			int courseUntisId = cursor.getInt(courseUntisId_);
			long id = cursor.getLong(idIndex_);


		DataBaseObject databaseObject = new DataBaseObject(courseName, courseLecturer, courseColor, courseUntisId, id);

		return databaseObject;
	}

	private DataBaseObject cursorTodatabaseObject2(Cursor cursor) {
		int eventId_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_EVENT_ID);
		int eventRoom_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_EVENT_ROOM);
		int eventTimestampStart_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_EVENT_TIMESTAMP_START);
		int eventTimestampEnd_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_EVENT_TIMESTAMP_END);
		int eventName_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_EVENT_NAME);
		int eventColor_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_EVENT_COLOR);
		int eventType_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_EVENT_TYPE);



		String eventRoom = cursor.getString(eventRoom_);
		int eventTimestampStart = cursor.getInt(eventTimestampStart_);
		int eventTimestampEnd = cursor.getInt(eventTimestampEnd_);
		String eventName = cursor.getString(eventName_);
		String eventColor = cursor.getString(eventColor_);
		String eventType = cursor.getString(eventType_);
		long eventId = cursor.getLong(eventId_);


		DataBaseObject databaseObject = new DataBaseObject(eventRoom, eventTimestampStart, eventTimestampEnd, eventName, eventColor, eventType, eventId);

		return databaseObject;
	}

	private DataBaseObject cursorTodatabaseObject3(Cursor cursor) {
		int authenticated_ = cursor.getColumnIndex(DatabaseHelper.COLUMNN_AUTHENTICATE);

		long authenticated = cursor.getInt(authenticated_);

		DataBaseObject databaseObject = new DataBaseObject(authenticated);

		return databaseObject;
	}

	public List<DataBaseObject> getAlldatabaseObjects() {
		Log.d(LOG_TAG, "methode liste drin");
		List<DataBaseObject> dataBaseObjectList = new ArrayList<>();

		Cursor cursor = database.query(dbHelper.TABLE_COURSE,
				columns, null, null, null, null, null);
		Cursor cursor2 = database.query(dbHelper.TABLE_EVENT,
				columns2, null, null, null, null, null);
		Cursor cursor3 = database.query(dbHelper.TABLE_PERSONAL_INFORMATION,
				columns3, null, null, null, null, null);


		cursor.moveToFirst();
		DataBaseObject dataBaseObject;

		cursor2.moveToFirst();
		DataBaseObject dataBaseObject2;

		cursor3.moveToFirst();
		DataBaseObject dataBaseObject3;

		while(!cursor.isAfterLast()) {
			dataBaseObject = cursorTodatabaseObject(cursor);
			dataBaseObjectList.add(dataBaseObject);
			Log.d(LOG_TAG, "ID: " + dataBaseObject.getCourse_id() + ", Inhalt: " + dataBaseObject.toString());
			cursor.moveToNext();
		}
		cursor.close();

		while(!cursor2.isAfterLast()) {
			dataBaseObject2 = cursorTodatabaseObject2(cursor2);
			dataBaseObjectList.add(dataBaseObject2);
			Log.d(LOG_TAG, "ID: " + dataBaseObject2.getEvent_id() + ", Inhalt: " + dataBaseObject2.toString());
			cursor2.moveToNext();
		}
		cursor2.close();

		while(!cursor3.isAfterLast()) {
			dataBaseObject3 = cursorTodatabaseObject3(cursor3);
			dataBaseObjectList.add(dataBaseObject3);
			Log.d(LOG_TAG, "ID: " + dataBaseObject3.getAuthenticated() + ", Inhalt: " + dataBaseObject3.toString());
			cursor3.moveToNext();
		}
		cursor3.close();

		return dataBaseObjectList;
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


    @SuppressLint("NewApi")
    public void deleteDatabase(){
        try{
            File outFile =dbHelper.myContext.getDatabasePath(dbHelper.DB_NAME);
            database.deleteDatabase(outFile);
            Log.d(LOG_TAG, "Datenbank wurde erfolgreich geloescht!");
            database.close();
        }
        catch(Exception e){
            throw new IllegalArgumentException("Fehler: "+e);

        }
    }
}