package com.wua.mc.webuntisapp.model;

import android.content.ContentValues;

import java.sql.Date;
import java.sql.Timestamp;


public class DataBaseObject {

    private long course_id;
    private String course_name;
    private String course_lecturer;
    private String course_color;
    private int course_untis_id;

    private long event_id;
    private String event_room;
    private int event_timestamp_start;
    private int event_timestamp_end;
    private String event_name;
    private String event_color;
    private String event_type;

    private long authenticated;

    public DataBaseObject(){}

    public DataBaseObject(String course_name, String course_lecturer, String course_color, int course_untis_id, long course_id){
        this.setCourse_id(course_id);
        this.setCourse_name(course_name);
        this.setCourse_lecturer(course_lecturer);
        this.setCourse_color(course_color);
        this.setCourse_untis_id(course_untis_id);

    }

    public DataBaseObject(String event_room, int event_timestamp_start, int event_timestamp_end, String event_name, String event_color, String event_type, long event_id){

        this.setEvent_id(event_id);
        this.setEvent_room(event_room);
        this.setEvent_timestamp_start(event_timestamp_start);
        this.setEvent_timestamp_end(event_timestamp_end);
        this.setEvent_name(event_name);
        this.setEvent_color(event_color);
        this.setEvent_type(event_type);
        //this.setCourse_id(course_id);
    }

    public DataBaseObject(long authenticated){

        this.setAuthenticated(authenticated);

    }

    public void setCourse_id(long course_id) {
        this.course_id = course_id;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public void setCourse_lecturer(String course_lecturer) {
        this.course_lecturer = course_lecturer;
    }

    public void setCourse_color(String course_color) {
        this.course_color = course_color;
    }

    public void setCourse_untis_id(int course_untis_id) {
        this.course_untis_id = course_untis_id;
    }

    public void setEvent_id(long event_id) {
        this.event_id = event_id;
    }

    public void setEvent_room(String event_room) {
        this.event_room = event_room;
    }

    public void setEvent_timestamp_start(int event_timestamp_start) {
        this.event_timestamp_start = event_timestamp_start;
    }

    public void setEvent_timestamp_end(int event_timestamp_end) {
        this.event_timestamp_end = event_timestamp_end;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public void setEvent_color(String event_color) {
        this.event_color = event_color;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public void setAuthenticated(long authenticated) {
        this.authenticated = authenticated;
    }

    public long getCourse_id() {

        return course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public String getCourse_lecturer() {
        return course_lecturer;
    }

    public String getCourse_color() {
        return course_color;
    }

    public int getCourse_untis_id() {
        return course_untis_id;
    }

    public long getEvent_id() {
        return event_id;
    }

    public String getEvent_room() {
        return event_room;
    }

    public int getEvent_timestamp_start() {
        return event_timestamp_start;
    }

    public int getEvent_timestamp_end() {
        return event_timestamp_end;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getEvent_color() {
        return event_color;
    }

    public String getEvent_type() {
        return event_type;
    }

    public long getAuthenticated() {
        return authenticated;
    }

    @Override
    public String toString() {
        String output = "";
        if(course_name!=null){
            output += course_id + " " + course_name + " " + course_lecturer + " " + course_color + " " + course_untis_id;
        }
        else if (event_name!=null){
            output += event_id + " " + event_room + " " + event_timestamp_start + " " + event_timestamp_end + " " + event_name + " " + event_color + " " + event_type;
        }
        else {
            output += authenticated;
        }
        return output;
    }


}
