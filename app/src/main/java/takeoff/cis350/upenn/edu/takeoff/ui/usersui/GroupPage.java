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
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

import takeoff.cis350.upenn.edu.takeoff.R;
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
                    for (String m : members) {
                        Log.e("GroupPage", "Members: " + m);
                    }
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

                // TODO: Here, check / add to firebase.
            }
        });

        builder.show();
    }

    /**
     *
     * @param v the view of the share button
     */
    public void share(View v) {
        // TODO: Implement
        Log.e("GroupPage", "in share()");
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
            Log.e("setMemberAdapter", "Group members include: " + members.get(i));
            groupMembers[i] = members.get(i);
        }
        groupMembersView = (ListView) findViewById(R.id.member_list);
        int layout = android.R.layout.simple_list_item_single_choice;
        groupMembersView.setAdapter(new ArrayAdapter(this, layout, groupMembers));
    }

}
