package takeoff.cis350.upenn.edu.takeoff;

import org.json.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.impl.client.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.*;

import android.app.Activity;
import android.util.Log;

import com.android.volley.*;
import com.android.volley.toolbox.*;

import org.json.JSONObject;

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

    public static boolean getSet() {
        if (isSet) {
            isSet = false;
            return true;
        }
        return isSet;
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

        if (2 == 4) {
            origin = sq.destination;
            destination = sq.origin;
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

    public static JSONArray executeAPIRequest(SearchQuery sq, String APIKey)
            throws JSONException {
        String jsonRequest = makeJSONObjectFromSearchQuery(sq);
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
            System.out.println(jsonArray.toString(2));
            return jsonArray;
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


    public static void executeAPIRequest(SearchQuery sq, String APIKey, Activity a)
            throws JSONException {
        JSONObject JORequest = new JSONObject(makeJSONObjectFromSearchQuery(sq));
        RequestQueue queue = Volley.newRequestQueue(a);
        String url = "https://www.googleapis.com/qpxExpress/v1/trips/search?key=" + APIKey;

        //  RequestFuture<JSONObject> future = RequestFuture.newFuture();
        //  JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, JORequest, future, future);

        Log.e("QPXAPIReader", "future future");


        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, JORequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("QPXAPIReader","onResponseTest");
                            JSONArray ja = response.getJSONObject("trips").getJSONArray("tripOption");
                            System.out.println(ja.toString(2));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("ERROR IN ON RESPONSE!");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            // set headers
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        Log.e("QPXAPIReader","addjsObjRequest");
        queue.add(jsObjRequest);
        Log.e("QPXAPIReader", "addjsObjRequestread");
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

