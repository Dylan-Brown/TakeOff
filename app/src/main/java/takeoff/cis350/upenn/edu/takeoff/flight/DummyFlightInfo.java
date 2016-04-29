package takeoff.cis350.upenn.edu.takeoff.flight;

/**
 * This class makes 20 dummy flight results for testing purposes
 * TODO: Decide if we are going to remove this
 */
public class DummyFlightInfo {

    private String[] flightInfo = new String[20];
    private Flight[] flights = new Flight[20];

    /**
     * The constructor for the DummyFlightInfo class. This populates the protected fields with
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
            temp.retDepartureCityCode="N/A";
            temp.retDepartureTime="N/A";
            temp.retDepartureDate="N/A";
            temp.retArrivalCityCode="N/A";
            temp.retArrivalDate="N/A";
            temp.retArrivalTime="N/A";

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