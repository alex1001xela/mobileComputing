package com.wua.mc.webuntisapp;

import com.wua.mc.webuntisapp.model.WebUntisClient;
import com.wua.mc.webuntisapp.model.iWebUntisClient;
import com.wua.mc.webuntisapp.presenter.CalendarPresenter;
import com.wua.mc.webuntisapp.presenter.FieldOfStudy;
import com.wua.mc.webuntisapp.presenter.Filter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WebUntisAPI {

    private iWebUntisClient wuc = new WebUntisClient("Usercampusap2", "konst6app6","HS+Reutlingen");
/*
    @Test
    public void getCurrentSchoolYear() throws Exception{
        boolean throwedException = false;


        //String jsonSample = .toString();
        JSONObject jsonObject = wuc.getCurrentSchoolYear();
       // JSONObject jsonObject = wuc.getClasses();
        //JSONArray jsonObject = wuc.getFilters();
        System.out.print(jsonObject.toString());
        try {








            JSONObject result = jsonObject.getJSONObject("response").getJSONObject("result");
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
    */

    // Ray: Tests-----------------------------------------------------------------------------------
    @Test
    public void Test_filter_classes() throws Exception{
         // this is the filter for INF .......
        boolean throwedException = false;
        JSONObject jsonObject = wuc.getClasses();
        CalendarPresenter cp = new CalendarPresenter();
        //JSONObject jsonObject = wuc.getFilters();
        //JSONArray jsonObject = wuc.getClasses();
        //System.out.print(jsonObject.toString());
        //-----------------------------
        try {

            /**
             by -ray :
             this is the test of choosing between "course of study backend" abbreviated in the below code as COS .
             The code takes as parameter : @param --> result ( this is the JSONArray of all COURSE OF STUDY IN our UNIVERSITY received from the API trough the getFilter method call.
             afterwads the result is looped (this first for loop) on in other retrieve the attribute = longName and stored dynamically in another Array list ( COS) .
             the second for loop  is to verify if the outputs are the ones expected. the || tokens are used to separate the COS.
             .
             JSONArray result = jsonObject.getJSONObject("response").getJSONArray("result");
             // JSONObject result = jsonObject.getJSONObject("response").getJSONObject("result");
             System.out.print("------------------------------------------------>");
             //System.out.print(result.getJSONObject(0).get("longName"));

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


             */





   ArrayList<String> namestest=new ArrayList<>();

            //JSONArray result = jsonObject.getJSONObject("response").getJSONArray("result"); // for classes
            JSONArray result = jsonObject.getJSONObject("response").getJSONArray("result");

            String name = "AC  Angewandte Chemie-AC";
            Filter filter=null;
            ArrayList<Filter> filters_container= cp.getFilters();
            ArrayList<FieldOfStudy>fieldOFstudy_container=null;


            for(int i =0;i<filters_container.size();i++){
                if(filters_container.get(i).toString().equals(name)){
                    filter=filters_container.get(i);// with equal not with == .
                    System.out.print("-++++---->"+filter.toString()+"<----++-----");
                    fieldOFstudy_container=cp.getFieldsOfStudy(filter);
                                  break;

                }



            }
            if(fieldOFstudy_container!=null  ){
                for(int a =0;a<fieldOFstudy_container.size();a++){
                    FieldOfStudy fos =fieldOFstudy_container.get(a);
                    // if(fos!=null){
                    System.out.print("-++++---->"+fos.toString()+"<----++-----");
                    // }

                }
            }








            // for filter .
           // result.getInt("id");
           // result.getString("name");
           // result.getInt("startDate");
            //result.getInt("endDate");
            String testFilterId="3";
            ArrayList<String> list_FOS = new ArrayList<>();
            // get the courses of a specific field of study
            for (int i = 0; i< result.length();i++){
             //   if( result.getJSONObject(i).getInt("id")==1748){
                   // String fildsName = result.getJSONObject(i).getString("longName");
                   // System.out.print(fildsName);
                   // list_FOS.add(fildsName);
                   // System.out.print("yes");
                   //   System.out.print("|"+result.getJSONObject(i).getString("longName")+"|");
             //  }

            }
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
    //-------------------------------------------futher tests could be inserted bellow.
    //Test für ADD mit (query)select kann man werte abfragen
}
