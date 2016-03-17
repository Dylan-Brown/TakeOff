package takeoff.cis350.upenn.edu.takeoff;

import java.util.*;

/**
 * Created by tangson on 2/19/16.
 */

/*
* This is the Flight datatype. Flight segments are sub-flights (i.e. layovers). */

public class Flight {
    String id;  //unique per flight

    boolean isRoundtrip;

    String airline;      //##
    double totalCost;     //##           //trips.tripOption[].saleTotal

    String departureCity;
    String departureCityCode;    //##
    String departureCountry;
    String departureDate;    //YYYY-MM-DD
    String departureTime;   //HH-MM

    String arrivalCity;
    String arrivalCityCode;      //##
    String arrivalCountry;
    String arrivalDate;      //##
    String arrivalTime;

    int totalTravelTime;    //in minutes

    boolean isSegment;
    Flight[] segments;

    String flightNumber;   //eg UA93

    String cabinClass;

    int mileage;
}