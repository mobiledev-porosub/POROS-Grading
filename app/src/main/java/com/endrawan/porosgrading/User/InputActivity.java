package com.endrawan.porosgrading.User;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.endrawan.porosgrading.Config;
import com.endrawan.porosgrading.Models.Action;
import com.endrawan.porosgrading.Models.ActionType;
import com.endrawan.porosgrading.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Components.AppCompatActivity;

public class InputActivity extends AppCompatActivity implements OnCompleteListener<DocumentReference> {

    private final String TAG = "InputActivity";

    private InputActivity activity = this;
    private Action action = new Action();
    private List<ActionType> actionTypes = new ArrayList<>();
    private List<String> spinnerValues = new ArrayList<>();

    private Toolbar toolbar;
    private EditText mName, mDescription;
    private Button mSubmit;
    private Spinner mSpinner;
    private ProgressBar mSpinnerLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        mName = findViewById(R.id.name);
        mDescription = findViewById(R.id.description);
        mSpinner = findViewById(R.id.category);
        mSubmit = findViewById(R.id.submit);
        toolbar = findViewById(R.id.toolbar);
        mSpinnerLoading = findViewById(R.id.spinner_loading);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Kegiatan Baru");

        loadActionTypes();

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionType actionType = actionTypes.get(mSpinner.getSelectedItemPosition());
                action.setName(mName.getText().toString());
                action.setActivity_type(actionType.getName());
                action.setPoints(actionType.getPoints());
                action.setDescription(mDescription.getText().toString());
                action.setUser_uid(firebaseUser.getUid());
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
            Toast.makeText(this, "Kegiatan baru berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Log.w(TAG, "Error adding document", task.getException());
            Toast.makeText(this, "Gagal menambahkan kegiatan!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadActionTypes() {
        db.collection(Config.DB_ACTIVITY_TYPES).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                mSpinnerLoading.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    QuerySnapshot documents = task.getResult();
                    if (documents != null) {
                        Log.d(TAG, task.getResult().toString());
                        for (QueryDocumentSnapshot doc : documents) {
                            ActionType actionType = doc.toObject(ActionType.class);
                            actionTypes.add(actionType);
                            spinnerValues.add(actionType.getName());
                        }
                        refreshSpinner();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void refreshSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setVisibility(View.VISIBLE);
    }
}
