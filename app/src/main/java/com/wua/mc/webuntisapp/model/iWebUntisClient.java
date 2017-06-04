package com.wua.mc.webuntisapp.model;
import org.json.JSONObject;

import java.util.*;

interface iWebUntisClient {

	JSONObject startSession();

	JSONObject endSession(String sessionID);

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

	JSONObject getTimetableForElement(String id, int type, Date startDate, Date endDate);

}