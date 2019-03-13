package com.endrawan.porosgrading.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.endrawan.porosgrading.Models.Action;
import com.endrawan.porosgrading.R;
import com.endrawan.porosgrading.ViewHolders.ActionViewHolder;

import java.util.List;

public class ActionsAdapter extends RecyclerView.Adapter<ActionViewHolder> {
    private List<Action> actions;

    public ActionsAdapter(List<Action> actions) {
        this.actions = actions;
    }

    @NonNull
    @Override
    public ActionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new
                ActionViewHolder(LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_linear_action,
                        viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ActionViewHolder holder, int i) {
        Action action = actions.get(i);
        holder.mTitle.setText(action.getName());
        holder.mType.setText(action.getActivity_type());
        holder.mDescription.setText(action.getDescription());
    }

    @Override
    public int getItemCount() {
        return actions.size();
    }
}
