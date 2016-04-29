package takeoff.cis350.upenn.edu.takeoff.flight;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * TODO: Write the class javadoc description
 */
public class JSONAsyncTask extends AsyncTask<String, Void, JSONArray> {

    private static final String HTTP_LINK = "https://www.googleapis.com/qpxExpress/v1/trips/"
            + "search?key=AIzaSyAvcsE9zxl3GvGtSncJYQf9zmSrRwSyAJQ";
    Context context;

    /**
     * Constructor for the class
     * @param context
     */
    public JSONAsyncTask(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * TODO: Write a method javadoc comment, explain parameters
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    /**
     * TODO: Write a method javadoc comment, explain parameters
     * @param params
     * @return
     */
    @Override
    protected JSONArray doInBackground(String... params) {
        try {
            // TODO: comment this code so that it's understandable
            JSONObject json = new JSONObject(params[0]);
            HttpPost httpPost = new HttpPost(HTTP_LINK);
            StringEntity SEJson = new StringEntity(json.toString());
            httpPost.setEntity(SEJson);
            httpPost.setHeader("Content-type", "application/json");
            HttpClient hc = HttpClientBuilder.create().build();
            ResponseHandler<String> rh = new BasicResponseHandler();
            HttpResponse response = hc.execute(httpPost);
            // StatusLine stat = response.getStatusLine();
            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                HttpEntity entity = response.getEntity();
                JSONObject jsonResponse = new JSONObject(EntityUtils.toString(entity));
                JSONObject trips = jsonResponse.getJSONObject("trips");
                JSONArray jsonArray = trips.getJSONArray("tripOption");
                return jsonArray;
            }

        } catch (Exception e) {
            Log.e("JSONAsyncTask", "doInBackground: " + e.getMessage());
        }
        return null;
    }

    /**
     * TODO: Write a method javadoc comment, explain parameters
     * @param result
     */
    @Override
    protected void onPostExecute(JSONArray result) {

        // TODO: comment this code so that it's understandable

        try {
            QPXJSONReader.getAPIResultsAsFlights(result);
            if(result != null) {
                System.out.println(result.toString(2));
            }
        } catch (JSONException e) {
            Log.e("ASyncTask", "onPostExecute: " + e.getMessage());
        }
    }
}