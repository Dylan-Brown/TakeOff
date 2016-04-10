package takeoff.cis350.upenn.edu.takeoff.ui.authentication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

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

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.ui.TabbingActivity;
import takeoff.cis350.upenn.edu.takeoff.ui.search.SearchPage;

/**
 * A login screen that offers login via email/password.
 */
public class LogInActivity extends AppCompatActivity {

    private static final int REQUEST_READ_CONTACTS = 0;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // set up the login form
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                } else {
                    return false;
                }
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // TODO: Handle
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form, handles errors
     */
    private void attemptLogin() {
        // reset errors
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // store values at the time of the login attempt
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        System.out.println("Email entered: " + email);
        System.out.println("Password entered: " + password);

        boolean cancel = false;
        View focusView = null;

        // check for a valid password, if the user entered one
        if (!TextUtils.isEmpty(password) && !(password.length() > 4)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // check for a valid email address
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
            // perform the user login attempt.

            Firebase usersRef = new Firebase("https://brilliant-inferno-6470.firebaseio.com/users");
            final Intent intent = new  Intent(this, TabbingActivity.class);

            // create a handler to handle the result of the authentication
            Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {

                @Override
                public void onAuthenticated(AuthData authData) {
                    // authenticated successfully with payload authData
                    Log.e("LoginActivity", "attemptLogin onAuthenticated: Authenticated");
                    // enter the search page
                    startActivity(intent);
                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    // authenticated failed with error firebaseError
                    Log.e("LoginActivity", "attemptLogin onAuthenticationError: Error");
                    Context context = getApplicationContext();
                    CharSequence text = "Incorrect email or password.";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            };

            // authenticate users with an email/password combination
            usersRef.authWithPassword(email, password, authResultHandler);
        }
    }

}

