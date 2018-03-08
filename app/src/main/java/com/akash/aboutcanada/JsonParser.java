package com.akash.aboutcanada;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by akash on 8/03/18.
 */

public class JsonParser {
    //static InputStream inputStream=null;
    static JSONObject jsonObject=null;
    static String jsonString="";
    public JSONObject getJsonObjectFromURL(String url){
        //make HTTP request
        HttpURLConnection connection=null;
        try {
            URL link=new URL(url);
            connection=(HttpURLConnection) link.openConnection();
            connection.connect();
            //reading json lines
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuilder=new StringBuilder();
            String line=null;
            while ((line=bufferedReader.readLine())!=null){
                stringBuilder.append(line+" \n ");
            }


            bufferedReader.close();
            jsonString=stringBuilder.toString();

            //return stringBuilder.toString();

        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if (connection != null){
                try {
                    connection.disconnect();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        //parsing jsonString to a JSonObject
        try {
            jsonObject=new JSONObject(jsonString);


        } catch (JSONException e){
            Log.e("JSONPARSER","Error parsing data "+ e.toString());

        }
        //returning json object
        return jsonObject;
    }
}
