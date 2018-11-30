package com.example.nikolay.recyclerview.popup;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nikolay.recyclerview.MainActivity;
import com.example.nikolay.recyclerview.R;

public class PopupActivity extends AppCompatActivity {

    private static final String TAG = "PopupActivity";

    private ImageView mImageView;
    private TextView mNameText, mDescriptionText;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        mImageView = findViewById(R.id.popup_image_view);
        mNameText = findViewById(R.id.popup_name_text);
        mDescriptionText = findViewById(R.id.popup_description_text);

        initToolbar();
        getExtras();

        MainActivity mainActivity = new MainActivity();

        Log.d(TAG, "onCreate: started");

    }

//    private void setupViewPager(final Context context) {
//        final TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabs);
//
//        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.new_icon).setText("New"));
//        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.fire_icon).setText("Popular"));
//
//        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                finish();
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//                finish();
//            }
//        });
//    }

    private void getExtras() {
        String url = "", name = "", description = "";

        try {
            Bundle arguments = getIntent().getExtras();
            if (arguments != null) {

                url += arguments.get("Url").toString();

                Glide.with(this)
                        .asBitmap()
                        .load(url)
                        .into(mImageView);

                name = arguments.get("Name").toString();
                description = arguments.get("Description").toString();
                Log.d(TAG, "onCreate: " + url);
            }
            mNameText.setText(name);
            mDescriptionText.setText(description);
            mImageView.setImageURI(Uri.parse(url));
        } catch (Exception e) {
            Toast.makeText(this, "Error in Popup Activity", Toast.LENGTH_SHORT).show();
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
