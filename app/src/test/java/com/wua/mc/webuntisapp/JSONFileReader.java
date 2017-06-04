package com.wua.mc.webuntisapp;

import org.json.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

class JSONFileReader {


    static JSONObject readPersonalData(){
        String path = "personal.json";
        JSONObject result = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            StringBuilder sb = new StringBuilder();
            String line = "";

            while((line = br.readLine()) != null){
                sb.append(line);

            }
            result = new JSONObject(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}
