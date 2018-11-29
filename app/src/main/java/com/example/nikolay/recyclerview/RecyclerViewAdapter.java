package com.example.nikolay.recyclerview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private List<Picture> mPictures;
    private Context mContext;

    public RecyclerViewAdapter(Context context, ArrayList<Picture> pictures) {
        mPictures = pictures;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.picture_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext)
                .asBitmap()
                .load(mPictures.get(i).getUrl())
                .into(viewHolder.image);


        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Dialog mDialog = new Dialog(mContext);
                mDialog.setContentView(R.layout.activity_popup);

                mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                        Dialog dialog = (Dialog) dialogInterface;
                        TextView dialogName = (TextView) dialog.findViewById(R.id.popup_name);
                        TextView dialogDescription = (TextView) dialog.findViewById(R.id.popup_description);
                        CircleImageView dialogImage = (CircleImageView) dialog.findViewById(R.id.popup_image);

                        Glide.with(mContext)
                                .asBitmap()
                                .load(mPictures.get(i).getUrl())
                                .into(dialogImage);
                        dialogName.setText(mPictures.get(i).getName());
                        dialogDescription.setText(mPictures.get(i).getDescription());
                    }
                });

                Log.d(TAG, "onClick: " + mPictures.get(i).getUrl());


                mDialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPictures.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView image;
        private RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

}