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

import com.endrawan.porosgrading.Adapters.ActionTypesAdapter;
import com.endrawan.porosgrading.Config;
import com.endrawan.porosgrading.Models.ActionType;
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
public class TypesFragment extends Fragment {

    private static final String TAG = "TypesFragment";
    private RecyclerView mRecyclerView;
    private List<ActionType> actionTypes = new ArrayList<>();
    private ActionTypesAdapter actionTypesAdapter = new ActionTypesAdapter(actionTypes);

    public TypesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db.collection(Config.DB_ACTIVITY_TYPES)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if (value != null) {
                            actionTypes.clear();
                            for(QueryDocumentSnapshot doc : value) {
                                ActionType actionType = doc.toObject(ActionType.class);
                                actionTypes.add(actionType);
                            }
                            actionTypesAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_types, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = getView().findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(actionTypesAdapter);
    }
}
