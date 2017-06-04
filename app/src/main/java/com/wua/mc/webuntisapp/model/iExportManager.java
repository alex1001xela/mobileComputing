package com.wua.mc.webuntisapp.model;

interface iExportManager {

	void exportEventICS(String path, String eventID);

	void exportCourseICS(String path, String courseID);

	void exportCalendarICS(String path);

}