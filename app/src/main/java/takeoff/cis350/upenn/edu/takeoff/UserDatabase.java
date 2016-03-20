package takeoff.cis350.upenn.edu.takeoff;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

/**
 * A singleton class to interact with FireBase, validating log-ins and getting user data
 */
public class UserDatabase {

    private static UserDatabase database = new UserDatabase();
    private final Toggle toggle = new Toggle();
    private Firebase myFirebaseRef;
    private Firebase usersRef;
    private UserInSession userInSes;

    private UserDatabase() {
        // This references our information stored on firebase, custom URL
        myFirebaseRef = new Firebase("https://brilliant-inferno-6470.firebaseio.com/");
        usersRef = new Firebase("https://brilliant-inferno-6470.firebaseio.com/users");
    }

    // Singleton style instance getter
    public static UserDatabase getInstance() { return database; }



    /**
     * Attempt to log in a user, given a username and password
     * @param uname the username entered
     * @param pass the password entered
     * @return true if successful
     */
    public boolean attemptLogin(String uname, String pass) {

        // Remove authorization on any previously logged in users
        usersRef.unauth();
        toggle.resetToggle();
        userInSes = null;

        // Create a handler to handle the result of the authentication
        Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
            @Override // Authenticated successfully with payload authData
            public void onAuthenticated(AuthData authData) {
                toggle.setToggle();
            }
            @Override // Authenticated failed with error firebaseError
            public void onAuthenticationError(FirebaseError firebaseError) {
            }
        };

        // Authenticate users with an email/password combination
        usersRef.authWithPassword(uname, pass, authResultHandler);

        // If user authorized, set user to the user in session
        if (toggle.getToggle()) {
            final Bin bin = new Bin();
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    bin.fillBin((User) snapshot.getValue());
                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });
            userInSes = new UserInSession(uname, (User) bin.emptyBin());
            return true;
        }

        // Unauthorized login attempt
        return false;
    }


    /**
     * Adds a new user to the database
     * @param uname the username entered
     * @param pass the password entered
     * @return 0 if user created, 1 if EMAIL_TAKEN, or 2 for some other error
     */
    public int addUser(final String uname, String pass)  {

        // Remove authorization on any previously logged in users
        usersRef.unauth();
        toggle.resetToggle();
        userInSes = null;

        // Create the user
        final Bin bin = new Bin();
        myFirebaseRef.createUser(uname, pass,
                new Firebase.ValueResultHandler<Map<String, Object>>() {

                    // On successful creation of a user, set that user to the current UserInSession
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        System.out.println("Successfully created user account with uid: "
                                + result.get("uid"));
                        User newUser = new User(result.get("uid").toString());
                        usersRef.child(uname).setValue(newUser);
                        // Make the new user the user in session
                        userInSes = new UserInSession(uname, newUser);
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        bin.fillBin(firebaseError);
                    }
                });

        // Check for errors
        if (bin.isFull()) {
            return (((FirebaseError) bin.emptyBin()).equals(FirebaseError.EMAIL_TAKEN)) ? 1 : 2;
        }
        return 0;
    }


    /**
     * Returns the currernt User in session
     * @return the currernt User in session
     */
    public User getUser() {
        return this.userInSes.getUser();
    }


    /**
     * Returns the currernt User in session's username
     * @return the currernt User in session's username
     */
    public String getUsername() {
        return this.userInSes.getUsername();
    }


    /**
     * Private class to represent the user that is in session
     */
    private class UserInSession {
        private User user;
        private String uname;
        UserInSession(String uname, User user) {
            this.uname = uname;
            this.user = user;
        }
        User getUser() { return user; }
        String getUsername() { return uname; }
    }


    /**
     * Classes to break encapsulation caused by FireBase
     */
    private class Bin {
        Object obj;
        Bin() {}
        void fillBin(Object obj) { this.obj = obj; }
        boolean isFull() { return (this.obj != null); }
        Object emptyBin() { return obj; }
    }
    private class Toggle {
        boolean toggle = false;
        Toggle() {}
        void resetToggle() { toggle = false; }
        void setToggle() { toggle = true; }
        boolean getToggle() { return toggle; }
    }

}
