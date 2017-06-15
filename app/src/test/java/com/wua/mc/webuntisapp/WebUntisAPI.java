package com.wua.mc.webuntisapp;

import com.wua.mc.webuntisapp.model.WebUntisClient;
import com.wua.mc.webuntisapp.model.iWebUntisClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WebUntisAPI {

    @Test
    public void getCurrentSchoolYear() throws Exception{
        boolean throwedException = false;
        iWebUntisClient wuc = new WebUntisClient("Usercampusap2", "konst6app6","HS+Reutlingen");

        //String jsonSample = .toString();
        JSONObject jsonObject = wuc.getClasses();
        //JSONArray jsonObject = wuc.getFilters();
        System.out.print(jsonObject.toString());
        try {

            JSONArray result = jsonObject.getJSONObject("response").getJSONArray("result");
           // JSONObject result = jsonObject.getJSONObject("response").getJSONObject("result");
            System.out.print("------------------------------------------------>");
            //System.out.print(result.getJSONObject(0).get("longName"));

            /**
             by -ray :
             this is the test of choosing between "course of study backend" abbreviated in the below code as COS .
             The code takes as parameter : @param --> result ( this is the JSONArray of all COURSE OF STUDY IN our UNIVERSITY received from the API trough the getFilter method call.
             afterwads the result is looped (this first for loop) on in other retrieve the attribute = longName and stored dynamically in another Array list ( COS) .
             the second for loop  is to verify if the outputs are the ones expected. the || tokens are used to separate the COS.
             .



             */

            ArrayList<String> All_course_of_study = new ArrayList<>();
            for(int i=0;i< result.length();i++){
                String name = result.getJSONObject(i).getString("longName"); // retrieving the Long name of each course of study as string

               // if(!All_course_of_study.contains(name)){
                    All_course_of_study.add(name);
              //  }
                // storing the course of study in a a list (ALL_course_of_study)

            }

            for ( int i = 0; i< All_course_of_study.size();i++){
               // System.out.print(All_course_of_study.get(i));
               // System.out.print("||");
            }



           // JSONObject result = jsonObject.getJSONObject("response").getJSONObject("result");
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
