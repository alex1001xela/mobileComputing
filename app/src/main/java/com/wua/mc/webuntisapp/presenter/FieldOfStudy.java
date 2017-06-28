package com.wua.mc.webuntisapp.presenter;

import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Integer.parseInt;

public class FieldOfStudy {

	//private String untisID;
	private int untisID;

	private String name;

	private String longName;

	private boolean active;

	private String output_name;

	//private String filterID;
	private int filterID;

	public FieldOfStudy(String untisID, String name, String longName, boolean active, String filterID) {
		this.untisID = parseInt(untisID);
		this.name = name;
		this.longName = longName;
		this.active = active;
		this.filterID = parseInt(filterID); // parsed the string input to int
	}

	public FieldOfStudy(JSONObject jsonObject){
		try{
			this.untisID = jsonObject.getInt("id"); // integer ??
			this.name = jsonObject.getString("name");
			this.longName = jsonObject.getString("longName");
			this.active = jsonObject.getBoolean("active");
			this.filterID = jsonObject.getInt("did");// Integer ??

			//Ray: it seems like the value did and id are Integers............. At least in Webunits it is....
		}
		catch (JSONException e){

		}

	}

	//public String getUntisID() {
	//	return untisID;
	//}
	public int getUntisID() {
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
/*
	public String getFilterID() {
		return filterID;
	}
	*/
	public int getFilterID() {
		return filterID;
	}
// by ray : change the toString of Field Of study .. to print a suitable field of Study + name
	//@//Override
	//public String toString(){
		//return "id: " + untisID + " - name: " + name;
	//}
	@Override
	public String toString(){
		return "" + this.getLongName() + "-" + this.getName();
	}
}