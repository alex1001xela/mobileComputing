package com.wua.mc.webuntisapp;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WebUntisAPI {

    @Test
    public void getCurrentSchoolYear() throws Exception{
        boolean throwedException = false;
        String jsonSample = "{\"result\":{\"id\": 7,\"name\": \"2016/2017\",\"startDate\": 20160829,\"endDate\": 20170731}}";
        JSONObject jsonObject = null;

        try{
            jsonObject = new JSONObject(jsonSample);
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        try {
            JSONObject result = jsonObject.getJSONObject("result");
            result.getInt("id");
            result.getString("name");
            result.getInt("startDate");
            result.getInt("endDate");
        }
        catch (JSONException e){
            throwedException = true;
        }

        assertFalse(throwedException);

        try {
            jsonObject.getInt("result");
        }
        catch (JSONException e){
            throwedException = true;
        }

        assertTrue(throwedException);
    }
}
