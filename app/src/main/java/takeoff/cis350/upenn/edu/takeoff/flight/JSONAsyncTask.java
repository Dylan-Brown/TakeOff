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
 * Created by anakagold on 4/8/16.
 */
public class JSONAsyncTask extends AsyncTask<String, Void, JSONArray> {

    Context context;

    /**
     * Constructor for the class
     * @param context
     */
    public JSONAsyncTask(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     *
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    /**
     *
     * @param params
     * @return
     */
    @Override
    protected JSONArray doInBackground(String... params) {
        try {
            JSONObject json = new JSONObject(params[0]);
            System.out.println(json.toString(4));
            HttpPost httpPost = new HttpPost("https://www.googleapis.com/qpxExpress/v1/trips/search?key=AIzaSyAvcsE9zxl3GvGtSncJYQf9zmSrRwSyAJQ");
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
                JSONArray jsonArray = jsonResponse.getJSONObject("trips").getJSONArray("tripOption");
                return jsonArray;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param result
     */
    @Override
    protected void onPostExecute(JSONArray result) {
        try {
            QPXJSONReader.getAPIResultsAsFlights(result);
            if(result != null) {
                System.out.println(result.toString(2));
            }
        } catch (JSONException e) {
            Log.e("ASyncTask", "onPostExecute: the task failed with a JSONException");

        }
        Log.i("ASyncTask", "onPostExecute: ending search page");
    }
}