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
import java.util.regex.Pattern;

import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.ui.TabbingActivity;
import takeoff.cis350.upenn.edu.takeoff.ui.WelcomeActivity;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class SignUpActivity extends AppCompatActivity {

    private final Firebase usersRef = WelcomeActivity.USER_FIREBASE;
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


        if (!TextUtils.isEmpty(password) && password.length() < 4) {
            // user did not enter password or a password that is too short
            mPasswordView.setError(getString(R.string.error_password_short));
            mPasswordView.requestFocus();
            return;

        } else if (!password.equals(mPasswordConfirmView.getText().toString())) {
            // passwords did not match
            mPasswordView.setError(getString(R.string.error_password_match));
            mPasswordView.requestFocus();
            return;
        }

        // check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            // no email entered
            mEmailView.setError(getString(R.string.error_field_required));
            mEmailView.requestFocus();
            return;

        } else if (!Pattern.compile(".+@.+\\.[a-z]+").matcher(email).matches()) {
            // invalid email address format
            mEmailView.setError(getString(R.string.error_invalid_email));
            mEmailView.requestFocus();
            return;
        }

        // make the sign-up request
        final Intent intent = new  Intent(this, TabbingActivity.class);
        usersRef.createUser(email, password,
                new Firebase.ValueResultHandler<Map<String, Object>>() {

                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        // the creation of a user was successful; create user
                        Map<String, Object> userMap = new HashMap<>();
                        String uid = getString(R.string.firebase_uid);
                        userMap.put(uid, result.get(uid));
                        userMap.put(getString(R.string.firebase_uname), email);
                        userMap.put(getString(R.string.prompt_password), password);
                        userMap.put(getString(R.string.firebase_fav), new ArrayList<>());
                        Map<String, Object> users = new HashMap<>();
                        users.put((String) result.get(uid), userMap);
                        usersRef.updateChildren(users);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        // authenticated failed
                        String error = "";
                        switch (firebaseError.getCode()) {
                            case FirebaseError.INVALID_EMAIL:
                                error = getString(R.string.error_invalid_email);
                                break;
                            case FirebaseError.INVALID_PASSWORD:
                                error = getString(R.string.error_invalid_password);
                                break;
                            default:
                                error = getString(R.string.error_invalid_credentials);
                        }
                        (Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT)).show();

                    }
                });
    }

}

