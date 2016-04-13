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

/**
 * This class is the list fragment for the dashboard, to display the search results
 */
public class Dashboard extends ListFragment {
    public static List<Flight> FlightCache=new ArrayList<Flight>();

    private final Firebase usersRef =
            new Firebase("https://brilliant-inferno-6470.firebaseio.com/users");
    public final static String FLIGHT_MESSAGE = "FlightActual";
    List<Flight> flightResults;
    ListView listView;

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        System.out.println("IN DASHBOARD");
        super.onActivityCreated(savedInstanceState);
        loadDashboard();
    }

    /**
     * Initializes all the variables and decides what to display
     */
    public void loadDashboard() {
        System.out.println("IN DASHBOARD LOADING");
        listView = getListView();

        // get the flight information information if there is any
        flightResults = QPXAPIParser.getFlightResultsFromMostRecentSearch();

        if (flightResults != null) {
            System.out.println("FLIGHTRESULTS NOT NULL");
            //flightResults =new ArrayList<>(Arrays.asList(new DummyFlightInfo().getFlights()));
            // Make flight information human readable
            if (flightResults.size() == 0) {
                //if there are no results, then just say so
                setToastText("No Results");
            }
            else {

                String[] flights = new String[flightResults.size()];
                int i = 0;
                for (Flight f : flightResults) {
                    flights[i++] = f.humanReadable();
                }

                // display a list of the information
                setListAdapter(new ArrayAdapter(getActivity(),
                        android.R.layout.simple_list_item_single_choice, flights));
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_list_item_1, flights);

                listView.setAdapter(adapter);
            }
        } else {
            // no results
            setToastText("No Searches Yet");
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
     * Handle the event in which a list item has been clicked
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new  Intent(getActivity(), FlightInfoActivity.class);
        // parse the TextView to a new Flight for comparsion
        Flight flight = Flight.fromHumanReadable(((TextView) v).getText().toString());

        // determine which Flight in flightResults this refers to
        boolean found = false;
        for (Flight f : flightResults) {
            if (Flight.minimalCompare(f, flight)) {
                intent.putExtra(FLIGHT_MESSAGE, f);
                found = true;
                break;
            }
        }
        if (!found) {
            intent.putExtra(FLIGHT_MESSAGE, flight);
        }
        // start the FlightInfoActivity
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu items for use in the action bar
        inflater.inflate(R.menu.menu_dashboard, menu);
    }

    public void sortByAirline() {
        bubbleSortBy("airline");
    }

    /**
     * Sort the displayed Flights by their cost
     */
    public void sortByCost() {
        bubbleSortBy("cost");
    }

    /**
     * Sort the displayed Flights by their departure date
     */
    public void sortByDepartureDate() {
        bubbleSortBy("dep_date");
    }

    /**
     * Sort the displayed Flights by their departure city
     */
    public void sortByDepartureCity() {
        bubbleSortBy("dep_city");
    }

    /**
     * Sort the displayed Flights by their arrival date
     */
    public void sortByArrivalDate() {
        bubbleSortBy("arr_date");
    }

    /**
     * Sort the displayed Flights by their arrival city
     */
    public void sortByArrivalCity() {
        bubbleSortBy("arr_city");
    }

    /**
     * Display the favorite Flights of the User on the dashboard
     */
    public void sortByFavoriteFlights() {
        // TODO: Remove this option from the menu xml file and here
        bubbleSortBy("fav_flights");
    }

    /**
     * Go to the AdvancedFilter activity after the user has selected that menu option
     */
    public void advancedFilter() {
        Log.e("AdvancedFilter", "Here");
        // TODO: Put search results into firebase
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
    public void bubbleSortBy(String feature) {
        // TODO: Change the sorting algorithm. While bubbleSort is efficient enough for our
        // TODO: purposes, I'd like to not go halfway on this

        // check for null input
        if(flightResults == null) {
            return;
        }

        // create the auxiliary array to assist sort
        Flight[] array = new Flight[flightResults.size()];
        flightResults.toArray(array);
        int n = array.length;
        int k;

        // sort based on the given feature
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
                        if (array[i].getCost() < array[k].getCost()) {
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
                // if there is a logged in user, display list of favorites
                if (usersRef.getAuth() !=  null) {
                    Log.e("Dashboard", "Authorized");

                    String uid = usersRef.getAuth().getUid();
                    usersRef.child(uid).child("favoriteFlights").addListenerForSingleValueEvent(
                            new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    // user has favorites; get this information and replace search results

                                    ArrayList<Object> userFavs = (ArrayList<Object>) snapshot.getValue();
                                    flightResults.clear();
                                    for (Object o : userFavs) {
                                        Flight f = Flight.parseFlight((String) o);
                                        flightResults.add(f);
                                        Log.i("Dashboard", "onDataChange: Flight: " + f.toString());
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
                                    // internally display error message, externally claim nothing found
                                    Log.e("Dashboard", "The read failed: " + firebaseError.getMessage());
                                    Toast toast = Toast.makeText(getActivity(),
                                            "No favorites found.", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                } else {
                    // no authenticated user (guestsession or some error) - no favorites data
                    Toast toast = Toast.makeText(getActivity(),
                            "No favorites found.", Toast.LENGTH_SHORT);
                    toast.show();
                }

                break;

            default:
        }

        // finally, display the sorted list
        String[] flightInfo = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            flightInfo[i] = array[i].humanReadable();
        }
        setAdapter(flightInfo);
    }

    /**
     *
     * @param info
     */
    private void setAdapter(String[] info) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                info);
        listView.setAdapter(adapter);
    }

    /**
     * Gets the Flight in the Flight results List corresponding to the given ID
     * @param id the unique id of the flight
     * @return the Flight if it is found, null otherwise
     */
    @SuppressWarnings("unused")
    private Flight getFlightByID(String id) {
        for (Flight f : flightResults) {
            if (f.getId().equals(id)) { return f; }
        }
        return null;
    }

    /**
     * Gets the List of Flight results from the search
     * @return the List of Flights
     */
    public List<Flight> getFlightResults() {
        return this.flightResults;
    }
}
