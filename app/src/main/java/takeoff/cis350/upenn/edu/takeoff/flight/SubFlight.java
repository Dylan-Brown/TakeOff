package takeoff.cis350.upenn.edu.takeoff.flight;

/**
 * A class representing one of the possible connecting flights (if not the only one) for a  given
 * Flight object
 */
public class SubFlight extends Flight {

    protected String flightNumber; 	// eg UA93
    protected String cabinClass;
    protected String airline = " ";
    protected int mileage;

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
     * Gets the mileage corresponding to this subFlight
     * @return the mileage
     */
    public int getMileage() {
        return this.mileage;
    }

    @Override
    public String toString() {
        String s = "(" + flightNumber;
        s += "-" + cabinClass;
        s += "-" + airline;
        s += "-" + mileage + ")";
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
        String[] info = s.replace(")", "").replace("(", "").split("-");
        int m =  Integer.parseInt(info[3]);
        return new SubFlight(info[0], info[1], info[2], m);
    }

}

