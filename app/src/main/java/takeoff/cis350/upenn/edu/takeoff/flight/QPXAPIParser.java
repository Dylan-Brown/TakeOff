package takeoff.cis350.upenn.edu.takeoff.flight;

import android.util.Log;

import org.json.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangson on 3/23/16.
 */
public class QPXAPIParser {

    public static List<Flight> FlightCache;

    /**
     *
     * @param jsonArray
     * @return
     * @throws JSONException
     */
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
                        sf.departureDate = leg.getString("departureDate").split("T")[1] + "";
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
            fullFlight.numOfConnections = roundTripCounter-1;
            fullFlight.departureCityCode = firstF.departureCityCode + "";
            fullFlight.departureTime = firstF.departureTime + "";
            fullFlight.departureDate = firstF.departureDate + "";
            fullFlight.arrivalCityCode = lastF.arrivalCityCode + "";
            fullFlight.arrivalTime = lastF.arrivalTime + "";
            fullFlight.arrivalDate = lastF.arrivalDate + "";
            fullFlight.duration = slices.getJSONObject(0).getInt("duration");
            if (fullFlight.isReturnTrip) {
                fullFlight.retnumOfConnections = sfList.size() - roundTripCounter-1;
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
            if ((fullFlight.isReturnTrip && sfList.size() == 2) || sfList.size() == 1) {
                fullFlight.isDirectFlight = true;
            }
            FlightCache.add(fullFlight);
            flightResults.add(fullFlight);
        } // tripOption
        printFlights(flightResults);
        return flightResults;
    }

    /**
     *
     * @return
     */
    public static List<Flight> getFlightResultsFromMostRecentSearch() {
        return FlightCache;
    }

    /**
     *
     * @param flightResults
     */
    public static void printFlights(List<Flight> flightResults) {
        int Counter = 0;
        for (Flight flight : flightResults) {
            Counter++;
            Log.i("QPXAPIPParser", "printFlights: Flight " + Counter);
            Log.i("QPXAPIPParser", "printFlights: Departure: " + flight.departureCityCode);
            Log.i("QPXAPIPParser", "printFlights: Departure Time: " + flight.departureTime);
            Log.i("QPXAPIPParser", "printFlights: Departure Date: " + flight.departureDate);
            Log.i("QPXAPIPParser", "printFlights: Arrival: " + flight.arrivalCityCode);
            Log.i("QPXAPIPParser", "printFlights: Arrival Time: " + flight.arrivalTime);
            Log.i("QPXAPIPParser", "printFlights: ArrivalDate: " + flight.arrivalDate);
            Log.i("QPXAPIPParser", "printFlights: Num Connections: " + flight.retnumOfConnections);

            int counter = 0;

            for (SubFlight sf : flight.subFlights) {
                counter++;
                Log.i("QPXAPIPParser", "printFlights: con " + counter + " Departure: " + sf.departureCityCode);
                Log.i("QPXAPIPParser", "printFlights: con " + counter + " Arrival: " + sf.arrivalCityCode);
                Log.i("QPXAPIPParser", "printFlights: con " + counter + " FlightNumber: " + sf.flightNumber);
                Log.i("QPXAPIPParser", "printFlights: con " + counter + " Airline: " + sf.airline);
                Log.i("QPXAPIPParser", "printFlights: con " + counter + " CabinClass: " + sf.cabinClass);
            }
            if (flight.isReturnTrip) {

                Log.i("QPXAPIPParser", "printFlights: Ret Departure: " + flight.retdepartureCityCode);
                Log.i("QPXAPIPParser", "printFlights: Ret Departure Time: " + flight.retdepartureTime);
                Log.i("QPXAPIPParser", "printFlights: Ret Departure Date: " + flight.retdepartureDate);
                Log.i("QPXAPIPParser", "printFlights: Ret Arrival: " + flight.retarrivalCityCode);
                Log.i("QPXAPIPParser", "printFlights: Ret Arrival Time: " + flight.retarrivalTime);
                Log.i("QPXAPIPParser", "printFlights: Ret ArrivalDate: " + flight.retarrivalDate);
                Log.i("QPXAPIPParser", "printFlights: Ret Num Connections: " + flight.retnumOfConnections);
                counter = 0;
                for (SubFlight sf : flight.subFlights) {
                    counter++;
                    Log.i("QPXAPIPParser", "printFlights: con " + counter + " Departure: " + sf.departureCityCode);
                    Log.i("QPXAPIPParser", "printFlights: con " + counter + " Arrival: " + sf.arrivalCityCode);
                    Log.i("QPXAPIPParser", "printFlights: con " + counter + " FlightNumber: " + sf.flightNumber);
                    Log.i("QPXAPIPParser", "printFlights: con " + counter + " Airline: " + sf.airline);
                    Log.i("QPXAPIPParser", "printFlights: con " + counter + " CabinClass: " + sf.cabinClass);
                }
            }
        }
    }
}
