package com.wua.mc.webuntisapp.presenter;

import java.util.Date;

public class CalendarPresenter  implements iCalendarPresenter.iCalendarDataManagement, iCalendarPresenter.iCalendarWebUntis {

    private Event[] currentShownEvents;

    private Filter[] filters;

    private FieldOfStudy[] fieldsOfStudy;

    public CalendarPresenter() {
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