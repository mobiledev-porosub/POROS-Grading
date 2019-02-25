package com.endrawan.porosgrading.ViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.endrawan.porosgrading.R;

public class ActionViewHolder extends RecyclerView.ViewHolder {
    public TextView mName, mDescription;
    public ActionViewHolder(@NonNull View itemView) {
        super(itemView);
        mName = itemView.findViewById(R.id.name);
        mDescription = itemView.findViewById(R.id.description);
    }
}
