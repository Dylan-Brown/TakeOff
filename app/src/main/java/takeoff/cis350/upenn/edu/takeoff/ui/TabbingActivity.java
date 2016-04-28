package takeoff.cis350.upenn.edu.takeoff.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
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
import takeoff.cis350.upenn.edu.takeoff.ui.usersui.GroupListActivity;
import takeoff.cis350.upenn.edu.takeoff.ui.usersui.ImageOps;
import takeoff.cis350.upenn.edu.takeoff.ui.usersui.ProfileFragment;

/**
 * This activity handles all of the tabs and fragments associated with them; after a user logs in
 * or a guest session begins, this activity will be the main use of the app.
 */
public class TabbingActivity extends AppCompatActivity {

    private static final int SEARCH_PAGE_REQUEST = 100;
    private static final int PROFILE_PICK_IMAGE = 110;
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
        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
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
        Dashboard dash;
        if((dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag(tag)) != null) {
            dash.sortBy(item.getItemId());
        }
    }

    /**
     * Make a call to the dashboard to start the Advanced Filter activity
     * @param item the MenuItem selected
     */
    public void dashboardAdvancedFilter(MenuItem item) {
        String tag = getString(R.string.dashboard_tag);
        Dashboard dash;
        if((dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag(tag)) != null) {
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
        ProfileFragment prof;

        if(requestCode == SEARCH_PAGE_REQUEST) {
            // returned from search; display search results
            fromSearch = true;
            if(tabHost != null) {
                String tag = getString(R.string.dashboard_tag);
                dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag(tag);
                dash.loadDashboard();
            }
        } else if (requestCode == PROFILE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            // returned from pick image; get the image selected and the URI
            if (intent == null) {
                // something went wrong; display an error
                String error = (String) getText(R.string.error_internal);
                (Toast.makeText(this, error, Toast.LENGTH_SHORT)).show();
                return;
            }

            // get the image URI, the base64 string, and the bitmap
            Uri imageUri = intent.getData();
            String path = ImageOps.getRealPathFromURI(imageUri, getApplicationContext());
            Bitmap image = ImageOps.pathToBitmap(path);
            image = ImageOps.scaleProfilePictureBitmap(image);
            String base64Image = ImageOps.bitmapToString(image);

            // creaete the icon
            Bitmap icon = ImageOps.createProfileIcon(image);
            String base64Icon = ImageOps.bitmapToString(icon);

            // store the string, display the iamge
            String tag = getString(R.string.dashboard_pro);
            prof = (ProfileFragment) this.getSupportFragmentManager().findFragmentByTag(tag);
            prof.setProfilePictrue(image, base64Image,  icon, base64Icon);

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
        if (WelcomeActivity.FIREBASE.getAuth() != null) {
            // create the intents to get the new image
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType(getString(R.string.firebase_img_type));

            Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Intent pickIntent = new Intent(Intent.ACTION_PICK, uri);

            pickIntent.setType(getString(R.string.firebase_img_type));
            String select = getString(R.string.profile_select);
            Intent chooserIntent = Intent.createChooser(getIntent, select);
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

            // start the intent
            startActivityForResult(chooserIntent, PROFILE_PICK_IMAGE);

        } else {
            // inform user to sign in to use this  feature
            String error = (String) getText(R.string.please_sign_in);
            (Toast.makeText(this, error, Toast.LENGTH_SHORT)).show();
        }

    }

    /**
     * When a user clicks on the MyGroups button in the Profile tab, go to the GroupPageActivity
     */
    public void goToMyGroups(View v) {
        if (WelcomeActivity.FIREBASE.getAuth() != null) {
            // the user is logged in and can go to groups
            Intent intent = new Intent(this, GroupListActivity.class);
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
        // check if there is a user logged in
        if (WelcomeActivity.FIREBASE.getAuth() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.profile_enter_name));
            final String userUid = WelcomeActivity.FIREBASE.getAuth().getUid();

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
                    String newGroupName = input.getText().toString();
                    setNewGroupGlobally(userUid, newGroupName);
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
     * Takes the newly set profile image and stores it in Firebase as a base64 binary string
     * @param image the base64 binary string
     */
    private void storeProfileImage(String image) {

    }

    /**
     * This helper method will add the new group to the global list of groups
     * @param userUid the user's uid who created the group
     * @param newGroupName the name of the new group
     */
    private void setNewGroupGlobally(final String userUid, final String newGroupName) {
        final String grp = getString(R.string.fb_grp);

        WelcomeActivity.FIREBASE.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // get the data from firebase about existing groups
                Map<String, Object> globalData = (Map<String, Object>) snapshot.getValue();
                String usrs = getString(R.string.fb_users);
                Map<String, Object> usersInfo = (Map<String, Object>) globalData.get(usrs);
                Map<String, Object> userInfo= (Map<String, Object>) usersInfo.get(userUid);
                String uName = (String) userInfo.get(getString(R.string.firebase_uname));
                HashMap<String, Object> groupsMap;

                // if there is no groups map, make one, otherwise get the existing map
                if (!globalData.containsKey(grp)) {
                    groupsMap = new HashMap<>();
                    globalData.put(grp, groupsMap);
                    WelcomeActivity.FIREBASE.setValue(globalData);
                } else {
                    groupsMap = (HashMap<String, Object>) globalData.get(grp);
                }

                // if the user does not belong to any groups, add to a new group category
                if (!groupsMap.containsKey(newGroupName)) {
                    HashMap<String, Object> newGroupMap = new HashMap<>();
                    newGroupMap.put(getString(R.string.firebase_grn), newGroupName);
                    ArrayList<String> members = new ArrayList<>();
                    members.add(uName);
                    String mem = getString(R.string.firebase_mem);
                    newGroupMap.put(mem, members);
                    groupsMap.put(newGroupName, newGroupMap);
                }
                WelcomeActivity.FIREBASE.child(grp).updateChildren(groupsMap);
                setNewGroupForUser(userUid, newGroupName);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // inform the user of an internal error
                String error = (String) getText(R.string.error_internal);
                (Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT)).show();
            }
        });
    }

    /**
     * This helper method will specify a user as a member of a new group in Firebase
     * @param userUid the unique uid of the user
     * @param newGroupName the name of the new group
     */
    private void setNewGroupForUser(final String userUid, final String newGroupName) {
        final Firebase usersRef = WelcomeActivity.USER_FIREBASE;
        final String grp = getString(R.string.fb_grp);

        // add the group to the user's list of groups
        usersRef.child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Map<String, Object> userData = (Map<String, Object>) snapshot.getValue();

                // get or create the user's group list and set the new group in it
                if (!userData.containsKey(grp)) {
                    ArrayList<String> groups = new ArrayList<>();
                    groups.add(newGroupName);
                    userData.put(grp, groups);
                    usersRef.child(userUid).updateChildren(userData);

                } else {
                    ArrayList<Object> groups = (ArrayList<Object>) userData.get(grp);
                    groups.add(newGroupName);
                    userData.put(grp, groups);
                    usersRef.child(userUid).updateChildren(userData);
                }

                // go to the new group page
                goToGroupPage(newGroupName);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // inform the user of an internal error
                String error = (String) getText(R.string.error_internal);
                (Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT)).show();
            }
        });
    }

    /**
     * After a new group is confirmed and created, go to the new group page
     * @param groupName the name of the group
     */
    protected void goToGroupPage(String groupName) {
        // start the GroupPage activity
        Intent intent = new Intent(this, GroupPage.class);
        intent.putExtra(getString(R.string.group_extra), groupName);
        startActivity(intent);
    }
}
