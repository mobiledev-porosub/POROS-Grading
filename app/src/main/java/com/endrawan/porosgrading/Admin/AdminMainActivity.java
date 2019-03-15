package com.endrawan.porosgrading.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.endrawan.porosgrading.Fragments.ActionsFragment;
import com.endrawan.porosgrading.Fragments.TypesFragment;
import com.endrawan.porosgrading.Fragments.UsersFragment;
import com.endrawan.porosgrading.Models.User;
import com.endrawan.porosgrading.R;
import com.endrawan.porosgrading.SignInActivity;

import Components.AppCompatActivity;

public class AdminMainActivity extends AppCompatActivity {

//    private static final String TAG = "AdminMainActivity";

    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private BottomNavigationView mBottomNavigation;
    private FloatingActionButton mAction;

    private ActionsFragment actionsFragment = new ActionsFragment();
    private UsersFragment usersFragment = UsersFragment.newInstance(User.LEVEL_USER);
    private UsersFragment adminsFragment = UsersFragment.newInstance(User.LEVEL_ADMIN);
    private TypesFragment typesFragment = new TypesFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_title);
        mBottomNavigation = findViewById(R.id.bottom_navigation);
        mAction = findViewById(R.id.action);

        setSupportActionBar(mToolbar);

        mBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (menuItem.getItemId()) {
                    case R.id.activities:
                        mToolbarTitle.setText(getString(R.string.kegiatan));
                        transaction.replace(R.id.container, actionsFragment);
                        transaction.commit();
                        hideAction();
                        return true;
                    case R.id.members:
                        mToolbarTitle.setText(getString(R.string.anggota));
                        transaction.replace(R.id.container, usersFragment);
                        transaction.commit();
                        hideAction();
                        return true;
                    case R.id.admins:
                        mToolbarTitle.setText(getString(R.string.admin));
                        transaction.replace(R.id.container, adminsFragment);
                        transaction.commit();
                        hideAction();
                        return true;
                    case R.id.categories:
                        mToolbarTitle.setText(getString(R.string.kategori_kegiatan));
                        transaction.replace(R.id.container, typesFragment);
                        transaction.commit();
                        showAction(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(
                                        AdminMainActivity.this,
                                        InsertActionTypeActivity.class));
                            }
                        });
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
        getMenuInflater().inflate(R.menu.main_admin, menu);
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
    private void hideAction() {
        mAction.hide();
    }

    private void showAction(View.OnClickListener listener) {
        mAction.show();
        mAction.setOnClickListener(listener);
    }
}
