package com.example.nikolay.recyclerview;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.nikolay.recyclerview.fragments.NewPicturesFragment;
import com.example.nikolay.recyclerview.fragments.PopularPicturesFragment;
import com.example.nikolay.recyclerview.fragments.SectionsPageAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private SectionsPageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.container);
        adapter = new SectionsPageAdapter(getSupportFragmentManager());

        adapter.addFragment(new NewPicturesFragment(), "New");
        adapter.addFragment(new PopularPicturesFragment(), "Popular");

        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        if (!hasConnection(this)) {
            ImageView noConnectionImageView = (ImageView) findViewById(R.id.no_connection_image_view);
            noConnectionImageView.setImageResource(R.drawable.no_connection);
            noConnectionImageView.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "onCreate: Connection!");
        }

        Log.d(TAG, "onCreate: started");
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

    //TODO CREATE POOL-REFRESH
    //TODO CREATE PAGINATION
    //TODO UPDATE DESIGN

}