package takeoff.cis350.upenn.edu.takeoff;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

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
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, flight);
        l.setAdapter(adapter);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setActionBar(myToolbar);
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
                        if (array[i].totalCost > array[k].totalCost) {
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
                        if (array[i].departureCity.compareTo(array[k].departureCity) < 0) {
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

            default: // arrive city
                for (int m = n; m >= 0; m--) {
                    for (int i = 0; i < n - 1; i++) {
                        k = i + 1;
                        if (array[i].arrivalCity.compareTo(array[k].arrivalCity) < 0) {
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
            flight[i] += "Total Cost : " + temp.totalCost + '\n';
            flight[i] += "Departure City : " + temp.departureCityCode + '\n';
            flight[i] += "Departure Date : " + temp.departureDate + '\n';
            flight[i] += "Arrival City : " + temp.arrivalCityCode + '\n';
            flight[i] += "Arrival Date : " + temp.arrivalDate + '\n';
            flightinfo[i] = temp;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, flight);
        l.setAdapter(adapter);
    }
}
