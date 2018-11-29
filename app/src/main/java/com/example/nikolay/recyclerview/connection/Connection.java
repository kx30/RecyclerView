package com.example.nikolay.recyclerview.connection;

import android.util.Log;

import com.example.nikolay.recyclerview.Picture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Connection {

    public String getJSON(String connectionURL) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        try {
            URL url = new URL(connectionURL);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            resultJson = buffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultJson;
    }

    public void parseItems(ArrayList<Picture> pictures, String json) {

        JSONObject dataJsonObj;

        try {
            dataJsonObj = new JSONObject(json);
            JSONArray data = dataJsonObj.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                JSONObject images = data.getJSONObject(i);

                JSONObject image = images.getJSONObject("image");

                pictures.add(new Picture(
                        images.getString("name"),
                        "http://gallery.dev.webant.ru/media/" + image.getString("contentUrl"),
                        images.getString("description")
                ));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
