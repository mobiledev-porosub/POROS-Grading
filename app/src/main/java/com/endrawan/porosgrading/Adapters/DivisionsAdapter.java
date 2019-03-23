package com.endrawan.porosgrading.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.endrawan.porosgrading.Models.Division;
import com.endrawan.porosgrading.R;
import com.endrawan.porosgrading.ViewHolders.DivisionViewHolder;

import java.util.List;

public class DivisionsAdapter extends RecyclerView.Adapter<DivisionViewHolder> {
    private List<Division> divisions;
    private Context context;
    private divisionActions actions;
    private int oldPosition;

    public DivisionsAdapter(Context context, List<Division> divisions, divisionActions actions) {
        this.divisions = divisions;
        this.context = context;
        this.actions = actions;
    }

    public DivisionsAdapter(Context context, List<Division> divisions, divisionActions actions, int selected) {
        this.divisions = divisions;
        this.context = context;
        this.actions = actions;
        oldPosition = selected;
    }

    @NonNull
    @Override
    public DivisionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new
                DivisionViewHolder(LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_grid_division,
                        viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final DivisionViewHolder holder, int i) {
        final Division division = divisions.get(i);
        holder.mName.setText(division.getName());
        holder.mImage.setImageDrawable(ContextCompat.getDrawable(context, division.getImage_primary()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actions.clicked(division, oldPosition);
                oldPosition = holder.getAdapterPosition();
                holder.changeToSelected(context, division);
            }
        });
        if (i == oldPosition) {
            holder.changeToSelected(context, division);
        }
    }

    @Override
    public int getItemCount() {
        return divisions.size();
    }

    public interface divisionActions {
        void clicked(Division newDivision, int oldPosition);
    }
}
