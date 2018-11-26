package com.example.nikolay.recyclerview;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private List<Picture> mPictures;
    private Context mContext;
    private Dialog mDialog;

    public RecyclerViewAdapter(Context context, ArrayList<Picture> pictures) {
        mPictures = pictures;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.picture_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);

        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.popup);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView dialogName = (TextView) mDialog.findViewById(R.id.popup_name);
                TextView dialogDescription = (TextView) mDialog.findViewById(R.id.popup_description);
                ImageView dialogImage = (ImageView) mDialog.findViewById(R.id.popup_image);

                dialogName.setText(mPictures.get(holder.getAdapterPosition()).getName());
                dialogDescription.setText(mPictures.get(holder.getAdapterPosition()).getDescription());

                Glide.with(mContext)
                        .asBitmap()
                        .load(mPictures.get(holder.getAdapterPosition()).getUrl())
                        .into(dialogImage);

                Log.d(TAG, "onClick: " + mPictures.get(holder.getAdapterPosition()).getUrl());


                mDialog.show();
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext)
                .asBitmap()
                .load(mPictures.get(i).getUrl())
                .into(viewHolder.image);

//        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
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