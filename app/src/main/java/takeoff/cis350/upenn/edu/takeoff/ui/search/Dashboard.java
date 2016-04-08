package takeoff.cis350.upenn.edu.takeoff.ui.search;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
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

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import takeoff.cis350.upenn.edu.takeoff.ui.favorites.FilterSearch;
import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.flight.Flight;
import takeoff.cis350.upenn.edu.takeoff.flight.FlightInfoActivity;
import takeoff.cis350.upenn.edu.takeoff.flight.QPXAPIParser;

public class Dashboard extends ListActivity {

    private final Firebase usersRef =
            new Firebase("https://brilliant-inferno-6470.firebaseio.com/users");
    public final static String FLIGHT_MESSAGE = "FlightActual";
    List<Flight> flightResults;
    ListView l;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        l = getListView();

        // get the flight information information
        // TODO: Get real flight information; right now it is dummy information
        flightResults = QPXAPIParser.getFlightResultsFromMostRecentSearch();
        //flightResults =new ArrayList<>(Arrays.asList(new DummyFlightInfo().getFlights()));
        // Make flight information human readable

        String[] flights;
        if (flightResults != null) {
            flights = new String[flightResults.size()];
            int i = 0;
            for (Flight f : flightResults) {
                flights[i++] = f.humanReadable();
            }
        } else {
            flights = new String[0];
        }



        // not sure what this does, commenting out for now
        /*Log.e("first", "Dashboard");
        try {
            flightResults = QPXAPIParser.getAPIResultsAsFlight();
        } catch (Exception e) {
        }*/

