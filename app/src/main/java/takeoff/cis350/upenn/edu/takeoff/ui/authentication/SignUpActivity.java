package takeoff.cis350.upenn.edu.takeoff.ui.authentication;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.ui.TabbingActivity;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class SignUpActivity extends AppCompatActivity {

    private final Firebase usersRef = new Firebase("https://brilliant-inferno-6470.firebaseio.com/users");
    private static final int REQUEST_READ_CONTACTS = 0;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordConfirmView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set up the login form
        setContentView(R.layout.activity_sign_up);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordConfirmView = (EditText) findViewById(R.id.password_retype);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptSignUp();
                    return true;
                } else {
                    return false;
                }
            }
        });
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_up_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });
    }

    /**
     * Returns true if this user may request contact
     */
    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // TODO: handle
            }
        }
    }


    /**
     * Attempts to sign up the  user
     */
    private void attemptSignUp() {
        // reset errors
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // store values at the time of the login attempt
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();
        String password2 = mPasswordConfirmView.getText().toString();
        System.out.println("Email entered: " + email);
        System.out.println("Password entered: " + password);
        System.out.println("Password2 entered: " + password2);

        boolean cancel = false;
        View focusView = null;

        // check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && password.length() < 4) {
            mPasswordView.setError("Password must be at least 4 characters");
            focusView = mPasswordView;
            cancel = true;
        }

        // check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!email.contains("@")) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // there was an error; don't attempt login and focus the first form field with an error
            focusView.requestFocus();
        } else {
            // continue  with the signup request
            final Intent intent = new  Intent(this, TabbingActivity.class);
            usersRef.createUser(email, password,
                    new Firebase.ValueResultHandler<Map<String, Object>>() {

                        @Override
                        public void onSuccess(Map<String, Object> result) {
                            // the creation of a user was successful
                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("uid", (String) result.get("uid"));
                            userMap.put("username", email);
                            userMap.put("password", password);
                            userMap.put("favoriteFlights", new ArrayList<>());
                            Map<String, Object> users = new HashMap<>();
                            users.put((String) result.get("uid"), userMap);
                            usersRef.updateChildren(users);
                            // go to search page
                            startActivity(intent);
                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {
                            // the creation of a user was not successful
                            Log.e("SignUpActivity", "attemptSignUp: onError "
                                    + firebaseError.toString() + ", no user account created");
                            // TODO: Make error message more specific
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "There is already an account associated with this email "
                                            + "address.", Toast.LENGTH_SHORT);
                            toast.show();

                        }
                    });
        }
    }

}

