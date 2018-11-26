package com.example.nikolay.recyclerview.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nikolay.recyclerview.R;
import com.example.nikolay.recyclerview.RecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NewPicturesFragment extends Fragment {

    private static final String TAG = "NewPicturesFragment";

    private RecyclerView mRecyclerView;
    private ArrayList<String> mImageUrls = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        new NewPicturesFragment.MyTask().execute();

        Log.d(TAG, "onCreate: created.");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_fragment_gallery, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.new_recycler_view);
        Log.d(TAG, "onCreateView: created.");
        return view;
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview");

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), mImageUrls);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    private class MyTask extends AsyncTask<Void, Void, String> {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            try {
                URL url = new URL("http://gallery.dev.webant.ru/api/photos?new=true");

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

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            // выводим целиком полученную json-строку
            Log.d(TAG, strJson);

            JSONObject dataJsonObj;

            try {
                dataJsonObj = new JSONObject(strJson);
                JSONArray data = dataJsonObj.getJSONArray("data");

                for (int i = 0; i < data.length(); i++) {
                    JSONObject images = data.getJSONObject(i);

                    JSONObject image = images.getJSONObject("image");

                    mImageUrls.add("http://gallery.dev.webant.ru/media/" + image.getString("contentUrl"));
                }
                initRecyclerView();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}