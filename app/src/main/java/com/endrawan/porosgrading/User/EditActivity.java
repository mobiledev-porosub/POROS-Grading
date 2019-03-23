package com.endrawan.porosgrading.User;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.endrawan.porosgrading.Adapters.DivisionsAdapter;
import com.endrawan.porosgrading.Config;
import com.endrawan.porosgrading.Models.Division;
import com.endrawan.porosgrading.R;
import com.endrawan.porosgrading.ViewHolders.DivisionViewHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

import Components.AppCompatActivity;

public class EditActivity extends AppCompatActivity implements DivisionsAdapter.divisionActions {


    private final String TAG = "SignUpActivity";
    private Division division;

    private Button mSubmit;
    private EditText mName, mNim, mEmail;
    private RecyclerView recyclerView;
    private DivisionsAdapter adapter;
    private ProgressBar mSubmitLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        user = getUserFromSP();
        if (user.getDivision() != -1)
            division = Config.DIVISIONS[user.getDivision()];

        mEmail = findViewById(R.id.email);
        mName = findViewById(R.id.name);
        mNim = findViewById(R.id.nim);
        recyclerView = findViewById(R.id.recyclerView);
        mSubmit = findViewById(R.id.submit);
        mSubmitLoading = findViewById(R.id.submitLoading);

        if (user.getDivision() == -1)
            adapter = new DivisionsAdapter(this, Arrays.asList(Config.DIVISIONS), this);
        else
            adapter = new DivisionsAdapter(this, Arrays.asList(Config.DIVISIONS), this, user.getDivision());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifAndEdit();
            }
        });

        setDefaultValue();
    }

    @Override
    public void clicked(Division newDivision, int oldPosition) {
        DivisionViewHolder viewHolder = (DivisionViewHolder) recyclerView.findViewHolderForAdapterPosition(oldPosition);
        if (viewHolder != null && division != null)
            viewHolder.changeToNormal(this, division);
        division = newDivision;
    }

    private void setDefaultValue() {
        mEmail.setText(user.getEmail());
        mName.setText(user.getName());
        mNim.setText(user.getNim());
    }

    private void verifAndEdit() {
        String name = mName.getText().toString().trim();
        String nim = mNim.getText().toString().trim();

        if(!(name.length() >=  Config.MIN_LENGTH_NAME)) {
            toast("Nama minimal " + Config.MIN_LENGTH_NAME + " Karakter!");
            return;
        }

        if(!(nim.length() >=  Config.MIN_LENGTH_NIM)) {
            toast("Email minimal " + Config.MIN_LENGTH_NIM + " Karakter!");
            return;
        }

        user.setName(name);
        user.setNim(nim);
        user.setDivision(division.getCode());

        submitHide();

        db.collection(Config.DB_USERS).document(user.getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    toast("Berhasil mengubah data!");
                    finish();
                } else {
                    toast("Gagal Mengubah data: " + task.getException());
                }
                submitShow();
            }
        });
    }

    private void submitHide() {
        mSubmit.setText("");
        mSubmitLoading.setVisibility(View.VISIBLE);
        mSubmit.setEnabled(false);
    }

    private void submitShow() {
        mSubmit.setText(getResources().getString(R.string.edit_akun));
        mSubmitLoading.setVisibility(View.GONE);
        mSubmit.setEnabled(true);
    }
}
