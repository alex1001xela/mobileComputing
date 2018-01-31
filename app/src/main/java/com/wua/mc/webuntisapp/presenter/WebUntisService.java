package com.wua.mc.webuntisapp.presenter;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.wua.mc.webuntisapp.model.ElementType;
import com.wua.mc.webuntisapp.model.WebUntisClient;
import com.wua.mc.webuntisapp.model.iWebUntisClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class WebUntisService extends Service {

    private final IBinder mBinder = new MyBinder();
    public static HashMap<String, Course> allCourses;
    public static HashMap<String, Teacher> allTeachers;
    private static iWebUntisClient wuc;

    public static iWebUntisClient getWuc() {
        return WebUntisService.wuc;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initializeWebUntisData();
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        initializeWebUntisData();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MyBinder extends Binder {
        public WebUntisService getService() {
            return WebUntisService.this;
        }
    }

    private void initializeWebUntisData() {
        if(wuc == null) {
            wuc = new WebUntisClient("Usercampusap2", "konst6app6", "HS+Reutlingen");
            try {
                allCourses = convertCoursesJSONToCourses(wuc.getCourses().getJSONObject("response").getJSONArray("result"));
                allTeachers = convertTeachersJSONToTeachers(wuc.getTeachers().getJSONObject("response").getJSONArray("result"));
            } catch (JSONException e) {

            }
        }
    }

    private HashMap<String, Course> convertCoursesJSONToCourses(JSONArray jsonArray) throws JSONException {
        HashMap<String, Course> allCourses = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Course course = new Course(jsonObject);
            //allCourses.put(course.getName(), course); // for this reason i will use the name of the course ..
            allCourses.put(Integer.toString(course.getUntisID()), course); // we cannot differenbtiate between course of different field of study using the untis id
            //it identifies the courses within all course ( I believe).
        }

        return allCourses;
    }

    private HashMap<String, Teacher> convertTeachersJSONToTeachers(JSONArray jsonArray) throws JSONException {
        HashMap<String, Teacher> allTeachers = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Teacher teacher = new Teacher(jsonObject);
            allTeachers.put(teacher.getId(), teacher);
        }

        return allTeachers;
    }

    public JSONObject getTimetableForElement(String id, ElementType type, String startDate, String endDate) {
        return wuc.getTimetableForElement(id, type, startDate, endDate);
    }

    public JSONObject getClasses() {
        return wuc.getClasses();
    }

    public JSONObject getFilters() {
        return wuc.getFilters();
    }

    public JSONObject getCourses() {
        return wuc.getCourses();
    }
}
