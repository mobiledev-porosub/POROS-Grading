package com.endrawan.porosgrading.Admin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.endrawan.porosgrading.Config;
import com.endrawan.porosgrading.Models.ActionType;
import com.endrawan.porosgrading.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import Components.AppCompatActivity;

public class InsertActionTypeActivity extends AppCompatActivity {

    private InsertActionTypeActivity activity = this;
    private final String TAG = "InsertActionTypeActivity";
    private Toolbar mToolbar;
    private EditText mName, mPoints;
    private Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_action_type);

        mName = findViewById(R.id.name);
        mPoints = findViewById(R.id.points);
        mSubmit = findViewById(R.id.submit);
        mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Tambah Kategori");

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int points = Integer.parseInt(mPoints.getText().toString());
                String name = mName.getText().toString().trim();
                if (name.isEmpty() || points <= 0) {
                    toast("Masukkan Nama atau point!");
                } else {
                    ActionType actionType = new ActionType();
                    actionType.setName(name);
                    actionType.setPoints(points);

                    DocumentReference ref = db.collection(Config.DB_ACTIVITY_TYPES).document();
                    actionType.setId(ref.getId());
                    ref.set(actionType).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                toast("Kategori berhasil ditambahkan!");
                                finish();
                            } else {
                                toast("Kategori gagal ditambahkan!");
                            }
                        }
                    });
                }
            }
        });

    }
}
