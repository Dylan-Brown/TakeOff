//DUMMY FLIGHT INFO
package takeoff.cis350.upenn.edu.takeoff;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by anakagold on 2/25/16.
 *
 * This class essentially makes 20 dummy flight results information
 */
public class DummyFlightInfo {

    String [] flightinfo = new String[20];
    Flight[] flights = new Flight[20];

    // DYLAN: Updated to conform to flight string parameters, less confusing
    public DummyFlightInfo() {
        int basecost = 1000;
        for (int i = 0; i < 20; i ++) {
            Flight temp = new Flight();
            temp.airline = "UA";
            basecost *= Math.random() * 2;
            temp.cost = (float)basecost;
            temp.id = "id" + i;
            temp.isReturnTrip= false;
            temp.cost = basecost *= Math.random();
            temp.numOfConnections = 5;
            temp.duration = 2;
            temp.airline = "Delta";
            temp.oneWayDuration=4320;
            temp.departureCityCode = "PHL";
            temp.departureDate = "01/01/2016";
            temp.departureTime="04.20";   //HH-MM
            temp.arrivalCityCode = "NY";
            temp.arrivalDate = "01/01/2017";
            temp.arrivalTime="14/20";
            temp.roundTripDuration=4320;
            temp.retdepartureCityCode="N/A";
            temp.retdepartureTime="N/A";
            temp.retdepartureDate="N/A";
            temp.retarrivalCityCode="N/A";
            temp.retarrivalDate="N/A";
            temp.retarrivalTime="N/A";
            temp.flightNumber = "W420";   //eg UA93
            temp.cabinClass = "coach";
            temp.mileage = 302;
            temp.flightDuration = 0;
            flights[i] = temp;
            flightinfo[i] = temp.toString();
        }
    }

    public Flight[] getFlights() {
        return this.flights;
    }

    public String[] getFlightInfo() {
        return this.flightinfo;
    }
}