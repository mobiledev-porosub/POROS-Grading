package com.endrawan.porosgrading;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.endrawan.porosgrading.Admin.AdminMainActivity;
import com.endrawan.porosgrading.Models.User;
import com.endrawan.porosgrading.User.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import Components.AppCompatActivity;

public class SignInActivity extends AppCompatActivity implements OnCompleteListener, AppCompatActivity.UpdateUserListener {

    private final String TAG = "SignInActivity";

    private Button mSignUp, mSignIn;
    private EditText mEmail, mPassword;
    private SignInActivity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mSignUp = findViewById(R.id.signUp);
        mSignIn = findViewById(R.id.signIn);

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = mEmail.getText().toString();
                password = mPassword.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(activity, activity);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUser(this);
    }

    @Override
    public void onComplete(@NonNull Task task) {
        if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "signInWithEmail:success");
            updateUser(this);
        } else {
            // If sign in fails, display a message to the user.
            Log.w(TAG, "signInWithEmail:failure", task.getException());
            Toast.makeText(activity, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updateUserExist() {
        if (user != null) {
            if(user.getLevel() == User.LEVEL_ADMIN)
                startActivity(new Intent(this, AdminMainActivity.class));
            else
                startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void updateUserNotFound() {

    }
}
