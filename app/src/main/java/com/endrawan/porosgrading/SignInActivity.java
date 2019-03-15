package com.endrawan.porosgrading;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.endrawan.porosgrading.Admin.AdminMainActivity;
import com.endrawan.porosgrading.Models.User;
import com.endrawan.porosgrading.User.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;

import Components.AppCompatActivity;

public class SignInActivity extends AppCompatActivity implements OnCompleteListener, AppCompatActivity.UpdateUserListener {

    private final String TAG = "SignInActivity";
    private final int RC_SIGN_IN = 1;

    private Button mSignUp, mSignIn, mGoogleSignIn;
    private EditText mEmail, mPassword;
    private SignInActivity activity = this;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mSignUp = findViewById(R.id.signUp);
        mSignIn = findViewById(R.id.signIn);
        mGoogleSignIn = findViewById(R.id.signInGoogle);

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

        mGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUser(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
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

    private void googleSignIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle: " + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
//                            activity.updateUser(activity);
                            addUserToDB();
                        } else {
                            Log.d(TAG, "signInWithCredential:failure", task.getException());
                            activity.toast("Authentication Failed");
                        }
                    }
                });
    }

    private void addUserToDB() {
        firebaseUser = mAuth.getCurrentUser();
        user = new User();
        user.setUid(firebaseUser.getUid());
        user.setName(firebaseUser.getDisplayName());
//        user.setDivision(null);
        user.setEmail(firebaseUser.getEmail());
        user.setLevel(User.LEVEL_USER);
//        user.setNim(mNim.getText().toString());
        if (firebaseUser.getPhotoUrl() != null)
            user.setPhoto_url(firebaseUser.getPhotoUrl().toString());

        db.collection(Config.DB_USERS)
                .document(user.getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        //updateUI();
                        activity.updateUser(activity);
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
