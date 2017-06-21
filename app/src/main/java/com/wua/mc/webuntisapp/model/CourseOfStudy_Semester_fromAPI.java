package com.wua.mc.webuntisapp.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ray on 6/15/2017.
 */

public class CourseOfStudy_Semester_fromAPI {


    iWebUntisClient wuc = new WebUntisClient("Usercampusap2", "konst6app6","HS+Reutlingen");

    JSONObject jsonObject = wuc.getFilters();
    //JSONArray jsonObject = wuc.getFilters();
   // FieldOfStudy fos = new FieldOfStudy()



    public ArrayList Get_string_COS() throws JSONException{

        JSONArray result = jsonObject.getJSONObject("response").getJSONArray("result");


        System.out.print("getting the list of all names in from the result object");
        ArrayList<String> All_course_of_study = new ArrayList<>();
        for(int i=0;i< result.length();i++){
            String name = result.getJSONObject(i).getString("longName"); // retrieving the Long name of each course of study as string
            if(!All_course_of_study.contains(name)){
                All_course_of_study.add(name);
            }  // storing the course of study in a a list (ALL_course_of_study)

        }

        System.out.print(" printing  the list of the All_course_of_study ------------------------- / this second loop is just for testing ....");

        for ( int i = 0; i< All_course_of_study.size();i++){
            System.out.print(All_course_of_study.get(i));
            System.out.print("||");
        }
        return All_course_of_study;
    }




}