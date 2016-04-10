package takeoff.cis350.upenn.edu.takeoff.flight;

/**
 * Created by anakagold on 2/25/16.
 *
 * This class makes 20 dummy flight results for testing purposes
 */
public class DummyFlightInfo {

    String[] flightInfo = new String[20];
    Flight[] flights = new Flight[20];

    /**
     * The constructor for  the DummyFlightInfo class. This populates the protected fields with
     * dummy flight information.
     */
    public DummyFlightInfo() {

        int baseCost = 1000;
        for (int i = 0; i < 20; i ++) {
            // randomize the cost for later testing sorting
            baseCost *= Math.random() * 2;

            // add all the relevant dummy information to the new Flight object
            Flight temp = new Flight();
            temp.cost = (float) baseCost;
            temp.id = "id" + i;
            temp.isReturnTrip = false;
            temp.cost = baseCost *= Math.random();
            temp.numOfConnections = 5;
            temp.duration = 2;
            temp.departureCityCode = "PHL";
            temp.departureDate = "01/01/2016";
            temp.departureTime="04.20";   //HH-MM
            temp.arrivalCityCode = "NY";
            temp.arrivalDate = "01/01/2017";
            temp.arrivalTime="14/20";
            temp.retdepartureCityCode="N/A";
            temp.retdepartureTime="N/A";
            temp.retdepartureDate="N/A";
            temp.retarrivalCityCode="N/A";
            temp.retarrivalDate="N/A";
            temp.retarrivalTime="N/A";

            // store the dummy Flight and its String representation
            flights[i] = temp;
            flightInfo[i] = temp.toString();
        }
    }

    /**
     * Gets the array of Flight objects containing dummy information
     * @return the flights array
     */
    public Flight[] getFlights() {
        return this.flights;
    }

    /**
     * Gets the array of String objects that represent the individual dummy flights' information
     * @return the flightInfo array
     */
    public String[] getFlightInfo() {
        return this.flightInfo;
    }
}