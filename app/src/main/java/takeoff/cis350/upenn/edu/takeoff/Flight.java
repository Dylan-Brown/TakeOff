package takeoff.cis350.upenn.edu.takeoff;

import java.util.*;

/**
 * Created by tangson on 2/19/16.
 */

/*
* This is the Flight datatype. Flight segments are sub-flights (i.e. layovers). */

public class Flight {

    static int counter=0;
    boolean isEntry=false;

    String id;  //unique per flight
    boolean isRoundtrip;

    double cost;     //in USD           //trips.tripOption[].saleTotal
    int numOfConnections;
    int duration;

    int oneWayDuration=0;
    String departureCityCode;
    String departureDate;    //YYYY-MM-DD
    String departureTime;   //HH-MM
    String arrivalCityCode;
    String arrivalDate;
    String arrivalTime;

    int roundTripDuration=0;
    String retdepartureCityCode;
    String retdepartureTime;
    String retdepartureDate;
    String retarrivalCityCode;
    String retarrivalDate;
    String retarrivalTime;


    List<Flight> subFlights=new ArrayList<Flight>();

    //SINGLE FLIGHT/Leg specific values
    String flightNumber;   //eg UA93
    String cabinClass;
    int mileage;
    int flightDuration;
}