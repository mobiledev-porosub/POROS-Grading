package com.endrawan.porosgrading.ViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.endrawan.porosgrading.R;

public class ActionViewHolder extends RecyclerView.ViewHolder {
    public TextView mTitle, mDescription, mType, mStatus, mPoints;
    public ImageView mImgStatus;

    public ActionViewHolder(@NonNull View itemView) {
        super(itemView);
        mTitle = itemView.findViewById(R.id.title);
        mDescription = itemView.findViewById(R.id.desc);
        mType = itemView.findViewById(R.id.type);
        mStatus = itemView.findViewById(R.id.name);
        mImgStatus = itemView.findViewById(R.id.img_status);
        mPoints = itemView.findViewById(R.id.points);
    }
}
