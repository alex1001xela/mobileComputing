package com.wua.mc.webuntisapp.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper{


    public static final String DB_NAME = "StudentCalendarDB";
    public static final int DB_VERSION = 11;
    public final Context myContext;

    public static final String TABLE_COURSE= "course";

    public static final String COLUMN_COURSE_ID = "course_id";
    public static final String COLUMN_COURSE_NAME = "course_name";
    public static final String COLUMN_COURSE_LECTURER = "course_lecturer";
    public static final String COLUMN_COURSE_COLOR = "course_color";
    public static final String COLUMN_COURSE_UNTIS_ID = "course_untis_id";

    public static final String TABLE_EVENT= "event";

    public static final String COLUMN_EVENT_ID = "event_id";
    public static final String COLUMN_EVENT_ROOM = "event_room";
    public static final String COLUMN_EVENT_TIMESTAMP_START = "event_timestamp_start";
    public static final String COLUMN_EVENT_TIMESTAMP_END = "event_timestamp_end";
    public static final String COLUMN_EVENT_NAME = "event_name";
    public static final String COLUMN_EVENT_COLOR = "event_color";
    public static final String COLUMN_EVENT_TYPE = "event_type";

    public static final String TABLE_PERSONAL_INFORMATION= "personal_information";

    public static final String COLUMNN_AUTHENTICATE = "authenticated";

    private static final String LOG_TAG = DatabaseHelper.class.getSimpleName();


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.myContext= context;
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }


    public static final String CREATE_TABLE_COURSE =
            " CREATE TABLE " + TABLE_COURSE +
                    "(" + COLUMN_COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_COURSE_NAME + " TEXT NOT NULL, " +
                    COLUMN_COURSE_LECTURER + " TEXT NOT NULL, " +
                    COLUMN_COURSE_COLOR + " TEXT NOT NULL, " +
                    COLUMN_COURSE_UNTIS_ID + " INTEGER NOT NULL" + ");";


    public static final String CREATE_TABLE_EVENT =
            " CREATE TABLE " + TABLE_EVENT  +
                    "(" + COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EVENT_ROOM + " TEXT NOT NULL, " +
                    COLUMN_EVENT_TIMESTAMP_START + " INTEGER NOT NULL, " +
                    COLUMN_EVENT_TIMESTAMP_END + " INTEGER NOT NULL, " +
                    COLUMN_EVENT_NAME + " TEXT NOT NULL, " +
                    COLUMN_EVENT_TYPE + " TEXT CHECK ("+COLUMN_EVENT_TYPE+"='LAB' or ("+COLUMN_EVENT_TYPE+"='EXAM' or ("+COLUMN_EVENT_TYPE+"='DEADLINE' or ("+COLUMN_EVENT_TYPE+"='LECTURE' or ("+COLUMN_EVENT_TYPE+"='PERSONAL'))))), " +
                    COLUMN_COURSE_ID + " INTEGER NOT NULL, " +
                    COLUMN_EVENT_COLOR + " TEXT NOT NULL, " +
                    "FOREIGN KEY ("+COLUMN_COURSE_ID+") REFERENCES "+TABLE_COURSE+"("+COLUMN_COURSE_ID+")"+ "ON DELETE CASCADE" + ");";

    public static final String CREATE_TABLE_PERSONAL_INFORMATION =
            " CREATE TABLE " + TABLE_PERSONAL_INFORMATION  +
                    "(" + COLUMNN_AUTHENTICATE +  " INTEGER DEFAULT 0" + ");";

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL("PRAGMA foreign_keys=ON");

            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl TABELLE: " + TABLE_COURSE + " angelegt.");
            db.execSQL(CREATE_TABLE_COURSE);

            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl TABELLE: " + TABLE_EVENT + " angelegt.");
            db.execSQL(CREATE_TABLE_EVENT);

            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl TABELLE: " + TABLE_PERSONAL_INFORMATION + " angelegt.");
            db.execSQL(CREATE_TABLE_PERSONAL_INFORMATION);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
