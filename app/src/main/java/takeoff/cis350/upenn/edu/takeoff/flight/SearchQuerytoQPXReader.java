package takeoff.cis350.upenn.edu.takeoff.flight;

import org.json.*;
import android.util.Log;
import com.android.volley.*;
import org.json.JSONObject;

import takeoff.cis350.upenn.edu.takeoff.ui.search.SearchQuery;

/**
 * This class holds methods that will convert between the desired search parameters (held in SearchQuery class) to
 * usable format to make the search and then to go from json objects to readable information for debugging
 */
public class SearchQuerytoQPXReader {

    static RequestQueue queue;
    static JSONArray JSONResponse;
    static boolean isSet = true;


    /**
     * Description: Given the object that holds the user's desired search parameters, format the information
     * correctly in json in preparation to be passed to the API
     *
     * @param sq - The search query object with all the parameters the user entered to create the API search
     * @return String - String representation of the search query that can be parsed into a json object
     */
    public static String makeJSONSearchObject(SearchQuery sq) {

        // TODO: comment this code so that it's understandable

        //extracting information for the API request from the object to use in the code
        int adultCount = sq.adultCount;
        boolean refundability = sq.refundability;
        int numberOfSolutions = sq.numberOfSolutions;
        double maxPrice = sq.maxPrice;

        String origin = "PHL";
        String destination = sq.destination;
        String date = sq.date;
        int maxStops = sq.maxStops;// 0 for nonstop
        String alliance = sq.alliance;
        int maxConnectionDuration = sq.maxConnectionDuration;
        String preferredCabin = sq.preferredCabin;
        String earliestTime = sq.earliestTime;
        String latestTime = sq.latestTime;

        //Formatting the request for a flight search
        String requestSlice = "{ \"request\":{\"slice\": " + "[{\"origin\": \"" + origin
                + "\",\"destination\": \"" + destination + "\",\"date\": \"" + date
                + "\",\"maxStops\": " + maxStops + ",\"preferredCabin\": \"" + preferredCabin
                + "\",\"alliance\": \"" + alliance + "\",\"permittedDepartureTime\": {"
                + "\"earliestTime\": \"" + earliestTime + "\",\"latestTime\": \"" + latestTime
                + "\"},\"maxConnectionDuration\": \"" + maxConnectionDuration + "\" } ";

        //If this is a return trip, will need to add an extra part to the json object
        if (sq.isRoundtrip) {
            //Some variables are changed in this slice
            origin = sq.destination;
            destination = "PHL";
            date = sq.returnDate;

            String slice = " , {\"origin\": \"" + origin + "\",\"destination\": \"" + destination
                    + "\",\"date\": \"" + date + "\",\"maxStops\": " + maxStops
                    + ",\"preferredCabin\": \"" + preferredCabin + "\",\"alliance\": \"" + alliance
                    + "\",\"permittedDepartureTime\": {" + "\"earliestTime\": \"" + earliestTime
                    + "\",\"latestTime\": \"" + latestTime + "\"},\"maxConnectionDuration\": \""
                    + maxConnectionDuration + "\" } ";
            requestSlice += slice;
        }

        //Remainder of the information
        String requestPassenger = "],\"passengers\": { \"adultCount\": " + adultCount
                + ", \"infantInLapCount\": 0, \"infantInSeatCount\": 0, \"childCount\": 0, "
                + "\"seniorCount\": 0 }, ";
        String requestInfo = "\"solutions\":" + numberOfSolutions + ", " + "\"refundable\":"
                + refundability + ", " + "\"maxPrice\": \"USD" + maxPrice + "\" }}";

        //Adding all the info together to get the final result
        String jsonRequest = requestSlice + requestPassenger + requestInfo;

        try {
            Log.i("SearchQuerytoQPXReader", "makeJSONSearchObject: "
                    + new JSONArray(jsonRequest).toString(2));
        } catch (Exception e) {
            // TODO: Handle
        }
        return jsonRequest;
    }

    /**
     * Description: prints out the json array assuming this json array is the result recieved from the QPX API
     * @param jsonArray - The json array to be printed
     * @throws JSONException
     */
    public static void printAPIResults(JSONArray jsonArray) throws JSONException {

        // TODO: comment this code so that it's understandable

        //Iterate through all the flights
        for (int index = 0; index < jsonArray.length(); index++) {
            JSONObject tripOption = jsonArray.getJSONObject(index);

            //Getting the relavent information for this specific flight
            JSONArray slices = tripOption.getJSONArray("slice");
            JSONArray segments = slices.getJSONObject(0).getJSONArray("segment");
            int segmentLength = segments.length();
            JSONObject firstLeg = segments.getJSONObject(0).getJSONArray("leg").getJSONObject(0);
            JSONArray tempArr = segments.getJSONObject(segmentLength - 1).getJSONArray("leg");
            JSONObject lastLeg = tempArr.getJSONObject(0);

            Log.i("SearchQuerytoQPXReader", "printAPIResults: Sale Total "
                    + tripOption.getString("saleTotal"));
            Log.i("SearchQuerytoQPXReader", "printAPIResults: Number of connections: "
                    + segmentLength);
            Log.i("SearchQuerytoQPXReader", "printAPIResults: Departure: "
                    + firstLeg.getString("origin"));
            Log.i("SearchQuerytoQPXReader", "printAPIResults: Departure Time: "
                    + firstLeg.getString("departureTime"));

            //If this has more than one connection, get all of those too
            if (segmentLength > 1) {
                for (int segIndex = 0; segIndex < segmentLength - 1; segIndex++) {
                    JSONObject connectingArrival = segments.getJSONObject(segIndex)
                            .getJSONArray("leg").getJSONObject(0);

                    Log.i("SearchQuerytoQPXReader", "printAPIResults: Connecting Arrival Time: "
                            + connectingArrival.getString("arrivalTime"));
                    Log.i("SearchQuerytoQPXReader", "printAPIResults: Connection: "
                            + connectingArrival.getString("destination"));
                    JSONObject connectingDeparture = segments.getJSONObject(segIndex + 1)
                            .getJSONArray("leg").getJSONObject(0);
                    Log.i("SearchQuerytoQPXReader", "printAPIResults: Connecting Departure Time: "
                            + connectingDeparture.getString("departureTime"));
                }
            }
            Log.i("SearchQuerytoQPXReader", "printAPIResults: Arrival: "
                    + lastLeg.getString("destination"));
            Log.i("SearchQuerytoQPXReader", "printAPIResults: Arrival Time: "
                    + lastLeg.getString("arrivalTime"));
        }
        Log.i("SearchQuerytoQPXReader", "printAPIResults: Length is: " + jsonArray.length());
    }
}

