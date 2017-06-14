package com.wua.mc.webuntisapp;

import com.wua.mc.webuntisapp.model.WebUntisClient;
import com.wua.mc.webuntisapp.model.iWebUntisClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WebUntisAPI {

    @Test
    public void getCurrentSchoolYear() throws Exception{
        boolean throwedException = false;
        iWebUntisClient wuc = new WebUntisClient("Usercampusap2", "konst6app6","HS+Reutlingen");

        //String jsonSample = .toString();
        JSONObject jsonObject = wuc.getCurrentSchoolYear();
        System.out.print(jsonObject.toString());
        try {

            JSONArray result = jsonObject.getJSONObject("response").getJSONArray("result");
            /*result.getInt("id");
            result.getString("name");
            result.getInt("startDate");
            result.getInt("endDate");
            */
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

    //Test für ADD mit (query)select kann man werte abfragen
}
