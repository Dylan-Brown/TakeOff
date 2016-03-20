package takeoff.cis350.upenn.edu.takeoff;

import org.json.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.impl.client.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;

/**
 * Created by tangson on 3/17/16.
 */
public class QPXAPIReader {


    public JSONArray executeAPIRequest(SearchQuery sp, String APIKey) {
        String origin = sp.origin;
        String destination = sp.destination;
        String date = sp.date;
        int adultCount = sp.adultCount;
        boolean refundability = sp.refundability;
        int numberOfSolutions = sp.numberOfSolutions;
        int maxStops = sp.maxStops;// 0 for nonstop
        String alliance = sp.alliance;
        int maxConnectionDuration = sp.maxConnectionDuration;
        String preferredCabin = sp.preferredCabin;
        int maxPrice =sp.maxPrice;
        String earliestTime =sp.earliestTime;
        String latestTime = sp.latestTime;

        String requestSlice = "{ \"request\":{\"slice\": "
                + "[{\"origin\": \"" + origin + "\",\"destination\": \"" + destination +
                "\",\"date\": \"" + date + "\",\"maxStops\": " + maxStops +
                ",\"preferredCabin\": \"" + preferredCabin +
                "\",\"alliance\": \"" + alliance + "\",\"permittedDepartureTime\": {"
                + "\"earliestTime\": \"" + earliestTime + "\",\"latestTime\": \"" + latestTime
                + "\"},\"maxConnectionDuration\": \"" + maxConnectionDuration + "\" } ], ";
        String requestPassenger = "\"passengers\": { \"adultCount\": " + adultCount
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

    public void printAPIResults(JSONArray jsonArray) throws JSONException {
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
                for (int sliceIndex = 0; sliceIndex < segmentLength - 1; sliceIndex++) {
                    JSONObject connectingArrival = segments.getJSONObject(sliceIndex).getJSONArray("leg")
                            .getJSONObject(0);

                    System.out.println("Connecting Arrival Time: " + connectingArrival.getString("arrivalTime"));
                    System.out.println("Connection: " + connectingArrival.getString("destination"));
                    JSONObject connectingDeparture = segments.getJSONObject(sliceIndex + 1).getJSONArray("leg")
                            .getJSONObject(0);
                    System.out.println("Connecting Departure Time: " + connectingDeparture.getString("departureTime"));
                }
            }
            System.out.println("Arrival: " + lastLeg.getString("destination"));
            System.out.println("Arrival Time: " + lastLeg.getString("arrivalTime"));

        }
        System.out.println("Length is: " + jsonArray.length());
    }

    public List<Flight> getAPIResultsAsFlight() throws JSONException {

        List<Flight> flightResults = new ArrayList<Flight>();
        JSONArray jsonArray = executeAPIRequest(new SearchQuery(),"AIzaSyAvcsE9zxl3GvGtSncJYQf9zmSrRwSyAJQ");
        printAPIResults(jsonArray);


        for (int index = 0; index < jsonArray.length(); index++) {
            JSONObject tripOption = jsonArray.getJSONObject(index);//each Trip
            JSONArray slices = tripOption.getJSONArray("slice");
            int sliceLength = slices.length();
            JSONArray segments = slices.getJSONObject(0).getJSONArray("segment");
            int segmentLength = segments.length();
            int numOfLegs = segments.getJSONObject(segmentLength - 1).getJSONArray("leg").length();
            JSONObject firstLeg = segments.getJSONObject(0).getJSONArray("leg").getJSONObject(0);
            JSONObject lastLeg = segments.getJSONObject(segmentLength - 1).getJSONArray("leg").getJSONObject(numOfLegs-1);

            Flight flight = new Flight();
            flight.isSegment = false;
            flight.isRoundtrip = (sliceLength > 1);
            String costWithoutUSD = tripOption.getString("saleTotal").replaceAll("[^0-9\\.]+", "");
            flight.totalCost = Double.parseDouble(costWithoutUSD);
            flight.segments = new Flight[segmentLength];
            for (int x=0;x<segmentLength;x++) {
                flight.segments[x] = new Flight();
            }
            flight.departureCityCode=firstLeg.getString("origin");
            flight.departureTime=firstLeg.getString("departureTime");
            flight.arrivalCityCode=lastLeg.getString("destination");
            flight.arrivalTime=lastLeg.getString("arrivalTime");
            flight.cabinClass=segments.getJSONObject(0).getString("cabin");
            flight.id=tripOption.getString("id");
            flight.totalTravelTime=tripOption.getInt("duration");
    ///to do
            if (segmentLength > 1) {
                for (int sliceIndex = 0; sliceIndex < segmentLength - 1; sliceIndex++) {
                    JSONObject connectingArrival = segments.getJSONObject(sliceIndex).getJSONArray("leg")
                            .getJSONObject(0);

                    System.out.println("Connecting Arrival Time: " + connectingArrival.getString("arrivalTime"));
                    System.out.println("Connection: " + connectingArrival.getString("destination"));
                    JSONObject connectingDeparture = segments.getJSONObject(sliceIndex + 1).getJSONArray("leg")
                            .getJSONObject(0);
                    System.out.println("Connecting Departure Time: " + connectingDeparture.getString("departureTime"));
                }
            }

        }
        return null;
    }

}

