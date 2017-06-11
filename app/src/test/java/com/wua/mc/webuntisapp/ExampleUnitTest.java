package com.wua.mc.webuntisapp;

import com.wua.mc.webuntisapp.model.WebUntisClient;

import org.json.JSONObject;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

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
        //JSONObject result = JSONFileReader.readPersonalData(); //hier wird die Datei gelesen


        String username = "Usercampusap2";//result.getString("username");
        String password = "konst6app6";//result.getString("password");

        WebUntisClient wuc = new WebUntisClient(username, password, "HS+Reutlingen");
        JSONObject res = new JSONObject("{\"result\":{\"id\": 7,\"name\": \"2016/2017\",\"startDate\": 20160829,\"endDate\": 20170731}}");
        assertEquals(res.getJSONObject("result").toString(),
                wuc.getCurrentSchoolYear().getJSONObject("response").getJSONObject("result").toString());

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
    public void gregorianCalendarTest(){
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Berlin"), Locale.GERMANY);
        System.out.println("DAY_OF_WEEK: " + calendar.get(Calendar.DAY_OF_WEEK));
        System.out.println("FIRST_DAY_OF_WEEK: " + calendar.getFirstDayOfWeek());


    }

}