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
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import takeoff.cis350.upenn.edu.takeoff.ui.WelcomeActivity;
import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.flight.Flight;

import takeoff.cis350.upenn.edu.takeoff.flight.FlightAdapter;
import takeoff.cis350.upenn.edu.takeoff.ui.results.FlightInfoActivity;
import takeoff.cis350.upenn.edu.takeoff.flight.QPXJSONReader;
import java.util.*;

/**
 * Description:
 * This class is the list fragment for the dashboard, to display the search results
 */
public class Dashboard extends ListFragment {

    public static ArrayList<Flight> FlightCache=new ArrayList<>();
    public static Map<String,Flight> FlightCacheById=new HashMap<>();
    private List<Flight> flightResults;
    private ListView listView;
    private String[] flights;
    private Flight[] flightObjects;
    private boolean initialLoad = true;
    private FlightAdapter adapter;

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
        flightResults = QPXJSONReader.getFlightResultsFromMostRecentSearch();

        if (flightResults != null && !initialLoad) {
            if (flightResults.size() != 0) {
                // display a list of the flight information
                loadFlights();
                int layout1 =R.layout.flight_item_one;

                adapter = new FlightAdapter(getActivity().getApplicationContext(), layout1);
                for (Flight flight : Dashboard.FlightCache) {
                    adapter.add(flight);
                }
                listView.setAdapter(adapter);

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
     * Description: Display a Toast message containing the relevant text
     * @param message the text to display
     */
    private void setToastText(String message) {
        Toast toast = Toast.makeText(getActivity(),
                message, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * Description: Load the flight results into human readable strings
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
     * Description: Handle the event in which a list item has been clicked
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new  Intent(getActivity(), FlightInfoActivity.class);
        // find the corresponding Flight object
        Flight f = (Flight) adapter.getItem(position);
        String flightTostring = f.toString();

        //Prepare a bundle/intent
        String extraMesg = getString(R.string.dashboard_flight_mesg);
        intent.putExtra(extraMesg, flightTostring);

        // start the FlightInfoActivity
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu items for use in the action bar
        inflater.inflate(R.menu.menu_dashboard, menu);
    }

    /**
     * Description: Go to the AdvancedFilter activity after the user has selected that menu option
     */
    public void advancedFilter() {
        SearchQuery sq = new SearchQuery();

        // make a dummy SearchQuery; one slice is direct, 2 slices implies a round trip
        sq.no_of_Slices = 1;
        sq.origin = "PHL";
        sq.destination = "NYC";
        sq.date = "2017-01-01";

        Intent intent = new Intent(getActivity(), FilterSearch.class);
        startActivity(intent);
    }

    /**
     * Description: Sorts the list of flights to be displayed on the dashboard.
     * @param feature the feature by which we sort the results
     */
    public void sortBy(int feature) {
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
     * Description: Set the ListView's adapter to a new adapter, based on the flight info in the array info
     * @param info the String array of human-readable flight information
     */
    protected void filterAdapter(Flight[] info) {
        String input[] = new String[info.length];
        int layout = android.R.layout.simple_list_item_1;
        for (int i = 0 ; i < info.length; i++) {
            input[i] = info[i].toString();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), layout, input);
        listView.setAdapter(adapter);
        listView.invalidate();
    }


    /**
     * Description: Set the ListView's adapter to a new adapter, based on the flight info in the array info
     * @param info the String array of human-readable flight information
     */
    private void setAdapter(String[] info) {
        int layout = android.R.layout.simple_list_item_1;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), layout, info);
        listView.setAdapter(adapter);
    }
}
