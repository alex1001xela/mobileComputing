package com.wua.mc.webuntisapp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import com.wua.mc.webuntisapp.presenter.Event;
import com.wua.mc.webuntisapp.presenter.FieldOfStudy;
import com.wua.mc.webuntisapp.presenter.UniversityEvent;

import java.util.ArrayList;
import java.util.List;


public class DatabaseManager implements iDatabaseManager {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

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
            DatabaseHelper.COLUMN_AUTHENTICATE,
            DatabaseHelper.COLUMN_LAST_FIELD_OF_STUDY_ID,
            DatabaseHelper.COLUMN_LAST_FIELD_OF_STUDY_FILTER,
            DatabaseHelper.COLUMN_LAST_FIELD_OF_STUDY_NAME,
            DatabaseHelper.COLUMN_LAST_FIELD_OF_STUDY_LONGNAME
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
        int courseColor = cursor.getInt(courseColor_);
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
        int eventColor = cursor.getInt(eventColor_);
        String eventType = cursor.getString(eventType_);
        long eventId = cursor.getLong(eventId_);
        long courseId = cursor.getLong(courseId_);

        return new DataBaseObject(eventRoom, eventTimestampStart, eventTimestampEnd, eventName, eventColor, eventType, eventId, courseId);
    }

    private DataBaseObject cursorToPersonalDatabaseObject(Cursor cursor) {
        int authenticated_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_AUTHENTICATE);

        long authenticated = cursor.getInt(authenticated_);

        return new DataBaseObject(authenticated);
    }

    @Override
    public List<DataBaseObject> getAllEventsDB() {

        List<DataBaseObject> dataBaseObjectList = new ArrayList<>();
        List<DataBaseObject> coursesList = new ArrayList<>();

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


        Cursor cursor2 = database.query(DatabaseHelper.TABLE_COURSE, columns, null, null, null, null, null);
        cursor2.moveToFirst();
        while (!cursor2.isAfterLast()) {
            dataBaseObject2 = cursorToCourseDatabaseObject(cursor2);
            coursesList.add(dataBaseObject2);
            cursor2.moveToNext();
        }

        cursor2.close();
        for (DataBaseObject dbo : dataBaseObjectList) {
            for (DataBaseObject dbo2 : coursesList){
                if(dbo2.getCourse_untis_id() == dbo.getCourse_id()){
                    dbo.setCourse_lecturer(dbo2.getCourse_lecturer());
                }
            }
        }

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
        values.put(DatabaseHelper.COLUMN_EVENT_TIMESTAMP_START, (int) (event.getStartTime().getTimeInMillis() / 1000));
        values.put(DatabaseHelper.COLUMN_EVENT_TIMESTAMP_END, (int) (event.getEndTime().getTimeInMillis() / 1000));
        values.put(DatabaseHelper.COLUMN_EVENT_NAME, event.getName());
        values.put(DatabaseHelper.COLUMN_EVENT_TYPE, event.getEventType().toString());
        values.put(DatabaseHelper.COLUMN_EVENT_COLOR, event.getColor());
        if (event instanceof UniversityEvent) {
            UniversityEvent ue = (UniversityEvent) event;
            room = ue.getRooms()[0];
            courseId = ue.getCourseID();
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
    public DataBaseObject setEventColorDB(long eventID, int color) {
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
        database.delete(DatabaseHelper.TABLE_EVENT, DatabaseHelper.COLUMN_COURSE_ID + "=" + course_id, null);
        return database.delete(DatabaseHelper.TABLE_COURSE, DatabaseHelper.COLUMN_COURSE_UNTIS_ID + "=" + course_id, null);
    }

    @Override
    public int deleteEventDB(long event_id) {
        return database.delete(DatabaseHelper.TABLE_EVENT, DatabaseHelper.COLUMN_EVENT_ID + "=" + event_id, null);
    }

    @Override
    public void loginDB() {

        long authenticate = 1;

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_AUTHENTICATE, authenticate);

        database.update(DatabaseHelper.TABLE_PERSONAL_INFORMATION, values, null, null);
    }

    @Override
    public void logoutDB() {

        long authenticate = 0;

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_AUTHENTICATE, authenticate);

        database.update(DatabaseHelper.TABLE_PERSONAL_INFORMATION, values, null, null);
    }

    @Override
    public void setSelectedFieldOfStudyDB(FieldOfStudy fieldOfStudy) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_LAST_FIELD_OF_STUDY_ID, fieldOfStudy.getUntisID());
        values.put(DatabaseHelper.COLUMN_LAST_FIELD_OF_STUDY_FILTER, fieldOfStudy.getFilterID());
        values.put(DatabaseHelper.COLUMN_LAST_FIELD_OF_STUDY_NAME, fieldOfStudy.getName());
        values.put(DatabaseHelper.COLUMN_LAST_FIELD_OF_STUDY_LONGNAME, fieldOfStudy.getLongName());
        database.update(DatabaseHelper.TABLE_PERSONAL_INFORMATION, values, null, null);
    }

    @Override
    public FieldOfStudy getSelectedFieldOfStudyDB() {

        Cursor cursor = database.query(DatabaseHelper.TABLE_PERSONAL_INFORMATION,
                columns3, null, null, null, null, null);

        cursor.moveToFirst();

        String field_of_study_id = "" + cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_LAST_FIELD_OF_STUDY_ID));
        String filter_id = "" + cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_LAST_FIELD_OF_STUDY_FILTER));
        String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LAST_FIELD_OF_STUDY_NAME));
        String longname = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LAST_FIELD_OF_STUDY_LONGNAME));

        cursor.close();

        return new FieldOfStudy(field_of_study_id, name, longname, true, filter_id);
    }

    @Override
    public boolean isLoggedIn() {
        Cursor cursor = database.query(DatabaseHelper.TABLE_PERSONAL_INFORMATION,
                columns3, null, null, null, null, null);

        List<DataBaseObject> dataBaseObjectList = new ArrayList<>();
        cursor.moveToFirst();

        if (cursor.isAfterLast()) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_AUTHENTICATE, 0);
            database.insert(DatabaseHelper.TABLE_PERSONAL_INFORMATION, null, values);
        }
        DataBaseObject dataBaseObject3;
        while (!cursor.isAfterLast()) {
            dataBaseObject3 = cursorToPersonalDatabaseObject(cursor);
            dataBaseObjectList.add(dataBaseObject3);
            cursor.moveToNext();
        }
        cursor.close();

        return dataBaseObjectList.size() > 0 && dataBaseObjectList.get(0).getAuthenticated() == 1L;
    }
}


