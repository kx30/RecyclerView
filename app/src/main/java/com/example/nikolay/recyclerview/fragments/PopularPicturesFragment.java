package com.example.nikolay.recyclerview.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PopularPicturesFragment extends Fragment {

    private static final String TAG = "PopularPicturesFragment";
    private static final String URL_CONNECTION = "http://gallery.dev.webant.ru/api/photos?popular=true&page=";

    private ProgressBar mProgressBar;

    private GridLayoutManager mManager;
    private RecyclerViewAdapter mAdapter;

    private RecyclerView mRecyclerView;
    private ArrayList<Picture> mPictures = new ArrayList<>();

    private int mCurrentPage = 1;
    private int mTotalPages, mCurrentItems, mTotalItems, mScrollOutItems;

    private Boolean mIsScrolling = false;
    private Boolean mIsDownloaded = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        new PopularPicturesFragment.MyTask().execute();

        Log.d(TAG, "onCreate: created.");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
        mManager = new GridLayoutManager(getContext(), 2);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    mIsScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mCurrentItems = mManager.getChildCount();
                mTotalItems = mManager.getItemCount();
                mScrollOutItems = mManager.findFirstVisibleItemPosition();
                Log.d(TAG, "onScrolled: scroll scrollOutItem = " + mScrollOutItems);
                Log.d(TAG, "onScrolled: scroll currentItems = " + mCurrentItems);
                Log.d(TAG, "onScrolled: scroll totalItems = " + mTotalItems);

                if (mIsScrolling && (mCurrentItems + mScrollOutItems == mTotalItems)) {
                    mIsScrolling = false;
                    fetchData();
                }
            }
        });

        initToolbar(view);

        Log.d(TAG, "onCreateView: created.");
        return view;
    }

    private void fetchData() {
        if (!mIsDownloaded) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mIsDownloaded && mCurrentPage <= mTotalPages) {
                    new MyTask().execute();
                    mCurrentPage++;
                    mProgressBar.setVisibility(View.GONE);
                }
                if (!mIsDownloaded && mCurrentPage > mTotalPages) {
                    Toast.makeText(getContext(), "All pictures have already been downloaded", Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                    mIsDownloaded = true;
                }
            }
        }, 2000);
    }

    private void initToolbar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        toolbar.setTitle("Popular");
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview");

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), mPictures);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(mManager);
    }

    private class MyTask extends AsyncTask<Void, Void, String> {
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            return resultJson = new Connection().getJSON(URL_CONNECTION, mCurrentPage);
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            mTotalPages = new Connection().parseItems(mPictures, resultJson);
            Log.d(TAG, "onPostExecute: " + strJson);
            Log.d(TAG, "onPostExecute: Total pages: " + mTotalPages);
            initRecyclerView();
        }
    }
}