        // display a list of the information
        setListAdapter(new ArrayAdapter(this,
                android.R.layout.simple_list_item_single_choice, flights));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                flights);
        l.setAdapter(adapter);

        // display the toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setActionBar(myToolbar);
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new  Intent (this, FlightInfoActivity.class);

        // Parse the TextView to a new Flight for comparsion
        Flight flight = Flight.fromHumanReadable(((TextView) v).getText().toString());

        // Determine which Flight in flightResults this refers to
        boolean found = false;
        for (Flight f : flightResults) {
            if (Flight.minimalCompare(f, flight)) {
                // This is the flight; pass it and it's fields
                intent.putExtra(FLIGHT_MESSAGE, f);
                found = true;
                break;
            }
        }
        if (!found) {
            intent.putExtra(FLIGHT_MESSAGE, flight);
        }

        // Start the FlightInfoActivity
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void sortByAirline(MenuItem item) {
        bubbleSortBy("airline");
    }

    public void sortByCost(MenuItem item) {
        bubbleSortBy("cost");
    }

    public void sortByDepartureDate(MenuItem item) {
        bubbleSortBy("dep_date");
    }

    public void sortByDepartureCity(MenuItem item) {
        bubbleSortBy("dep_city");
    }

    public void sortByArrivalDate(MenuItem item) {
        bubbleSortBy("arr_date");
    }

    public void sortByArrivalCity(MenuItem item) {
        bubbleSortBy("arr_city");
    }

    public void sortByFavoriteFlights(MenuItem item) {
        bubbleSortBy("fav_flights");
    }

    public void advancedFilter(MenuItem item) {
        Log.e("AdvancedFilter", "Here");
        // TODO: Put search results into firebase
        SearchQuery sq = new SearchQuery();


        // Makiing Dummy Search Query
        sq.no_of_Slices=1; //number of total trips; one-way is 1 slice, roundtrip is 2 slices
        sq.origin="PHL";
        sq.destination="NYC";
        sq.date="2017-01-01"; //in the format of YYYY-MM-DD

        Intent intent = new Intent(this, FilterSearch.class);
        intent.putExtra("searchQuery", sq.toString());
        startActivity(intent);

        // TODO: Start new activity called FilterSearch
    }

    private void bubbleSortBy(String feature) {
        Flight[] array = new Flight[flightResults.size()];
        flightResults.toArray(array);
        int n = array.length;
        int k;
        switch (feature) {
            case "airline":
                for (int m = n; m >= 0; m--) {
                    for (int i = 0; i < n - 1; i++) {
                        k = i + 1;
                        if (array[i].getSubFlights().get(0).getAirline().compareTo(array[k].getSubFlights().get(0).getAirline()) < 0) {
                            Flight temp;
                            temp = array[i];
                            array[i] = array[k];
                            array[k] = temp;
                        }
                    }
                }
                break;
            case "cost":
                for (int m = n; m >= 0; m--) {
                    for (int i = 0; i < n - 1; i++) {
                        k = i + 1;
                        if (array[i].getCost() > array[k].getCost()) {
                            Flight temp;
                            temp = array[i];
                            array[i] = array[k];
                            array[k] = temp;
                        }
                    }
                }
                break;
            case "dep_date":
                for (int m = n; m >= 0; m--) {
                    for (int i = 0; i < n - 1; i++) {
                        k = i + 1;
                        if (array[i].getDepartureDate().compareTo(array[k].getDepartureDate()) < 0) {
                            Flight temp;
                            temp = array[i];
                            array[i] = array[k];
                            array[k] = temp;
                        }
                    }
                }
                break;
            case "dep_city":
                // depart city
                for (int m = n; m >= 0; m--) {
                    for (int i = 0; i < n - 1; i++) {
                        k = i + 1;
                        if (array[i].getDepartureCityCode().compareTo(array[k].getDepartureCityCode()) < 0) {
                            Flight temp;
                            temp = array[i];
                            array[i] = array[k];
                            array[k] = temp;
                        }
                    }
                }
                break;
            case "arr_date":
                for (int m = n; m >= 0; m--) {
                    for (int i = 0; i < n - 1; i++) {
                        k = i + 1;
                        if (array[i].getArrivalDate().compareTo(array[k].getArrivalDate()) < 0) {
                            Flight temp;
                            temp = array[i];
                            array[i] = array[k];
                            array[k] = temp;
                        }
                    }
                }
                break;
            case "arr_city":
                for (int m = n; m >= 0; m--) {
                    for (int i = 0; i < n - 1; i++) {
                        k = i + 1;
                        if (array[i].getArrivalCityCode().compareTo(array[k].getArrivalCityCode()) < 0) {
                            Flight temp;
                            temp = array[i];
                            array[i] = array[k];
                            array[k] = temp;
                        }
                    }
                }
            case "fav_flights":
                // If there is a logged in user, display list of favorites
                if (usersRef.getAuth() !=  null) {
                    Log.e("Dashboard", "Authorized");

                    String uid = usersRef.getAuth().getUid();
                    usersRef.child(uid).child("favoriteFlights").addListenerForSingleValueEvent(
                            new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            // User has favorites; get this information and replace search results
                            Log.e("Dashboard", "onDataChange");

                            ArrayList<Object> userFavs = (ArrayList<Object>) snapshot.getValue();
                            flightResults.clear();
                            for (Object o : userFavs) {
                                Flight f = Flight.parseFlight((String) o);
                                flightResults.add(f);
                                Log.e("Dashboard", "onDataChange: Flight: " + f.toString());
                            }
                            String[] flightInfo = new String[flightResults.size()];
                            int i = 0;
                            for (Flight f : flightResults) {
                                flightInfo[i++] = f.humanReadable();
                            }
                            setAdapter(flightInfo);
                            // TODO: flightInfo is the humanReadable list of favorites, display it.
                        }
                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            // Internally display error message, externally claim nothing found
                            Log.e("Dashboard", "The read failed: " + firebaseError.getMessage());
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "No favorites found.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                } else {
                    // No authenticated user (guestsession or some error) - no favorites data
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "No favorites found.", Toast.LENGTH_SHORT);
                    toast.show();
                }

                break;

            default:
        }

        String[] flightInfo = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            flightInfo[i] = array[i].humanReadable();
        }
        setAdapter(flightInfo);
    }

    private void setAdapter(String[] info) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                info);
        l.setAdapter(adapter);
    }

    @SuppressWarnings("unused")
    private Flight getFlightByID(String id) {
        for (Flight f : flightResults) {
            if (f.getId().equals(id)) { return f; }
        }
        return null;
    }
}
