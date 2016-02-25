//DUMMY FLIGHT INFO
package takeoff.cis350.upenn.edu.takeoff;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by anakagold on 2/25/16.
 *
 * This class essentially makes 20 dummy flight results information
 */
public class DummyFlightInfo {

    String[] flights = new String[20];
    Flight[] flightinfo = new Flight[20];

    DummyFlightInfo() {
        Flight temp = new Flight();
        temp.airline = "Airline";
        temp.totalCost = (float)100.0;
        temp.departureCityCode = "PHL";
        temp.departureDate = "01/01/2016";
        temp.arrivalDate = "01/01/2017";
        temp.arrivalCityCode = "NY";

        for (int i = 0; i < 20; i ++) {
            flights[i] = "Airline : " + temp.airline + '\n';
            flights[i] += "Total Cost : " + temp.totalCost + '\n';
            flights[i] += "Departure City : " + temp.departureCityCode + '\n';
            flights[i] += "Departure Date : " + temp.departureDate + '\n';
            flights[i] += "Arrival City : " + temp.arrivalCityCode + '\n';
            flights[i] += "Arrival Date : " + temp.arrivalDate + '\n';
            flightinfo[i] = temp;
        }
    }
}