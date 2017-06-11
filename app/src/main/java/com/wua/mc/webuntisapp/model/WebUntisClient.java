package com.wua.mc.webuntisapp.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class WebUntisClient implements iWebUntisClient {

	private String username = "";
	private String password = "";
	private String school = "?school=";

	private final String webPath = "https://poly.webuntis.com/WebUntis/jsonrpc.do";
	private JSONObject standardJSONData;
	private JSONObject emptyParamsJSON;

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

	@Override
	public JSONObject authenticate(){return getData("authenticate", emptyParamsJSON);}

	@Override
	public JSONObject getTeachers() {
		return getData("getTeachers", emptyParamsJSON);
	}

	@Override
	public JSONObject getClasses() {
		return getData("getKlassen", emptyParamsJSON);
	}

	@Override
	public JSONObject getCourses() {
		return getData("getSubjects", emptyParamsJSON);
	}

	@Override
	public JSONObject getRooms() {
		return getData("getRooms", emptyParamsJSON);
	}

	@Override
	public JSONObject getFilters() {
		return getData("getDepartments", emptyParamsJSON);
	}

	@Override
	public JSONObject getHolidays() {
		return getData("getHolidays", emptyParamsJSON);
	}

	@Override
	public JSONObject getTimegridUnits() {
		return getData("getTimegridUnits", emptyParamsJSON);
	}

	@Override
	public JSONObject getStatusInformation() {
		return getData("getStatusData", emptyParamsJSON);
	}

	@Override
	public JSONObject getCurrentSchoolYear() {
		return getData("getCurrentSchoolyear", emptyParamsJSON);
	}

	@Override
	public JSONObject getLatestImportTime() {
		return getData("getLatestImportTime", emptyParamsJSON);
	}

	@Override
	public JSONObject getTimetableForElement(String id, String type, String startDate, String endDate) {
		JSONObject params = new JSONObject();

		try {
			params.put("id", id);
			params.put("type", type);
			params.put("startDate", startDate);
			params.put("endDate", endDate);
		}
		catch (JSONException e){
			Log.i("JSONException", e.toString());
		}

		return getData("getTimetable", params);
	}

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

	private JSONObject httpPostJSON(URL url, JSONObject jsonData){
		HttpURLConnection connection = null;
		String line;
		String jsonString = "";
		JSONObject response = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
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
			JSONObject session;
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
					response.put("response", wuc.httpPostJSON(url, jsonData));
					wuc.endSession(sessionId);
				}
			}
			catch (JSONException error){
				Log.i("JSONException", error.toString());
			}
			catch (MalformedURLException error){
				Log.i("MalformedURLException", error.toString());
			}
			finally {
				this.response = response;
			}
		}

		JSONObject getResponse(){
			return this.response;
		}
	}

	private JSONObject startSession(){
		URL url = null;
		JSONObject authenticationData = null;
		try {
			authenticationData = new JSONObject(standardJSONData.toString());
			JSONObject params = new JSONObject();
			params.put("user", username);
			params.put("password", password);
			params.put("client", "CLIENT");

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
		return httpPostJSON(url, authenticationData);
	}

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
		return httpPostJSON(url, jsonData);
	}
}