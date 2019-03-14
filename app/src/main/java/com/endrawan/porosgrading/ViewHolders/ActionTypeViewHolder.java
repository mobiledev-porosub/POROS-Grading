package com.endrawan.porosgrading.ViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.endrawan.porosgrading.R;

public class ActionTypeViewHolder extends RecyclerView.ViewHolder {
    public TextView mName, mPoints;

    public ActionTypeViewHolder(@NonNull View itemView) {
        super(itemView);
        mName = itemView.findViewById(R.id.name);
        mPoints = itemView.findViewById(R.id.points);
    }
}
