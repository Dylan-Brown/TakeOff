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
    List<Flight> flightResults;
    DummySearchQueryHistory history = new DummySearchQueryHistory();
    String[] stringhistory = history.stringHistory2;

    ListView l;

    public final static String EXTRA_MESSAGE1 = "Flight";
    public final static String EXTRA_MESSAGE2 = "FavFlight";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("ENTER SEARCH HISTORY PAGE");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        l = getListView();
        Log.e("getListView", "Dashboard");
        setListAdapter(new ArrayAdapter(this,
                android.R.layout.simple_list_item_single_choice, stringhistory));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringhistory);
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
