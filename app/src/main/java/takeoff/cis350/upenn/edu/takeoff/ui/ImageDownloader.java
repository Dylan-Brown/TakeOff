package takeoff.cis350.upenn.edu.takeoff.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * The AsyncTask used to download the image as the flight page background
 */
public class ImageDownloader extends AsyncTask<String, Void, String> {

    private View v;
    private Activity root;
    private BitmapDrawable bmd;
    private BitmapFactory.Options options;
    private String city;
    private String country;
    private String format;
    private String numResults;
    private String imageUrl;
    private String bingRoot = "https://api.datamarket.azure.com/Bing/Search/Image?Query=%27";
    private String key = "aa7k+1ynDhv9wK01uchT5Zb5wyV6LmiS6xKwblneys8";

    //AQuery aq;

    /**
     * Constructor for the image downloader
     * @param v the view we are editing
     * @param root the activity from which this was created
     */
    public ImageDownloader(View v, Activity root){
        options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        this.v = v;
        this.root = root;
        this.format = "&$format=json";
        imageUrl = null;
        bmd = null;
        numResults = "%27&$top=1";
    }

    @Override
    protected String doInBackground(String... params) {
        this.city = params[0];
        this.country = params[1];
        String location = city + ", " + country;
        String search = "";
        try {
            search = URLEncoder.encode(location, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("ImageDownloader", "doInBackground: " + e.getMessage());
            return null;
        }
        String link = bingRoot + search + numResults + format;
        return loadImageFromNetwork(link);
    }

    /**
     * After getting the image, set the background as the returned image
     * @param result the image result
     */
    protected void onPostExecute(String result) {
        System.out.println("IN POST EXECUTE: " + result);
        if(result != null) {
            // TODO: Implement or remove
            //(NetworkImageView) findViewById()

            /*aq.ajax(imageUrl, BitmapDrawable.class, 0, new AjaxCallback<BitmapDrawable>() {
                @Override
                public void callback(String url, BitmapDrawable object, AjaxStatus status) {
                    super.callback(url, object, status);
                    System.out.println("in callback");
                    System.out.println("url: " + url);
                    aq.id(v).image(url, true, true);
                    System.out.println("ending aq");
                }

            });*/
        }
    }

    /**
     * Load the image from the given link
     * @param link the link from which we load the image
     * @return
     */
    protected String loadImageFromNetwork(String link) {
        String searchRequestString = getSearchRequest(link);
        if(searchRequestString != null) {
            return processJSONRequest(searchRequestString);
        } else {
            return null;
        }
    }

    /**
     * Get the search request string based off of the link
     * @param link the link of the search request
     * @return the search request
     */
    private String getSearchRequest(String link) {
        byte[] keyBytes = Base64.encode((key + ":" + key).getBytes(),  Base64.NO_WRAP);
        String keyEnc = new String(keyBytes);
        String basicKey = String.format("Basic %s", keyEnc);

        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(link);
            request.setHeader("Content-type", "application/json");
            request.setHeader("Authorization", basicKey);
            ResponseHandler<String> rh = new BasicResponseHandler();
            String response = client.execute(request, rh);
            return response;
        } catch (Exception e) {
            Log.e("ImageDownloader", "getSearchRequest: " + e.getMessage());
        }
        return null;
    }

    /**
     * Process a JSON Request and get the links
     * @param searchRequestString the JSON request
     * @return the links
     */
    private String processJSONRequest(String searchRequestString) {
        try {
            JSONObject responseObj = new JSONObject(searchRequestString);
            JSONObject response = responseObj.getJSONObject("d");
            JSONArray responseArray = response.getJSONArray("results");
            this.imageUrl = responseArray.getJSONObject(0).getString("MediaUrl").toString();
            return imageUrl;
        } catch (JSONException e) {
            Log.e("ImageDownloader", "processJSONRequest: " + e.getMessage());
        }
        return null;
    }

    /**
     * Create a thread to process the image
     */
    private void processImage() {
        Thread t = new Thread(){
            public void run(){
                try{
                    loadImage(imageUrl);
                }
                catch(Exception ex){
                    return;
                }
            }
        };
        t.start();
    }

    /**
     * Open a stream to connect to the image
     * @param link the link to the image
     */
    private void loadImage(String link) {
        InputStream inputStream = null;
        try{
            inputStream = (InputStream) new URL(link).getContent();
            Bitmap bm = BitmapFactory.decodeStream(inputStream);
            bmd = new BitmapDrawable(v.getResources(), bm);

        } catch (Exception e) {
            Log.e("ImageDownloader", "loadImage: " + e.getMessage());
        }
    }
}