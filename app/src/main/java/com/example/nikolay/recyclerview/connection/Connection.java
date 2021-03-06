package com.example.nikolay.recyclerview.connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;

import com.example.nikolay.recyclerview.Picture;
import com.example.nikolay.recyclerview.R;

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

    private int mTotalPages;

    public String getJSON(String connectionURL, int currentPage) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        try {
            URL url = new URL(connectionURL + currentPage + "&limit=10");

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

    public int parseItems(ArrayList<Picture> pictures, String json) {

        JSONObject dataJsonObj;

        try {
            dataJsonObj = new JSONObject(json);
            JSONArray data = dataJsonObj.getJSONArray("data");

            mTotalPages = dataJsonObj.getInt("countOfPages");

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
        return mTotalPages;
    }

    public static boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
