package com.endrawan.porosgrading.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.endrawan.porosgrading.Models.Action;
import com.endrawan.porosgrading.R;
import com.endrawan.porosgrading.ViewHolders.ActionViewHolder;

import java.util.List;

public class ActionsAdapter extends RecyclerView.Adapter<ActionViewHolder> {
    private List<Action> actions;
    private Context context;

    public ActionsAdapter(List<Action> actions) {
        this.actions = actions;
    }

    @NonNull
    @Override
    public ActionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        return new
                ActionViewHolder(LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_linear_action,
                        viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ActionViewHolder holder, int i) {
        Action action = actions.get(i);
        String points = "+" + action.getPoints() + " Points";
        holder.mTitle.setText(action.getName());
        holder.mType.setText(action.getActivity_type());
        holder.mDescription.setText(action.getDescription());
        holder.mPoints.setText(points);
        if (action.getStatus() == Action.STATUS_ACCEPTED) {
            holder.mImgStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_primary_24dp));
            holder.mStatus.setText("Diterima");
            holder.mStatus.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        } else if(action.getStatus() == Action.STATUS_REJECTED) {
            holder.mImgStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_clear_red_24dp));
            holder.mStatus.setText("Ditolak");
            holder.mStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
    }

    @Override
    public int getItemCount() {
        return actions.size();
    }
}
