package takeoff.cis350.upenn.edu.takeoff.flight;

/**
 * Created by dylan on 4/6/16.
 */
public class SubFlight extends Flight {



    //SINGLE FLIGHT/Leg specific values
    String flightNumber;   //eg UA93
    String cabinClass;
    int mileage;
    int flightDuration;
    String airline=" ";

    int duration=4320;
    String departureCityCode=" ";
    String departureDate=" ";    //YYYY-MM-DD
    String departureTime=" ";   //HH-MM
    String arrivalCityCode=" ";
    String arrivalDate=" ";
    String arrivalTime=" ";

    public String getAirline() {
        return this.airline;
    }

}
