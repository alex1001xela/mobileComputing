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
import java.util.Date;

public class WebUntisClient implements iWebUntisClient {

	private String username = "";
	private String password = "";
	private String school = "?school=";

	private final String webPath = "https://poly.webuntis.com/WebUntis/jsonrpc.do";
	private JSONObject standardJSONData;

	public WebUntisClient(String username, String password, String school) {
		this.username = username;
		this.password = password;
		this.school += school;

		try {
			standardJSONData = new JSONObject();
			standardJSONData.put("id", "ID");
			standardJSONData.put("jsonrpc", "2.0");
			standardJSONData.put("params", new JSONObject());
		} catch (JSONException error) {
			Log.i("JSONException", error.toString());
		}
	}

	@Override
	public JSONObject startSession(){return getData("authenticate", null);}

	@Override
	public JSONObject endSession(String sessionID){
		return null;
	}

	@Override
	public JSONObject getTeachers() {
		return null;
	}

	@Override
	public JSONObject getClasses() {
		return null;
	}

	@Override
	public JSONObject getCourses() {
		return null;
	}

	@Override
	public JSONObject getRooms() {
		return null;
	}

	@Override
	public JSONObject getFilters() {
		return null;
	}

	@Override
	public JSONObject getHolidays() {
		return null;
	}

	@Override
	public JSONObject getTimegridUnits() {
		return null;
	}

	@Override
	public JSONObject getStatusInformation() {
		return null;
	}

	@Override
	public JSONObject getCurrentSchoolYear() {
		return getData("getCurrentSchoolyear", null);
	}

	@Override
	public JSONObject getLatestImportTime() {
		return null;
	}

	@Override
	public JSONObject getTimetableForElement(String id, int type, Date startDate, Date endDate) {
		return null;
	}

	private JSONObject getData(String methodName, Object[] args){
		JSONObject response = null;
		try {
			WebUntisTask wut = new WebUntisTask(this, methodName, args);
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
		private Object args;
		private JSONObject response;
		private WebUntisClient wuc;

		WebUntisTask(WebUntisClient wuc, String method, Object args){
			this.method = method;
			this.args = args;
			this.wuc = wuc;
		}

		@Override
		public void run() {
			JSONObject response = null;
			JSONObject session;
			JSONObject jsonData;
			String sessionId;
			try {
				session = wuc.getSession();
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

	private JSONObject getSession(){
		URL url = null;
		JSONObject authenticationData = null;
		try {
			authenticationData = new JSONObject(standardJSONData.toString());
			JSONObject params = authenticationData.getJSONObject("params");
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
}