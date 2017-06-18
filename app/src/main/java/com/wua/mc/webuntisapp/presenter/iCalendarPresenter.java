package com.wua.mc.webuntisapp.presenter;

import com.wua.mc.webuntisapp.view.iCalendarView;

import java.util.Date;

public interface iCalendarPresenter {

    interface iCalendarDataManagement {


        Event[] getWeeklyCalendarPersonal(iCalendarView calendarView, Date date);

        Event[] getMonthlyCalendarPersonal(iCalendarView calendarView, Date date);

        void getEventInformation(String eventID);

        void addCourse(String courseID);

        void addEvent(String eventID);

        void createEvent(String name, String details, Date date, long startTime, long endTime);

        void deleteEvent(String eventID);

        void deleteCourse(String courseID);

        void setEventColor(String color, String eventID);

        void exportCalendarICS(String path);

        void exportCourseICS(String path, String courseID);

        void exportEventICS(String path, String eventID);

        void login(String username, String password);

        void logout();

    }

    interface iCalendarWebUntis {


        Event[] getWeeklyCalendarGlobal(iCalendarView calendarView, Date date, FieldOfStudy fieldOfStudy);

        Event[] getMonthlyCalendarGlobal(iCalendarView calendarView, Date date, FieldOfStudy fieldOfStudy);

        FieldOfStudy[] getFieldsOfStudy(Filter[] filter);

        void getEventInformation(String eventID);

        Filter[] getFilters();

        boolean areLoginDataValid(String username, String password);
    }


}