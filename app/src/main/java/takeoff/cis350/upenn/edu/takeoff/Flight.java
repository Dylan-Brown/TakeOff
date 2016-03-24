package takeoff.cis350.upenn.edu.takeoff;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.*;
import java.util.*;

/**
 * Created by tangson on 2/19/16.
 */

/*
* This is the Flight datatype. Flight segments are sub-flights (i.e. layovers). */

public class Flight implements Serializable {
    //Flight is one level recursive.
    //The master Flight (i.e. the Ticket) is composed of individual flights (eg connections, roundtrips)
    //If the flight is the master flight, then isEntry is set to true.
    boolean isEntry=false;
    List<Flight> subFlights=new ArrayList<Flight>();

    String id="";  //unique per flight
    boolean isRoundtrip=false;

    double cost=99999.99;     //in USD           //trips.tripOption[].saleTotal
    int numOfConnections=5;
    int duration;

    String airline="";

    int oneWayDuration=4320;
    String departureCityCode="";
    String departureDate="";    //YYYY-MM-DD
    String departureTime="";   //HH-MM
    String arrivalCityCode="";
    String arrivalDate="";
    String arrivalTime="";
    //Only if there is a return flight
    int roundTripDuration=4320;
    String retdepartureCityCode="";
    String retdepartureTime="";
    String retdepartureDate="";
    String retarrivalCityCode="";
    String retarrivalDate="";
    String retarrivalTime="";


    //SINGLE FLIGHT/Leg specific values
    String flightNumber;   //eg UA93
    String cabinClass;
    int mileage;
    int flightDuration;

    @Override
    public String toString() {
        return "id: + " + id + ", departureCityCode = " + departureCityCode
                + ", departureDate = " + departureDate + ", departureTime = " + departureTime
                + ", arrivalCityCode = " + arrivalCityCode + ", arrivalDate = " + arrivalDate
                + ", arrivalTime = " + arrivalTime + ", flightNumber = " + flightNumber
                + ", cabinClass = " + cabinClass;
    }

}

