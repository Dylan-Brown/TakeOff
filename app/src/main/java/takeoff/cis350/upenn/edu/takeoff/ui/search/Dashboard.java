package takeoff.cis350.upenn.edu.takeoff.ui.search;

import android.support.v4.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;

import takeoff.cis350.upenn.edu.takeoff.ui.WelcomeActivity;
import takeoff.cis350.upenn.edu.takeoff.ui.favorites.FilterSearch;
import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.flight.Flight;
import takeoff.cis350.upenn.edu.takeoff.flight.FlightInfoActivity;
import takeoff.cis350.upenn.edu.takeoff.flight.QPXJSONReader;

/**
 * This class is the list fragment for the dashboard, to display the search results
 */
public class Dashboard extends ListFragment {

    public static List<Flight> FlightCache=new ArrayList<>();
    private List<Flight> flightResults;
    private ListView listView;
    private String[] flights;
    private Flight[] flightObjects;
    private boolean initialLoad = true;

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadDashboard();
    }

    /**
     * Initializes all the variables and decides what to display
     */
    public void loadDashboard() {
        listView = getListView();
        Log.e("Dashboard","LogDashboard");
        // get the flight information information, if there is any
        flightResults = QPXJSONReader.getFlightResultsFromMostRecentSearch();

        if (flightResults != null && !initialLoad) {
            if (flightResults.size() == 0) {
                // no results; display the messag
                String mesg = getString(R.string.dashboard_no_results);
                setToastText(mesg);
            }
            else {
                // display a list of the flight information
                loadFlights();
                int layout1 = android.R.layout.simple_list_item_single_choice;
                int layout2 = android.R.layout.simple_list_item_1;
                setListAdapter(new ArrayAdapter(getActivity(), layout1, flights));
                listView.setAdapter(new ArrayAdapter<>(getActivity(), layout2, flights));
            }
        } else if (initialLoad) {
            // this is the first time the user is entering the dashboard; display nothing
            initialLoad = false;
        }

        // display the toolbar
        Toolbar myToolbar = (Toolbar) getView().findViewById(R.id.my_toolbar);
        getActivity().setActionBar(myToolbar);
        getActivity().getActionBar().setTitle("");
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return rootView;
    }

    /**
     * Display a Toast message containing the relevant text
     * @param message the text to display
     */
    private void setToastText(String message) {
        Toast toast = Toast.makeText(getActivity(),
                message, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * Load the flight results into human readable strings
     * @return the array of human-readable flight strings
     */
    private void loadFlights() {
        // instantiate and populate the arrays
        flights = new String[flightResults.size()];
        flightObjects = new Flight[flightResults.size()];
        for (int i = 0; i < flightResults.size(); i++) {
            flightObjects[i] = flightResults.get(i);
            flights[i] = flightResults.get(i).humanReadable();
        }
    }

    /**
     * Handle the event in which a list item has been clicked
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new  Intent(getActivity(), FlightInfoActivity.class);

        // find the corresponding Flight object
        String text = ((TextView) v).getText().toString();
        Flight flightReferenced = new Flight();
        for (int i = 0; i < flights.length; i++) {
            if (flights[i].equals(text)) {
                flightReferenced = flightObjects[i];
                break;
            }
        }
        String extraMesg = getString(R.string.dashboard_flight_mesg);
        Log.e("Dashboard", "flighReferenced.toString() is " + flightReferenced.toString());
        intent.putExtra(extraMesg, flightReferenced.toString());

        // start the FlightInfoActivity
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu items for use in the action bar
        inflater.inflate(R.menu.menu_dashboard, menu);
    }

    /**
     * Go to the AdvancedFilter activity after the user has selected that menu option
     */
    public void advancedFilter() {
        Log.e("AdvancedFilter", "Here");
        SearchQuery sq = new SearchQuery();

        // make a dummy SearchQuery
        // one slice is direct, 2 slices implies a round trip
        sq.no_of_Slices = 1;
        sq.origin = "PHL";
        sq.destination = "NYC";
        sq.date = "2017-01-01";

        Intent intent = new Intent(getActivity(), FilterSearch.class);
        intent.putExtra("searchQuery", sq.toString());
        startActivity(intent);
        // TODO: Start new activity called FilterSearch
    }

    /**
     * Sorts the list of flights to be displayed on the dashboard.
     * @param feature the feature by which we sort the results
     */
    public void sortBy(int feature) {
        // TODO: Change the sorting algorithm. While bubbleSort is efficient enough for our
        // TODO: purposes, I'd like to not go halfway on this

        if(flightResults != null) {
            // create the auxiliary array to assist sort
            Flight[] array = new Flight[flightResults.size()];
            flightResults.toArray(array);
            int n = array.length;
            int k;

            // sort based on the given feature
            for (int m = n; m >= 0; m--) {
                for (int i = 0; i < n - 1; i++) {
                    k = i + 1;
                    if (compare(i, k, array, feature) < 0) {
                        Flight temp;
                        temp = array[i];
                        array[i] = array[k];
                        array[k] = temp;
                    }
                }
            }

            // finally, display the sorted list
            String[] flightInfo = new String[array.length];
            for (int i = 0; i < array.length; i++) {
                flightInfo[i] = array[i].humanReadable();
            }
            setAdapter(flightInfo);
        }
    }


    /**
     * Get the compareTo value between to Flights based on a specific feature of the Flight class
     * @param feature the parameter to compare
     * @param array the array of flights
     * @param i the index of the first flight to compare
     * @param k the index of the second flight to compare
     * @return the value of the comparison
     */
    private int compare(int i, int k, Flight[] array, int feature) {
        switch (feature) {
            case R.id.sort_by_1:
                // airline
                String iAirline =  array[i].getSubFlights().get(0).getAirline();
                String kAirline =  array[k].getSubFlights().get(0).getAirline();
                return iAirline.compareTo(kAirline);

            case R.id.sort_by_2:
                // cost
                return array[i].getCost() < array[k].getCost() ? 1 : -1;

            case R.id.sort_by_3:
                // departure date
                return array[i].getDepartureDate().compareTo(array[k].getDepartureDate());

            case R.id.sort_by_4:
                // departure city
                return array[i].getDepartureCityCode().compareTo(array[k].getDepartureCityCode());

            case R.id.sort_by_5:
                // arrival city
                return array[i].getArrivalCityCode().compareTo(array[k].getArrivalCityCode());

            case R.id.sort_by_6:
                // arrival date
                return array[i].getArrivalDate().compareTo(array[k].getArrivalDate());

            default:
                // advanced_filter
                return 0;
        }
    }


    /**
     * Set the ListView's adapter to a new adapter, based on the flight info in the array info
     * @param info the String array of human-readable flight information
     */
    private void setAdapter(String[] info) {
        int layout = android.R.layout.simple_list_item_1;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), layout, info);
        listView.setAdapter(adapter);
    }

    /**
     * Gets the List of Flight results from the search
     * @return the List of Flights
     */
    public List<Flight> getFlightResults() {
        return this.flightResults;
    }
}
