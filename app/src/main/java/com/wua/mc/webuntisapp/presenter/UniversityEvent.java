package com.wua.mc.webuntisapp.presenter;

import android.util.Log;

import com.wua.mc.webuntisapp.model.DataBaseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.GregorianCalendar;

public class UniversityEvent extends Event {

	private String untisID = "";

	private String courseID = "";

	private String[] teachers = new String[1];

	private String[] rooms = new String[1];

	private String semester = "";

	public UniversityEvent(String id, String name, String details, GregorianCalendar startTime, GregorianCalendar endTime, EventType eventType, String untisID, String courseID, String[] teachers, String[] rooms, String semester) {
		super(id, name, details, startTime, endTime, eventType);
		this.untisID = untisID;
		this.courseID = courseID;
		this.teachers = teachers;
		this.rooms = rooms;
		this.semester = semester;
	}

	public UniversityEvent(JSONObject jsonObject, String name){
		super(jsonObject, name);
		try {
			this.untisID = jsonObject.getString("id");
			this.rooms = convertRoomsJSONToRooms(jsonObject.getJSONArray("ro"));
			this.teachers = convertTeachersJSONToTeachers(jsonObject.getJSONArray("te"));
			this.courseID = jsonObject.getJSONArray("su").getJSONObject(0).getString("id");
		}
		catch (JSONException e){
			Log.e("JSONException", e.toString());
		}
	}

	public UniversityEvent(DataBaseObject dbObject){
		super(dbObject);
		this.courseID = "" + dbObject.getCourse_id();
        this.teachers[0] = dbObject.getCourse_lecturer();
        this.rooms[0] = dbObject.getEvent_room();
	}

	private String[] convertRoomsJSONToRooms(JSONArray jsonArray) throws JSONException{
		String[] rooms = new String[jsonArray.length()];
		for(int i = 0; i < jsonArray.length(); i++){
			JSONObject room = jsonArray.getJSONObject(i);
			rooms[i] = room.getString("id");
		}
		return rooms;
	}

	private String[] convertTeachersJSONToTeachers(JSONArray jsonArray) throws JSONException{
		String[] lecturers = new String[jsonArray.length()];
		for(int i = 0; i < jsonArray.length(); i++){
			JSONObject lecturer = jsonArray.getJSONObject(i);
			lecturers[i] = CalendarPresenter.allTeachers.get(lecturer.getString("id")).getLongName();
		}
		return lecturers;
	}

	public String getUntisID() {
		return untisID;
	}

	public String getCourseID() {
		return courseID;
	}

	public String[] getTeachers() {
		return teachers;
	}

	public String[] getRooms() {
		return rooms;
	}

	public String getSemester() {
		return semester;
	}

	@Override
	public String toString(){
		return getName() + "\n" + getTeachers()[0];
	}


}