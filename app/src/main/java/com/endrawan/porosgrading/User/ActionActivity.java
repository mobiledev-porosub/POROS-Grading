package com.endrawan.porosgrading.User;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.endrawan.porosgrading.Adapters.ActionsAdapter;
import com.endrawan.porosgrading.Config;
import com.endrawan.porosgrading.Models.Action;
import com.endrawan.porosgrading.R;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import Components.AppCompatActivity;

public class ActionActivity extends AppCompatActivity implements
        EventListener<QuerySnapshot> {

    private final String TAG = "ActionActivity";

    private List<Action> actions = new ArrayList<>();
    private ActionsAdapter adapter = new ActionsAdapter(actions, ActionsAdapter.USER_STYLE);
    private int totalPoints = 0;

    private ProgressBar progressBar;
    private Toolbar toolbar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);

        user = getUserFromSP();

        progressBar = findViewById(R.id.progressBar);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);

        db.collection(Config.DB_ACTIVITIES)
                .whereEqualTo("user_uid", firebaseUser.getUid())
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener(this);

        setSupportActionBar(toolbar);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
        if (e != null) {
            Log.w(TAG, "Listen failed.", e);
            return;
        }

        actions.clear();
        totalPoints = 0;
        for (QueryDocumentSnapshot doc : value) {
            final Action action = doc.toObject(Action.class);
            actions.add(action);
            if (action.getStatus() == Action.STATUS_ACCEPTED)
                totalPoints += action.getPoints();
        }
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }
}
