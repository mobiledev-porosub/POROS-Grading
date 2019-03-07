package com.endrawan.porosgrading;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.endrawan.porosgrading.Models.User;
import com.endrawan.porosgrading.User.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import Components.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity implements OnCompleteListener {

    private final String TAG = "SignUpActivity";
    private Button mSubmit;
    private EditText mName, mNim, mUsername, mEmail, mPassword;
    private Spinner mSpinner;
    SignUpActivity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mName = findViewById(R.id.name);
        mNim = findViewById(R.id.nim);
        //mUsername = findViewById(R.id.username);
        mSubmit = findViewById(R.id.submit);
        mSpinner = findViewById(R.id.divisi);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username, email, password;
                //username = mUsername.getText().toString();
                email = mEmail.getText().toString();
                password = mPassword.getText().toString();

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(activity, activity);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI();
    }

    @Override
    public void onComplete(@NonNull Task task) {
        if (task.isSuccessful()) {
            Log.d(TAG, "createUserWithEmail:success");
            firebaseUser = mAuth.getCurrentUser();
            addUserToDB();
        } else {
            Log.w(TAG, "createUserWithEmail:failure", task.getException());
            Toast.makeText(activity, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
            //updateUserExist(null);
        }
    }

    private void updateUI() {
        if (mAuth.getCurrentUser() != null) {
            finish();
        } else {

        }
    }

    private void addUserToDB() {
        user = new User();
        user.setUid(firebaseUser.getUid());
        user.setName(mName.getText().toString());
        user.setDivision(mSpinner.getSelectedItem().toString());
        user.setEmail(mEmail.getText().toString());
        user.setLevel(User.LEVEL_USER);
        user.setNim(mNim.getText().toString());
        if(firebaseUser.getPhotoUrl() != null)
        user.setPhoto_url(firebaseUser.getPhotoUrl().toString());

        db.collection(Config.DB_USERS)
                .document(user.getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        updateUI();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
}
