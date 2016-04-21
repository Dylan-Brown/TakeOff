package takeoff.cis350.upenn.edu.takeoff.ui.usersui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Set;

import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.ui.WelcomeActivity;

/**
 * This class represents the activity to display the list of groups to which the logged-in user
 * belongs
 */
public class GroupPageActivity extends ListActivity {

    private ListView groupView;
    private String[] groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadGroupPage();
    }

    /**
     * Fill the groups array with any groups the user is a member of, or display a message if the
     * user is not a member of any groups
     */
    protected void loadGroupPage() {
        groupView = new ListView(getApplicationContext());


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
                        // user is not a member of a group
                        noGroups();
                    }
                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    noGroups();
                }
            });

        } else {
            noGroups();
        }
    }

    /**
     * Gets the name of the group clicked, and start the GroupPage activity
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new  Intent(this, GroupPage.class);
        intent.putExtra(getString(R.string.group_extra), ((TextView) v).getText().toString());
        startActivity(intent);
    }

    /**
     * Display a Toast message notify the user that no groups they were a member of were found
     */
    public void noGroups() {
        String error = getString(R.string.error_no_groups);
        (Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT)).show();
    }

    /**
     * Displays the names of the user's groups in a list
     * @param groupNames the set containing each of the user's groups' names
     */
    public void setGroups(Set<String> groupNames) {
        // store each group's name in the array
        groups = new String[groupNames.size()];
        int i = 0;
        for (String g : groupNames) {
            groups[i++] = g;
        }

        // set the layout
        int layout1 = android.R.layout.simple_list_item_single_choice;
        int layout2 = android.R.layout.simple_list_item_1;
        setListAdapter(new ArrayAdapter(this, layout1, groups));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, layout2, groups);
        groupView.setAdapter(adapter);
        setContentView(R.layout.activity_group_page_view);
    }

}
