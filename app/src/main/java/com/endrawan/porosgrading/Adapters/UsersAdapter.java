package com.endrawan.porosgrading.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.endrawan.porosgrading.Admin.DetailActivity;
import com.endrawan.porosgrading.Config;
import com.endrawan.porosgrading.Models.User;
import com.endrawan.porosgrading.R;
import com.endrawan.porosgrading.ViewHolders.UserViewHolder;
import com.google.gson.Gson;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UserViewHolder> {
    private List<User> users;
    private Context context;
    private Gson gson = new Gson();

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
        final User user = users.get(i);
        holder.mName.setText(user.getName());
        if (user.getDivision() != -1)
            holder.mDivision.setText(Config.DIVISIONS[user.getDivision()].getName());
        else
            holder.mDivision.setText("Tidak ada Divisi");

        if (user.getPhoto_url() != null)
            Glide.with(context).load(user.getPhoto_url()).into(holder.mImage);
        else
            Glide.with(context).load(R.drawable.no_profile_pic).into(holder.mImage);

        holder.mAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("user", gson.toJson(user));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
