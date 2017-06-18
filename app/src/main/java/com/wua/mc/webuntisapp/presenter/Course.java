package com.wua.mc.webuntisapp.presenter;

import org.json.JSONException;
import org.json.JSONObject;

public class Course {

    private String id;
    private String untisID;
    private String name;
    private String longName;
    private String alternateName;
    private boolean active;


    public Course(String id, String untisID, String name, String longName, String alternateName, boolean active){
        this.id = id;
        this.untisID = untisID;
        this.name = name;
        this.longName = longName;
        this.alternateName = alternateName;
        this.active = active;
    }

    public Course(JSONObject jsonObject){
        try {
            this.untisID = jsonObject.getString("id");
            this.name = jsonObject.getString("name");
            this.longName = jsonObject.getString("longName");
            this.active = jsonObject.getBoolean("active");
            this.alternateName = jsonObject.getString("alternateName");
        }
        catch (JSONException e){

        }
    }

    public String getId() {
        return id;
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

    public String getAlternateName() {
        return alternateName;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return longName;
    }
}
