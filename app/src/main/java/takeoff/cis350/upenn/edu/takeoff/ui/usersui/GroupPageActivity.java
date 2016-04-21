package takeoff.cis350.upenn.edu.takeoff.ui.usersui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.ui.WelcomeActivity;

/**
 * Class representing the Activity to display a group's shared flights
 */
public class GroupPageActivity extends ListActivity {

    private final String GROUP_MESSAGE = "GROUP_MESSAGE";
    ListView groupView;
    String[] groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadGroupPage();
        // TODO: Set the view
    }

    protected void loadGroupPage() {
        groupView = new GroupPageView(getApplicationContext());

        // TODO: Actually get group information
        groups = getDummyGroups();

        if (WelcomeActivity.USER_FIREBASE.getAuth() != null) {
            AuthData auth = WelcomeActivity.USER_FIREBASE.getAuth();
            final String uid = auth.getUid();
            Firebase userRef = WelcomeActivity.USER_FIREBASE.child(uid);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    // get the username to display
                    HashMap<String, Object> uData = (HashMap<String, Object>) snapshot.getValue();
                    String grp = getString(R.string.firebase_grp);
                    if (uData.get(grp) != null) {
                        // user is a member of some groups; get their names
                        HashMap<String, String> uGroups = (HashMap<String, String>) uData.get(grp);
                        setGroups(uGroups.keySet());
                    } else {
                        noGroups();
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    // failed to retrieve user information
                    noGroups();
                }
            });
        } else {
            noGroups();
        }
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new  Intent(this, GroupPage.class);
        Log.e("GroupPage", "Here!!!");

        // parse the TextView to a group name
        // TODO: Actually make this work
        String groupInfo = ((TextView) v).getText().toString();
        Log.e("GroupPage", "groupInfo: " + groupInfo);

        // add  the relevant extras

        // Start the FlightInfoActivity
        intent.putExtra(GROUP_MESSAGE, groupInfo);
        startActivity(intent);
    }

    public void noGroups() {
        String error = getString(R.string.error_no_groups);
        (Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT)).show();
    }

    public void setGroups(Set<String> groupNames) {
        groups = new String[groupNames.size()];
        int i = 0;
        for (String g : groupNames) {
            groups[i++] = g;
        }

        // display group information
        int layout1 = android.R.layout.simple_list_item_single_choice;
        int layout2 = android.R.layout.simple_list_item_1;
        setListAdapter(new ArrayAdapter(this, layout1, groups));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, layout2, groups);
        groupView.setAdapter(adapter);
        setContentView(R.layout.activity_group_page_view);
    }

    // generate dummy group information
    public static String[] getDummyGroups() {
        String[] dGroups = new String[10];
        for (int i = 0;  i < dGroups.length; i++) {
            dGroups[i] = "mock_group_" + i;
        }
        return dGroups;
    }

}
