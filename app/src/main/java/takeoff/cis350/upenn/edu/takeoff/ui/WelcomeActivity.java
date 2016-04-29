package takeoff.cis350.upenn.edu.takeoff.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;
import android.widget.AdapterView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import takeoff.cis350.upenn.edu.takeoff.ui.authentication.LogInActivity;
import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.ui.authentication.SignUpActivity;

/**
 * This Activity is the first activity that starts when the App is opened; it has the name of the
 * app displayed, and buttons to continue as guest, log in, or sign up.
 */
public class WelcomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // FireBase instance should be accessible to entire app
    public static Firebase FIREBASE;
    public static Firebase USER_FIREBASE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up FireBase
        Firebase.setAndroidContext(this);
        FIREBASE = new Firebase(getString(R.string.firebase_ref));
        USER_FIREBASE = FIREBASE.child(getString(R.string.fb_users));

        USER_FIREBASE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // if users map does not exist, set it
                if (snapshot.getValue() == null) {
                    Map<String, Map<String, String>> users = new HashMap<String, Map<String, String>>();
                    USER_FIREBASE.setValue(users);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    @Override
    protected void onResume() {
        // log out users returning to the welcome page
        super.onResume();
        FIREBASE.unauth();
        USER_FIREBASE.unauth();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
    }

    /**
     * Log In button clicked; go to the LogInActivity
     * @param view the view of the Log In button
     */
    public void login(View view) {
        Intent intent = new  Intent (this, LogInActivity.class);
        startActivity(intent);
    }

    /**
     * Continue as Guest button clicked; go to the TabbingActivity
     * @param view the view of the Continue as Guest button
     */
    public void guest (View view) {
        Intent intent = new  Intent (this, TabbingActivity.class);
        startActivity(intent);
    }

    /**
     * Sign Up button clicked; go to the SignUpActivity
     * @param view the view of the Sign Up button
     */
    public void sign (View view) {
        Intent intent = new  Intent (this, SignUpActivity.class);
        startActivity(intent);
    }
}
