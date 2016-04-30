package takeoff.cis350.upenn.edu.takeoff.ui.results;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toolbar;

import java.util.List;

import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.flight.Flight;
import takeoff.cis350.upenn.edu.takeoff.flight.FlightAdapter;
import takeoff.cis350.upenn.edu.takeoff.flight.QPXJSONReader;

/**
 * Created by tangson on 4/28/16.
 */
public class FlightListFragment  extends ListFragment {

    private List<Flight> flightResults;
    private ListView listView;
    private String[] flights;
    private Flight[] flightObjects;
    private boolean initialLoad = true;


    public void loadDashboard() {
        listView = getListView();
        // get the flight information information, if there is any
        flightResults = QPXJSONReader.getFlightResultsFromMostRecentSearch();

        if (flightResults != null && !initialLoad) {
            if (flightResults.size() == 0) {
                // no results; display the messag
                String mesg = getString(R.string.dashboard_no_results);
            }
            else {
                // display a list of the flight information
                int layout1 = R.layout.flight_item_round;
                int layout2 = R.layout.flight_item_round;
                setListAdapter(new FlightAdapter(getActivity(), layout1));
                listView.setAdapter(new FlightAdapter(getActivity(), layout2));
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

}
