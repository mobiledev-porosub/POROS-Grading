package Components;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.endrawan.porosgrading.Config;
import com.endrawan.porosgrading.Models.User;
import com.endrawan.porosgrading.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class AppCompatActivity extends android.support.v7.app.AppCompatActivity {
    protected FirebaseAuth mAuth = FirebaseAuth.getInstance();
    protected FirebaseUser firebaseUser = mAuth.getCurrentUser();
    protected User user;
    protected FirebaseFirestore db = FirebaseFirestore.getInstance();
    protected Gson gson = new Gson();
    protected SharedPreferences SP;

    public interface UpdateUserListener {
        void updateUserExist();
        void updateUserNotFound();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SP = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
    }

    protected void updateUser(final UpdateUserListener listener) {
        firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            db.collection(Config.DB_USERS)
                    .document(firebaseUser.getUid())
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    user = documentSnapshot.toObject(User.class);
                    writeUserToSP(user);
                    listener.updateUserExist();
                }
            });
        } else {
            listener.updateUserNotFound();
        }
    }

    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void writeUserToSP(User user){
        SharedPreferences.Editor editor = SP.edit();
        editor.putString("user", gson.toJson(user));
        editor.apply();
    }

    protected User getUserFromSP() {
        String userJSON = SP.getString("user", null);
        return gson.fromJson(userJSON, User.class);
    }

    protected void logout() {
        SharedPreferences.Editor editor = SP.edit();
        editor.remove("user");
        editor.apply();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient gsc = GoogleSignIn.getClient(this, gso);

        gsc.signOut();

        mAuth.signOut();

    }
}
