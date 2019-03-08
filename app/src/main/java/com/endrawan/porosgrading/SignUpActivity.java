package com.endrawan.porosgrading;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.endrawan.porosgrading.Adapters.DivisionsAdapter;
import com.endrawan.porosgrading.Models.Division;
import com.endrawan.porosgrading.Models.User;
import com.endrawan.porosgrading.ViewHolders.DivisionViewHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Components.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity implements OnCompleteListener, DivisionsAdapter.divisionActions {

    private final String TAG = "SignUpActivity";
    private Division division;

    private Button mSubmit;
    private EditText mName, mNim, mUsername, mEmail, mPassword;
    private RecyclerView recyclerView;
    private DivisionsAdapter adapter;
    SignUpActivity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mName = findViewById(R.id.name);
        mNim = findViewById(R.id.nim);
        recyclerView = findViewById(R.id.recyclerView);
        //mUsername = findViewById(R.id.username);
        mSubmit = findViewById(R.id.submit);

        adapter = new DivisionsAdapter(this, Arrays.asList(Config.DIVISIONS), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

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

    /*private void initDivisions() {
        divisions.add(
                new Division("Mobile",
                        R.drawable.ic_smartphone_primary_24dp,
                        R.drawable.ic_smartphone_white_24dp,
                        1),
                new Division(""))
    }*/

    private void updateUI() {
        if (mAuth.getCurrentUser() != null) {
            finish();
        }
    }

    private void addUserToDB() {
        user = new User();
        user.setUid(firebaseUser.getUid());
        user.setName(mName.getText().toString());
        //user.setDivision(mSpinner.getSelectedItem().toString());
        user.setDivision(String.valueOf(division.getCode()));
        user.setEmail(mEmail.getText().toString());
        user.setLevel(User.LEVEL_USER);
        user.setNim(mNim.getText().toString());
        if (firebaseUser.getPhotoUrl() != null)
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

    @Override
    public void clicked(Division newDivision, int oldPosition) {
        DivisionViewHolder viewHolder = (DivisionViewHolder) recyclerView.findViewHolderForAdapterPosition(oldPosition);
        if(viewHolder != null && division != null)
        viewHolder.changeToNormal(this, division);
        division = newDivision;
    }
}
