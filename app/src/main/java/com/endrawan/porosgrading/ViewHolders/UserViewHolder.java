package com.endrawan.porosgrading.ViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.endrawan.porosgrading.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView mImage;
    public Button mAction;
    public TextView mName, mDivision;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        mImage = itemView.findViewById(R.id.image);
        mAction = itemView.findViewById(R.id.action);
        mName = itemView.findViewById(R.id.name);
        mDivision = itemView.findViewById(R.id.division);
    }
}
