package takeoff.cis350.upenn.edu.takeoff.ui.results;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.flight.Flight;
import takeoff.cis350.upenn.edu.takeoff.flight.QPXJSONReader;
import takeoff.cis350.upenn.edu.takeoff.ui.search.Dashboard;

/**
 * Created by tangson on 2/23/16.
 */
public class FlightListActivity extends ListActivity {

    List<Flight> flightList = QPXJSONReader.getFlightResultsFromMostRecentSearch();
    Flight[] flights = flightList.toArray(new Flight[flightList.size()]);
    FlightAdapter adapter;
    ListView listView;
    // TODO: flight list will be bundled in. Get the flights from the favorite_list.

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_list);
        listView = (ListView) findViewById(android.R.id.list);//this goes to the favorite_list
        adapter = new FlightAdapter(getApplicationContext(), R.layout.flight_item_round);
        for (Flight flight : flights) {
            adapter.add(flight);
        }
        listView.setAdapter(adapter);

        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent i = new Intent(getApplicationContext(), FlightResultActivity.class);
                i.putExtra("departureDate", flights[position].getDepartureDate());
                i.putExtra("arrivalDate", flights[position].getArrivalDate());
                i.putExtra("departureCityCode", flights[position].getDepartureCityCode());
                i.putExtra("arrivalCityCode", flights[position].getArrivalCityCode());
                i.putExtra("cost", flights[position].getCost());
                i.putExtra("departureTime", flights[position].getDepartureTime());
                i.putExtra("arrivalTime", flights[position].getArrivalTime());
                i.putExtra("connections", flights[position].getNumOfConnections());
                startActivity(i);
            }
        });
    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        Flight f= Dashboard.FlightCache.get(index);
        boolean returnTrip = f.isReturnTrip();
        boolean directFlight = f.isDirectFlight();
       Intent myIntent = new Intent();
        // myIntent= new Intent(getBaseContext(),CLASSTHATITSGOINGTO);
        myIntent.putExtra("flightIndex", index);
        startActivity(myIntent);

        return super.onOptionsItemSelected(item);
    }


}