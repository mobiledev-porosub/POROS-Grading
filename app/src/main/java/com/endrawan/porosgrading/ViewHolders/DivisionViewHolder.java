package com.endrawan.porosgrading.ViewHolders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.endrawan.porosgrading.Models.Division;
import com.endrawan.porosgrading.R;

public class DivisionViewHolder extends RecyclerView.ViewHolder {
    public ImageView mImage;
    public TextView mName;
    public FrameLayout mRoot;

    public DivisionViewHolder(@NonNull View itemView) {
        super(itemView);
        mImage = itemView.findViewById(R.id.image);
        mName = itemView.findViewById(R.id.name);
        mRoot = itemView.findViewById(R.id.root);
    }

    public void changeToSelected(Context context, Division division) {
        mImage.setImageDrawable(ContextCompat.getDrawable(context, division.getImage_white()));
        mRoot.setBackground(ContextCompat.getDrawable(context, R.drawable.division_background_pressed));
        mName.setTextColor(ContextCompat.getColor(context, R.color.white));
    }

    public void changeToNormal(Context context, Division division) {
        mImage.setImageDrawable(ContextCompat.getDrawable(context, division.getImage_primary()));
        mRoot.setBackground(ContextCompat.getDrawable(context, R.drawable.division_background_normal));
        mName.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
    }
}
