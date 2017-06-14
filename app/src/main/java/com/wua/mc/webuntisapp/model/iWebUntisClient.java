package com.wua.mc.webuntisapp.model;

import org.json.JSONObject;

public interface iWebUntisClient {

	JSONObject authenticate(); //todo classdiagram

	JSONObject getTeachers();

	JSONObject getClasses();

	JSONObject getCourses();

	JSONObject getRooms();

	JSONObject getFilters();

	JSONObject getHolidays();

	JSONObject getTimegridUnits();

	JSONObject getStatusInformation();

	JSONObject getCurrentSchoolYear();

	JSONObject getLatestImportTime();

	JSONObject getTimetableForElement(String id, ElementType type, String startDate, String endDate); //todo classdiagram

}