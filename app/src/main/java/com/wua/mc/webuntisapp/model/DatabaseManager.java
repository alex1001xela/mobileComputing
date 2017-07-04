package com.wua.mc.webuntisapp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.wua.mc.webuntisapp.presenter.Event;
import com.wua.mc.webuntisapp.presenter.UniversityEvent;

import java.util.ArrayList;
import java.util.List;


public class DatabaseManager implements iDatabaseManager {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private static final String LOG_TAG = DatabaseManager.class.getSimpleName();

    private String[] columns = {
            DatabaseHelper.COLUMN_COURSE_ID,
            DatabaseHelper.COLUMN_COURSE_NAME,
            DatabaseHelper.COLUMN_COURSE_LECTURER,
            DatabaseHelper.COLUMN_COURSE_COLOR,
            DatabaseHelper.COLUMN_COURSE_UNTIS_ID,
    };

    private String[] columns2 = {
            DatabaseHelper.COLUMN_EVENT_ID,
            DatabaseHelper.COLUMN_EVENT_ROOM,
            DatabaseHelper.COLUMN_EVENT_TIMESTAMP_START,
            DatabaseHelper.COLUMN_EVENT_TIMESTAMP_END,
            DatabaseHelper.COLUMN_EVENT_NAME,
            DatabaseHelper.COLUMN_EVENT_COLOR,
            DatabaseHelper.COLUMN_EVENT_TYPE,
            DatabaseHelper.COLUMN_COURSE_ID
    };

    private String[] columns3 = {
            DatabaseHelper.COLUMNN_AUTHENTICATE
    };


    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void connectToDatabase() {
        database = dbHelper.getWritableDatabase();
    }

    public void disconnectFromDatabase() {
        dbHelper.close();
    }

    public void beginTransaction() {
        database.beginTransaction();
    }

    public void endTransaction() {
        database.endTransaction();
    }

    public void setTransactionSuccessful(){
        database.setTransactionSuccessful();
    }

