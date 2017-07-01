package com.wua.mc.webuntisapp.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class WebUntisClient implements iWebUntisClient {

	private String username = "";
	private String password = "";
	private String school = "?school=";

	private final String webPath = "https://poly.webuntis.com/WebUntis/jsonrpc.do";
	private JSONObject standardJSONData;
	private JSONObject emptyParamsJSON;

	/**
	 * The only valid constructor.
	 *
	 * @param username The username of a user
	 * @param password The password of a user
	 * @param school The school. Perhaps could it be used for extending the app to other Unis.
	 */
	public WebUntisClient(String username, String password, String school) {
		this.username = username;
		this.password = password;
		this.school += school;

		try {
			standardJSONData = new JSONObject();
			emptyParamsJSON = new JSONObject();

			standardJSONData.put("id", "ID");
			standardJSONData.put("jsonrpc", "2.0");
		} catch (JSONException error) {
			Log.i("JSONException", error.toString());
		}
	}

	/**
	 * The default constructor made private to avoid its usage.
	 */
	private WebUntisClient(){}

	/**
	 * Use this to authenticate your personal username and password. Only used to check after pressing
	 * login. As soon as your data are authenticated, the internal login data are used, and you need
	 * to do nothing with sessions.
	 *
	 * @return The server response as JSONObject, depending on the validity of the data
	 */
	@Override
	public JSONObject authenticate(){return getData("authenticate", emptyParamsJSON);}

	/**
	 * Gets the teacher data.
	 *
	 * @return The server response as a JSONObject.
	 */
	@Override
	public JSONObject getTeachers() {
		return getData("getTeachers", emptyParamsJSON);
	}

	/**
	 * Gets all the 'Field of Study'-Semester combinations. In the website it is under the Semester
	 * drop-down menu, and in the WebUntis API it is called getKlassen.
	 *
	 * @return The server response as a JSONObject.
	 */
	@Override
	public JSONObject getClasses() {
		return getData("getKlassen", emptyParamsJSON);
	}

	/**
	 * Gets the Courses (aka Subjects in the WebUntis API).
	 *
	 * @return  The server response as a JSONObject.
	 */
	@Override
	public JSONObject getCourses() {
		return getData("getSubjects", emptyParamsJSON);
	}

	/**
	 * Gets the room data.
	 *
	 * @return The server response as a JSONObject.
	 */
	@Override
	public JSONObject getRooms() {
		return getData("getRooms", emptyParamsJSON);
	}

	/**
	 * Gets the Faculty, Exams, etc filters. In the website these are the results under
	 * the "Fakultät" dropdown, and in the WebUntis API known as getDepartments.
	 *
	 * @return The server response as a JSONObject.
	 */
	@Override
	public JSONObject getFilters() {
		return getData("getDepartments", emptyParamsJSON);
	}

	/**
	 * Gets the holidays.
	 *
	 * @return The server response as a JSONObject.
	 */
	@Override
	public JSONObject getHolidays() {
		return getData("getHolidays", emptyParamsJSON);
	}

	/**
	 * Gets the time-blocks used. In RT -> 8:00 - 9:30 / 9:45 - 11:15 etc
	 *
	 * @return The server response as a JSONObject.
	 */
	@Override
	public JSONObject getTimegridUnits() {
		return getData("getTimegridUnits", emptyParamsJSON);
	}

	/**
	 * Gets color-code information about types of lessons. Known as getStatusData in the WebUntis API.
	 *
	 * @return The server response as a JSONObject.
	 */
	@Override
	public JSONObject getStatusInformation() {
		return getData("getStatusData", emptyParamsJSON);
	}

	/**
	 * Gets the current school year
	 *
	 * @return The server response as a JSONObject.
	 */
	@Override
	public JSONObject getCurrentSchoolYear() {
		return getData("getCurrentSchoolyear", emptyParamsJSON);
	}

	/**
	 * Gets the latest time something was written in the WebUntis database.
	 *
	 * @return The server response as a JSONObject.
	 */
	@Override
	public JSONObject getLatestImportTime() {
		return getData("getLatestImportTime", emptyParamsJSON);
	}

	/**
	 * Gets the timetable for an element.
	 *
	 * @param id		The id of the element
	 * @param type 		The type of the element. Please insert one of the following numbers as a
	 *                  String  1 = Class | 2 = Teacher | 3 = Course | 4 = Room | 5 = Student
	 * @param startDate optional, default: actual date
	 * @param endDate 	optional, default: actual date
	 *
	 * @return The server response as a JSONObject.
	 */
	@Override
	public JSONObject getTimetableForElement(String id, ElementType type, String startDate, String endDate) {
		JSONObject params = new JSONObject();

		try {
			params.put("id", id);
			params.put("type", "" + (type.ordinal() + 1));
			params.put("startDate", startDate);
			params.put("endDate", endDate);
		}
		catch (JSONException e){
			Log.i("JSONException", e.toString());
		}

		return getData("getTimetable", params);
	}

	/**
	 * Gets data from the WebUntis database depending on methodName and the params given.
	 *
	 * @param methodName The name of the WebUntis API method
	 * @param params Parameters depending on the WebUntis API method
	 *
	 * @return The server response as a JSONObject.
	 */
	private JSONObject getData(String methodName, JSONObject params){
		JSONObject response = null;
		try {
			WebUntisTask wut = new WebUntisTask(this, methodName, params);
			Thread t = new Thread(wut);
			t.start();
			t.join();
			response = wut.getResponse();
		}
		catch (Exception e){
			Log.i("Exception", e.toString());
		}
		return response;
	}

	/**
	 * Makes an https POST-request sending JSON data in the process.
	 *
	 * @param url The target url
	 * @param jsonData The json parameter to be sent
	 *
	 * @return The server response as a JSONObject.
	 */
	private JSONObject httpsPostJSON(URL url, JSONObject jsonData){
		HttpsURLConnection connection = null;
		String line;
		String jsonString = "";
		JSONObject response = null;
		try {
			connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Cache-Control", "no-cache");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			DataOutputStream outputPost = new DataOutputStream(connection.getOutputStream());
			outputPost.writeBytes(jsonData.toString());

			outputPost.flush();
			outputPost.close();

			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while((line = br.readLine()) != null){
				jsonString += line;
			}
			br.close();
		} catch (SocketTimeoutException error) {
			Log.i("SocketTimeoutException", error.toString());
		} catch (IOException error) {
			Log.i("IOException", error.toString());
		} finally {
			if (connection != null) {
				connection.disconnect();
				try {
					response = new JSONObject(jsonString);
				}
				catch(JSONException error){
					Log.i("JSONException", error.toString());
				}
			}
		}
		return response;
	}

	/**
	 * This private class is responsible for creating a session with the WebUntis server, using
	 * the requested method, and then ending the session. It runs in a separate thread.
	 */
	private class WebUntisTask implements Runnable {

		private String method = "";
		private JSONObject params;
		private JSONObject response;
		private WebUntisClient wuc;

		WebUntisTask(WebUntisClient wuc, String method, JSONObject params){
			this.method = method;
			this.params = params;
			this.wuc = wuc;
		}

		@Override
		public void run() {
			JSONObject response = null;
			JSONObject session = null;
			JSONObject jsonData;
			String sessionId;
			try {
				session = wuc.startSession();
				sessionId = session.getJSONObject("result").getString("sessionId");

				if(this.method.equals("authenticate")){
					response = session;
					wuc.endSession(sessionId);
				}
				else{
					URL url = new URL(webPath + ";jsessionid=" + sessionId + school);
					response = new JSONObject();
					jsonData = new JSONObject(standardJSONData.toString());
					jsonData.put("method", this.method);
					jsonData.put("params", params);

					response.put("session", sessionId);
					response.put("response", wuc.httpsPostJSON(url, jsonData));
					wuc.endSession(sessionId);
				}
			}
			catch (JSONException error){
				this.response = session;
			}
			catch (MalformedURLException error){
				//Log.i("MalformedURLException", error.toString());
			}
			finally {
				this.response = response;
			}
		}

		JSONObject getResponse(){
			return this.response;
		}
	}

	/**
	 * Starts a session with the WebUntis server.
	 *
	 * @return The server response as a JSONObject.
	 */
	private JSONObject startSession(){
		URL url = null;
		JSONObject authenticationData = null;
		try {
			authenticationData = new JSONObject(standardJSONData.toString());
			JSONObject params = new JSONObject();
			params.put("user", username);
			params.put("password", password);
			params.put("client", "RU-App");

			authenticationData.put("method", "authenticate");
			authenticationData.put("params", params);

			url = new URL(webPath + school);
		}
		catch (MalformedURLException error){
			Log.i("MalformedURLException", error.toString());
		}
		catch (JSONException error){
			Log.i("JSONException", error.toString());
		}
		return httpsPostJSON(url, authenticationData);
	}

	/**
	 * Ends a session with the WebUntis server.
	 *
	 * @param sessionID The id of the session to be ended
	 *
	 * @return The server response as a JSONObject.
	 */
	private JSONObject endSession(String sessionID){
		JSONObject jsonData = null;
		URL url = null;
		try {
			jsonData = new JSONObject(standardJSONData.toString());
			jsonData.put("method", "logout");
			url = new URL(webPath + ";jsessionid=" + sessionID + school);
		}
		catch (JSONException e){
			Log.i("JSONException", e.toString());
		}
		catch (MalformedURLException e){
			Log.i("MalformedURLException", e.toString());
		}
		return httpsPostJSON(url, jsonData);
	}
}