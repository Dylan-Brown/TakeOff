package takeoff.cis350.upenn.edu.takeoff.flight;

import java.io.*;
import java.util.*;
import takeoff.cis350.upenn.edu.takeoff.ui.results.*;
/**
 * Created by tangson on 2/19/16.
 */

/*
* This is the Flight datatype. Flight segments are sub-flights (i.e. layovers). */

public class Flight implements Serializable {
    //Flight is one level recursive.
    //The master Flight (i.e. the Ticket) is composed of individual flights (eg connections, roundtrips)
    //If the flight is the master flight, then isEntry is set to true.
    ArrayList<SubFlight> subFlights=new ArrayList<SubFlight>();

    String id="";  //unique per flight
    boolean isReturnTrip=false;
    boolean isDirectFlight=false;
    double cost=99999.99;     //in USD           //trips.tripOption[].saleTotal

    int duration=0;
    int numOfConnections=0;
    String departureCityCode=" ";
    String departureDate=" ";    //YYYY-MM-DD
    String departureTime=" ";   //HH-MM
    String arrivalCityCode=" ";
    String arrivalDate=" ";
    String arrivalTime=" ";
    //Only if there is a return flight
    int retduration=0;
    int retnumOfConnections=0;
    String retdepartureCityCode=" ";
    String retdepartureTime=" ";
    String retdepartureDate=" ";
    String retarrivalCityCode=" ";
    String retarrivalDate=" ";
    String retarrivalTime=" ";

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
        // FORMAT: id-isRoundtrip-(int)cost-numOfConnections-airline-oneWayDuration
        // -departureCityCode-departureDate-departureTime-arrivalCityCode-arrivalDate-arrivalTime
        // -roundTripDuration-retdepartureCityCode-retdepartureTime-retdepartureDate
        // -retarrivalCityCode-retarrivalDate-retarrivalTime-flightNumber-cabinClass-mileage
        // -flightDuration
        StringBuilder sb = new StringBuilder();
        sb.append(id + "-");
        sb.append(isReturnTrip + "-");
        sb.append(((int) cost) + "-");
        sb.append(numOfConnections + "-");
      //  sb.append(airline + "-");
       // sb.append(oneWayDuration + "-");
        sb.append(departureCityCode + "-");
        sb.append(departureDate + "-");
        sb.append(departureTime + "-");
        sb.append(arrivalCityCode + "-");
        sb.append(arrivalDate + "-");
        sb.append(arrivalTime + "-");
      //  sb.append(roundTripDuration + "-");
        sb.append(retdepartureCityCode + "-");
        sb.append(retdepartureTime + "-");
        sb.append(retdepartureDate + "-");
        sb.append(retarrivalCityCode + "-");
        sb.append(retarrivalDate + "-");
        sb.append(retarrivalTime + "-");
        /*sb.append(flightNumber + "-");
        sb.append(cabinClass + "-");
        sb.append(mileage + "-");
        sb.append(flightDuration);*/
        return sb.toString();
    }


    // Return a string representing the human-readable data about a flight for dashboard
    public String humanReadable() {
        // FORMAT: airline, cost, departure city, departure date, arrival city, arrival date
        StringBuilder sb = new StringBuilder();
        //sb.append("Airline: " + this.airline + "\n");
        sb.append("Cost: " + this.cost + "\n");
        sb.append("Departure City: " + this.departureCityCode + "\n");
        sb.append("Departure Date: " + this.departureDate + "\n");
        sb.append("Arrival City: " + this.arrivalCityCode + "\n");
        sb.append("Arrival Date: " + this.arrivalDate + "\n");
        return sb.toString();
    }

    // Return a string array representing the information from the human-readable format
    public static Flight fromHumanReadable(String hr) {
        // FORMAT: airline, cost, departure city, departure date, arrival city, arrival date
        Flight f = new Flight();
        String[] info = hr.split("\n");
        for (int i = 0; i < info.length; i++) {
            info[i] = info[i].split(": ")[1];
        }
     //   f.airline = info[0];
        f.cost = Double.parseDouble(info[1]);
        f.departureCityCode = info[2];
        f.departureDate = info[3];
        f.arrivalCityCode = info[4];
        f.arrivalDate = info[5];

        return f;
    }


    // Return true if Flight F1 and Flight F2 have the same minimal fields, false otherwise
    public static boolean minimalCompare(Flight f1, Flight f2) {
        // COMPARE: airline, cost, departure city, departure date, arrival city, arrival date
        boolean compare = f1.id.equals(f2.id);
        return compare;
    }


    public static Flight parseFlight(String s) {
        String[] info = s.contains(":") ?
                Flight.fromHumanReadable(s).toString().split("-") : s.split("-");

        Flight f = new Flight();
        if (info.length < 23) {
            // FORMAT: airline, cost, departure city, departure date, arrival city, arrival date
      //      f.airline = info[0];
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
        //    f.airline = info[4];
      //      f.oneWayDuration = Integer.parseInt(info[5]); // 5
            f.departureCityCode = info[6];
            f.departureDate = info[7];
            f.departureTime = info[8];
            f.arrivalCityCode = info[9];
            f.arrivalDate = info[10];  //10
            f.arrivalTime = info[11];
          //  f.roundTripDuration = Integer.parseInt(info[12]);
            f.retdepartureCityCode = info[13];
            f.retdepartureTime = info[14];
            f.retdepartureDate = info[15];
            f.retarrivalCityCode = info[16];
            f.retarrivalDate = info[17];
            f.retarrivalTime = info[18];
           /* f.flightNumber = info[19];
            f.cabinClass = info[20];
            f.mileage = Integer.parseInt(info[21]);
            f.flightDuration = Integer.parseInt(info[22]);*/
            if (info.length >= 24)
                f.arrivalDate = info[23];
        }
        return f;
    }

}
