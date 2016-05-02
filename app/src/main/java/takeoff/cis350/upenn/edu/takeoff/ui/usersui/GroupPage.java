package takeoff.cis350.upenn.edu.takeoff.ui.usersui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.flight.Flight;
import takeoff.cis350.upenn.edu.takeoff.ui.WelcomeActivity;

/**
 * This class represents the Activity relevant to displaying a group's information.
 */
@SuppressWarnings("all")
public class GroupPage extends Activity {

    // constant strings
    private final String UID = WelcomeActivity.FIREBASE.getAuth().getUid();
    private final String FAV = "favoriteFlights";
    private final String MEM = "members";
    private final String ERROR_INTRNL = "Sorry, firbase has experienced an internal error";
    private final int TOAST_LEN = Toast.LENGTH_SHORT;

    // Firebase references
    private final Firebase usersRef = WelcomeActivity.FIREBASE.child("users");
    private final Firebase userRef =usersRef.child(UID);
    private final Firebase groups = WelcomeActivity.FIREBASE.child("groups");

    // views and information
    private TextView nameView = null;
    private ListView membersView = null;
    private ListView sharedView = null;
    private String[] members = null;
    private String[] shared = null;
    private String groupName = null;
    private boolean userHasFavorites = false;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);
        groupName = getIntent().getStringExtra("GROUP_MESSAGE");
        setDisplay();

        // populate array with actual group members
        groups.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // get the group members
                HashMap<String, Object> allGroups = (HashMap<String, Object>) snapshot.getValue();
                HashMap<String, Object> data = (HashMap<String, Object>) allGroups.get(groupName);
                String mem = "members";
                ArrayList<String> tempMembers = (ArrayList<String>) data.get(mem);
                if (tempMembers != null) {
                    setMemberAdapter(tempMembers);
                }

                // get the shared flights
                String mShared = "shared";
                if (data.get(mShared) != null) {
                    setSharedAdapter((ArrayList<String>) data.get(mShared));
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                (Toast.makeText(getApplicationContext(), ERROR_INTRNL, TOAST_LEN)).show();
            }
        });
        checkFavorites();
    }

    /**
     * Set up the core display items of the group page
     */
    private void setDisplay() {
        // display the group name
        nameView = (TextView) findViewById(R.id.textViewGroup);
        if (nameView == null) {
            nameView = new TextView(getApplicationContext());
        }
        nameView.setText(groupName);

        // display the toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.group_toolbar);
        this.setActionBar(myToolbar);
        this.getActionBar().setTitle("");
    }

    /**
     * Description:
     * @param v the view of the invite button
     */
    public void invite(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.group_invite_email));

        // set the expected input type
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        String buttonText = getString(R.string.profile_ok);
        builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // update the global, user's lists of groups to include this new group
                String newMemberEmail = input.getText().toString();
                if (!Pattern.compile(".+@.+\\.[a-z]+").matcher(newMemberEmail).matches()) {
                    String error = (String) getText(R.string.group_invite_error);
                    (Toast.makeText(getApplicationContext(), error, TOAST_LEN)).show();
                }
                // check with Firebase and finlize adding member
                confirmInvite(newMemberEmail);
            }
        });

        builder.show();
    }

    /**
     * Once the user clicks on the share button, go into firebase to share the flight
     * @param v the view of the share button
     */
    public void share(View v) {
        String error = "Sorry, this feature is not yet implemented!";
        (Toast.makeText(getApplicationContext(), error, TOAST_LEN)).show();
    }


    /**
     * After getting an email to invite, check firebase that the user exists. If so, this method
     * will add the user to the list of groups.
     * @param email the email of the new user to add
     */
    private void confirmInvite(final String email) {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // get the group members
                HashMap<String, Object> users = (HashMap<String, Object>) snapshot.getValue();
                boolean added = false;
                for (String uid : users.keySet()) {
                    HashMap<String, Object> user = (HashMap<String, Object>) users.get(uid);
                    String username = (String) user.get(getString(R.string.firebase_uname));
                    if (username.toLowerCase().equals(email.toLowerCase())) {
                        addToGroup(email);
                        addGroupToUserInfo(email);
                        added = true;
                        break;
                    }
                }
                if (!added) {
                    String error = (String) getText(R.string.group_invite_dne);
                    (Toast.makeText(getApplicationContext(), error, TOAST_LEN)).show();
                }

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                (Toast.makeText(getApplicationContext(), ERROR_INTRNL, TOAST_LEN)).show();
            }
        });
    }

    /**
     * After the email has been confirmed, we add a new user to the group
     * @param email the email of the new user to add
     */
    private void addToGroup(final String email) {
        final Firebase groupRef = groups.child(groupName);
        groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // get the group members
                HashMap<String, Object> group = (HashMap<String, Object>) snapshot.getValue();
                ArrayList<String> tempMembers = (ArrayList<String>) group.get(MEM);
                tempMembers.add(email);
                groupRef.child(MEM).setValue(tempMembers);
                String mesg = (String) getText(R.string.group_invite_success);
                (Toast.makeText(getApplicationContext(), mesg, TOAST_LEN)).show();

                // restart the activity
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                (Toast.makeText(getApplicationContext(), ERROR_INTRNL, TOAST_LEN)).show();
            }
        });
    }

    /**
     * Finds the user to be invited to the group in the user's list, and updates their personal
     * datastructure
     * @param email the user's email
     */
    private void addGroupToUserInfo(String email) {
        // TODO: Implement
    }

    /**
     * Determine if a user has any favorited flights. This function is called when the activity
     * begins in order to prevent having to check after a user decides to share a Flight with the
     * group.
     */
    private void checkFavorites() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // if favorites string is not  a key in the map, user does not have favorites
                HashMap<String, Object> userInfo = (HashMap<String, Object>) snapshot.getValue();
                userHasFavorites = userInfo.get(FAV) != null;
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                (Toast.makeText(getApplicationContext(), ERROR_INTRNL, TOAST_LEN)).show();
            }
        });
    }

    /**
     * This method sets the adapter for the member's ListView to populate it with group member names
     * @param members the list of group members
     */
    private void setMemberAdapter(ArrayList<String> members) {
        this.members = new String[members.size()];
        for (int i = 0; i < members.size(); i++) {
            this.members[i] = members.get(i);
        }
        membersView = (ListView) findViewById(R.id.member_list);
        int layout = android.R.layout.simple_list_item_single_choice;
        membersView.setAdapter(new ArrayAdapter(this, layout, this.members));
    }

    /**
     * This method sets the adapter for the shared flights's ListView to populate it with flights
     * @param favorites the list of shared flights
     */
    private void setSharedAdapter(ArrayList<String> favorites) {
        shared = new String[favorites.size()];
        for (int i = 0; i < favorites.size(); i++) {
            shared[i] = favorites.get(i);
        }
        sharedView = (ListView) findViewById(R.id.group_favs);
        int layout = android.R.layout.simple_list_item_single_choice;
        sharedView.setAdapter(new ArrayAdapter(this, layout, shared));
    }
}
