package takeoff.cis350.upenn.edu.takeoff.ui.authentication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.regex.Pattern;

import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.ui.TabbingActivity;
import takeoff.cis350.upenn.edu.takeoff.ui.WelcomeActivity;

/**
 * Description:
 * A Log In screen that offers users the ability to log into their accounts via an email/password
 * combination.
 */
public class LogInActivity extends AppCompatActivity {

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
     * Description: Attempts to sign in or register the account specified by the login form, handles errors
     */
    private void attemptLogin() {
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // store values at the time of the login attempt
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        // check for a valid password, if the user entered one
        if (password.length() < 4) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            mPasswordView.requestFocus();
            return;
        }

        // check for a valid email address
        if (TextUtils.isEmpty(email)) {
            // no email address entered
            mEmailView.setError(getString(R.string.error_field_required));
            mEmailView.requestFocus();
            return;

        } else if (!Pattern.compile(".+@.+\\.[a-z]+").matcher(email).matches()) {
            // invalid email address format
            mEmailView.setError(getString(R.string.error_invalid_email));
            mEmailView.requestFocus();
            return;
        }

        // create a handler to handle the result of the authentication
        final Intent intent = new  Intent(this, TabbingActivity.class);
        Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {

            @Override
            public void onAuthenticated(AuthData authData) {
                // authenticated successfully; enter the search page
                startActivity(intent);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // authenticated failed
                String error = "";
                switch (firebaseError.getCode()) {
                    case FirebaseError.INVALID_EMAIL:
                        error = getString(R.string.error_invalid_email);
                        break;
                    case FirebaseError.INVALID_PASSWORD:
                        error = getString(R.string.error_incorrect_password);
                        break;
                    default:
                        break;
                }
                if (!error.isEmpty()) {
                    (Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT)).show();
                }
            }
        };

        // make the authentication attempt
        WelcomeActivity.USER_FIREBASE.authWithPassword(email, password, authResultHandler);
    }
}

