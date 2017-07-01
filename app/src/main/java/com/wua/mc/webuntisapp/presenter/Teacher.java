package com.wua.mc.webuntisapp.presenter;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Teacher {

    private String id = "";
    private String longName = "";

    public Teacher(JSONObject jsonObject){
        try {
            this.id = jsonObject.getString("id");
            this.longName = jsonObject.getString("longName");
        }
        catch (JSONException e){
            Log.i("JSONException", e.toString());
        }
    }

    public String getId() {
        return id;
    }

    public String getLongName() {
        return longName;
    }
}
