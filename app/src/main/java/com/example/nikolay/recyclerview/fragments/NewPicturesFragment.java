package com.example.nikolay.recyclerview.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nikolay.recyclerview.Picture;
import com.example.nikolay.recyclerview.R;
import com.example.nikolay.recyclerview.RecyclerViewAdapter;
import com.example.nikolay.recyclerview.connection.Connection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewPicturesFragment extends Fragment {

    private static final String TAG = "NewPicturesFragment";
    private static final String URL_CONNECTION = "http://gallery.dev.webant.ru/api/photos?new=true";

    private RecyclerView mRecyclerView;
    private ArrayList<Picture> mPictures = new ArrayList<>();

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

        initToolbar(view);

        Log.d(TAG, "onCreateView: created.");
        return view;
    }

    private void initToolbar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        toolbar.setTitle("New");
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview");

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), mPictures);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    private class MyTask extends AsyncTask<Void, Void, String> {
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            return resultJson = new Connection().getJSON(URL_CONNECTION);
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            new Connection().parseItems(mPictures, resultJson);
            initRecyclerView();
        }
    }
}
