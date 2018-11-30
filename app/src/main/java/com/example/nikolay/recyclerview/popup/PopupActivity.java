package com.example.nikolay.recyclerview.popup;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nikolay.recyclerview.R;

public class PopupActivity extends AppCompatActivity {

    private static final String TAG = "PopupActivity";
    
    private ImageView mImageView;
    private TextView mNameText, mDescriptionText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        mImageView = findViewById(R.id.popup_image_view);
        mNameText = findViewById(R.id.popup_name_text);
        mDescriptionText = findViewById(R.id.popup_description_text);

        String url = "", name = "", description = "";

        try {
            Bundle arguments = getIntent().getExtras();
            if(arguments != null) {

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
}
