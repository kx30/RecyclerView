package com.example.nikolay.recyclerview;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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



        Log.d(TAG, "onCreate: started");
    }


}