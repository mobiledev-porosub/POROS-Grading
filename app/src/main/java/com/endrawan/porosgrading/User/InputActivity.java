package com.endrawan.porosgrading.User;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.endrawan.porosgrading.Models.Action;
import com.endrawan.porosgrading.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InputActivity extends AppCompatActivity implements OnCompleteListener<DocumentReference> {

    private final String TAG = "InputActivity";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = mAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private InputActivity activity = this;
    private Action action = new Action();
    private Toolbar toolbar;
    private EditText mName, mDescription;
    private Button mSubmit;
    private Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        mName = findViewById(R.id.name);
        mDescription = findViewById(R.id.description);
        mSpinner = findViewById(R.id.category);
        mSubmit = findViewById(R.id.submit);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(firebaseUser.getUid());

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.setName(mName.getText().toString());
                action.setActivity_type(mSpinner.getSelectedItem().toString());
                action.setDescription(mDescription.getText().toString());
                action.setUser_uid(firebaseUser.getUid());
                action.setScore(100);
                db.collection("activities")
                        .add(action).addOnCompleteListener(activity);
            }
        });
    }

    @Override
    public void onComplete(@NonNull Task<DocumentReference> task) {
        if (task.isSuccessful()) {
            DocumentReference documentReference = task.getResult();
            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
        } else {
            Log.w(TAG, "Error adding document", task.getException());
        }
    }
}
