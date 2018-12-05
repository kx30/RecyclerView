package com.example.nikolay.recyclerview.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nikolay.recyclerview.Picture;
import com.example.nikolay.recyclerview.R;
import com.example.nikolay.recyclerview.RecyclerViewAdapter;
import com.example.nikolay.recyclerview.connection.Connection;

import java.util.ArrayList;

public class NewPicturesFragment extends Fragment {

    private static final String TAG = "NewPicturesFragment";
    private static final String URL_CONNECTION = "http://gallery.dev.webant.ru/api/photos?new=true&page=";

    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;

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

        new NewPicturesFragment.MyTask().execute();

        Log.d(TAG, "onCreate: created.");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mManager = new GridLayoutManager(getContext(), 2);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContent();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });


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

    private void swipeContent() {
        ImageView noConnectionImageView = (ImageView) getView().findViewById(R.id.no_connection_image_view);
        TextView noConnectionMainText = (TextView) getView().findViewById(R.id.no_connection_main_text);
        TextView noConnectionDescriptionText = (TextView) getView().findViewById(R.id.no_connection_description_text);

        if (!new Connection().hasConnection(getContext())) {
            mRecyclerView.setVisibility(View.INVISIBLE);
            Log.d(TAG, "swipeContent: no connection");

            noConnectionImageView.setVisibility(View.VISIBLE);
            noConnectionMainText.setVisibility(View.VISIBLE);
            noConnectionDescriptionText.setVisibility(View.VISIBLE);
        }
        if (new Connection().hasConnection(getContext())) {
            mRecyclerView.setVisibility(View.VISIBLE);
            Log.d(TAG, "swipeContent: connection!");

            noConnectionImageView.setVisibility(View.INVISIBLE);
            noConnectionMainText.setVisibility(View.INVISIBLE);
            noConnectionDescriptionText.setVisibility(View.INVISIBLE);
        }
    }

    private void fetchData() {
        if (!mIsDownloaded) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mIsDownloaded && mCurrentPage <= mTotalPages) {
                    swipeContent();
                    new MyTask().execute();
                    mCurrentPage++;
                    mProgressBar.setVisibility(View.GONE);
                }
                if (!mIsDownloaded && mCurrentPage > mTotalPages) {
                    swipeContent();
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
        toolbar.setTitle("New");
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recycler view");

        mAdapter = new RecyclerViewAdapter(getContext(), mPictures);
        mRecyclerView.setAdapter(mAdapter);
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
