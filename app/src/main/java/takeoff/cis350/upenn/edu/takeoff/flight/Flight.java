package takeoff.cis350.upenn.edu.takeoff.flight;

import java.io.*;
import java.util.*;

/**
 * Created by tangson on 2/19/16.
 *
 * Class representing the Flight datatype; Flight segments are refered to as sub-flights, ie.
 * layovers.
 */
public class Flight implements Serializable {

    // the list of flight segments; can be one or more subFlights
    ArrayList<SubFlight> subFlights = new ArrayList<SubFlight>();

    String id = "";
    boolean isReturnTrip = false;
    boolean isDirectFlight = false;

    // cost is represented in US dolalrs
    double cost=99999.99;
    int duration=0;
    int numOfConnections=0;
    String departureCityCode=" ";

    // Dates are formatted YYYY-MM-DD, and the time is formatted  HH-MM
    String departureDate=" ";
    String departureTime=" ";
    String arrivalCityCode=" ";
    String arrivalDate=" ";
    String arrivalTime=" ";

    // These variables are important if this is a return flight
    int retduration=0;
    int retnumOfConnections=0;
    String retdepartureCityCode=" ";
    String retdepartureTime=" ";
    String retdepartureDate=" ";
    String retarrivalCityCode=" ";
    String retarrivalDate=" ";
    String retarrivalTime=" ";

    /**
     * The default, empty constructor for the class.
     */
    public Flight() {
    }

    /**
     * Gets the list of SubFlights for this flight, or all subFlights from the departure city to the
     * arrival city
     * @return the list of subflights
     */
    public ArrayList<SubFlight> getSubFlights() {
        return this.subFlights;
    }

    /**
     * Gets the number of connecting flights
     * @return the number of connecting flights
     */
    public int getNumOfConnections() {
        return this.numOfConnections;
    }

    /**
     * Returns a boolean representing if this flight is a return trip
     * @return true if the flight is a return trip, and false otherwise
     */
    public boolean isReturnTrip() {
        return this.isReturnTrip;
    }

    /**
     * Returns a boolean representing if this flight is directly from the departure city to the
     * arrival city
     * @return true if this is a direct flight, false otherwise
     */
    public boolean isDirectFlight() {
        return this.isDirectFlight;
    }

    /**
     * Returns this flight's unique id
     * @return this flight's unique id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Returns the city code of the city from which this Flight departs
     * @return  the departure city code
     */
    public String getDepartureCityCode() {
        return this.departureCityCode;
    }

    /**
     * Returns the departure date of this Flight in a string format
     * @return the string representing the departure date
     */
    public String getDepartureDate() {
        return this.departureDate;
    }

    /**
     * Returns the departure time of this Flight in a string format
     * @return the string representing the departure time
     */
    public String getDepartureTime() {
        return this.departureTime;
    }

    /**
     * Returns the city code of the city at which this Flight arrives
     * @return  the arrival city code
     */
    public String getArrivalCityCode() {
        return this.arrivalCityCode;
    }

    /**
     * Returns the arrival date of this Flight in a string format
     * @return the string representing the arrival date
     */
    public String getArrivalDate() {
        return this.arrivalDate;
    }

    /**
     * Returns the arrival time of this Flight in a string format
     * @return the string representing the arrival time
     */
    public String getArrivalTime() {
        return this.arrivalTime;
    }

    /**
     * Gets the cost associated with this Flight. The cost is the sum of the costs of all
     * subFlights.
     * @return the total cost associated with this Flight, in US dollars
     */
    public double getCost() {
        return this.cost;
    }

