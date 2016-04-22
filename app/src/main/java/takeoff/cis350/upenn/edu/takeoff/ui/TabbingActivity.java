package takeoff.cis350.upenn.edu.takeoff.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.ui.favorites.FavoritesFragment;
import takeoff.cis350.upenn.edu.takeoff.ui.search.DashBoardSearchHistory;
import takeoff.cis350.upenn.edu.takeoff.ui.search.Dashboard;
import takeoff.cis350.upenn.edu.takeoff.ui.search.SearchPage;
import takeoff.cis350.upenn.edu.takeoff.ui.usersui.GroupPage;
import takeoff.cis350.upenn.edu.takeoff.ui.usersui.GroupPageActivity;
import takeoff.cis350.upenn.edu.takeoff.ui.usersui.ProfileFragment;

/**
 * This activity handles all of the tabs and fragments associated with them; after a user logs in
 * or a guest session begins, this activity will be the main use of the app.
 */
public class TabbingActivity extends AppCompatActivity {

    private static final int SEARCH_PAGE_REQUEST = 100;
    private static final int GROUP_PAGE_REQUEST = 120;
    private boolean fromSearch = false;
    private FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbing);
        createTabHost();
    }

    /**
     * Create all the tabs to see in this activity and assign their contents
     */
    private void createTabHost() {
        tabHost = (FragmentTabHost) (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        // set the tags (text) of each tab
        TabSpec dashboardTab = tabHost.newTabSpec(getString(R.string.dashboard_tag));
        TabSpec favoritesTab = tabHost.newTabSpec(getString(R.string.dashboard_fav));
        TabSpec searchHistoryTab = tabHost.newTabSpec(getString(R.string.dashboard_his));
        TabSpec profileTab = tabHost.newTabSpec(getString(R.string.dashboard_pro));

        // set the names shown for each tag
        dashboardTab.setIndicator(getString(R.string.dashboard_Tag));
        favoritesTab.setIndicator(getString(R.string.dashboard_Fav));
        searchHistoryTab.setIndicator(getString(R.string.dashboard_His));
        profileTab.setIndicator(getString(R.string.dashboard_Pro));

        // set the correct fragment corresponding to each tag
        tabHost.addTab(dashboardTab, Dashboard.class, null);
        tabHost.addTab(searchHistoryTab, DashBoardSearchHistory.class, null);
        tabHost.addTab(favoritesTab, FavoritesFragment.class, null);
        tabHost.addTab(profileTab, ProfileFragment.class, null);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle action bar item clicks
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    /**
     * Make a call to the dashboard to sort the Flight results in it. The MenuItem's id, defined in
     * menu_dashboard.xml, determines the feature by which we sort.
     * @param item the MenuItem selected
     */
    public void sortDashboardResults(MenuItem item) {
        String tag = getString(R.string.dashboard_tag);
        Dashboard dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag(tag);
        if (dash != null) {
            dash.sortBy(item.getItemId());
        }
    }

    /**
     * Make a call to the dashboard to start the Advanced Filter activity
     * @param item the MenuItem selected
     */
    public void advancedFilter(MenuItem item) {
        String tag = getString(R.string.dashboard_tag);
        Dashboard dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag(tag);
        if(dash != null) {
            dash.advancedFilter();
        }
    }

    /**
     * Go to the search page after the user has clicked the Search button
     * @param v the view of the search button
     */
    public void goToSearchPage(View v) {
        Intent intent = new Intent(this, SearchPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(intent, SEARCH_PAGE_REQUEST);
    }

    /**
     * This method handles when the activity is restarted after leaving to some other activity
     * @param requestCode the requestCode of the previous activity
     * @param resultCode the resultCode of the previous activity
     * @param intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Dashboard dash;
        if(requestCode == 100) {
            // returned from search; display search results
            fromSearch = true;
            if(tabHost != null) {
                String tag = getString(R.string.dashboard_tag);
                dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag(tag);
                dash.loadDashboard();
            }
        } else if (requestCode == 120) {
            // returned from groups; TODO: Handle
        }
    }

    /**
     * Display search results
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(fromSearch){
            String tag = getString(R.string.dashboard_tag);
            Dashboard dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag(tag);
            if (dash != null) {
                dash.loadDashboard();
            }
        }
    }


    /**
     * This method is called when the user clicks on the profile picture
     * @param view
     */
    public void onClickProfilePicture(View view){
        // TODO: Implement
        if (WelcomeActivity.FIREBASE.getAuth() != null) {
            Log.e("TabbingActivity", "onClickProfilePicture: User clicked on profile image");
        } else {
            Log.e("TabbingActivity", "onClickProfilePicture: Guest clicked on profile image");
        }

    }


    /**
     * When a user clicks on the MyGroups button in the Profile tab, go to the GroupPageActivity
     */
    public void goToMyGroups(View v) {
        if (WelcomeActivity.FIREBASE.getAuth() != null) {
            // the user is logged in and can go to groups
            Intent intent = new Intent(this, GroupPageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(intent, GROUP_PAGE_REQUEST);

        } else {
            // the user is a guest and cannot create a group
            String error = (String) getText(R.string.please_sign_in);
            (Toast.makeText(this, error, Toast.LENGTH_SHORT)).show();
        }
    }

    /**
     * Create a pop-up dialog asking for the name of the new group
     * @param v the view of the New Group button
     */
    public void goToMakeGroup(View v) {
        if (WelcomeActivity.FIREBASE.getAuth() != null) {
            // the user is logged in and can create a group
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.profile_enter_name));

            // set the expected input type
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            // Set up the buttons
            String buttonText = getString(R.string.profile_ok);
            builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO: Set restrictions on group names
                    // TODO: Check if a group name already exists
                    String newGroupName = input.getText().toString();
                    goToNewGroupPage(newGroupName);
                }
            });

            builder.show();
        } else {
            // The user logged in is anonymous and cannot create a group
            String error = (String) getText(R.string.please_sign_in);
            (Toast.makeText(this, error, Toast.LENGTH_SHORT)).show();
        }
    }

    /**
     * Only called from New Group button dialog, goes to the page of the new group
     * @param newGroupName the name of the new group
     */
    protected void goToNewGroupPage(final String newGroupName) {
        final Intent intent = new Intent(this, GroupPage.class);

        if (WelcomeActivity.FIREBASE.getAuth() != null) {
            final String userUid = WelcomeActivity.FIREBASE.getAuth().getUid();
            final String grp = getString(R.string.firebase_grp);
            final String uid = getString(R.string.firebase_uid);
            final String unm = getString(R.string.firebase_uname);
            final String urs = getString(R.string.firebase_users);

            WelcomeActivity.FIREBASE.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    // get the data from firebase about existing groups
                    Map<String, Object> globalData = (Map<String, Object>) snapshot.getValue();
                    HashMap<String, Object> groupMap;

                    if (!globalData.containsKey(grp)) {
                        // there is no groups map
                        groupMap = new HashMap<>();
                        globalData.put(grp, groupMap);

                    } else {
                        // a groups map  already exists
                        groupMap = (HashMap<String, Object>) globalData.get(grp);
                    }

                    Map<String, Object> usersInfo = (Map<String, Object>) globalData.get(urs);
                    Map<String, Object> userInfo = (Map<String, Object>)  usersInfo.get(uid);
                    String username = (String) userInfo.get(unm);
                    // TODO: What is this for?

                    // if the user does not belong to any  groups, add to a new group category
                    if (!groupMap.containsKey(newGroupName)) {
                        // add the group name to the intent, update firebase
                        intent.putExtra(getString(R.string.group_extra), newGroupName);
                        WelcomeActivity.FIREBASE.child(grp).updateChildren(groupMap);

                    } else {
                        // TODO: Handle the event that a group with the same name already exists

                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    // TODO: Handle
                }
            });


            // add the group to the user's list of groups
            final Firebase ref = WelcomeActivity.USER_FIREBASE;
            ref.child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Map<String, Object> userData = (Map<String, Object>) snapshot.getValue();
                    // if the user does not belong to any  groups, add to a new group category
                    if (!userData.containsKey(grp)) {
                        ArrayList<String> groups = new ArrayList<>();
                        // add the group to the user data
                        groups.add(newGroupName);
                        userData.put(grp, groups);
                        ref.child(userUid).updateChildren(userData);

                    } else {
                        Map<String, Object> groups = (Map<String, Object>) userData.get(grp);
                        groups.put(newGroupName, "");
                        userData.put(grp, groups);
                        ref.child(userUid).updateChildren(userData);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    // TODO: Handle
                }
            });



        }

        // Start the FlightInfoActivity
        startActivity(intent);
    }
}
