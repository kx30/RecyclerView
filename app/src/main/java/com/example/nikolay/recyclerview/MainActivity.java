package com.example.nikolay.recyclerview;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nikolay.recyclerview.connection.Connection;
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

        setupViewPager(this);

        Log.d(TAG, "onCreate: started");
    }

    private void setupViewPager(final Context context) {
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.container);
        adapter = new SectionsPageAdapter(getSupportFragmentManager());

        adapter.addFragment(new NewPicturesFragment(), "New");
        adapter.addFragment(new PopularPicturesFragment(), "Popular");

        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.getTabAt(0).setIcon(R.drawable.new_icon);
        mTabLayout.getTabAt(1).setIcon(R.drawable.fire_icon);
    }
}