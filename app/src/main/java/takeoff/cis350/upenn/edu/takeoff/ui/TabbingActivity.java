package takeoff.cis350.upenn.edu.takeoff.ui;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import takeoff.cis350.upenn.edu.takeoff.ui.*;
import takeoff.cis350.upenn.edu.takeoff.ui.authentication.LogInActivity;
import takeoff.cis350.upenn.edu.takeoff.ui.favorites.FavoritesFragment;
import takeoff.cis350.upenn.edu.takeoff.ui.search.DashBoardSearchHistory;
import takeoff.cis350.upenn.edu.takeoff.ui.search.Dashboard;
import takeoff.cis350.upenn.edu.takeoff.ui.search.SearchPage;
import takeoff.cis350.upenn.edu.takeoff.ui.usersui.GroupPage;
import takeoff.cis350.upenn.edu.takeoff.ui.usersui.GroupPageActivity;
import takeoff.cis350.upenn.edu.takeoff.ui.usersui.ProfileFragment;
import takeoff.cis350.upenn.edu.takeoff.user.Group;

/**
 *
 */
public class TabbingActivity extends AppCompatActivity {

    private static final int SEARCH_PAGE_REQUEST = 100;
    private static final int GROUP_PAGE_REQUEST = 120;
    private static final String GROUP_MESSAGE = "GROUP_MESSAGE";
    private FragmentTabHost tabhost;
    private boolean fromSearch = false;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbing);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //set and create TabHost
        Firebase.setAndroidContext(this);
        createTabHost();
        System.out.println("ACTIVITY ONCREATE END");
    }

    /**
     * Helper to create all the tabs and assign their contents
     */
    private void createTabHost() {
        tabhost = (FragmentTabHost) (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabhost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        //Setting the tags for each tab
        TabSpec dashboardTab = tabhost.newTabSpec("dashboard");
        TabSpec favoritesTab = tabhost.newTabSpec("favorites");
        TabSpec searchHistoryTab = tabhost.newTabSpec("searchHistory");
        TabSpec profileTab = tabhost.newTabSpec("profile");

        //Setting the names shown for each tag
        dashboardTab.setIndicator("DashBoard");
        favoritesTab.setIndicator("Favorites");
        searchHistoryTab.setIndicator("Search History");
        profileTab.setIndicator("Profile");

        //Setting the correct activity each
        tabhost.addTab(dashboardTab, Dashboard.class, null);
        tabhost.addTab(searchHistoryTab, DashBoardSearchHistory.class, null);
        tabhost.addTab(favoritesTab, FavoritesFragment.class, null);
        tabhost.addTab(profileTab, ProfileFragment.class, null);
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    /**
     * Call the dashboard's sort method to sort by airline
     * @param item
     */
    public void sortByAirline(MenuItem item) {
        Dashboard dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag("dashboard");
        if(dash != null) {
            dash.sortByAirline();
        }

    }

    /**
     * Call the dashboard's sort method to sort by cost
     * @param item
     */
    public void sortByCost(MenuItem item) {
        Dashboard dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag("dashboard");
        if(dash != null) {
            dash.sortByCost();
        }
    }

    /**
     *
     * @param item
     */
    public void sortByDepartureDate(MenuItem item) {
        Dashboard dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag("dashboard");
        if(dash != null) {
            dash.sortByDepartureDate();
        }
    }

    /**
     * Call the dashboard's sort method to sort by departure city
     * @param item
     */
    public void sortByDepartureCity(MenuItem item) {
        Dashboard dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag("dashboard");
        if(dash != null) {
            dash.sortByDepartureCity();
        }
    }

    /**
     * Call the dashboard's sort method to sort by arrival date
     * @param item
     */
    public void sortByArrivalDate(MenuItem item) {
        Dashboard dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag("dashboard");
        if(dash != null) {
            dash.sortByArrivalDate();
        }
    }

    /**
     * Call the dashboard's sort method to sort by arrival city
     * @param item
     */
    public void sortByArrivalCity(MenuItem item) {
        Dashboard dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag("dashboard");
        if(dash != null) {
            dash.sortByArrivalCity();
        }
    }

    /**
     * Call the dashboard's sort method to sort by favorite flight
     * @param item
     */
    public void sortByFavoriteFlights(MenuItem item) {
        Dashboard dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag("dashboard");
        if(dash != null) {
            dash.sortByFavoriteFlights();
        }
    }

    /**
     * Call the dashboard's sort method to sort by advance filter
     * @param item
     */
    public void advancedFilter(MenuItem item) {
        Dashboard dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag("dashboard");
        if(dash != null) {
            dash.advancedFilter();
        }
    }

    /**
     * go to the search page
     * @param v
     */
    public void goToSearchPage(View v) {
        Intent intent = new Intent(this, SearchPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        System.out.println("REQUEST CODE = " + SEARCH_PAGE_REQUEST);
        startActivityForResult(intent, SEARCH_PAGE_REQUEST);
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == 100) {
            //if the activity is restarted from search, display search results
            System.out.println("ON ACTIVITYRESULT");
            fromSearch = true;
            if(tabhost != null) {
                System.out.println("ON ACTIVITYRESULT again");
                Dashboard dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag("dashboard");
                dash.loadDashboard();
            }
            System.out.println("ON ACTIVITYRESULT end");
        }
    }

    /**
     * Display search results
     */
    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("ON RESUME");
        if(fromSearch){
            System.out.println("GETTING DASHY DASH");
            Dashboard dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag("dashboard");
            if (dash != null) {
                dash.loadDashboard();
            }
        }
    }


    /**
     * This method is called when the user clicks on the profile picture
     * @param view
     */
    public void picOnClick(View view){
        if (WelcomeActivity.FIREBASE.getAuth() != null) {
            Log.e("TabbingActivity", "picOnClick: User clicked on profile image");
        } else {
            Log.e("TabbingActivity", "picOnClick: Guest clicked on profile image");
        }

    }


    /**
     * called when clicking on the MyGroups button in the Profile tab
     */
    public void goToMyGroups(View v) {

        Firebase usersRef = new Firebase("https://brilliant-inferno-6470.firebaseio.com/users");
        if (usersRef.getAuth() != null) {
            // the user is logged in and can go to groups
            Intent intent = new Intent(this, GroupPageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            System.out.println("REQUEST CODE = " + GROUP_PAGE_REQUEST);
            startActivityForResult(intent, GROUP_PAGE_REQUEST);

        } else {
            // The user logged in is anonymous and cannot create a group
            String error = (String) getText(R.string.please_sign_in);
            (Toast.makeText(this, error, Toast.LENGTH_SHORT)).show();
        }

    }

    /**
     * called when clicking  on the New Group button in the Profile tab
     */
    public void goToMakeGroup(View v) {
        // Create a pop-up dialog asking for the name of the new group

        Firebase usersRef = new Firebase("https://brilliant-inferno-6470.firebaseio.com/users");
        if (usersRef.getAuth() != null) {
            // the user is logged in and can create a group
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter the new group name:");

            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String m_Text = input.getText().toString();
                    Log.e("MakeGroupFromProfile", m_Text);
                    goToNewGroupPage(m_Text);
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
     * @param newGroupName
     */
    protected void goToNewGroupPage(final String newGroupName) {
        final Intent intent = new Intent(this, GroupPage.class);
        Log.e("JustMadeANewGroupAndGo", "Here!!!");

        final Firebase ref = new Firebase("https://brilliant-inferno-6470.firebaseio.com/");
        if (ref.getAuth() != null) {
            final String uid = ref.getAuth().getUid();

            // and the group  to the global list of groups
            ref.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Map<String, Object> globalData = (Map<String, Object>) snapshot.getValue();

                    HashMap<String, Object> groupMap;

                    // add  a groups section if one does not exist
                    if (!globalData.containsKey("groups")) {
                        Log.e("GroupStuffInTabs", "Made a global groups map thing");
                        groupMap = new HashMap<String, Object>();
                        globalData.put("groups", groupMap);
                    } else {
                        Log.e("GroupStuffInTabs", "Getting existing group map");
                        groupMap = (HashMap<String, Object>) globalData.get("groups");
                    }

                    String username = (String) ((Map<String, Object>) ((Map<String, Object>)
                            globalData.get("users")).get(uid)).get("username");
                    // if the user does not belong to any  groups, add to a new group category
                    if (!groupMap.containsKey(newGroupName)) {
                        Log.e("GroupStuffInTabs", "New group  does not yet exist");
                        Group group = new Group(username, newGroupName);
                        // add the group to the global list of groups
                        groupMap.put(newGroupName, group.toString());
                        // add the group name to the intent
                        intent.putExtra(GROUP_MESSAGE, group.toString());
                        // update firebase
                        ref.child("groups").updateChildren(groupMap);
                    } else {
                        Log.e("GroupStuffInTabs", "Group by same name already exists");

                        // TODO: Handle the event that a group with the same name already exists

                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.e("FlightInfoView", "getAuth onCancelled for error: "
                            + firebaseError.getMessage());
                    // TODO: Handle
                }
            });

            final Firebase usersRef = new Firebase("https://brilliant-inferno-6470.firebaseio.com/users");

            // add the group to the user's list of groups
            usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Map<String, Object> userData = (Map<String, Object>) snapshot.getValue();
                    // if the user does not belong to any  groups, add to a new group category
                    if (!userData.containsKey("groups")) {
                        Log.e("GroupStuffInTabs", "Creating user's new group array");
                        ArrayList<String> groups = new ArrayList<>();
                        // add the group to the user data
                        groups.add(newGroupName);
                        userData.put("groups", groups);
                        usersRef.child(uid).updateChildren(userData);

                    } else {
                        Log.e("GroupStuffInTabs", "Getting user's group array");
                        Map<String, Object> groups = (Map<String, Object>) userData.get("groups");
                        groups.put(newGroupName, "");
                        userData.put("groups", groups);
                        usersRef.child(uid).updateChildren(userData);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.e("FlightInfoView", "getAuth onCancelled for error: "
                            + firebaseError.getMessage());
                    // TODO: Handle
                }
            });



        }

        // Start the FlightInfoActivity
        startActivity(intent);
    }
}
