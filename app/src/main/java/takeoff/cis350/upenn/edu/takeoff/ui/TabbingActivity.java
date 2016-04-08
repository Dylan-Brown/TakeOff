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
import takeoff.cis350.upenn.edu.takeoff.ui.search.DashBoardSearchHistory;
import takeoff.cis350.upenn.edu.takeoff.ui.search.Dashboard;
import takeoff.cis350.upenn.edu.takeoff.ui.search.SearchPage;

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

        //Setting the names shown for each tag
        dashboardTab.setIndicator("DashBoard");
        favoritesTab.setIndicator("Favorites");
        searchHistoryTab.setIndicator("Search History");

        //Setting the correct activity each
        //dashboardTab.setContent(new Intent(this, Dashboard.class));
        //favoritesTab.setContent()
        //searchHistoryTab.setContent(new Intent(this, DashBoardSearchHistory.class));
        System.out.println("BEFORE ADDING TABS");
        tabhost.addTab(dashboardTab, Dashboard.class, null);
        System.out.println("DASHBOARD TAB AFTER");
        tabhost.addTab(searchHistoryTab, DashBoardSearchHistory.class, null);
        System.out.println("SEARCHISTORYTAB AFTER");
        //tabhost.addTab(favoritesTab);
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
    }

    public void sortByCost(MenuItem item) {

    }

    public void sortByDepartureDate(MenuItem item) {

    }

    public void sortByDepartureCity(MenuItem item) {

    }

    public void sortByArrivalDate(MenuItem item) {

    }

    public void sortByArrivalCity(MenuItem item) {
    }

    public void sortByFavoriteFlights(MenuItem item) {

    }

    public void advancedFilter(MenuItem item) { }
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
                //tabhost.getTabWidget().getChildAt(2).performClick();
                FragmentManager fm = getFragmentManager();
                //Fragment f = fm.findFragmentByTag("dashboard");
                System.out.println("ON ACTIVITYRESULT again");
                Dashboard dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag("dashboard");
                dash.loadDashboard();
            }
            //tabhost.setCurrentTab(2);
            System.out.println("ON ACTIVITYRESULT end");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("ON RESUME");
        FragmentManager fm = getFragmentManager();
        //Fragment f = fm.findFragmentByTag("dashboard");
        System.out.println("GETTING DASHY DASH");
        Dashboard dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag("dashboard");
        if(dash != null) {
            dash.loadDashboard();
        }
    }
}
