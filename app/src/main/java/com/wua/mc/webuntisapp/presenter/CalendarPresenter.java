package com.wua.mc.webuntisapp.presenter;

import android.app.Activity;
import android.util.Log;

import com.wua.mc.webuntisapp.model.DatabaseManager;
import com.wua.mc.webuntisapp.model.ElementType;
import com.wua.mc.webuntisapp.model.WebUntisClient;
import com.wua.mc.webuntisapp.model.iWebUntisClient;
import com.wua.mc.webuntisapp.view.iCalendarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class CalendarPresenter  implements iCalendarPresenter.iCalendarDataManagement, iCalendarPresenter.iCalendarWebUntis {

    private iWebUntisClient wuc;
    private DatabaseManager dbManager;

    private Event[] currentShownPersonalEvents =  {};
    private Event[] currentShownGlobalEvents =  {};

    private Filter[] filters;

    private FieldOfStudy[] fieldsOfStudy;

    public CalendarPresenter(Activity calendarView) {
        dbManager = new DatabaseManager(calendarView);
        wuc = new WebUntisClient("Usercampusap2", "konst6app6","HS+Reutlingen");
    }

    private ArrayList<Event> getAlreadySavedWeekEvents(GregorianCalendar gc, Event[] savedEvents){
        ArrayList<Event> weekEvents = new ArrayList<>();
        GregorianCalendar[] startAndEndOfWeek = GregorianCalendarFactory.getStartAndEndOfWeek(gc);
        for(Event event : savedEvents){

            GregorianCalendar eventGregorianStart = GregorianCalendarFactory.getGregorianCalendar();
            eventGregorianStart.setTime(event.getStartTime());
            GregorianCalendar eventGregorianEnd = GregorianCalendarFactory.getGregorianCalendar();
            eventGregorianEnd.setTime(event.getEndTime());
            int startDayOfYear = startAndEndOfWeek[0].get(Calendar.DAY_OF_YEAR);
            int endDayOfYear = startAndEndOfWeek[1].get(Calendar.DAY_OF_YEAR);

            if(startDayOfYear <= eventGregorianStart.get(Calendar.DAY_OF_YEAR) &&
                    endDayOfYear >= eventGregorianEnd.get(Calendar.DAY_OF_YEAR)){
                weekEvents.add(event);
            }
        }
        return weekEvents;
    }

    @Override
    public Event[] getWeeklyCalendarPersonal(iCalendarView calendarView, GregorianCalendar gc){

        ArrayList<Event> weekEvents = getAlreadySavedWeekEvents(gc, currentShownPersonalEvents);
        Event[] weekEventsArray = {};
        if(weekEvents.size() > 0){
            weekEventsArray = weekEvents.toArray(new Event[weekEvents.size()]);

        }
        else{
            weekEventsArray = getPersonalCalendar("startDate", "endDate");
        }
        calendarView.showEventsOnCalendar(weekEventsArray);
        return weekEventsArray;
    }


    @Override
    public Event[] getWeeklyCalendarGlobal(iCalendarView calendarView, GregorianCalendar gc, FieldOfStudy fieldOfStudy) {
        // todo remove example
        fieldOfStudy = new FieldOfStudy("1798", "3MKIB1", null, false, null);


        ArrayList<Event> weekEvents = new ArrayList<>();//getAlreadySavedWeekEvents(gc, currentShownGlobalEvents); //todo already saved
        Event[] weekEventsArray = {};
        if(weekEvents.size() > 0){
            weekEventsArray = weekEvents.toArray(new Event[weekEvents.size()]);
        }
        else{
            GregorianCalendar weekStart = GregorianCalendarFactory.getStartOfWeek(gc);
            GregorianCalendar weekEnd = GregorianCalendarFactory.getEndOfWeek(gc);

            String weekStartString = GregorianCalendarFactory.gregorianCalendarToWebUntisDate(weekStart);
            String weekEndString = GregorianCalendarFactory.gregorianCalendarToWebUntisDate(weekEnd);

            weekEventsArray = getGlobalCalendar(weekStartString, weekEndString, fieldOfStudy);
        }
        calendarView.showEventsOnCalendar(weekEventsArray);
        return weekEventsArray;
    }

    private Event[] convertTimetableToEvents(HashMap<String, Course> allCourses, JSONObject timetable) throws JSONException{
        ArrayList<Event> events = new ArrayList<>();
        JSONObject response = timetable.getJSONObject("response");
        JSONArray result = response.getJSONArray("result");

        for(int i = 0; i < result.length(); i++){
            JSONObject eventJSON = result.getJSONObject(i);
            String courseID = eventJSON.getJSONArray("su").getJSONObject(0).getString("id");
            events.add(new UniversityEvent(eventJSON, allCourses.get(courseID).getLongName()));
        }

        return events.toArray(new Event[]{});
    }

    private HashMap<String, Course> convertCoursesJSONToCourses(JSONArray jsonArray) throws JSONException{
        HashMap<String, Course> allCourses = new HashMap<>();

        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Course course = new Course(jsonObject);
            allCourses.put(course.getUntisID(), course);
        }

        return allCourses;
    }

    private FieldOfStudy[] convertClassesJSONToFieldsOfStudy(JSONArray jsonArray) throws JSONException{
        FieldOfStudy[] allFieldsOfStudy = new FieldOfStudy[jsonArray.length()];

        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            allFieldsOfStudy[i] = new FieldOfStudy(jsonObject);
        }

        return allFieldsOfStudy;
    }

    @Override
    public Event[] getMonthlyCalendarPersonal(iCalendarView calendarView, GregorianCalendar gc) {
        return new Event[0];
    }

    @Override
    public Event[] getMonthlyCalendarGlobal(iCalendarView calendarView, GregorianCalendar gc, FieldOfStudy fieldOfStudy) {
        return new Event[0];
    }

    @Override
    public void getEventInformation(String eventID) {
        dbManager.connectToDatabase();
        dbManager.disconnectFromDatabase();
    }

    @Override
    public void addCourse(String courseID) {
        dbManager.connectToDatabase();
        dbManager.disconnectFromDatabase();
    }

    @Override
    public void addEvent(String eventID){
        dbManager.connectToDatabase();
        dbManager.disconnectFromDatabase();
        
    }


    @Override
    public void createEvent(String name, String details, GregorianCalendar gc, long startTime, long endTime) {
        dbManager.connectToDatabase();
        dbManager.disconnectFromDatabase();
    }

    @Override
    public void deleteEvent(String eventID) {
        dbManager.connectToDatabase();
        dbManager.disconnectFromDatabase();
    }

    @Override
    public void deleteCourse(String courseID) {
        dbManager.connectToDatabase();
        dbManager.disconnectFromDatabase();
    }

    @Override
    public void setEventColor(String color, String eventID) {
        dbManager.connectToDatabase();
        dbManager.disconnectFromDatabase();
    }

    @Override
    public void exportCalendarICS(String path) {

    }

    @Override
    public void exportCourseICS(String path, String courseID) {

    }

    @Override
    public void exportEventICS(String path, String eventID) {

    }

    @Override
    public boolean login(String username, String password) {

        wuc = new WebUntisClient(username, password, "HS+Reutlingen");
        JSONObject jsonObject = wuc.authenticate();
        boolean dataValid = false;
        try {

            jsonObject.getJSONObject("result").getString("sessionId");
            dbManager.connectToDatabase();
            dbManager.loginDB();
            dataValid = true;
            dbManager.disconnectFromDatabase();

        }catch (Exception e){
            Log.e("no sessionID", e.toString());
        }

        return dataValid;
    }

    @Override
    public boolean areLoginDataValid(String username, String password){
        dbManager.connectToDatabase();
        dbManager.disconnectFromDatabase();
        return true;
    }

    @Override
    public boolean isAlreadyLoggedIn(){
        dbManager.connectToDatabase();
        boolean isLoggedIn = dbManager.isLoggedIn();
        dbManager.disconnectFromDatabase();
        return isLoggedIn;
    }

    @Override
    public void logout() {
        dbManager.connectToDatabase();
        dbManager.disconnectFromDatabase();
    }

    @Override
    public FieldOfStudy[] getFieldsOfStudy(Filter[] filter) {
        return new FieldOfStudy[0];
    }

    @Override
    public Filter[] getFilters() {
        return new Filter[0];
    }

    private Event[] getPersonalCalendar(String startDate, String endDate) {
        /*dbManager.connectToDatabase();
        List<DataBaseObject> allDatabaseObjects =  dbManager.getAlldatabaseObjects();
        Log.i("DB_STUFF", allDatabaseObjects.toString());*/
        dbManager.connectToDatabase();
        dbManager.disconnectFromDatabase();

        return null;
    }

    private Event[] getGlobalCalendar(String startDate, String endDate, FieldOfStudy fieldOfStudy) {
        Event[] events = {};
        try{
            HashMap<String, Course> allCourses = convertCoursesJSONToCourses(wuc.getCourses().getJSONObject("response").getJSONArray("result")); //todo public static, also teachers

            JSONObject timetable = wuc.getTimetableForElement(fieldOfStudy.getUntisID(), ElementType.CLASS, startDate, endDate);

            events = convertTimetableToEvents(allCourses, timetable);
        }
        catch (JSONException e){
            Log.i("JSONException", e.toString());
        }
        return events;
    }

}
