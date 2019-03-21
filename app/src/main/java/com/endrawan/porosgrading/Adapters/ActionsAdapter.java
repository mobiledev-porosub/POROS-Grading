package com.endrawan.porosgrading.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.endrawan.porosgrading.Config;
import com.endrawan.porosgrading.Models.Action;
import com.endrawan.porosgrading.Models.User;
import com.endrawan.porosgrading.R;
import com.endrawan.porosgrading.ViewHolders.ActionAdminViewHolder;
import com.endrawan.porosgrading.ViewHolders.ActionViewHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ActionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Action> actions;
    private List<User> users = new ArrayList<>();
    private Context context;
    private int style;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final int ADMIN_STYLE = 1;
    public static final int USER_STYLE = 0;


    public ActionsAdapter(List<Action> actions, int style) {
        this.actions = actions;
        this.style = style;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        if (style == ADMIN_STYLE)
            return new
                    ActionAdminViewHolder(LayoutInflater
                    .from(viewGroup.getContext())
                    .inflate(R.layout.item_linear_action_admin,
                            viewGroup, false));
        else
            return new
                    ActionViewHolder(LayoutInflater
                    .from(viewGroup.getContext())
                    .inflate(R.layout.item_linear_action,
                            viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Action action = actions.get(i);
        if (style == ADMIN_STYLE) {
//            User user = users.get(i);
            setAdminVH((ActionAdminViewHolder) viewHolder, action);
        } else {
            setUserVH((ActionViewHolder) viewHolder, action);
        }
    }

    private void setAdminVH(final ActionAdminViewHolder holder, final Action action) {
        String points = "+" + action.getPoints() + " Points";
        holder.mTitle.setText(action.getName());
        holder.mType.setText(action.getActivity_type());
        holder.mDescription.setText(action.getDescription());
        holder.mPoints.setText(points);

        if (action.getStatus() != Action.STATUS_UNCONFIRMED) {
            holder.mChoice.setVisibility(View.GONE);
        } else {
            View.OnClickListener accept = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = users.get(holder.getAdapterPosition());
                    user.setTotalPoints(user.getTotalPoints() + action.getPoints());
                    holder.mChoice.setVisibility(View.GONE);
                    action.setStatus(Action.STATUS_ACCEPTED);
                    db.collection(Config.DB_ACTIVITIES).document(action.getId()).set(action);
                    db.collection(Config.DB_USERS).document(action.getUser_uid()).set(user);
                }
            };

            View.OnClickListener decline = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mChoice.setVisibility(View.GONE);
                    action.setStatus(Action.STATUS_REJECTED);
                    db.collection(Config.DB_ACTIVITIES).document(action.getId()).set(action);
                }
            };

            holder.mAccept.setOnClickListener(accept);
            holder.mImgAccept.setOnClickListener(accept);
            holder.mDecline.setOnClickListener(decline);
            holder.mImgDecline.setOnClickListener(decline);

        }

        db.collection(Config.DB_USERS).document(action.getUser_uid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            User user = doc.toObject(User.class);
                            users.add(user);
                            holder.mName.setText(user.getName());
                            if (user.getPhoto_url() != null) {
                                Glide.with(context).load(user.getPhoto_url()).into(holder.mImgUser);
                            }
                        }
                    }
                });
    }

    private void setUserVH(ActionViewHolder holder, Action action) {
        String points = "+" + action.getPoints() + " Points";
        holder.mTitle.setText(action.getName());
        holder.mType.setText(action.getActivity_type());
        holder.mDescription.setText(action.getDescription());
        holder.mPoints.setText(points);
        if (action.getStatus() == Action.STATUS_ACCEPTED) {
            holder.mImgStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_primary_24dp));
            holder.mStatus.setText("Diterima");
            holder.mStatus.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            holder.mPoints.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        } else if (action.getStatus() == Action.STATUS_REJECTED) {
            holder.mImgStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_clear_red_24dp));
            holder.mStatus.setText("Ditolak");
            holder.mStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
            holder.mPoints.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
    }

    @Override
    public int getItemCount() {
        return actions.size();
    }
}
