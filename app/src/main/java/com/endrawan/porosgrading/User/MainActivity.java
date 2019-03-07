package com.endrawan.porosgrading.User;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.endrawan.porosgrading.Adapters.ActionsAdapter;
import com.endrawan.porosgrading.Models.Action;
import com.endrawan.porosgrading.R;
import com.endrawan.porosgrading.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import Components.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements
        EventListener<QuerySnapshot> {

    private final String TAG = "MainActivity";

    private List<Action> actions = new ArrayList<>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = mAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ActionsAdapter adapter = new ActionsAdapter(actions);

    private FloatingActionButton fab;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.add);
        progressBar = findViewById(R.id.progressBar);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);

        db.collection("activities")
                .whereEqualTo("user_uid", firebaseUser.getUid())
                .addSnapshotListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, InputActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signOut:
                mAuth.signOut();
                updateUI();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
        if (e != null) {
            Log.w(TAG, "Listen failed.", e);
            return;
        }

        for (QueryDocumentSnapshot doc : value) {
            Action action = doc.toObject(Action.class);
            actions.add(action);
        }
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }


    private void updateUI() {
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
    }
}
