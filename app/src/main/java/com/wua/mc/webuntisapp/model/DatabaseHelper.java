package com.wua.mc.webuntisapp.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Manny on 13.06.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper{


    public static final String DB_NAME = "StudentCalendarDB";
    public static final int DB_VERSION = 1;

    public static final String TABLE_COURSE= "course";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "course_name";
    public static final String COLUMN_Lecturer = "lecturer";

    private static final String LOG_TAG = DatabaseHelper.class.getSimpleName();


    public DatabaseHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_COURSE +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_Lecturer + " INTEGER NOT NULL);";


    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE + " angelegt.");
            db.execSQL(SQL_CREATE);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
