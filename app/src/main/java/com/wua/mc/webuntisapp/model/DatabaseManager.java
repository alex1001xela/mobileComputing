package com.wua.mc.webuntisapp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.wua.mc.webuntisapp.presenter.Event;

import java.util.ArrayList;
import java.util.List;


public class DatabaseManager implements iDatabaseManager {

	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	private static final String LOG_TAG = DatabaseManager.class.getSimpleName();

	private String[] columns = {
			DatabaseHelper.COLUMN_ID,
			DatabaseHelper.COLUMN_NAME,
			DatabaseHelper.COLUMN_Lecturer
	};

	public DatabaseManager(Context context) {
		dbHelper = new DatabaseHelper(context);
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

	public DataBaseObject createShoppingMemo(String id, String name,String lecturer) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COLUMN_ID, id);
		values.put(DatabaseHelper.COLUMN_NAME, name);
		values.put(DatabaseHelper.COLUMN_Lecturer,lecturer);

		long insertId = database.insert(DatabaseHelper.TABLE_COURSE, null, values);

		Cursor cursor = database.query(DatabaseHelper.TABLE_COURSE, columns, DatabaseHelper.COLUMN_ID + "=" + insertId,
				null, null, null, null);

		cursor.moveToFirst();
		DataBaseObject shoppingMemo = cursorToShoppingMemo(cursor);
		cursor.close();

		return shoppingMemo;
	}

	private DataBaseObject cursorToShoppingMemo(Cursor cursor) {
		int id_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
		int name_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME);
		int lecturer_ = cursor.getColumnIndex(DatabaseHelper.COLUMN_Lecturer);

		String id = cursor.getString(id_);
		String name = cursor.getString(name_);
		String lecturer = cursor.getString(lecturer_);

		DataBaseObject shoppingMemo = new DataBaseObject(id, name,lecturer);

		return shoppingMemo;
	}

	public List<DataBaseObject> getAllShoppingMemos() {
		List<DataBaseObject> dataBaseObjectList = new ArrayList<>();

		Cursor cursor = database.query(DatabaseHelper.TABLE_COURSE,
				columns, null, null, null, null, null);

		cursor.moveToFirst();
		DataBaseObject dataBaseObject;

		while(!cursor.isAfterLast()) {
			dataBaseObject = cursorToShoppingMemo(cursor);
			dataBaseObjectList.add(dataBaseObject);
			Log.d(LOG_TAG, "ID: " + dataBaseObject.getId_course() + ", Inhalt: " + dataBaseObject.toString());
			cursor.moveToNext();
		}

		cursor.close();

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
}