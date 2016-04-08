package takeoff.cis350.upenn.edu.takeoff.flight;

/**
 * Created by dylan on 4/6/16.
 */
public class SubFlight extends Flight {



    //SINGLE FLIGHT/Leg specific values
    String flightNumber;   //eg UA93
    String cabinClass;
    int mileage;
    String airline=" ";

    public String getAirline() {
        return this.airline;
    }

}
