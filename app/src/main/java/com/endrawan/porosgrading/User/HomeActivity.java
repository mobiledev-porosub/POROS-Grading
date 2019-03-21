package com.endrawan.porosgrading.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.endrawan.porosgrading.Config;
import com.endrawan.porosgrading.Models.User;
import com.endrawan.porosgrading.ProfileActivity;
import com.endrawan.porosgrading.R;
import com.endrawan.porosgrading.SignInActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import Components.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private final int RC_PROFILE = 1;
    private final String TAG = "HomeActivity";

    private TextView mName, mScore, mMessage;
    private Button mRead, mInsert, mCertificate;
    private CircleImageView mImage;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        user = getUserFromSP();

        mName = findViewById(R.id.name);
        mScore = findViewById(R.id.score);
        mMessage = findViewById(R.id.message);
        mRead = findViewById(R.id.read);
        mInsert = findViewById(R.id.insert);
        mCertificate = findViewById(R.id.certificate);
        mImage = findViewById(R.id.image);
        mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburger_black_24dp);

        mRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ActionActivity.class));
            }
        });

        mInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, InputActivity.class));
            }
        });

        mCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO add action to certificate
                toast("Selamat anda telah mendapatkan sertifikat!");
            }
        });

        updateView();
        setListener();
    }


    @Override
    public boolean onSupportNavigateUp() {
        startActivityForResult(new Intent(this, ProfileActivity.class), RC_PROFILE);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_PROFILE) {
            switch (resultCode) {
                case ProfileActivity.RESULT_DELETE:
                    toast("DELETE");
                    break;
                case ProfileActivity.RESULT_EDIT:
                    startActivity(new Intent(this, EditActivity.class));
                    break;
                case ProfileActivity.RESULT_LOGOUT:
                    logout();
                    updateUI();
                    break;
            }
        }
    }

    private void updateView() {
        mName.setText(user.getName());
        if (user.getPhoto_url() != null)
            Glide.with(this).load(user.getPhoto_url()).into(mImage);
        String pointsOutput = String.valueOf(user.getTotalPoints()) + "/" + String.valueOf(Config.MIN_POINTS);
        mScore.setText(pointsOutput);
        if (user.getTotalPoints() >= Config.MIN_POINTS) {
            mCertificate.setEnabled(true);
            mMessage.setText("Selamat score yang anda telah kumpulkan telah mencapai target!, Anda bisa mengambil sertifikat!");
        }
    }

    private void setListener() {
        final DocumentReference docRef = db.collection("users").document(firebaseUser.getUid());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    user = snapshot.toObject(User.class);
                    writeUserToSP(user);
                    updateView();
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    private void updateUI() {
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
    }
}
