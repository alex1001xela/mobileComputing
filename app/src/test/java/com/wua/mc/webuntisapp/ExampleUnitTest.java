package com.wua.mc.webuntisapp;

import com.wua.mc.webuntisapp.model.ElementType;
import com.wua.mc.webuntisapp.model.WebUntisClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void randomTests(){
        System.out.println(ElementType.CLASS.ordinal());
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void LoginTest(){
        String username = "Usercampusap2";//result.getString("username");
        String password = "konst6app6";//result.getString("password");

        WebUntisClient wuc = new WebUntisClient(username, password, "HS+Reutlingen");
        JSONObject json= wuc.authenticate();
        System.out.println(json.toString());
        try {

            String sessionID = json.getJSONObject("result").getString("sessionId");
            System.out.println("Session ID : "+sessionID);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void jsonStructureTest(){
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