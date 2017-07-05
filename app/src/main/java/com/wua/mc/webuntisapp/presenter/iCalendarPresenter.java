package com.wua.mc.webuntisapp.presenter;

import com.wua.mc.webuntisapp.view.iCalendarView;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

public interface iCalendarPresenter {

    interface iCalendarDataManagement {

        ArrayList<Event> getWeeklyCalendarPersonal(iCalendarView calendarView, GregorianCalendar gc);//todo class diagram

        ArrayList<Event> getMonthlyCalendarPersonal(iCalendarView calendarView, GregorianCalendar gc);//todo class diagram

        String getEventInformationPersonal(String eventID); //todo class diagram

        void addCourse(String untisEventID);

        void addEvent(String untisEventID);

        void createEvent(String name, String details, GregorianCalendar gc, long startTime, long endTime);//todo class diagram

        void deleteEvent(String eventID);

        ArrayList<String> deleteCourse(String eventID);

        void setEventColor(String color, String eventID);

        void exportCalendarICS(String path);

        void exportCourseICS(String path, String courseID);

        void exportEventICS(String path, String eventID);

        boolean login(String username, String password);

        void logout();

        void setSelectedFieldOfStudy(FieldOfStudy selectedFieldOfStudy);

        FieldOfStudy getSelectedFieldOfStudy();

        FieldOfStudy findChosenFieldOfSTudy(String selectedname);

    }

    interface iCalendarWebUntis {


        ArrayList<Event> getWeeklyCalendarGlobal(iCalendarView calendarView, GregorianCalendar gc, FieldOfStudy fieldOfStudy);//todo class diagram

        ArrayList<Event> getMonthlyCalendarGlobal(iCalendarView calendarView, GregorianCalendar gc, FieldOfStudy fieldOfStudy);//todo class diagram

       // FieldOfStudy[] getFieldsOfStudy(Filter[] filter);
        ArrayList<FieldOfStudy>getFieldsOfStudy(Filter filter);


        String getEventInformationGlobal(String eventID);  //todo class diagram

       // Filter[] getFilters();
        ArrayList<Filter> getFilters();
        HashMap<String, Course> getCourses();

        boolean areLoginDataValid(String username, String password);

        boolean isAlreadyLoggedIn();

        void resetCurrentShownGlobalEvents();

        HashMap<Integer,Integer> getEventsPerMonths(int year, int month);
    }


}