package com.endrawan.porosgrading.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.endrawan.porosgrading.Config;
import com.endrawan.porosgrading.Models.User;
import com.endrawan.porosgrading.R;
import com.endrawan.porosgrading.ViewHolders.UserViewHolder;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UserViewHolder> {
    private List<User> users;
    private Context context;

    public UsersAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        return new
                UserViewHolder(LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_linear_user,
                        viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int i) {
        User user = users.get(i);
        holder.mName.setText(user.getName());
        holder.mDivision.setText(Config.DIVISIONS[user.getDivision()].getName());
        if (user.getPhoto_url() != null) {
            Glide.with(context).load(user.getPhoto_url()).into(holder.mImage);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
