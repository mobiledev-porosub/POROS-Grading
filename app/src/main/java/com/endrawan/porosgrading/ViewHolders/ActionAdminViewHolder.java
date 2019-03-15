package com.endrawan.porosgrading.ViewHolders;

import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.endrawan.porosgrading.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActionAdminViewHolder extends RecyclerView.ViewHolder {
    public TextView mTitle, mDescription, mType, mPoints, mName, mDecline, mAccept;
    public CircleImageView mImgUser;
    public ImageView mImgDecline, mImgAccept;
    public Group mChoice;

    public ActionAdminViewHolder(@NonNull View itemView) {
        super(itemView);
        mTitle = itemView.findViewById(R.id.title);
        mDescription = itemView.findViewById(R.id.desc);
        mType = itemView.findViewById(R.id.type);
        mPoints = itemView.findViewById(R.id.points);
        mName = itemView.findViewById(R.id.name);
        mDecline = itemView.findViewById(R.id.decline);
        mImgDecline = itemView.findViewById(R.id.img_decline);
        mAccept = itemView.findViewById(R.id.accept);
        mImgAccept = itemView.findViewById(R.id.img_accept);
        mImgUser = itemView.findViewById(R.id.img_user);
        mChoice = itemView.findViewById(R.id.choice);
    }
}
