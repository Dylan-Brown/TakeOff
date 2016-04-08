package takeoff.cis350.upenn.edu.takeoff.ui;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost.TabSpec;

import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.ui.*;
import takeoff.cis350.upenn.edu.takeoff.ui.favorites.FavoritesFragment;
import takeoff.cis350.upenn.edu.takeoff.ui.search.DashBoardSearchHistory;
import takeoff.cis350.upenn.edu.takeoff.ui.search.Dashboard;
import takeoff.cis350.upenn.edu.takeoff.ui.search.SearchPage;
import takeoff.cis350.upenn.edu.takeoff.ui.usersui.ProfileFragment;

public class TabbingActivity extends AppCompatActivity {
    private FragmentTabHost tabhost;
    private static final int SEARCH_PAGE_REQUEST = 100;
    private boolean fromSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbing);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //set and create TabHost
        createTabHost();
        System.out.println("ACTIVITY ONCREATE END");
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    public void sortByAirline(MenuItem item) {
        Dashboard dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag("dashboard");
        if(dash != null) {
            dash.sortByAirline();
        }

    }

    public void sortByCost(MenuItem item) {
        Dashboard dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag("dashboard");
        if(dash != null) {
            dash.sortByCost();
        }
    }

    public void sortByDepartureDate(MenuItem item) {
        Dashboard dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag("dashboard");
        if(dash != null) {
            dash.sortByDepartureDate();
        }
    }

    public void sortByDepartureCity(MenuItem item) {
        Dashboard dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag("dashboard");
        if(dash != null) {
            dash.sortByDepartureCity();
        }
    }

    public void sortByArrivalDate(MenuItem item) {
        Dashboard dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag("dashboard");
        if(dash != null) {
            dash.sortByArrivalDate();
        }
    }

    public void sortByArrivalCity(MenuItem item) {
        Dashboard dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag("dashboard");
        if(dash != null) {
            dash.sortByArrivalCity();
        }
    }

    public void sortByFavoriteFlights(MenuItem item) {
        Dashboard dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag("dashboard");
        if(dash != null) {
            dash.sortByFavoriteFlights();
        }
    }

    public void advancedFilter(MenuItem item) {
        Dashboard dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag("dashboard");
        if(dash != null) {
            dash.advancedFilter();
        }
    }
    public void goToSearchPage(View v) {
        Intent intent = new Intent(this, SearchPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        System.out.println("REQUEST CODE = " + SEARCH_PAGE_REQUEST);
        startActivityForResult(intent, SEARCH_PAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == 100) {
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

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("ON RESUME");
            System.out.println("GETTING DASHY DASH");
            Dashboard dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag("dashboard");
            if (dash != null) {
                dash.loadDashboard();

        }
    }
}
