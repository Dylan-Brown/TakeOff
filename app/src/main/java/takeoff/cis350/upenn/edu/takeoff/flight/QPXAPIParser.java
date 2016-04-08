package takeoff.cis350.upenn.edu.takeoff.flight;

import android.util.Log;

import org.json.*;

import java.util.ArrayList;
import java.util.List;

import takeoff.cis350.upenn.edu.takeoff.flight.Flight;
import takeoff.cis350.upenn.edu.takeoff.flight.SubFlight;

/**
 * Created by tangson on 3/23/16.
 */
public class QPXAPIParser {

    static List<Flight> FlightCache;

    public static List<Flight> getAPIResultsAsFlight(JSONArray jsonArray) throws JSONException {
        FlightCache = new ArrayList<Flight>();
        List<Flight> flightResults = new ArrayList<Flight>();
        System.out.println("QPXAPIParser: attempt to getAPIResultsAsFlight");
        Log.e("QPXAPIParser", " attempt to getAPIResultsAsFlight");

        // tripOptions, a list of flights
        for (int index = 0; index < jsonArray.length(); index++) {
            JSONObject tripOption = jsonArray.getJSONObject(index);// each Trip
            JSONArray slices = tripOption.getJSONArray("slice");
            int numOfSlices = slices.length();

            Flight fullFlight = new Flight();
            fullFlight.subFlights = new ArrayList<SubFlight>();
            fullFlight.numOfConnections = 0;
            if (numOfSlices > 1) {
                fullFlight.isReturnTrip = true;
            } else {
                fullFlight.isReturnTrip = false;
            }
            fullFlight.id = tripOption.getString("id");
            String costWithoutUSD = tripOption.getString("saleTotal").replaceAll("[^0-9\\.]+", "");
            fullFlight.cost = Double.parseDouble(costWithoutUSD);

            // slice, divided into one way or two way.
            for (int i_sl = 0; i_sl < numOfSlices; i_sl++) {
                JSONArray segments = slices.getJSONObject(i_sl).getJSONArray("segment");
                int numOfSegments = segments.length();
                // segments, i.e. number of flight connections
                for (int i_s = 0; i_s < numOfSegments; i_s++) {
                    JSONObject segment = segments.getJSONObject(i_s);
                    JSONArray legs = segments.getJSONObject(i_s).getJSONArray("leg");
                    int numOfLegs = legs.length();
                    // legs, i.e. number of flights
                    for (int i = 0; i < numOfLegs; i++) {
                        JSONObject leg = legs.getJSONObject(i);
                        fullFlight.numOfConnections += numOfLegs;
                        SubFlight sf;
                        sf = new SubFlight();
                        if (i_sl == 1) {
                            fullFlight.isReturnTrip = true;
                            sf.isReturnTrip = true;
                        }

                        sf.flightNumber = segment.getJSONObject("flight").getString("carrier")
                                + segment.getJSONObject("flight").getString("number") + "";
                        sf.cabinClass = segment.getString("cabin") + "";
                        sf.airline = segment.getJSONObject("flight").getString("carrier") + "";
                        sf.departureCityCode = leg.getString("origin") + "";
                        sf.departureTime = leg.getString("departureTime").split("T")[0] + "";
                        sf.departureDate = leg.getString("departureTime").split("T")[1] + "";
                        sf.arrivalCityCode = leg.getString("destination") + "";
                        sf.arrivalTime = leg.getString("arrivalTime").split("T")[1] + "";
                        sf.arrivalDate = leg.getString("arrivalTime").split("T")[0] + "";
                        sf.id = tripOption.getString("id") + "";
                        sf.duration = leg.getInt("duration") + 0;
                        sf.mileage = leg.getInt("mileage") + 0;
                        fullFlight.subFlights.add(sf);
                    } // leg
                } // segment
            } // slice
            ArrayList<SubFlight> sfList = (ArrayList) fullFlight.subFlights;
            SubFlight firstF = fullFlight.getSubFlights().get(0);
            int roundTripCounter = 0;
            for (int i = 0; i < sfList.size(); i++) {
                if (sfList.get(i).isReturnTrip == false) {
                    roundTripCounter++;
                } else {
                    break;
                }
            }

            SubFlight lastF = sfList.get(roundTripCounter - 1);
            fullFlight.numOfConnections = roundTripCounter;
            fullFlight.departureCityCode = firstF.departureCityCode + "";
            fullFlight.departureTime = firstF.departureTime + "";
            fullFlight.departureDate = firstF.departureDate + "";
            fullFlight.arrivalCityCode = lastF.arrivalCityCode + "";
            fullFlight.arrivalTime = lastF.arrivalTime + "";
            fullFlight.arrivalDate = lastF.arrivalDate + "";
            fullFlight.duration = slices.getJSONObject(0).getInt("duration");
            if (fullFlight.isReturnTrip) {
                fullFlight.retnumOfConnections = sfList.size() - roundTripCounter;
                firstF = sfList.get(roundTripCounter);
                lastF = sfList.get(sfList.size() - 1);
                fullFlight.retduration = slices.getJSONObject(1).getInt("duration");
                fullFlight.retdepartureCityCode = firstF.departureCityCode + "";
                fullFlight.retdepartureTime = firstF.departureTime + "";
                fullFlight.retdepartureDate = firstF.departureDate + "";
                fullFlight.retarrivalCityCode = lastF.arrivalCityCode + "";
                fullFlight.retarrivalTime = lastF.arrivalTime + "";
                fullFlight.retarrivalDate = lastF.arrivalDate + "";
                fullFlight.retdepartureCityCode = lastF.departureCityCode + "";
            }
            FlightCache.add(fullFlight);
            flightResults.add(fullFlight);
        } // tripOption
        printFlights(flightResults);
        return flightResults;
    }

