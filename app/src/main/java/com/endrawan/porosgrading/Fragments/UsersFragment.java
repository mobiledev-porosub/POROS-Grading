package com.endrawan.porosgrading.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.endrawan.porosgrading.Adapters.UsersAdapter;
import com.endrawan.porosgrading.Config;
import com.endrawan.porosgrading.Models.User;
import com.endrawan.porosgrading.R;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Components.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment {

    private static final String TAG = "UsersFragment";
    private static final String USER_LEVEL_ARG = "user_level";

    private RecyclerView mRecyclerView;
    private List<User> users = new ArrayList<>();
    private UsersAdapter usersAdapter = new UsersAdapter(users);

    public UsersFragment() {
        // Required empty public constructor
    }

    public static UsersFragment newInstance(int user_level) {

        Bundle args = new Bundle();
        args.putInt(USER_LEVEL_ARG, user_level);

        UsersFragment fragment = new UsersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        int level = args.getInt(USER_LEVEL_ARG);

        db.collection(Config.DB_USERS)
                .whereEqualTo("level", level)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if (value != null) {
                            users.clear();
                            for (QueryDocumentSnapshot doc : value) {
                                User user = doc.toObject(User.class);
                                users.add(user);
                            }
                            usersAdapter.notifyDataSetChanged();
                        }
                    }
                });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = getView().findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(usersAdapter);
    }
}
