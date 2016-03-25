package takeoff.cis350.upenn.edu.takeoff.test2;

import android.app.Activity;

import com.android.volley.*;
import com.android.volley.toolbox.*;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import takeoff.cis350.upenn.edu.takeoff.VolleyCallback;

/**
 * Created by tangson on 3/24/16.
 */
public class test2 extends Activity {
    RequestQueue queue;

    public void executeQueue(String APIKey, JSONObject jo, Activity a) {
        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(a);
        String url = "https://www.googleapis.com/qpxExpress/v1/trips/search?key=" + APIKey;

        // Request a string response from the provided URL.

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, jo, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       try{ System.out.println(response.toString(2));}
                       catch (Exception e){}
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
// Add the request to the RequestQueue.
        queue.add(jsObjRequest);
    }

    public void getResponse(String APIKey, JSONObject jsonValue, final VolleyCallback callback) {
        String url = "https://www.googleapis.com/qpxExpress/v1/trips/search?key=" + APIKey;

        JsonObjectRequest strreq = new JsonObjectRequest(Request.Method.POST, url, jsonValue, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject Response) {
                callback.onSuccessResponse(Response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
            }
        }) {
            // set headers
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        queue.add(strreq);
    }


}
