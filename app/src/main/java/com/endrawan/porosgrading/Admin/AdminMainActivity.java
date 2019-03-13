package com.endrawan.porosgrading.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.endrawan.porosgrading.Adapters.UsersAdapter;
import com.endrawan.porosgrading.Config;
import com.endrawan.porosgrading.Models.Action;
import com.endrawan.porosgrading.Models.User;
import com.endrawan.porosgrading.R;
import com.endrawan.porosgrading.SignInActivity;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import Components.AppCompatActivity;

public class AdminMainActivity extends AppCompatActivity {

    private static final int USERS_CODE = 1;
    private static final int ADMINS_CODE = 2;
    private static final int ACTIVITIES_CODE = 3;
    private static final String TAG = "AdminMainActivity";

    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private BottomNavigationView mBottomNavigation;
    private RecyclerView mRecyclerView;

    private List<User> users = new ArrayList<>();
    private List<User> admins = new ArrayList<>();
    private List<Action> activities = new ArrayList<>();

    private UsersAdapter usersAdapter, adminsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_title);
        mBottomNavigation = findViewById(R.id.bottom_navigation);
        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadAllData();
        usersAdapter = new UsersAdapter(users);
        adminsAdapter = new UsersAdapter(admins);

        setSupportActionBar(mToolbar);

        mBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.activities:
                        mToolbarTitle.setText("Kegiatan");
                        mRecyclerView.setAdapter(null);
                        return true;
                    case R.id.members:
                        mToolbarTitle.setText("Anggota");
                        mRecyclerView.setAdapter(usersAdapter);
                        return true;
                    case R.id.admins:
                        mToolbarTitle.setText("Admin");
                        mRecyclerView.setAdapter(adminsAdapter);
                        return true;
                    default:
                        return false;
                }
            }
        });

        mBottomNavigation.setSelectedItemId(R.id.activities);
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
                return false;
        }
    }

    private void updateUI() {
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
    }

    private void loadAllData() {
        db.collection(Config.DB_ACTIVITIES)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        loadCompleted(queryDocumentSnapshots, e, ACTIVITIES_CODE);
                    }
                });

        db.collection(Config.DB_USERS)
                .whereEqualTo("level", User.LEVEL_USER)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        loadCompleted(queryDocumentSnapshots, e, USERS_CODE);
                    }
                });

        db.collection(Config.DB_USERS)
                .whereEqualTo("level", User.LEVEL_ADMIN)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        loadCompleted(queryDocumentSnapshots, e, ADMINS_CODE);
                    }
                });
    }

    private void loadCompleted(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e, int dataCode) {
        if (e != null) {
            Log.w(TAG, "Listen failed.", e);
            return;
        }

        if (value != null)
            switch (dataCode) {
                case ACTIVITIES_CODE:
                    for (QueryDocumentSnapshot doc : value) {
                        Action action = doc.toObject(Action.class);
                        activities.add(action);
                    }
                    break;
                case USERS_CODE:
                    for (QueryDocumentSnapshot doc : value) {
                        User user = doc.toObject(User.class);
                        users.add(user);
                    }
                    usersAdapter.notifyDataSetChanged();
                    break;
                case ADMINS_CODE:
                    for (QueryDocumentSnapshot doc : value) {
                        User admin = doc.toObject(User.class);
                        admins.add(admin);
                    }
                    adminsAdapter.notifyDataSetChanged();
                    break;
            }
    }
}
