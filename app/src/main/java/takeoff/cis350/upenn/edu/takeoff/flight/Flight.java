package takeoff.cis350.upenn.edu.takeoff.flight;

import android.util.Log;

import java.io.*;
import java.util.*;

/**
 * This is the Flight datatype. Flight segments are sub-flights (i.e. layovers, connecting flights),
 * which are an extension of Flight. The master Flight (i.e. the Ticket) is composed of individual
 * flights (eg connections, roundtrips). If the flight is the master flight, then isEntry is set to
 * true.Flight is one level recursive.
 */
public class Flight implements Serializable {
    //The delimiter used to split the flight information provided
    static String delimiter = "-";

    //----Default values of Flight class variables---
    ArrayList<SubFlight> subFlights = new ArrayList<SubFlight>();

    public String id = "";              // unique per flight
    boolean isReturnTrip = false;
    boolean isDirectFlight = false;
    double cost = 99999.99;             // in USD

    int duration = 0;                   // duration of the Flight in minutes
    int numOfConnections = 0;           // number of one-way connections

    // these variables are for the departing flight
    String departureCityCode = "";      // XYZ
    String departureDate = "";          // YYYY-MM-DD
    String departureTime = "";          // HH-MM
    String arrivalCityCode = "";        // XYZ
    String arrivalDate = "";            // YYYY-MM-DD
    String arrivalTime = "";            // HH-MM

    // these variables are for the return flight
    int retDuration = 0;                // duration of the Flight in minutes
    int retNumOfConnections = 0;        // number of one-way connections
    String retDepartureCityCode = "";   // XYZ
    String retDepartureDate = "";       // YYYY-MM-DD
    String retDepartureTime = "";       // HH-MM
    String retArrivalCityCode = "";     // XYZ
    String retArrivalDate = "";         // YYYY-MM-DD
    String retArrivalTime = "";         // HH-MM

    /**
     * Get the SubFlights of this Flight
     * @return the SubFlights
     */
    public ArrayList<SubFlight> getSubFlights() {
        return this.subFlights;
    }

    /**
     * Description: Get the number of connections of this Flight
     * @return the number of connections
     */
    public int getNumOfConnections() {
        return this.numOfConnections;
    }

    /**
     * Description: Get the boolean representing if this Flight is a return Flight
     * @return the boolean
     */
    public boolean isReturnTrip() {
        return this.isReturnTrip;
    }

    /**
     * Description: Get the boolean representing if this Flight is a direct Flight
     * @return the boolean indicating if its a direct flight
     */
    public boolean isDirectFlight() {
        return this.isDirectFlight;
    }

    /**
     * Description: Get the unique ID of this Flight
     * @return the unique ID
     */
    public String getId() {
        return this.id;
    }

    /**
     * Description: Get the city code of the city from which this Flight departs
     * @return the city code
     */
    public String getDepartureCityCode() {
        return this.departureCityCode;
    }

    /**
     * Description: Get the date on which this Flight departs
     * @return the date
     */
    public String getDepartureDate() {
        return this.departureDate;
    }

    /**
     * Description: Get the time at which this Flight departs
     * @return the time
     */
    public String getDepartureTime() {
        return this.departureTime;
    }

    /**
     * Description: Get the city code of the city at which this Flight arrives
     * @return the city code
     */
    public String getArrivalCityCode() {
        return this.arrivalCityCode;
    }

    /**
     * Description: Get the date on which this Flight arrives
     * @return the date
     */
    public String getArrivalDate() {
        return this.arrivalDate;
    }

    /**
     * Description: Get the time at which this Flight arrives
     * @return the time
     */
    public String getArrivalTime() {
        return this.arrivalTime;
    }

    /**
     * Description: Get the cost of this Flight
     * @return the cost
     */
    public double getCost() {
        return this.cost;
    }

