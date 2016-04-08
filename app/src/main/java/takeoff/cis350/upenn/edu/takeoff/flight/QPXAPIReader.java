package takeoff.cis350.upenn.edu.takeoff.flight;

import org.json.*;

import android.util.Log;

import com.android.volley.*;

import org.json.JSONObject;

import takeoff.cis350.upenn.edu.takeoff.ui.search.SearchQuery;

/**
 * Created by tangson on 3/17/16.
 */
public class QPXAPIReader {
    static RequestQueue queue;
    static JSONArray JSONResponse;
    static boolean isSet = true;

    public static void changeResponse(JSONArray ja) {
        JSONResponse = ja;
        isSet = true;
    }

    public static String makeJSONObjectFromSearchQuery(SearchQuery sq) {
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

        String requestSlice = "{ \"request\":{\"slice\": " + "[{\"origin\": \"" + origin + "\",\"destination\": \""
                + destination + "\",\"date\": \"" + date + "\",\"maxStops\": " + maxStops + ",\"preferredCabin\": \""
                + preferredCabin + "\",\"alliance\": \"" + alliance + "\",\"permittedDepartureTime\": {"
                + "\"earliestTime\": \"" + earliestTime + "\",\"latestTime\": \"" + latestTime
                + "\"},\"maxConnectionDuration\": \"" + maxConnectionDuration + "\" } ";

        if (sq.isRoundtrip) {
            origin = sq.destination;
            destination = "PHL";
            date = sq.returnDate;
            maxStops = sq.maxStops;// 0 for nonstop
            alliance = sq.alliance;
            maxConnectionDuration = sq.maxConnectionDuration;
            preferredCabin = sq.preferredCabin;
            earliestTime = sq.earliestTime;
            latestTime = sq.latestTime;

            String slice = " , {\"origin\": \"" + origin + "\",\"destination\": \""
                    + destination + "\",\"date\": \"" + date + "\",\"maxStops\": " + maxStops + ",\"preferredCabin\": \""
                    + preferredCabin + "\",\"alliance\": \"" + alliance + "\",\"permittedDepartureTime\": {"
                    + "\"earliestTime\": \"" + earliestTime + "\",\"latestTime\": \"" + latestTime
                    + "\"},\"maxConnectionDuration\": \"" + maxConnectionDuration + "\" } ";
            requestSlice += slice;
        }
        String requestPassenger = "],\"passengers\": { \"adultCount\": " + adultCount
                + ", \"infantInLapCount\": 0, \"infantInSeatCount\": 0, \"childCount\": 0, \"seniorCount\": 0 }, ";
        String requestInfo = "\"solutions\":" + numberOfSolutions + ", " + "\"refundable\":" + refundability + ", "
                + "\"maxPrice\": \"USD" + maxPrice + "\" }}";
        String jsonRequest = requestSlice + requestPassenger + requestInfo;
        Log.e("JSONREQUEST", jsonRequest);
        try {
            System.out.println(new JSONArray(jsonRequest).toString(2));
        } catch (Exception e) {
        }
        return jsonRequest;
    }

    public static void printAPIResults(JSONArray jsonArray) throws JSONException {
        for (int index = 0; index < jsonArray.length(); index++) {
            JSONObject tripOption = jsonArray.getJSONObject(index);

            JSONArray slices = tripOption.getJSONArray("slice");
            JSONArray segments = slices.getJSONObject(0).getJSONArray("segment");
            int segmentLength = segments.length();
            JSONObject firstLeg = segments.getJSONObject(0).getJSONArray("leg").getJSONObject(0);
            JSONObject lastLeg = segments.getJSONObject(segmentLength - 1).getJSONArray("leg").getJSONObject(0);

            System.out.println("");
            System.out.println("Sale Total " + tripOption.getString("saleTotal"));
            System.out.println("Number of connections: " + segmentLength);
            System.out.println("Departure: " + firstLeg.getString("origin"));
            System.out.println("Departure Time: " + firstLeg.getString("departureTime"));
            if (segmentLength > 1) {
                for (int segIndex = 0; segIndex < segmentLength - 1; segIndex++) {
                    JSONObject connectingArrival = segments.getJSONObject(segIndex).getJSONArray("leg")
                            .getJSONObject(0);

                    System.out.println("Connecting Arrival Time: " + connectingArrival.getString("arrivalTime"));
                    System.out.println("Connection: " + connectingArrival.getString("destination"));
                    JSONObject connectingDeparture = segments.getJSONObject(segIndex + 1).getJSONArray("leg")
                            .getJSONObject(0);
                    System.out.println("Connecting Departure Time: " + connectingDeparture.getString("departureTime"));
                }
            }
            System.out.println("Arrival: " + lastLeg.getString("destination"));
            System.out.println("Arrival Time: " + lastLeg.getString("arrivalTime"));
        }
        System.out.println("Length is: " + jsonArray.length());
    }


}

