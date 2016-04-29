package takeoff.cis350.upenn.edu.takeoff.flight;

import java.io.*;
import java.util.*;

import takeoff.cis350.upenn.edu.takeoff.ui.results.*;

/**
 * This is the Flight datatype. Flight segments are sub-flights (i.e. layovers, connecting flights),
 * which are an extension of Flight. The master Flight (i.e. the Ticket) is composed of individual
 * flights (eg connections, roundtrips). If the flight is the master flight, then isEntry is set to
 * true.Flight is one level recursive.
 */
public class Flight implements Serializable {

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
     * Get the number of connections of this Flight
     * @return the number of connections
     */
    public int getNumOfConnections() {
        return this.numOfConnections;
    }

    /**
     * Get the boolean representing if this Flight is a return Flight
     * @return the boolean
     */
    public boolean isReturnTrip() {
        return this.isReturnTrip;
    }

    /**
     * Get the boolean representing if this Flight is a direct Flight
     * @return the boolean
     */
    public boolean isDirectFlight() {
        return this.isDirectFlight;
    }

    /**
     * Get the unique ID of this Flight
     * @return the unique ID
     */
    public String getId() {
        return this.id;
    }

    /**
     * Get the city code of the city from which this Flight departs
     * @return the city code
     */
    public String getDepartureCityCode() {
        return this.departureCityCode;
    }

    /**
     * Get the date on which this Flight departs
     * @return the date
     */
    public String getDepartureDate() {
        return this.departureDate;
    }

    /**
     * Get the time at which this Flight departs
     * @return the time
     */
    public String getDepartureTime() {
        return this.departureTime;
    }

    /**
     * Get the city code of the city at which this Flight arrives
     * @return the city code
     */
    public String getArrivalCityCode() {
        return this.arrivalCityCode;
    }

    /**
     * Get the date on which this Flight arrives
     * @return the date
     */
    public String getArrivalDate() {
        return this.arrivalDate;
    }

    /**
     * Get the time at which this Flight arrives
     * @return the time
     */
    public String getArrivalTime() {
        return this.arrivalTime;
    }

    /**
     * Get the cost of this Flight
     * @return the cost
     */
    public double getCost() {
        return this.cost;
    }

    /**
     * Turns a Flight object into a String that contains the basic information
     * about the Flight
     * @return a human readable String
     */
    public String humanReadable() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cost: " + this.cost + "\n");
        sb.append("Departure City: " + this.departureCityCode + "\n");
        sb.append("Departure Date: " + this.departureDate + "\n");
        sb.append("Arrival City: " + this.arrivalCityCode + "\n");
        sb.append("Arrival Date: " + this.arrivalDate + "\n");
        sb.append("id: " + this.id);
        return sb.toString();
    }

    /**
     * Compares two Flights; the id field is assumed to be unique among Flights
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
     * Takes a string representation of a Flight and parses it into a new Flight
     * object
     * @param o the string representing the flight
     * @return the new Flight object
     */
    public static Flight parseFlight(Object o) {
        // assert o is not null and is a Flight class instance
        if (o == null || !o.getClass().equals(String.class)) {
            return new SubFlight("", "", "", 0);
        }
        String s = (String) o;
        String[] info = s.split("-");
        Flight f = new Flight();

        // parse the instance variables
        f.id = info[0];
        f.isReturnTrip = Boolean.parseBoolean(info[1]);
        f.isDirectFlight = Boolean.parseBoolean(info[2]);
        f.cost = Double.parseDouble(info[3]);
        f.duration = Integer.parseInt(info[4]);
        f.numOfConnections = Integer.parseInt(info[5]);
        f.departureCityCode = info[6];
        f.departureDate = info[7];
        f.departureTime = info[8];
        f.arrivalCityCode = info[9];
        f.arrivalDate = info[10];
        f.arrivalTime = info[11];
        f.retDuration = Integer.parseInt(info[12]);
        f.retNumOfConnections = Integer.parseInt(info[13]);
        f.retDepartureCityCode = info[14];
        f.retDepartureDate = info[15];
        f.retDepartureTime = info[16];
        f.retArrivalCityCode = info[17];
        f.retArrivalDate = info[18];
        f.retArrivalTime = info[19];

        // parse each SubFlight
        for (int i = 20; i < info.length; i++) {
            f.subFlights.add(SubFlight.parseSubFlight(info[i]));
        }
        return f;
    }

    /**
     * Generate a string based on the Flight's instance variables that can later
     * be parsed into another Flight instance
     * @return the string containing the instance variables
     */
    @Override
    public String toString() {
        String s = id;
        s += "-" + isReturnTrip;
        s += "-" + isDirectFlight;
        s += "-" + cost;
        s += "-" + duration;
        s += "-" + numOfConnections;
        s += "-" + departureCityCode;
        s += "-" + departureDate;
        s += "-" + departureTime;
        s += "-" + arrivalCityCode;
        s += "-" + arrivalDate;
        s += "-" + arrivalTime;
        s += "-" + retDuration;
        s += "-" + retNumOfConnections;
        s += "-" + retDepartureCityCode;
        s += "-" + retDepartureDate;
        s += "-" + retDepartureTime;
        s += "-" + retArrivalCityCode;
        s += "-" + retArrivalDate;
        s += "-" + retArrivalTime;
        for (SubFlight sf :  subFlights) {
            s += "-" + sf.toString();
        }
        return s;
    }

}
