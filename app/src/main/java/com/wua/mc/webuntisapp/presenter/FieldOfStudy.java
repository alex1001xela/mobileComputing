package com.wua.mc.webuntisapp.presenter;

import org.json.JSONException;
import org.json.JSONObject;

public class FieldOfStudy {

	private String untisID;

	private String name;

	private String longName;

	private boolean active;

	private String filterID;

	public FieldOfStudy(String untisID, String name, String longName, boolean active, String filterID) {
		this.untisID = untisID;
		this.name = name;
		this.longName = longName;
		this.active = active;
		this.filterID = filterID;
	}

	public FieldOfStudy(JSONObject jsonObject){
		try{
			this.untisID = jsonObject.getString("id");
			this.name = jsonObject.getString("name");
			this.longName = jsonObject.getString("longName");
			this.active = jsonObject.getBoolean("active");
			this.filterID = jsonObject.getString("did");
		}
		catch (JSONException e){

		}

	}

	public String getUntisID() {
		return untisID;
	}

	public String getName() {
		return name;
	}

	public String getLongName() {
		return longName;
	}

	public boolean isActive() {
		return active;
	}

	public String getFilterID() {
		return filterID;
	}

	@Override
	public String toString(){
		return "id: " + untisID + " - name: " + name;
	}
}