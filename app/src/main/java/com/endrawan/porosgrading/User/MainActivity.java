package com.endrawan.porosgrading.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.endrawan.porosgrading.Adapters.ActionsAdapter;
import com.endrawan.porosgrading.Config;
import com.endrawan.porosgrading.Models.Action;
import com.endrawan.porosgrading.ProfileActivity;
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
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements
        EventListener<QuerySnapshot> {

    private final String TAG = "MainActivity";

    private final int RC_PROFILE = 1;

    public static final String EXTRAS_POINTS = "points";

    private List<Action> actions = new ArrayList<>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = mAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ActionsAdapter adapter = new ActionsAdapter(actions, ActionsAdapter.USER_STYLE);
    private int totalPoints = 0;

    private FloatingActionButton fab;
    private CircleImageView mImage;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = getUserFromSP();

        fab = findViewById(R.id.add);
        progressBar = findViewById(R.id.progressBar);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        mImage = findViewById(R.id.image);

        db.collection(Config.DB_ACTIVITIES)
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

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra(EXTRAS_POINTS, totalPoints);
                startActivityForResult(intent, RC_PROFILE);
            }
        });

        if (user.getPhoto_url() != null)
            Glide.with(this).load(user.getPhoto_url()).into(mImage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_PROFILE) {
            switch (resultCode) {
                case ProfileActivity.RESULT_DELETE:
                    toast("DELETE");
                    break;
                case ProfileActivity.RESULT_EDIT:
                    startActivity(new Intent(this, EditActivity.class));
                    break;
                case ProfileActivity.RESULT_LOGOUT:
                    logout();
                    updateUI();
                    break;
            }
        }
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


    private void updateUI() {
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
    }
}
