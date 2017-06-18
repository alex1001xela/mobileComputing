package com.wua.mc.webuntisapp.presenter;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class UniversityEvent extends Event {

	private String untisID = "";

	private String courseID = "";

	private String[] lecturers = {};

	private String[] rooms = {};

	private String semester = "";

	public UniversityEvent(String id, String name, String details, Date startTime, Date endTime, EventType eventType, String untisID, String courseID, String[] lecturers, String[] rooms, String semester) {
		super(id, name, details, startTime, endTime, eventType);
		this.untisID = untisID;
		this.courseID = courseID;
		this.lecturers = lecturers;
		this.rooms = rooms;
		this.semester = semester;
	}

	public UniversityEvent(JSONObject jsonObject, String name){
		super(jsonObject, name);
		try {
			this.untisID = jsonObject.getString("id");
			this.rooms = convertRoomsJSONToRooms(jsonObject.getJSONArray("ro"));
			this.lecturers = convertLecturesJSONToLecturers(jsonObject.getJSONArray("te"));
			this.courseID = jsonObject.getJSONArray("su").getJSONObject(0).getString("id");
		}
		catch (JSONException e){
			Log.e("JSONException", e.toString());
		}
	}

	private String[] convertRoomsJSONToRooms(JSONArray jsonArray) throws JSONException{
		String[] rooms = new String[jsonArray.length()];
		for(int i = 0; i < jsonArray.length(); i++){
			JSONObject room = jsonArray.getJSONObject(i);
			rooms[i] = room.getString("id");
		}
		return rooms;
	}

	private String[] convertLecturesJSONToLecturers(JSONArray jsonArray) throws JSONException{
		String[] lecturers = new String[jsonArray.length()];
		for(int i = 0; i < jsonArray.length(); i++){
			JSONObject lecturer = jsonArray.getJSONObject(i);
			lecturers[i] = lecturer.getString("id");
		}
		return lecturers;
	}

	@Override
	public String toString(){
		return getName();
	}


}