    public static List<Flight> getFlightResultsFromMostRecentSearch() {
        return FlightCache;
    }


    public static void printFlights(List<Flight> flightResults) {
        int Counter = 0;
        for (Flight flight : flightResults) {
            Counter++;
            System.out.println("Flight " + Counter);
            System.out.println("Departure: " + flight.departureCityCode);
            System.out.println("Departure Time: " + flight.departureTime);
            System.out.println("Departure Date: " + flight.departureDate);
            System.out.println("Arrival: " + flight.arrivalCityCode);
            System.out.println("Arrival Time: " + flight.arrivalTime);
            System.out.println("ArrivalDate: " + flight.arrivalDate);
            System.out.println("Num Connections: " + flight.retnumOfConnections);

            int counter = 0;

            for (SubFlight sf : flight.subFlights) {
                counter++;
                System.out.println("con "+counter+" Departure: " + sf.departureCityCode);
                System.out.println("con "+counter+" Arrival: " + sf.arrivalCityCode);
                System.out.println("con "+counter+" FlightNumber: " + sf.flightNumber);
                System.out.println("con "+counter+" Airline: " + sf.airline);
                System.out.println("con "+counter+" CabinClass: " + sf.cabinClass);
            } if (flight.isReturnTrip) {

                System.out.println(" Ret Departure: " + flight.retdepartureCityCode);
                System.out.println(" Ret Departure Time: " + flight.retdepartureTime);
                System.out.println(" Ret Departure Date: " + flight.retdepartureDate);
                System.out.println(" Ret Arrival: " + flight.retarrivalCityCode);
                System.out.println(" Ret Arrival Time: " + flight.retarrivalTime);
                System.out.println(" Ret ArrivalDate: " + flight.retarrivalDate);
                System.out.println("Ret Num Connections: " + flight.retnumOfConnections);
                counter = 0;
                for (SubFlight sf : flight.subFlights) {
                    counter++;
                    System.out.println("con "+counter+" Departure: " + sf.departureCityCode);
                    System.out.println("con "+counter+" Arrival: " + sf.arrivalCityCode);
                    System.out.println("con "+counter+" FlightNumber: " + sf.flightNumber);
                    System.out.println("con "+counter+" Airline: " + sf.airline);
                    System.out.println("con "+counter+" CabinClass: " + sf.cabinClass);
                }
            }
        }
    }
}
