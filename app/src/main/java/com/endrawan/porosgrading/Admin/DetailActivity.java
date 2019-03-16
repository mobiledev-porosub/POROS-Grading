package com.endrawan.porosgrading.Admin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.endrawan.porosgrading.Config;
import com.endrawan.porosgrading.Models.Action;
import com.endrawan.porosgrading.Models.User;
import com.endrawan.porosgrading.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import Components.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class DetailActivity extends AppCompatActivity {

    private int totalPoints = 0;
    private final String TAG = "DetailActivity";

    private TextView mPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        TextView mName, mEmail, mNim, mDivision;// mPoints;// mDelete, mLogout, mEdit;
        CircleImageView mImage;

        Bundle extras = getIntent().getExtras();
        String userJSON = extras.getString("user");
        User member = gson.fromJson(userJSON, User.class);

        user = getUserFromSP();

        mName = findViewById(R.id.name);
        mEmail = findViewById(R.id.email);
        mNim = findViewById(R.id.nim);
        mDivision = findViewById(R.id.division);
        mPoints = findViewById(R.id.points);
//        mDelete = findViewById(R.id.delete);
//        mLogout = findViewById(R.id.logout);
        mImage = findViewById(R.id.image);
//        mEdit = findViewById(R.id.edit);

        mName.setText(member.getName());
        mEmail.setText(member.getEmail());
        mNim.setText(member.getNim());
        if (member.getDivision() != -1)
            mDivision.setText(Config.DIVISIONS[user.getDivision()].getName());

        if (member.getPhoto_url() != null)
            Glide.with(this).load(member.getPhoto_url()).into(mImage);

        db.collection(Config.DB_ACTIVITIES).whereEqualTo("user_uid", member.getUid())
                .whereEqualTo("status", Action.STATUS_ACCEPTED)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Log.d(TAG, document.getId() + " => " + document.getData());
                        Action action = document.toObject(Action.class);
                        totalPoints += action.getPoints();
                    }
                    mPoints.setText(String.valueOf(totalPoints));
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }

            }
        });
    }
}
