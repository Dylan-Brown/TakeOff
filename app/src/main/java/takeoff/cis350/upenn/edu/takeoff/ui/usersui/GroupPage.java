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
import takeoff.cis350.upenn.edu.takeoff.ui.search.SearchPage;

/**
 * This class represents the Activity relevant to displaying a group's information. The name of the
 * group is displayed at the top of the page, and... TODO: finish class description
 */
public class GroupPage extends Activity {

    private TextView groupNameView;
    private ListView groupMembersView;
    private String[] groupMembers;
    private String groupName;
    private boolean userHasFavorites = false;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_page);
        groupName = getIntent().getStringExtra(getString(R.string.group_extra));
        setDisplay();


        // populate array with actual group members
        final String grp = getString(R.string.firebase_grp);
        final Firebase groups = WelcomeActivity.FIREBASE.child(grp);

        groups.addListenerForSingleValueEvent(new ValueEventListener() {
            public Object List;

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // get the group members
                HashMap<String, Object> allGroups = (HashMap<String, Object>) snapshot.getValue();
                HashMap<String, Object> data = (HashMap<String, Object>) allGroups.get(groupName);
                String mem = getString(R.string.firebase_mem);
                ArrayList<String> members = (ArrayList<String>) data.get(mem);
                if (members != null) {
                    setMemberAdapter(members);
                }

                // get the shared flights
                String shared = getString(R.string.firebase_shared);
                if (data.get(shared) != null) {
                    ArrayList<String> sharedFlights = (ArrayList<String>) data.get(shared);
                    // TODO: Display these shared flights
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                String error = (String) getText(R.string.error_internal);
                (Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT)).show();
            }
        });

        checkFavorites();
    }

    /**
     *
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
                    (Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT)).show();
                }
                // check with Firebase and finlize adding member
                confirmInvite(newMemberEmail);
            }
        });

        builder.show();
    }

    /**
     *
     * @param v the view of the share button
     */
    public void share(View v) {
        Log.e("GroupPage", "in share()");
        userHasFavorites = true;
        if (userHasFavorites) {
            final String fav = getString(R.string.firebase_fav);
            final String users = getString(R.string.firebase_users);
            String uid = WelcomeActivity.FIREBASE.getAuth().getUid();
            final Firebase userRef = WelcomeActivity.FIREBASE.child(users).child(uid);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                public Object List;

                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    // get the favorited flights
                    HashMap<String, Object> userInfo = (HashMap<String, Object>) snapshot.getValue();
                    ArrayList<String> favs = (ArrayList<String>) userInfo.get(fav);
                    Flight[] flights = new Flight[favs.size()];
                    String[] flightsInfo = new String[favs.size()];
                    for (int i = 0; i < favs.size(); i++) {
                        flights[i] = Flight.parseFlight(favs.get(i));
                        flightsInfo[i] = flights[i].humanReadable();
                    }
                    sharePopup(flightsInfo, flights);

                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    String error = (String) getText(R.string.error_internal);
                    (Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT)).show();
                }
            });


        } else {
            String error = (String) getText(R.string.group_share_none);
            (Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT)).show();
        }
        userHasFavorites = false;
    }

    /**
     * Display a spinner popup for the user to pick from their list of favorites
      * @param flightInfo the different favorites in String form
     */
    private void sharePopup(String[] flightInfo, Flight[] flights) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(getString(R.string.group_share_choose));

        b.setItems(flightInfo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // update the global, user's lists of groups to include this new group

            }
        });
        b.show();
    }

    /**
     *
     */
    private void postNewSharedFlight(Flight flight) {

        // firebase, post, update children, restart activity
        /*
        // restart the activity
        Intent intent = getIntent();
        finish();
        startActivity(intent);
        */
    }


    /**
     * After getting an email to invite, check firebase that the user exists. If so, this method
     * will add the user to the list of groups.
     * @param email the email of the new user to add
     */
    private void confirmInvite(final String email) {
        final String users = getString(R.string.firebase_users);
        final Firebase usersRef = WelcomeActivity.FIREBASE.child(users);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            public Object List;

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
                        added = true;
                        break;
                    }
                }
                if (!added) {
                    String error = (String) getText(R.string.group_invite_dne);
                    (Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT)).show();
                }

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                String error = (String) getText(R.string.error_internal);
                (Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT)).show();
            }
        });
    }

    /**
     * After the email has been confirmed, we add a new user to the group
     * @param email the email of the new user to add
     */
    private void addToGroup(final String email) {
        final String grp = getString(R.string.firebase_grp);
        final Firebase groupRef = WelcomeActivity.FIREBASE.child(grp).child(groupName);

        groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // get the group members
                HashMap<String, Object> group = (HashMap<String, Object>) snapshot.getValue();
                String mem = getString(R.string.firebase_mem);
                ArrayList<String> members = (ArrayList<String>) group.get(mem);
                members.add(email);
                groupRef.child(mem).setValue(members);
                String mesg = (String) getText(R.string.group_invite_success);
                (Toast.makeText(getApplicationContext(), mesg, Toast.LENGTH_SHORT)).show();

                // restart the activity
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                String error = (String) getText(R.string.error_internal);
                (Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT)).show();
            }
        });
    }

    /**
     * Determine if a user has any favorited flights. This function is called when the activity
     * begins in order to prevent having to check after a user decides to share a Flight with the
     * group.
     */
    private void checkFavorites() {
        final String users = getString(R.string.firebase_users);
        final String fav = getString(R.string.firebase_fav);
        final String uid = WelcomeActivity.FIREBASE.getAuth().getUid();
        final Firebase userRef = WelcomeActivity.FIREBASE.child(users).child(uid);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // if favorites string is not  a key in the map, user does not have favorites
                HashMap<String, Object> userInfo = (HashMap<String, Object>) snapshot.getValue();
                userHasFavorites = userInfo.get(fav) != null;
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                String error = (String) getText(R.string.error_internal);
                (Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT)).show();
            }
        });
    }

    /**
     * Set up the core display items of the group page
     */
    private void setDisplay() {
        // display the group name
        groupNameView = (TextView) findViewById(R.id.textViewGroup);
        if (groupNameView == null) {
            groupNameView = new TextView(getApplicationContext());
        }
        groupNameView.setText(groupName);

        // display the toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.group_toolbar);
        this.setActionBar(myToolbar);
        this.getActionBar().setTitle("");
    }

    /**
     * This method sets the adapter for the Listview to populate it with group member names
     * @param members the list of group members
     */
    private void setMemberAdapter(ArrayList<String> members) {
        groupMembers = new String[members.size()];
        for (int i = 0; i < members.size(); i++) {
            groupMembers[i] = members.get(i);
        }
        groupMembersView = (ListView) findViewById(R.id.member_list);
        int layout = android.R.layout.simple_list_item_single_choice;
        groupMembersView.setAdapter(new ArrayAdapter(this, layout, groupMembers));
    }

}
