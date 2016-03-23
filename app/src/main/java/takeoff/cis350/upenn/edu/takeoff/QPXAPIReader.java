package takeoff.cis350.upenn.edu.takeoff;
        import org.json.*;

        import java.io.*;
        import java.util.ArrayList;
        import java.util.List;

        import org.apache.http.impl.client.*;
        import org.apache.http.client.*;
        import org.apache.http.client.methods.*;
        import org.apache.http.entity.*;

/**
 * Created by tangson on 3/17/16.
 */
public class QPXAPIReader {
    public static JSONArray executeAPIRequest(SearchQuery sq, String APIKey) {
        int adultCount = sq.adultCount;
        boolean refundability = sq.refundability;
        int numberOfSolutions = sq.numberOfSolutions;
        int maxPrice = sq.maxPrice;

        String origin = sq.origin;
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
            destination = sq.origin;
            date = sq.returnDate ;
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
            requestSlice+=slice;
        }
        String requestPassenger = "],\"passengers\": { \"adultCount\": " + adultCount
                + ", \"infantInLapCount\": 0, \"infantInSeatCount\": 0, \"childCount\": 0, \"seniorCount\": 0 }, ";
        String requestInfo = "\"solutions\":" + numberOfSolutions + ", " + "\"refundable\":" + refundability + ", "
                + "\"maxPrice\": \"USD" + maxPrice + "\" }}";
        String jsonRequest = requestSlice + requestPassenger + requestInfo;
        System.out.println(jsonRequest);

        try {
            JSONObject json = new JSONObject(jsonRequest);
            System.out.println(json.toString(4));
            HttpPost httpPost = new HttpPost("https://www.googleapis.com/qpxExpress/v1/trips/search?key=" + APIKey);
            StringEntity SEJson = new StringEntity(jsonRequest);
            httpPost.setEntity(SEJson);
            httpPost.setHeader("Content-type", "application/json");

            HttpClient hc = HttpClientBuilder.create().build();
            ResponseHandler<String> rh = new BasicResponseHandler();
            String response = hc.execute(httpPost, rh);
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray jsonArray = jsonResponse.getJSONObject("trips").getJSONArray("tripOption");
            return jsonArray;
            // System.out.println(jsonArray.toString(2));
        } catch (JSONException e) {
            System.out.println("JSON unable to be read.");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            System.out.println("RESPONSE/EXECUTE ERROR!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

