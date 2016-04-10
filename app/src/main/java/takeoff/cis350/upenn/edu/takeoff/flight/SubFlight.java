package takeoff.cis350.upenn.edu.takeoff.flight;

/**
 * A class representing one of the possible connecting flights (if not the only one) for a  given
 * Flight object
 */
public class SubFlight extends Flight {

    //eg UA93
    String flightNumber;
    String cabinClass;
    int mileage;
    String airline = " ";

    /**
     * Gets the name of the Airline which this subFlight is under
     * @return the Airline name
     */
    public String getAirline() {
        return this.airline;
    }

}
