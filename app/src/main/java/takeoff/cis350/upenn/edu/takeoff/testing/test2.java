package takeoff.cis350.upenn.edu.takeoff.testing;

import android.app.Activity;

import com.android.volley.*;
import com.android.volley.toolbox.*;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import takeoff.cis350.upenn.edu.takeoff.volley.VolleyCallback;

/**
 *  TODO: Write a class description
 */
public class test2 extends Activity {
    RequestQueue queue;

    public void executeQueue(String APIKey, JSONObject jo, Activity a) {
        // instantiate the RequestQueue.
        queue = Volley.newRequestQueue(a);
        String url = "https://www.googleapis.com/qpxExpress/v1/trips/search?key=" + APIKey;

        // request a string response from the provided URL.
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
        // add the request to the RequestQueue.
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
