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

public class Dashboard extends ListActivity {
    List<Flight> flightResults;
    DummyFlightInfo flights = new DummyFlightInfo();
    String[] flight = flights.flights;

    //This is the peice of information that I should be receiving while someone calls this activity.
    Flight[] flightinfo = flights.flightinfo;
    ListView l;

    public final static String EXTRA_MESSAGE1 = "Flight";
    public final static String EXTRA_MESSAGE2 = "FavFlight";

    public Dashboard() {
        //Default Constructor
    }

    public Dashboard(Flight f[]) {
        // here the array f is the list of flights
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Dashboard");
        /*Log.e("first", "Dashboard");
        try {
            flightResults = QPXAPIParser.getAPIResultsAsFlight();
        } catch (Exception e) {
        }*/
        Log.e("QPIXParser", "Dashboard");
        super.onCreate(savedInstanceState);
        Log.e("savedInstanceState", "Dashboard");
        setContentView(R.layout.activity_dashboard);
        l = getListView();
        Log.e("getListView", "Dashboard");
        setListAdapter(new ArrayAdapter(this,
                android.R.layout.simple_list_item_single_choice, flight));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, flight);
        l.setAdapter(adapter);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setActionBar(myToolbar);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        TextView temp = (TextView) v;
        // Toast.makeText(this,""+temp.getText() + " " + position, Toast.LENGTH_SHORT).show();

        // here i want to go to another activity that displays the information of individual flights
        /*here I should pass the information of the flight clicked and also the Favorites list so I
        can have a copy of that.
         */
        Intent intent = new  Intent (this, FlightInfoActivity.class);
        intent.putExtra(EXTRA_MESSAGE1, temp.getText());
        /// We want to pass the information of the flight.
        // We also want to pass a Set of favorited flights

        /** All code below here are temporary. This needs to be accessible from Flight info page */
        /*String flight_details = temp.getText().toString();
        String[] fd = flight_details.split("\n");
        String depart = fd[2].split(":")[1].trim();
        String arrive = fd[4].split(":")[1].trim();
        String f = fd[3].split(":")[1].trim();
        String r = fd[5].split(":")[1].trim();
        String url = "https://www.google.com/flights/#search;f="+depart+",ZFV;t="+arrive+";";//d=2016-04-08";*/
        // I only want from, to, leaving, returning

        //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        //startActivity(browserIntent);

        System.out.println("Entering the flight activity. Not sure how to pass information");
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void sortByAirline(MenuItem item) {
        bubbleSortBy(0);
    }

    public void sortByCost(MenuItem item) {
        bubbleSortBy(1);
    }

    public void sortByDepartureDate(MenuItem item) {
        bubbleSortBy(2);
    }

    public void sortByDepartureCity(MenuItem item) {
        bubbleSortBy(3);
    }

    public void sortByArrivalDate(MenuItem item) {
        bubbleSortBy(4);
    }

    public void sortByArrivalCity(MenuItem item) {
        bubbleSortBy(5);
    }

    public void sortByFavoriteFlights(MenuItem item) {
        bubbleSortBy(6);
    }

    private void bubbleSortBy(int feature) {
        Flight[] array = new Flight[flightinfo.length];
        for (int i = 0; i < flightinfo.length; i++) {
            array[i] = flightinfo[i];
        }
        int n = flight.length;
        int k;

        switch (feature) {
            case 0: // airline'
                for (int m = n; m >= 0; m--) {
                    for (int i = 0; i < n - 1; i++) {
                        k = i + 1;
                        if (array[i].airline.compareTo(array[k].airline) < 0) {
                            Flight temp;
                            temp = array[i];
                            array[i] = array[k];
                            array[k] = temp;
                        }
                    }
                }
                break;

            case 1: // cost
                for (int m = n; m >= 0; m--) {
                    for (int i = 0; i < n - 1; i++) {
                        k = i + 1;
                        if (array[i].cost > array[k].cost) {
                            Flight temp;
                            temp = array[i];
                            array[i] = array[k];
                            array[k] = temp;
                        }
                    }
                }
                break;

            case 2: // depart date
                for (int m = n; m >= 0; m--) {
                    for (int i = 0; i < n - 1; i++) {
                        k = i + 1;
                        if (array[i].departureDate.compareTo(array[k].departureDate) < 0) {
                            Flight temp;
                            temp = array[i];
                            array[i] = array[k];
                            array[k] = temp;
                        }
                    }
                }
                break;

            case 3: // depart city
                for (int m = n; m >= 0; m--) {
                    for (int i = 0; i < n - 1; i++) {
                        k = i + 1;
                        if (array[i].departureCityCode.compareTo(array[k].departureCityCode) < 0) {
                            Flight temp;
                            temp = array[i];
                            array[i] = array[k];
                            array[k] = temp;
                        }
                    }
                }
                break;

            case 4: // arrive date
                for (int m = n; m >= 0; m--) {
                    for (int i = 0; i < n - 1; i++) {
                        k = i + 1;
                        if (array[i].arrivalDate.compareTo(array[k].arrivalDate) < 0) {
                            Flight temp;
                            temp = array[i];
                            array[i] = array[k];
                            array[k] = temp;
                        }
                    }
                }
                break;

            case 6:  //  view favorites

                Log.e("Dashboard", "Going into  favorites list");

                final Firebase usersRef =
                        new Firebase("https://brilliant-inferno-6470.firebaseio.com/users");
                if (usersRef.getAuth() !=  null) {
                    String uid = usersRef.getAuth().getUid();
                    usersRef.child(uid).child("favoriteFlights").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            Map<String, Object> userFavs = (Map<String, Object>) snapshot.getValue();
                            Log.e("Dashboard", "The read succeeded!!!");

                            // TODO: userFavs is a map from strings to flights. Display this information
                            // the objects must be cast to the  flight type

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            Log.e("Dashboard", "The read failed: " + firebaseError.getMessage());

                            // TODO: Display some message if reading the favorite flights failed

                        }
                    });
                } else {

                    // TODO: What do  we do if a guest user tries to view favorite flights??

                }



                break;

            default: // arrive city
                for (int m = n; m >= 0; m--) {
                    for (int i = 0; i < n - 1; i++) {
                        k = i + 1;
                        if (array[i].arrivalCityCode.compareTo(array[k].arrivalCityCode) < 0) {
                            Flight temp;
                            temp = array[i];
                            array[i] = array[k];
                            array[k] = temp;
                        }
                    }
                }
        }
        flightinfo = array;

        for (int i = 0; i < array.length; i++) {
            Flight temp = array[i];
            flight[i] = "Airline : " + temp.airline + '\n';
            flight[i] += "Total Cost : " + temp.cost + '\n';
            flight[i] += "Departure City : " + temp.departureCityCode + '\n';
            flight[i] += "Departure Date : " + temp.departureDate + '\n';
            flight[i] += "Arrival City : " + temp.arrivalCityCode + '\n';
            flight[i] += "Arrival Date : " + temp.arrivalDate + '\n';
            flightinfo[i] = temp;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, flight);
        l.setAdapter(adapter);
    }


    private Flight getFlightByID(String id) {
        for (Flight f : flightResults) {
            if (f.id==id){return f;}
        }
        return null;
    }
}
