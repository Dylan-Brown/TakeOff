package takeoff.cis350.upenn.edu.takeoff;

import android.util.Log;

import org.json.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.impl.client.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.*;

/**
 * Created by tangson on 3/23/16.
 */
public class QPXAPIParser {

    static List<Flight> FlightCache;

    public static List<Flight> getAPIResultsAsFlight(JSONArray jsonArray) throws JSONException {
        FlightCache=new ArrayList<Flight>();
        List<Flight> flightResults = new ArrayList<Flight>();
        System.out.println("QPXAPIParser: attempt to getAPIResultsAsFlight");
        Log.e("QPXAPIParser", " attempt to getAPIResultsAsFlight");
        // tripOptions
        for (int index = 0; index < jsonArray.length(); index++) {
            JSONObject tripOption = jsonArray.getJSONObject(index);// each Trip

            JSONArray slices = tripOption.getJSONArray("slice");
            int numOfSlices = slices.length();


            Flight fullFlight = new Flight();
            if (numOfSlices>1){fullFlight.isRoundtrip=true;}
            else {fullFlight.isRoundtrip=false;}
            fullFlight.id = tripOption.getString("id");
            String costWithoutUSD = tripOption.getString("saleTotal").replaceAll("[^0-9\\.]+", "");
            fullFlight.cost = Double.parseDouble(costWithoutUSD);

            // slice
            for (int i_sl = 0; i_sl < numOfSlices; i_sl++) {
                JSONArray segments = slices.getJSONObject(i_sl).getJSONArray("segment");
                int numOfSegments = segments.length();
                // segments
                for (int i_s = 0; i_s < numOfSegments; i_s++) {
                    JSONObject segment = segments.getJSONObject(i_s);
                    JSONArray legs = segments.getJSONObject(i_s).getJSONArray("leg");
                    int numOfLegs = legs.length();
                    // legs
                    for (int i = 0; i < numOfLegs; i++) {
                        Flight flight = new Flight();
                        if (i_sl == 1) {
                            flight.isRoundtrip = true;
                        }
                        JSONObject leg = legs.getJSONObject(i);

                        fullFlight.numOfConnections = numOfSegments - 1;

                        if (i_sl == 0) {
                            flight.oneWayDuration = slices.getJSONObject(i_sl).getInt("duration");
                        } else {
                            flight.roundTripDuration = slices.getJSONObject(i_sl).getInt("duration");
                        }
                        flight.flightNumber = segment.getJSONObject("flight").getString("carrier")
                                + segment.getJSONObject("flight").getString("number");
                        flight.cabinClass = segment.getString("cabin");
                        flight.airline=segment.getJSONObject("flight").getString("carrier");
                        flight.departureCityCode = leg.getString("origin");
                        flight.departureTime = leg.getString("departureTime").split("T")[0];
                        flight.departureDate = leg.getString("departureTime").split("T")[1];
                        flight.arrivalCityCode = leg.getString("destination");
                        flight.arrivalTime = leg.getString("arrivalTime").split("T")[1];
                        flight.arrivalDate = leg.getString("arrivalTime").split("T")[0];
                        flight.id = tripOption.getString("id");
                        flight.flightDuration = leg.getInt("duration");
                        flight.mileage = leg.getInt("mileage");

                        fullFlight.subFlights.add(flight);

                        System.out.println("Flight: " + fullFlight.subFlights.size() + " .");
                        System.out.println("Departure Code: " + flight.departureCityCode);
                        System.out.println("Arrival Code: " + flight.arrivalCityCode);
                        System.out.println(flight.departureTime);
                        System.out.println(flight.departureDate);
                        System.out.println("tripOptions: "+jsonArray.length());
                    } // leg
                } // segment
            } // slice
            List<Flight> fl = fullFlight.subFlights;
            for (Flight f : fl) {
                System.out.println("__________________");
                System.out.println(f.departureCityCode);
                System.out.println(f.departureTime);
                System.out.println(f.departureDate);
                System.out.println(f.arrivalCityCode);
                System.out.println(f.arrivalTime);
                System.out.println(f.arrivalDate);
            }
            fullFlight.isEntry = true;


            int counter=0;
            for (int asdz=0;asdz< fl.size();asdz++){
                if (fl.get(asdz).isRoundtrip){
                    System.out.println("BROKEN!");
                    counter=asdz;
                    break;
                }
            }
            Flight firstF = fl.get(0);
            if (counter<=1){counter=1;}
            Flight lastF = fl.get(counter-1);
            System.out.println("firstF.departureCityCode: " + firstF.departureCityCode);
            System.out.println("");
            fullFlight.departureCityCode = firstF.departureCityCode;
            fullFlight.departureTime = firstF.departureTime;
            fullFlight.departureDate = firstF.departureDate;
            fullFlight.arrivalCityCode = lastF.arrivalCityCode;
            fullFlight.arrivalTime = lastF.arrivalTime;
            fullFlight.arrivalDate = lastF.arrivalDate;
            fullFlight.oneWayDuration = lastF.flightDuration;
            fullFlight.departureCityCode = lastF.departureCityCode;

            if (fullFlight.isRoundtrip) {
                firstF=fl.get(counter);
                firstF=fl.get(fl.size()-1);

                fullFlight.retdepartureCityCode = firstF.departureCityCode;
                fullFlight.retdepartureTime = firstF.departureTime;
                fullFlight.retdepartureDate = firstF.departureDate;
                fullFlight.retarrivalCityCode = lastF.arrivalCityCode;
                fullFlight.retarrivalTime = lastF.arrivalTime;
                fullFlight.retarrivalDate = lastF.arrivalDate;
                fullFlight.roundTripDuration = lastF.flightDuration;
                fullFlight.retdepartureCityCode = lastF.departureCityCode;
            }
            FlightCache.add(fullFlight);
            flightResults.add(fullFlight);
        } // tripOption
        return flightResults;
    }

    public static List<Flight> getFlightResultsFromMostRecentSearch(){
        return FlightCache;
    };
    public static void printFlights(List<Flight> flightResults) {
        for (Flight flight : flightResults) {
            System.out.println("");
            System.out.println("Departure: " + flight.departureCityCode);
            System.out.println("Departure Time: " + flight.departureTime);
            System.out.println("Departure Date: " + flight.departureDate);
            System.out.println("Arrival: " + flight.arrivalCityCode);
            System.out.println("Arrival Time: " + flight.arrivalTime);
            System.out.println("ArrivalDate: " + flight.arrivalDate);


            System.out.println("Ret Departure: " + flight.retdepartureCityCode);
            System.out.println("Ret Departure Time: " + flight.retdepartureTime);
            System.out.println("Ret Departure Date: " + flight.retdepartureDate);
            System.out.println("Ret Arrival: " + flight.retarrivalCityCode);
            System.out.println("Ret Arrival Time: " + flight.retarrivalTime);
            System.out.println("Ret ArrivalDate: " + flight.retarrivalDate);

            System.out.println("Num Connections: " + flight.numOfConnections);
            System.out.println("Flight number: " + flight.subFlights.get(0).flightNumber);
            System.out.println("Flight time: " + flight.oneWayDuration + " minutes");
        }
    }
}
