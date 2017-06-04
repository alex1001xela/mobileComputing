package com.wua.mc.webuntisapp;

import com.wua.mc.webuntisapp.model.WebUntisClient;

import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void getCurrentSchoolYear() throws Exception{
        JSONObject result = JSONFileReader.readPersonalData(); //hier wird die Datei gelesen
        String username = result.getString("username");
        String password = result.getString("password");

        WebUntisClient wuc = new WebUntisClient(username, password, "HS+Reutlingen");
        JSONObject res = new JSONObject("{\"result\":{\"id\": 7,\"name\": \"2016/2017\",\"startDate\": 20160829,\"endDate\": 20170731}}");
        assertEquals(res.getJSONObject("result").toString(),
                wuc.getCurrentSchoolYear().getJSONObject("response").getJSONObject("result").toString());
    }
}