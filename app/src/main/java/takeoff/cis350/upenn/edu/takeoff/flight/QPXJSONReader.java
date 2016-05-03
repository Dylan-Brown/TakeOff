package takeoff.cis350.upenn.edu.takeoff.flight;

import org.json.*;

import java.util.ArrayList;
import takeoff.cis350.upenn.edu.takeoff.ui.search.Dashboard;

/**
 * This parser takes the JSON results and parses them into the Flight objects. For more information
 * about the structure of the JSON result, go to
 * https://developers.google.com/qpx-express/v1/trips/search#request-body
 */
public class QPXJSONReader {

    /**
     * Description: This method parses all the information recieved from the QPX API
     * It saves flights with all its necessary information, such as their subflights, too
     * @param jsonArray - the jsonArray formed from the json object recieved from the API
     * @return ArrayList of the flights generated from the json Array
     * @throws JSONException
     */
    public static ArrayList<Flight> getAPIResultsAsFlights(JSONArray jsonArray) throws JSONException {
        //reset the search results
        Dashboard.FlightCache = new ArrayList<Flight>();

        ArrayList<Flight> flightResults = new ArrayList<Flight>();

        // tripOptions, a list of flights
        for (int index = 0; index < jsonArray.length(); index++) {
            JSONObject tripOption = jsonArray.getJSONObject(index);// each Trip
            JSONArray slices = tripOption.getJSONArray("slice");
            int numOfSlices = slices.length();

            //Create a fullFlight local variable to add to the flightResults
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

            /*Now we look at unique legs, which represent subflights*/
            // slice, divided into one way or two way.
            for (int i_sl = 0; i_sl < numOfSlices; i_sl++) {
                JSONArray segments = slices.getJSONObject(i_sl).getJSONArray("segment");
                int numOfSegments = segments.length();

                // segments, i.e. number of flight connections
                for (int i_s = 0; i_s < numOfSegments; i_s++) {
                    JSONObject segment = segments.getJSONObject(i_s);
                    JSONArray legs = segments.getJSONObject(i_s).getJSONArray("leg");
                    int numOfLegs = legs.length();

                    for (int i = 0; i < numOfLegs; i++) {
                        JSONObject leg = legs.getJSONObject(i);
                        //create a subflight object.
                        SubFlight sf;
                        sf = new SubFlight();
                        //if there are two slices, then the flight is roundtrip, and the subflight
                        // represents a roundtrip flight.
                        if (i_sl == 1) {
                            fullFlight.isReturnTrip = true;
                            sf.isReturnTrip = true;
                        }
                        //set variables.
                        sf.flightNumber = segment.getJSONObject("flight").getString("carrier")
                                + segment.getJSONObject("flight").getString("number");
                        sf.cabinClass = segment.getString("cabin") + "";
                        sf.airline = segment.getJSONObject("flight").getString("carrier");
                        sf.departureCityCode = leg.getString("origin") + "";
                        sf.departureTime = leg.getString("departureTime").split("T")[1].replace("-", "X");
                        sf.departureDate = leg.getString("departureTime").split("T")[0].replace("-", "X");
                        sf.arrivalCityCode = leg.getString("destination") + "";
                        sf.arrivalTime = leg.getString("arrivalTime").split("T")[1].replace("-", "X");
                        sf.arrivalDate = leg.getString("arrivalTime").split("T")[0].replace("-", "X");
                        sf.id = tripOption.getString("id");
                        sf.duration = leg.getInt("duration") + 0;
                        sf.mileage = leg.getInt("mileage") + 0;

                        fullFlight.subFlights.add(sf);
                    }
                }
            }
            //add subflight to the list of subflights
            ArrayList<SubFlight> sfList = (ArrayList) fullFlight.subFlights;
            SubFlight firstF = fullFlight.getSubFlights().get(0);
            int roundTripCounter = 0;
            //roundtrip counter is the index where the subflight becomes a return flight.
            for (SubFlight sf: sfList) {
                if (!sf.isReturnTrip) {
                    roundTripCounter++;
                } else {
                    break;
                }
            }

            //last flight for one way trip
            SubFlight lastF = sfList.get(roundTripCounter - 1);
            fullFlight.numOfConnections = roundTripCounter-1;
            fullFlight.departureCityCode = firstF.departureCityCode + "";
            fullFlight.departureTime = firstF.departureTime.replace("-", "X") + "";
            fullFlight.departureDate = firstF.departureDate.replace("-", "X") + "";
            fullFlight.arrivalCityCode = lastF.arrivalCityCode + "";
            fullFlight.arrivalTime = lastF.arrivalTime.replace("-", "X") + "";
            fullFlight.arrivalDate = lastF.arrivalDate.replace("-", "X") + "";
            fullFlight.duration = slices.getJSONObject(0).getInt("duration");

            if (fullFlight.isReturnTrip) {
                fullFlight.retNumOfConnections = sfList.size() - roundTripCounter-1;
                //first flight for return trip
                firstF = sfList.get(roundTripCounter-1);
                //last flight for return trip
                lastF = sfList.get(sfList.size() - 1);
                fullFlight.retDuration = slices.getJSONObject(1).getInt("duration");
                fullFlight.retDepartureCityCode = firstF.departureCityCode + "";
                fullFlight.retDepartureTime = firstF.departureTime.replace("-", "X") + "";
                fullFlight.retDepartureDate = firstF.departureDate.replace("-", "X") + "";
                fullFlight.retArrivalCityCode = lastF.arrivalCityCode + "";
                fullFlight.retArrivalTime = lastF.arrivalTime.replace("-", "X") + "";
                fullFlight.retArrivalDate = lastF.arrivalDate.replace("-", "X") + "";
                fullFlight.retDepartureCityCode = lastF.departureCityCode + "";
            }

            if ((fullFlight.isReturnTrip && sfList.size() == 2) || sfList.size() == 1) {
                fullFlight.isDirectFlight = true;
            }
            else {fullFlight.isDirectFlight=false;}
            //add
            flightResults.add(fullFlight);
        }
        //update the FlightCache
        Dashboard.FlightCache.addAll(flightResults);
        //return the results for other use
        return flightResults;
    }

    /**
     * Description: Gets the last search's flight results
     * @return ArrayList of the flights from the most recent search
     */
    public static ArrayList<Flight> getFlightResultsFromMostRecentSearch() {
        return Dashboard.FlightCache;
    }
}
