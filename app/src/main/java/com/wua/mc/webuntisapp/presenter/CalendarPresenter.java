package com.wua.mc.webuntisapp.presenter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Date;

public class CalendarPresenter extends Service  implements iCalendarPresenter.iCalendarDataManagement, iCalendarPresenter.iCalendarWebUntis {

    private Event[] currentShownEvents;

    private Filter[] filters;

    private FieldOfStudy[] fieldsOfStudy;

    public CalendarPresenter() {
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public Event[] getWeeklyCalendar(Date date, FieldOfStudy fieldOfStudy) {
        return new Event[0];
    }

    @Override
    public Event[] getMonthlyCalendar(Date date, FieldOfStudy fieldOfStudy) {
        return new Event[0];
    }

    @Override
    public void getEventInformation(String eventID) {

    }

    @Override
    public void addCourse(String courseID) {

    }

    @Override
    public void addEvent(String eventID) {

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
        // TODO implement here
        return null;
    }

}