    /**
     * Description: Turns a Flight object into a String that contains the basic information
     * about the Flight
     * @return a human readable String
     */
    public String humanReadable() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cost: " + this.cost + "\n");
        sb.append("Departure City: " + this.departureCityCode + "\n");
        sb.append("Departure Date: " + this.departureDate.replace("X", "-") + "\n");
        sb.append("Arrival City: " + this.arrivalCityCode + "\n");
        sb.append("Arrival Date: " + this.arrivalDate.replace("X", "-") + "\n");
        sb.append("id: " + this.id);
        return sb.toString();
    }

    /**
     * Description: Compares two Flights; the id field is assumed to be unique among Flights
     * and is the only field necessary to compare.
     * @return true if the id of this  flight and the id of o are the same
     */
    @Override
    public boolean equals(Object o) {
        // assert o is not null and is a Flight class instance
        if (o == null || !o.getClass().equals(Flight.class)) {
            return false;
        }
        Flight f = (Flight) o;

        // compare the ids of the Flights
        return f.id.equals(this.id);
    }

    /**
     * Description: Takes a string representation of a Flight and parses it into a new Flight
     * object
     * @param o the string representing the flight
     * @return the new Flight object
     */
    public static Flight parseFlight(String o) {
        // assert o is not null and is a Flight class instance
        if (o == null) {
            return new Flight();
        }
        String[] info = o.split(delimiter);

        Flight f = new Flight();
        if (info.length <= 1) {
            Log.e("Flight", "parse....");
            return new Flight();
        }

        Log.e("Parse", "" + o.indexOf(delimiter));

        // parse the instance variables
        f.id = info[0];
        f.isReturnTrip = Boolean.parseBoolean(info[1]);
        f.isDirectFlight = Boolean.parseBoolean(info[2]);
        f.cost = Double.parseDouble(info[3]);
        f.duration = Integer.parseInt(info[4]);
        f.numOfConnections = Integer.parseInt(info[5]);
        f.departureCityCode = info[6];
        f.departureDate = info[7].replace("X", "-");
        f.departureTime = info[8].replace("X", "-");
        f.arrivalCityCode = info[9];
        f.arrivalDate = info[10].replace("X", "-");
        f.arrivalTime = info[11].replace("X", "-");
        f.retDuration = Integer.parseInt(info[12]);
        f.retNumOfConnections = Integer.parseInt(info[13]);
        f.retDepartureCityCode = info[14];
        f.retDepartureDate = info[15].replace("X", "-");
        f.retDepartureTime = info[16].replace("X", "-");
        f.retArrivalCityCode = info[17];
        f.retArrivalDate = info[18].replace("X", "-");
        f.retArrivalTime = info[19].replace("X", "-");

        // parse each SubFlight
        for (int i = 20; i < info.length; i++) {
            f.subFlights.add(SubFlight.parseSubFlight(info[i]));
        }
        return f;
    }

    /**
     * Description: Generate a string based on the Flight's instance variables that can later
     * be parsed into another Flight instance
     * @return the string containing the instance variables
     */
    @Override
    public String toString() {
        String s = id;
        s += delimiter + isReturnTrip;
        s += delimiter + isDirectFlight;
        s += delimiter + cost;
        s += delimiter + duration;
        s += delimiter + numOfConnections;
        s += delimiter + departureCityCode;
        s += delimiter + departureDate;
        s += delimiter + departureTime;
        s += delimiter + arrivalCityCode;
        s += delimiter + arrivalDate;
        s += delimiter + arrivalTime;
        s += delimiter + retDuration;
        s += delimiter + retNumOfConnections;
        s += delimiter + retDepartureCityCode;
        s += delimiter + retDepartureDate;
        s += delimiter + retDepartureTime;
        s += delimiter + retArrivalCityCode;
        s += delimiter + retArrivalDate;
        s += delimiter + retArrivalTime;
        for (SubFlight sf :  subFlights) {
            s += delimiter + sf.toString();
        }
        return s;
    }

}
