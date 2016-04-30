package takeoff.cis350.upenn.edu.takeoff.flight;

import junit.framework.Assert;

/**
 * A class representing one of the possible connecting flights (if not the only one) for a  given
 * Flight object
 */
public class SubFlight {

    private final static String delimiter = "&";

    boolean isReturnTrip = false;
    String id = "";
    String departureCityCode = "";      // XYZ
    String departureDate = "";          // YYYY-MM-DD
    String departureTime = "";          // HH-MM
    String arrivalCityCode = "";        // XYZ
    String arrivalDate = "";            // YYYY-MM-DD
    String arrivalTime = "";            // HH-MM

    protected String flightNumber; 	    // eg UA93
    protected String cabinClass;
    protected String airline = " ";
    protected int mileage;
    protected int duration;

    public SubFlight(String flightNo, String cClass, String airline, int m) {
        this.flightNumber = flightNo;
        this.cabinClass = cClass;
        this.airline = airline;
        this.mileage = m;
    }

    public SubFlight() {
        this.flightNumber = "";
        this.cabinClass = "";
        this.airline = "";
        this.mileage = 0;
    }

    /**
     * Gets the name of the Airline which this subFlight is under
     * @return the Airline name
     */
    public String getAirline() {
        return this.airline;
    }

    /**
     * Gets the flight number corresponding to this subFlight
     * @return the flight number
     */
    public String getFlightNumber() {
        return this.flightNumber;
    }

    /**
     * Gets the cabin class corresponding to this subFlight
     * @return the cabin class
     */
    public String getCabinClass() {
        return this.cabinClass;
    }

    /**
     * Get the boolean representing if this Flight is a return Flight
     * @return the boolean
     */
    public boolean isReturnTrip() {
        return this.isReturnTrip;
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
     * Gets the mileage corresponding to this subFlight
     * @return the mileage
     */
    public int getMileage() {
        return this.mileage;
    }

    @Override
    public String toString() {

        String s = "";
        if (isReturnTrip) {
            s += "(true";
        } else {
            s += "(false";
        }
        s += delimiter + departureCityCode;
        s += delimiter + departureDate;
        s += delimiter + departureTime;
        s += delimiter + arrivalCityCode;
        s += delimiter + arrivalDate;
        s += delimiter + arrivalTime;
        s += delimiter + id;
        s += delimiter + flightNumber;
        s += delimiter + cabinClass;
        s += delimiter + airline;
        s += delimiter + mileage;
        s += delimiter + duration + ")";
        return s;
    }

    /**
     * Takes a string represnting a
     * @param o the string representing the SubFlight
     * @return the new SubFlight instance
     */
    public static SubFlight parseSubFlight(Object o) {
        if (o == null || !o.getClass().equals(String.class)) {
            return new SubFlight("", "", "", 0);
        }
        String s = (String) o;

        /*
        System.out.println("-----");
        System.out.println("-----");
        System.out.println("-----");
        System.out.println("SUBSTRING");
        System.out.println(o);
        System.out.println("SUBSTRING DONE");
        System.out.println("-----");
        System.out.println("-----");
        System.out.println("-----");*/
        String[] info = s.replace(")", "").replace("(", "").split(delimiter, -1);

        /*
        System.out.println("-----");
        System.out.println("-----");
        System.out.println("-----");
        System.out.println("PRINTING EVERYTHING THAT IS SUBFLIGHT");*/

        for (int i = 0; i < info.length; i++) {
            System.out.println("info[" + i + "] = " + info[i]);
        }

        /*
        System.out.println("PRINT DONE");
        System.out.println("-----");
        System.out.println("-----");
        System.out.println("-----");*/
        SubFlight sf = new SubFlight();
        // System.out.println("sf.isReturnTrip = " + info[0]);
        sf.isReturnTrip = Boolean.parseBoolean(info[0]);

        // System.out.println("sf.departureCityCode = " + info[1]);
        sf.departureCityCode = info[1];

        // System.out.println("sf.departureDate = " + info[2]);
        sf.departureDate = info[2];

        //  System.out.println("sf.departureTime = " + info[3]);
        sf.departureTime = info[3];
        // System.out.println("sf.arrivalCityCode = " + info[4]);
        sf.arrivalCityCode  = info[4];
        // System.out.println("sf.arrivalDate = " + info[5]);
        sf.arrivalDate = info[5];
        // System.out.println("sf.arrivalTime = " + info[6]);
        sf.arrivalTime = info[6];
        //System.out.println("sf.id = " + info[7]);
        sf.id = info[7];
        // System.out.println("sf.flightNumber = " + info[8]);
        sf.flightNumber = info[8];
        // System.out.println("sf.cabinClass = " + info[9]);
        sf.cabinClass = info[9];
        // System.out.println("sf.airline = " + info[10]);
        sf.airline = info[10];
        // System.out.println("sf.mileage = " + info[11]);
        sf.mileage = Integer.parseInt(info[11]);
        //System.out.println("sf.duration = " + info[12]);
        sf.duration  = Integer.parseInt(info[12]);
        Assert.assertEquals(sf.toString(), s);
        return sf;
    }

}

