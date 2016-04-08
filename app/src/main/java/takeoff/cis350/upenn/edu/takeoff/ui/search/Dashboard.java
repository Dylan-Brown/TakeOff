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

public class Dashboard extends ListFragment {

    private final Firebase usersRef =
            new Firebase("https://brilliant-inferno-6470.firebaseio.com/users");
    public final static String FLIGHT_MESSAGE = "FlightActual";
    List<Flight> flightResults;
    ListView l;

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
        l = getListView();

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
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                        flights);

                l.setAdapter(adapter);
            }
        } else {
            //Print grey font, saying no results
            System.out.println("FLIGHTRESULTS NULL");
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

    //This message prints the background text according to either 1. no searches yet or 2. no results
    private void setToastText(String message) {
        Toast toast = Toast.makeText(getActivity(),
                message, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new  Intent(getActivity(), FlightInfoActivity.class);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu items for use in the action bar
        inflater.inflate(R.menu.menu_dashboard, menu);
    }

    public void sortByAirline() {
        bubbleSortBy("airline");
    }

    public void sortByCost() {
        bubbleSortBy("cost");
    }

    public void sortByDepartureDate() {
        bubbleSortBy("dep_date");
    }

    public void sortByDepartureCity() {
        bubbleSortBy("dep_city");
    }

    public void sortByArrivalDate() {
        bubbleSortBy("arr_date");
    }

    public void sortByArrivalCity() {
        bubbleSortBy("arr_city");
    }

    public void sortByFavoriteFlights() {
        bubbleSortBy("fav_flights");
    }

    public void advancedFilter() {
        Log.e("AdvancedFilter", "Here");
        // TODO: Put search results into firebase
        SearchQuery sq = new SearchQuery();


        // Makiing Dummy Search Query
        sq.no_of_Slices=1; //number of total trips; one-way is 1 slice, roundtrip is 2 slices
        sq.origin="PHL";
        sq.destination="NYC";
        sq.date="2017-01-01"; //in the format of YYYY-MM-DD

        Intent intent = new Intent(getActivity(), FilterSearch.class);
        intent.putExtra("searchQuery", sq.toString());
        startActivity(intent);
        // TODO: Start new activity called FilterSearch
    }

    public void bubbleSortBy(String feature) {
        if(flightResults == null) {
            return;
        }
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
                                    Toast toast = Toast.makeText(getActivity(),
                                            "No favorites found.", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                } else {
                    // No authenticated user (guestsession or some error) - no favorites data
                    Toast toast = Toast.makeText(getActivity(),
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
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
