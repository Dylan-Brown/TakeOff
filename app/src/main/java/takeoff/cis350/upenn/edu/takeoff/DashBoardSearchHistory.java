package takeoff.cis350.upenn.edu.takeoff;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
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

import org.json.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashBoardSearchHistory extends ListActivity {
    private final Firebase usersRef =
            new Firebase("https://brilliant-inferno-6470.firebaseio.com/users");
    List<Flight> flightResults;
    DummySearchQueryHistory history = new DummySearchQueryHistory();
    String[] stringhistory = history.stringHistory2;

    ListView l;

    public DashBoardSearchHistory() {

    }

    public final static String EXTRA_MESSAGE1 = "Flight";
    public final static String EXTRA_MESSAGE2 = "FavFlight";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("ENTER SEARCH HISTORY PAGE");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        l = getListView();
        Log.e("getListView", "Dashboard");

        // Retrieve the search queries from the database
        if (usersRef.getAuth() !=  null) {
            Log.e("SearchPage", "Authorized to store query");

            final String uid = usersRef.getAuth().getUid();
            usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Log.e("DashBoardSearchHistory", "onDataChange: retrieving queries ");
                    Map<String, Object> userInfo = (Map<String, Object>) snapshot.getValue();
                    if (!userInfo.containsKey("searchQueries")) {
                        ArrayList<Object> queries = new ArrayList<>();
                        userInfo.put("searchQueries", queries);
                    } else {
                        ArrayList<Object> queries = (ArrayList<Object>) userInfo.get("searchQueries");
                        String[] queryStrings = new String[queries.size()];
                        int i = 0;
                        for (Object q : queries) {
                            Log.e("SearchQueries", "query: " + q.toString());
                            SearchQuery sq = SearchQuery.parseSearchQuery(q.toString());
                            Log.e("SearchQueries", "query is " + sq);
                            queryStrings[i++] = sq.humanReadable();
                        }
                        stringhistory = queryStrings;
                        setAdapter(queryStrings);
                    }

                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    // TODO: Internally display error message, externally claim nothing found
                }
            });
        } else {
            // No authenticated user (guestsession or some error) - no favorites data
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No favorites found.", Toast.LENGTH_SHORT);
            toast.show();
        }


        setListAdapter(new ArrayAdapter(this,
                android.R.layout.simple_list_item_single_choice, stringhistory));
        setAdapter(stringhistory);
    }

    private void setAdapter(String[] info) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, info);
        l.setAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        // I want it to go to the DashBoard from here.
        // So the information needs to be passed to Jason and Jason sends Anaka a List of Flights to Dashboard
        // For now, I'm calling the Dashboard with Dummy information.
        TextView temp = (TextView) v;

        Intent intent = new  Intent (this, Dashboard.class);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