    private DataBaseObject cursorToCourseDatabaseObject(Cursor cursor) {

        int idIndex_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE_ID);
        int courseName_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE_NAME);
        int courseLecturer_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE_LECTURER);
        int courseColor_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE_COLOR);
        int courseUntisId_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE_UNTIS_ID);

        String courseName = cursor.getString(courseName_);
        String courseLecturer = cursor.getString(courseLecturer_);
        String courseColor = cursor.getString(courseColor_);
        int courseUntisId = cursor.getInt(courseUntisId_);
        long id = cursor.getLong(idIndex_);

        return new DataBaseObject(courseName, courseLecturer, courseColor, courseUntisId, id);
    }

    private DataBaseObject cursorToEventDatabaseObject(Cursor cursor) {
        int eventId_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_EVENT_ID);
        int eventRoom_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_EVENT_ROOM);
        int eventTimestampStart_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_EVENT_TIMESTAMP_START);
        int eventTimestampEnd_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_EVENT_TIMESTAMP_END);
        int eventName_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_EVENT_NAME);
        int eventColor_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_EVENT_COLOR);
        int eventType_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_EVENT_TYPE);
        int courseId_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE_ID);

        String eventRoom = cursor.getString(eventRoom_);

        long eventTimestampStart = (long) cursor.getInt(eventTimestampStart_) * 1000;
        long eventTimestampEnd = (long) cursor.getInt(eventTimestampEnd_) * 1000;
        String eventName = cursor.getString(eventName_);
        String eventColor = cursor.getString(eventColor_);
        String eventType = cursor.getString(eventType_);
        long eventId = cursor.getLong(eventId_);
        long courseId = cursor.getLong(courseId_);

        return new DataBaseObject(eventRoom, eventTimestampStart, eventTimestampEnd, eventName, eventColor, eventType, eventId, courseId);
    }

    private DataBaseObject cursorToPersonalDatabaseObject(Cursor cursor) {
        int authenticated_ = cursor.getColumnIndex(DatabaseHelper.COLUMNN_AUTHENTICATE);

        long authenticated = cursor.getInt(authenticated_);

        return new DataBaseObject(authenticated);
    }

    @Override
    public List<DataBaseObject> getAllEventsDB() {

        List<DataBaseObject> dataBaseObjectList = new ArrayList<>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_EVENT,
                columns2, null, null, null, null, null);

        cursor.moveToFirst();
        DataBaseObject dataBaseObject2;

        while (!cursor.isAfterLast()) {
            dataBaseObject2 = cursorToEventDatabaseObject(cursor);
            dataBaseObjectList.add(dataBaseObject2);
            cursor.moveToNext();
        }
        cursor.close();

        return dataBaseObjectList;
    }

    @Override
    public DataBaseObject getCourseDB(String courseID) {
        DataBaseObject dataBaseObject = null;
        boolean courseFound = false;
        long courseIDLong = Long.parseLong(courseID);
        Cursor cursor = database.query(DatabaseHelper.TABLE_COURSE,
                columns2, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast() && !courseFound) {
            dataBaseObject = cursorToCourseDatabaseObject(cursor);
            if (dataBaseObject.getCourse_id() == courseIDLong) {
                courseFound = true;
            }
            cursor.moveToNext();
        }
        cursor.close();

        return dataBaseObject;
    }

    @Override
    public DataBaseObject getEventDB(String eventID) {
        DataBaseObject dataBaseObject = null;
        boolean eventFound = false;
        long eventIDLong = Long.parseLong(eventID);
        Cursor cursor = database.query(DatabaseHelper.TABLE_EVENT,
                columns2, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast() && !eventFound) {
            dataBaseObject = cursorToEventDatabaseObject(cursor);
            if (dataBaseObject.getEvent_id() == eventIDLong) {
                eventFound = true;
            }
            cursor.moveToNext();
        }
        cursor.close();

        return dataBaseObject;
    }

    @Override
    public DataBaseObject saveEventDB(Event event) {
        //Tabelle Event
        ContentValues values = new ContentValues();
        String room = "";
        String courseId = "";

        //values.put(DatabaseHelper.COLUMN_EVENT_ID, event_id);

        values.put(DatabaseHelper.COLUMN_EVENT_TIMESTAMP_START, (int) (event.getStartTime().getTime() / 1000));
        values.put(DatabaseHelper.COLUMN_EVENT_TIMESTAMP_END, (int) (event.getEndTime().getTime() / 1000));
        values.put(DatabaseHelper.COLUMN_EVENT_NAME, event.getName());
        values.put(DatabaseHelper.COLUMN_EVENT_TYPE, event.getEventType().toString());
        values.put(DatabaseHelper.COLUMN_EVENT_COLOR, event.getColor());
        if (event instanceof UniversityEvent) {
            room = ((UniversityEvent) event).getRooms()[0];
            courseId = ((UniversityEvent) event).getCourseID();
        }
        values.put(DatabaseHelper.COLUMN_EVENT_ROOM, room);
        values.put(DatabaseHelper.COLUMN_COURSE_ID, courseId); //todo could be null

        //Event

        long insertId = database.insert(DatabaseHelper.TABLE_EVENT, null, values);

        Cursor cursor = database.query(DatabaseHelper.TABLE_EVENT, columns2, DatabaseHelper.COLUMN_EVENT_ID + "=" + insertId, null, null, null, null);

        cursor.moveToFirst();
        DataBaseObject databaseObject = cursorToEventDatabaseObject(cursor);
        cursor.close();
        return databaseObject;
    }

    @Override
    public DataBaseObject setEventColorDB(long eventID, String color) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_EVENT_COLOR, color);

        database.update(DatabaseHelper.TABLE_EVENT,
                values,
                DatabaseHelper.COLUMN_EVENT_ID + "=" + eventID,
                null);

        Cursor cursor = database.query(DatabaseHelper.TABLE_EVENT,
                columns2, DatabaseHelper.COLUMN_EVENT_ID + "=" + eventID,
                null, null, null, null);

        cursor.moveToFirst();
        DataBaseObject dataBaseObject = cursorToEventDatabaseObject(cursor);
        cursor.close();

        return dataBaseObject;
    }

    @Override
    public DataBaseObject saveCourseDB(UniversityEvent event) {
        //Tabelle Course
        ContentValues values = new ContentValues();
        Log.i("COURSE_ID", event.getCourseID());
        values.put(DatabaseHelper.COLUMN_COURSE_NAME, event.getName());
        values.put(DatabaseHelper.COLUMN_COURSE_LECTURER, event.getTeachers()[0]);
        values.put(DatabaseHelper.COLUMN_COURSE_COLOR, event.getColor());
        values.put(DatabaseHelper.COLUMN_COURSE_UNTIS_ID, event.getCourseID());

        //Course Tabelle
        long insertId = database.insert(DatabaseHelper.TABLE_COURSE, null, values);
        DataBaseObject databaseObject;
        Cursor cursor = database.query(DatabaseHelper.TABLE_COURSE, columns, DatabaseHelper.COLUMN_COURSE_ID + "=" + insertId, null, null, null, null);
        cursor.moveToFirst();
        databaseObject = cursorToCourseDatabaseObject(cursor);
        cursor.close();

        return databaseObject;
    }

    @Override
    public int deleteCourseDB(long course_id) {
        return database.delete(DatabaseHelper.TABLE_COURSE, DatabaseHelper.COLUMN_COURSE_ID + "=" + course_id, null);
    }

    @Override
    public int deleteEventDB(long event_id) {
        return database.delete(DatabaseHelper.TABLE_EVENT, DatabaseHelper.COLUMN_EVENT_ID + "=" + event_id, null);
    }

    @Override
    public void loginDB() {

        long authenticate = 1;

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMNN_AUTHENTICATE, authenticate);

        database.update(DatabaseHelper.TABLE_PERSONAL_INFORMATION, values, null, null);
    }

    @Override
    public void logoutDB() {

        long authenticate = 1;

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMNN_AUTHENTICATE, 0);

        database.update(DatabaseHelper.TABLE_PERSONAL_INFORMATION, values,
                DatabaseHelper.COLUMNN_AUTHENTICATE + "=" + authenticate, null);
    }

    @Override
    public boolean isLoggedIn() {
        Cursor cursor = database.query(DatabaseHelper.TABLE_PERSONAL_INFORMATION,
                columns3, null, null, null, null, null);

        List<DataBaseObject> dataBaseObjectList = new ArrayList<>();
        cursor.moveToFirst();

        if (cursor.isAfterLast()) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMNN_AUTHENTICATE, 0);
            database.insert(DatabaseHelper.TABLE_PERSONAL_INFORMATION, null, values);
        }
        DataBaseObject dataBaseObject3;
        while (!cursor.isAfterLast()) {
            dataBaseObject3 = cursorToPersonalDatabaseObject(cursor);
            dataBaseObjectList.add(dataBaseObject3);
            cursor.moveToNext();
        }
        cursor.close();
        Log.i("MERO", ""+dataBaseObjectList.get(0).getAuthenticated());

        return dataBaseObjectList.size() > 0 && dataBaseObjectList.get(0).getAuthenticated() == 1L;
    }


