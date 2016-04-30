package takeoff.cis350.upenn.edu.takeoff.ui.search;

import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import takeoff.cis350.upenn.edu.takeoff.flight.Flight;

/**
 * Created by anakagold on 4/29/16.
 */
public class CurrentFlightResults {
    private static CurrentFlightResults cs = new CurrentFlightResults();
    private List<Flight> flightResults;

    private CurrentFlightResults() {
        flightResults = new ArrayList<Flight>();
    }

    public static CurrentFlightResults getInstance() {
        return cs;
    }

    public void storeCurrentSearch(List<Flight> c) {
        this.flightResults = c;
    }

    public List<Flight> getCurrentSearch() {
        return this.flightResults;
    }
}
