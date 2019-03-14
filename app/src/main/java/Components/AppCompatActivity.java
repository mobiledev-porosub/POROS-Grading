package Components;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.endrawan.porosgrading.Config;
import com.endrawan.porosgrading.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AppCompatActivity extends android.support.v7.app.AppCompatActivity {
    protected FirebaseAuth mAuth = FirebaseAuth.getInstance();
    protected FirebaseUser firebaseUser = mAuth.getCurrentUser();
    protected User user;
    protected FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface UpdateUserListener {
        void updateUserExist();
        void updateUserNotFound();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