/*

	 @SuppressLint("NewApi")
    public void deleteDatabase(){
        try{
            File outFile =dbHelper.myContext.getDatabasePath(DatabaseHelper.DB_NAME);
            database.deleteDatabase(outFile);
            Log.d(LOG_TAG, "Datenbank wurde erfolgreich geloescht!");
            database.close();
        }
        catch(Exception e){
            throw new IllegalArgumentException("Fehler: "+e);

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
		databaseObject = cursorToCourseDatabaseObject(cursor);
		cursor.close();

		return databaseObject;

	}

	public DataBaseObject createPersonalInformation(int authenticate) {

		//Tabelle Personal_Information
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COLUMNN_AUTHENTICATE, authenticate);

		//Personal information Tabelle
		long insertId = database.insert(DatabaseHelper.TABLE_PERSONAL_INFORMATION, null, values);

		Cursor cursor = database.query(DatabaseHelper.TABLE_PERSONAL_INFORMATION, columns3, DatabaseHelper.COLUMNN_AUTHENTICATE + "=" + insertId , null, null, null, null);

		cursor.moveToFirst();
		DataBaseObject databaseObject = cursorToPersonalDatabaseObject(cursor);
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
		DataBaseObject databaseObject = cursorToEventDatabaseObject(cursor);
		cursor.close();

		return databaseObject;

	}

	public List<DataBaseObject> getAlldatabaseObjects() {
		Log.d(LOG_TAG, "methode liste drin");
		List<DataBaseObject> dataBaseObjectList = new ArrayList<>();

		Cursor cursor = database.query(DatabaseHelper.TABLE_COURSE,
				columns, null, null, null, null, null);
		Cursor cursor2 = database.query(DatabaseHelper.TABLE_EVENT,
				columns2, null, null, null, null, null);
		Cursor cursor3 = database.query(DatabaseHelper.TABLE_PERSONAL_INFORMATION,
				columns3, null, null, null, null, null);


		cursor.moveToFirst();
		DataBaseObject dataBaseObject;

		cursor2.moveToFirst();
		DataBaseObject dataBaseObject2;

		cursor3.moveToFirst();
		DataBaseObject dataBaseObject3;

		while(!cursor.isAfterLast()) {
			dataBaseObject = cursorToCourseDatabaseObject(cursor);
			dataBaseObjectList.add(dataBaseObject);
			Log.d(LOG_TAG, "ID: " + dataBaseObject.getCourse_id() + ", Inhalt: " + dataBaseObject.toString());
			cursor.moveToNext();
		}
		cursor.close();

		while(!cursor2.isAfterLast()) {
			dataBaseObject2 = cursorToEventDatabaseObject(cursor2);
			dataBaseObjectList.add(dataBaseObject2);
			Log.d(LOG_TAG, "ID: " + dataBaseObject2.getEvent_id() + ", Inhalt: " + dataBaseObject2.toString());
			cursor2.moveToNext();
		}
		cursor2.close();

		while(!cursor3.isAfterLast()) {
			dataBaseObject3 = cursorToPersonalDatabaseObject(cursor3);
			dataBaseObjectList.add(dataBaseObject3);
			Log.d(LOG_TAG, "ID: " + dataBaseObject3.getAuthenticated() + ", Inhalt: " + dataBaseObject3.toString());
			cursor3.moveToNext();
		}
		cursor3.close();

		return dataBaseObjectList;
	}
	public DataBaseObject updateCourse(long id, String new_course_name, String new_course_lecturer, String new_course_color, int new_course_untis_id) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COLUMN_COURSE_NAME, new_course_name);
		values.put(DatabaseHelper.COLUMN_COURSE_LECTURER, new_course_lecturer);
		values.put(DatabaseHelper.COLUMN_COURSE_COLOR, new_course_color);
		values.put(DatabaseHelper.COLUMN_COURSE_UNTIS_ID, new_course_untis_id);

		database.update(DatabaseHelper.TABLE_COURSE,
				values,
				DatabaseHelper.COLUMN_COURSE_ID + "=" + id,
				null);

		Cursor cursor = database.query(DatabaseHelper.TABLE_COURSE,
				columns, DatabaseHelper.COLUMN_COURSE_ID + "=" + id,
				null, null, null, null);

		cursor.moveToFirst();
		DataBaseObject dataBaseObject = cursorToCourseDatabaseObject(cursor);
		cursor.close();

		return dataBaseObject;
	}*/


}


