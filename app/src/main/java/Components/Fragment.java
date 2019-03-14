package Components;

import com.google.firebase.firestore.FirebaseFirestore;

public class Fragment extends android.support.v4.app.Fragment {
    protected FirebaseFirestore db = FirebaseFirestore.getInstance();
}
