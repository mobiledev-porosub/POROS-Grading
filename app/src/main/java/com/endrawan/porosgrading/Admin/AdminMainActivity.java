package com.endrawan.porosgrading.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.endrawan.porosgrading.R;
import com.endrawan.porosgrading.SignInActivity;

import Components.AppCompatActivity;

public class AdminMainActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Admin");
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
}