    /**
     * Converts this flight into a String representation that can later be parsed back into a Flight
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id + "-");
        sb.append(isReturnTrip + "-");
        sb.append(((int) cost) + "-");
        sb.append(numOfConnections + "-");
        // sb.append(airline + "-");
        // sb.append(oneWayDuration + "-");
        sb.append(departureCityCode + "-");
        sb.append(departureDate + "-");
        sb.append(departureTime + "-");
        sb.append(arrivalCityCode + "-");
        sb.append(arrivalDate + "-");
        sb.append(arrivalTime + "-");
        // sb.append(roundTripDuration + "-");
        sb.append(retdepartureCityCode + "-");
        sb.append(retdepartureTime + "-");
        sb.append(retdepartureDate + "-");
        sb.append(retarrivalCityCode + "-");
        sb.append(retarrivalDate + "-");
        sb.append(retarrivalTime + "-");
        // sb.append(flightNumber + "-");
        // sb.append(cabinClass + "-");
        // sb.append(mileage + "-");
        // sb.append(flightDuration);
        return sb.toString();
    }

    /**
     * Takes the Flight's information and returns the relevant information in a human readable
     * format to display.
     * @return a human readable string containing an overview of this Flights information
     */
    public String humanReadable() {
        StringBuilder sb = new StringBuilder();
        //sb.append("Airline: " + this.airline + "\n");
        sb.append("Cost: " + this.cost + "\n");
        sb.append("Departure City: " + this.departureCityCode + "\n");
        sb.append("Departure Date: " + this.departureDate + "\n");
        sb.append("Arrival City: " + this.arrivalCityCode + "\n");
        sb.append("Arrival Date: " + this.arrivalDate + "\n");
        return sb.toString();
    }

    /**
     * Return a Flight representing the information from the human readable format
     * @param hr the human readable String to be parsed
     * @return the new Flight object; note that this does not contain all the information as the
     *         original Flight item before it was parsed into a human readable format
     */
    public static Flight fromHumanReadable(String hr) {
        Flight f = new Flight();
        String[] info = hr.split("\n");
        for (int i = 0; i < info.length; i++) {
            info[i] = info[i].split(": ")[1];
        }
        // f.airline = info[0];
        f.cost = Double.parseDouble(info[0]);
        f.departureCityCode = info[1];
        f.departureDate = info[2];
        f.arrivalCityCode = info[3];
        f.arrivalDate = info[4];
        return f;
    }

    /**
     * Compares the id's of two flights. Since we have the  hueristic that each flight has a unique
     * id, then we can avoid comparing all of the fields of two flights to compare them.
     * @param f1 the first flight for comparison
     * @param f2 the second flight for comparison
     * @return true if the two flights ids are the same
     */
    public static boolean minimalCompare(Flight f1, Flight f2) {
        return f1.id.equals(f2.id);
    }


    public static Flight parseFlight(String s) {
        // split the string based on whether or not it is in a human readable format
        String[] info = s.contains(":") ?
                Flight.fromHumanReadable(s).toString().split("-") : s.split("-");
        Flight f = new Flight();

        if (info.length < 23) {
            // the string is in the human readable format
            // f.airline = info[0];
            f.cost = Double.parseDouble(info[1]);
            f.departureCityCode = info[2];
            f.departureDate = info[3];
            f.arrivalCityCode = info[4];
            f.arrivalDate = info[5];

        } else {
            // the full information relevant to this flight is available
            f.id = info[0];
            f.isReturnTrip = Boolean.parseBoolean(info[1]);
            f.cost = Double.parseDouble(info[2]);
            f.numOfConnections = Integer.parseInt(info[3]);
            // f.airline = info[4];
            // f.oneWayDuration = Integer.parseInt(info[5]);
            f.departureCityCode = info[6];
            f.departureDate = info[7];
            f.departureTime = info[8];
            f.arrivalCityCode = info[9];
            f.arrivalDate = info[10];  //10
            f.arrivalTime = info[11];
            // f.roundTripDuration = Integer.parseInt(info[12]);
            f.retdepartureCityCode = info[13];
            f.retdepartureTime = info[14];
            f.retdepartureDate = info[15];
            f.retarrivalCityCode = info[16];
            f.retarrivalDate = info[17];
            f.retarrivalTime = info[18];
            // f.flightNumber = info[19];
            // f.cabinClass = info[20];
            // f.mileage = Integer.parseInt(info[21]);
            // f.flightDuration = Integer.parseInt(info[22]);
            if (info.length >= 24) {
                f.arrivalDate = info[23];
            }
        }
        return f;
    }

}
