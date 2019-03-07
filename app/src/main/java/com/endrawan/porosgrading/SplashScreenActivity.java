package com.endrawan.porosgrading;

import android.content.Intent;
import android.os.Bundle;

import com.endrawan.porosgrading.Admin.AdminMainActivity;
import com.endrawan.porosgrading.Models.User;
import com.endrawan.porosgrading.User.MainActivity;

import Components.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity implements AppCompatActivity.UpdateUserListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        updateUser(this);
    }

    @Override
    public void updateUserExist() {
        if (user != null) {
            if (user.getLevel() == User.LEVEL_ADMIN)
                startActivity(new Intent(this, AdminMainActivity.class));
            else
                startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void updateUserNotFound() {
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }
}
