package com.wua.mc.webuntisapp.model;

import android.content.ContentValues;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by Manny on 13.06.2017.
 */

public class DataBaseObject {

    private String id_course;
    private String room;
    private String course_color;
    private String name;
    private String lecturer_name;
    private int semester;
    private String id_private_calendar;
    private String course_name;
    private String course_lecturer;
    private int course_semester;
    private String room_course;
    private Timestamp event_time;
    private Date event_date;
    private String id_lab;
    private String lab_room;
    private Timestamp lab_time;
    private Date lab_date;


    public DataBaseObject(String id_course,String name, String lecturer_name){

        this.setId_course(id_course);
        this.setRoom(room);
        this.setCourse_color(course_color);
        this.setName(name);
        this.setLecturer_name(lecturer_name);
        this.setSemester(semester);
        this.setId_private_calendar(id_private_calendar);
        this.setCourse_name(course_name);
        this.setRoom_course(room_course);
        this.setEvent_time(event_time);
        this.setEvent_date(event_date);
        this.setId_lab(id_lab);
        this.setLab_room(lab_room);
        this.setLab_time(lab_time);
        this.setLab_date(lab_date);
    }

    public Timestamp getEvent_time() {
        return event_time;
    }

    public Date getEvent_date() {
        return event_date;
    }

    public String getId_lab() {
        return id_lab;
    }

    public String getLab_room() {
        return lab_room;
    }

    public Timestamp getLab_time() {
        return lab_time;
    }

    public Date getLab_date() {
        return lab_date;
    }


    public String getId_course() {
        return id_course;
    }

    public String getRoom() {
        return room;
    }

    public String getCourse_color() {
        return course_color;
    }

    public String getName() {
        return name;
    }

    public String getLecturer_name() {
        return lecturer_name;
    }

    public int getSemester() {
        return semester;
    }

    public String getId_private_calendar() {
        return id_private_calendar;
    }

    public String getCourse_name() {
        return course_name;
    }

    public String getCourse_lecturer() {
        return course_lecturer;
    }

    public int getCourse_semester() {
        return course_semester;
    }

    public String getRoom_course() {
        return room_course;
    }


    public void setId_course(String id_course) {

        this.id_course = id_course;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setCourse_color(String course_color) {
        this.course_color = course_color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLecturer_name(String lecturer_name) {
        this.lecturer_name = lecturer_name;
    }


    public void setSemester(int semester) {
        this.semester = semester;
    }

    public void setId_private_calendar(String id_private_calendar) {
        this.id_private_calendar = id_private_calendar;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public void setCourse_lecturer(String course_lecturer) {
        this.course_lecturer = course_lecturer;
    }

    public void setCourse_semester(int course_semester) {
        this.course_semester = course_semester;
    }

    public void setRoom_course(String room_course) {
        this.room_course = room_course;
    }


    public void setEvent_time(Timestamp event_time) {

        this.event_time = event_time;
    }

    public void setEvent_date(Date event_date) {
        this.event_date = event_date;
    }

    public void setId_lab(String id_lab) {
        this.id_lab = id_lab;
    }

    public void setLab_room(String lab_room) {
        this.lab_room = lab_room;
    }

    public void setLab_time(Timestamp lab_time) {
        this.lab_time = lab_time;
    }

    public void setLab_date(Date lab_date) {
        this.lab_date = lab_date;
    }

    @Override
    public String toString() {
        String output = id_course + " " + name+ " " + lecturer_name ;
        return output;
    }


}
