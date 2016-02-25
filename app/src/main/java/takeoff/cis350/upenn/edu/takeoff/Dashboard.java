package takeoff.cis350.upenn.edu.takeoff;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Dashboard extends ListActivity {

    DummyFlightInfo flights = new DummyFlightInfo();
    String[] flight = flights.flights;

    //This is the peice of information that I should be receiving while someone calls this activity.
    Flight[] flightinfo = flights.flightinfo;
    ListView l;

    public Dashboard() {
        //Default Constructor
    }

    public Dashboard (Flight f[]) {
        // here the array f is the list of flights
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        l = getListView();
        setListAdapter(new ArrayAdapter(this,
                android.R.layout.simple_list_item_single_choice, flight));
        //ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, flight);
        //l.setAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        TextView temp = (TextView) v;
        Toast.makeText(this,""+temp.getText() + " " + position, Toast.LENGTH_SHORT).show();

        // here i want to go to another activity that displays the information of individual flights
        Intent intent = new  Intent (this, FlightInfo.class);
        System.out.println("Entering the new activity. Not sure how to pass information");
        startActivity(intent);
    }
}
