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

    String id = "";                     // unique per flight
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
    int retduration = 0;                // duration of the Flight in minutes
    int retnumOfConnections = 0;        // number of one-way connections
    String retdepartureCityCode = "";   // XYZ
    String retdepartureDate = "";       // YYYY-MM-DD
    String retdepartureTime = "";       // HH-MM
    String retarrivalCityCode = "";     // XYZ
    String retarrivalDate = "";         // YYYY-MM-DD
    String retarrivalTime = "";         // HH-MM

    public ArrayList<SubFlight> getSubFlights() {
        return this.subFlights;
    }

    public int getNumOfConnections() {
        return this.numOfConnections;
    }

    public boolean isReturnTrip() {
        return this.isReturnTrip;
    }

    public boolean isDirectFlight() {
        return this.isDirectFlight;
    }

    public String getId() {
        return this.id;
    }

    public String getDepartureCityCode() {
        return this.departureCityCode;
    }

    public String getDepartureDate() {
        return this.departureDate;
    }

    public String getDepartureTime() {
        return this.departureTime;
    }

    public String getArrivalCityCode() {
        return this.arrivalCityCode;
    }

    public String getArrivalDate() {
        return this.arrivalDate;
    }

    public String getArrivalTime() {
        return this.arrivalTime;
    }

    public double getCost() {
        return this.cost;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id + "-");
        sb.append(isReturnTrip + "-");
        sb.append(((int) cost) + "-");
        sb.append(numOfConnections + "-");
        sb.append(departureCityCode + "-");
        sb.append(departureDate + "-");
        sb.append(departureTime + "-");
        sb.append(arrivalCityCode + "-");
        sb.append(arrivalDate + "-");
        sb.append(arrivalTime + "-");
        sb.append(retdepartureCityCode + "-");
        sb.append(retdepartureTime + "-");
        sb.append(retdepartureDate + "-");
        sb.append(retarrivalCityCode + "-");
        sb.append(retarrivalDate + "-");
        sb.append(retarrivalTime + "-");
        return sb.toString();
    }

    /**
     * Turns a Flight object into a String that contains the basic information about the Flight
     * @return a human readable String
     */
    public String humanReadable() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cost: " + this.cost + "\n");
        sb.append("Departure City: " + this.departureCityCode + "\n");
        sb.append("Departure Date: " + this.departureDate + "\n");
        sb.append("Arrival City: " + this.arrivalCityCode + "\n");
        sb.append("Arrival Date: " + this.arrivalDate + "\n");
        return sb.toString();
    }

    /**
     * Creates a Flight object representing the information from the human-readable format
     * @param hr the human readable String containing the Flight information
     * @return the new Flight object
     */
    public static Flight fromHumanReadable(String hr) {
        Flight f = new Flight();
        String[] info = hr.split("\n");
        for (int i = 0; i < info.length; i++) {
            info[i] = info[i].split(": ")[1];
        }
        f.cost = Double.parseDouble(info[0]);
        f.departureCityCode = info[1];
        f.departureDate = info[2];
        f.arrivalCityCode = info[3];
        f.arrivalDate = info[4];

        return f;
    }


    /**
     * Compares the flightID's of two Flights
     * @param f1 the first Flight to compare
     * @param f2 the second Flight to compare
     * @return true if the id's are equal, false otherwase
     */
    public static boolean minimalCompare(Flight f1, Flight f2) {
        return f1.id.equals(f2.id);
    }

    /**
     * Takes a string representation of a Flight and parses it into a new Flight object
     * @param o the string representing the flight
     * @return the new Flight object
     */
    public static Flight parseFlight(String o) {
        String[] info = o.contains(":") ?
                Flight.fromHumanReadable(o).toString().split("-") : o.split("-");

        Flight f = new Flight();
        if (info.length < 23) {
            f.cost = Double.parseDouble(info[1]);
            f.departureCityCode = info[2];
            f.departureDate = info[3];
            f.arrivalCityCode = info[4];
            f.arrivalDate = info[5];

        } else {
            f.id = info[0];
            f.isReturnTrip = Boolean.parseBoolean(info[1]);
            f.cost = Double.parseDouble(info[2]);
            f.numOfConnections = Integer.parseInt(info[3]);
            f.departureCityCode = info[6];
            f.departureDate = info[7];
            f.departureTime = info[8];
            f.arrivalCityCode = info[9];
            f.arrivalDate = info[10];  //10
            f.arrivalTime = info[11];
            f.retdepartureCityCode = info[13];
            f.retdepartureTime = info[14];
            f.retdepartureDate = info[15];
            f.retarrivalCityCode = info[16];
            f.retarrivalDate = info[17];
            f.retarrivalTime = info[18];
            if (info.length >= 24) {
                f.arrivalDate = info[23];
            }
        }
        return f;
    }

}
