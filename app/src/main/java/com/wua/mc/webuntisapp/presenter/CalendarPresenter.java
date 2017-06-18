package com.wua.mc.webuntisapp.presenter;

import android.util.Log;

import com.wua.mc.webuntisapp.model.WebUntisClient;
import com.wua.mc.webuntisapp.model.iDatabaseManager;
import com.wua.mc.webuntisapp.model.iWebUntisClient;
import com.wua.mc.webuntisapp.view.iCalendarView;

import org.json.JSONObject;

import java.util.Date;

public class CalendarPresenter  implements iCalendarPresenter.iCalendarDataManagement, iCalendarPresenter.iCalendarWebUntis {

    iWebUntisClient wuc;
    iDatabaseManager dbManager;
    static String sessionID;
    private Event[] currentShownEvents;

    private Filter[] filters;

    private FieldOfStudy[] fieldsOfStudy;

    public CalendarPresenter() {

    }


    @Override
    public Event[] getWeeklyCalendarPersonal(iCalendarView calendarView, Date date){
        wuc = new WebUntisClient("Usercampusap2", "konst6app6","HS+Reutlingen");
        /*if(isDateInCurrentEvents(date)){

        }
        else{
            dbManager.getAllEventsDB();
        }*/

        // EXAMPLE
        String[] profs = {"Prof. John", "Prof. Jane"};
        String[] rooms = {"9-101"};
        Event[] weekEvents = {
                new Event("0", "Event 0", "Bloooooo", new Date(0), new Date(900000), EventType.DEADLINE),
                new Event("1", "Event 1", "Blaaaaaaa", new Date(0), new Date(9000000), EventType.DEADLINE),
                new Event("2", "Event 2", "Bluuuuuuu", new Date(8900000), new Date(26900000), EventType.DEADLINE),
                new UniversityEvent("3", "Event 3", "Bleeeeeee", new Date(8900000), new Date(26900000), EventType.DEADLINE, "5", "10", profs, rooms, "MKI5" )
        };

        calendarView.showEventsOnCalendar(weekEvents);

        return weekEvents;
    }

    @Override
    public Event[] getMonthlyCalendarPersonal(iCalendarView calendarView, Date date) {
        return new Event[0];
    }

    private boolean isDateInCurrentEvents(Date date){
        int i = 0;
        boolean dateFound = false;
        while(i < currentShownEvents.length && !dateFound){
            Event event = currentShownEvents[i];
            dateFound = event.getStartTime().getDate() == date.getDate();
            i++;
        }
        return dateFound;
    }

    @Override
    public Event[] getWeeklyCalendarGlobal(iCalendarView calendarView, Date date, FieldOfStudy fieldOfStudy) {

        return new Event[0];
    }

    @Override
    public Event[] getMonthlyCalendarGlobal(iCalendarView calendarView, Date date, FieldOfStudy fieldOfStudy) {
        return new Event[0];
    }

    @Override
    public void getEventInformation(String eventID) {

    }

    @Override
    public void addCourse(String courseID) {

    }

    @Override
    public void addEvent(String eventID){

        
    }


    @Override
    public void createEvent(String name, String details, Date date, long startTime, long endTime) {

    }

    @Override
    public void deleteEvent(String eventID) {

    }

    @Override
    public void deleteCourse(String courseID) {

    }

    @Override
    public void setEventColor(String color, String eventID) {

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
    public void login(String username, String password) {

        wuc = new WebUntisClient(username, password, "HS+Reutlingen");
        JSONObject jsonObject = wuc.authenticate();
        Log.v("login", jsonObject.toString());
        try {

            sessionID = jsonObject.getJSONObject("result").getString("sessionId");

        }catch (Exception e){
            Log.e("sessionID", e.toString());
        }


    }

    @Override
    public boolean areLoginDataValid(String username, String password){
        return true;
    }

    @Override
    public void logout() {

    }

    @Override
    public FieldOfStudy[] getFieldsOfStudy(Filter[] filter) {
        return new FieldOfStudy[0];
    }

    @Override
    public Filter[] getFilters() {
        return new Filter[0];
    }

    private Event[] getCalendar(Date[] dateRange, FieldOfStudy fieldOfStudy) {
        return null;
    }

}
