package com.wua.mc.webuntisapp.presenter;

import com.wua.mc.webuntisapp.view.iCalendarView;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public interface iCalendarPresenter {

    interface iCalendarDataManagement {

        ArrayList<Event> getWeeklyCalendarPersonal(iCalendarView calendarView, GregorianCalendar gc);//todo class diagram

        ArrayList<Event> getMonthlyCalendarPersonal(iCalendarView calendarView, GregorianCalendar gc);//todo class diagram

        String getEventInformationPersonal(String eventID); //todo class diagram

        void addCourse(String courseID);

        void addEvent(String eventID);

        void createEvent(String name, String details, GregorianCalendar gc, long startTime, long endTime);//todo class diagram

        void deleteEvent(String eventID);

        void deleteCourse(String courseID);

        void setEventColor(String color, String eventID);

        void exportCalendarICS(String path);

        void exportCourseICS(String path, String courseID);

        void exportEventICS(String path, String eventID);

        boolean login(String username, String password);

        void logout();

    }

    interface iCalendarWebUntis {


        ArrayList<Event> getWeeklyCalendarGlobal(iCalendarView calendarView, GregorianCalendar gc, FieldOfStudy fieldOfStudy);//todo class diagram

        ArrayList<Event> getMonthlyCalendarGlobal(iCalendarView calendarView, GregorianCalendar gc, FieldOfStudy fieldOfStudy);//todo class diagram

       // FieldOfStudy[] getFieldsOfStudy(Filter[] filter);
        ArrayList<FieldOfStudy>getFieldsOfStudy(Filter filter);


        String getEventInformationGlobal(String eventID);  //todo class diagram

       // Filter[] getFilters();
        ArrayList<Filter> getFilters();

        boolean areLoginDataValid(String username, String password);

        boolean isAlreadyLoggedIn();
    }


}