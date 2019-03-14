package com.endrawan.porosgrading.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.endrawan.porosgrading.Models.ActionType;
import com.endrawan.porosgrading.R;
import com.endrawan.porosgrading.ViewHolders.ActionTypeViewHolder;

import java.util.List;

public class ActionTypesAdapter extends RecyclerView.Adapter<ActionTypeViewHolder> {
    private List<ActionType> actionTypes;

    public ActionTypesAdapter(List<ActionType> actionTypes) {
        this.actionTypes = actionTypes;
    }

    @NonNull
    @Override
    public ActionTypeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new
                ActionTypeViewHolder(LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_linear_action_type,
                        viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ActionTypeViewHolder holder, int i) {
        ActionType actionType = actionTypes.get(i);
        String pointsText = actionType.getPoints() + " Points";
        holder.mName.setText(actionType.getName());
        holder.mPoints.setText(pointsText);
    }

    @Override
    public int getItemCount() {
        return actionTypes.size();
    }
}
