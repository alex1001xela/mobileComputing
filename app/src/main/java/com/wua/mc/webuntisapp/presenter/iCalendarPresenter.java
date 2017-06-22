package com.wua.mc.webuntisapp.presenter;

import com.wua.mc.webuntisapp.view.iCalendarView;

import java.util.GregorianCalendar;

public interface iCalendarPresenter {

    interface iCalendarDataManagement {

        Event[] getWeeklyCalendarPersonal(iCalendarView calendarView, GregorianCalendar gc);//todo class diagram

        Event[] getMonthlyCalendarPersonal(iCalendarView calendarView, GregorianCalendar gc);//todo class diagram

        void getEventInformation(String eventID);

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


        Event[] getWeeklyCalendarGlobal(iCalendarView calendarView, GregorianCalendar gc, FieldOfStudy fieldOfStudy);//todo class diagram

        Event[] getMonthlyCalendarGlobal(iCalendarView calendarView, GregorianCalendar gc, FieldOfStudy fieldOfStudy);//todo class diagram

        FieldOfStudy[] getFieldsOfStudy(Filter[] filter);

        void getEventInformation(String eventID);

        Filter[] getFilters();

        boolean areLoginDataValid(String username, String password);

        boolean isAlreadyLoggedIn();
    }


}