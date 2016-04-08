package takeoff.cis350.upenn.edu.takeoff.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.View;

import com.android.volley.toolbox.NetworkImageView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;


/**
 * Created by Sandy on 3/24/2016.
 */
public class ImageDownloader extends AsyncTask<String, Void, String> {
    View v;
    Activity root;
    BitmapDrawable bmd;
    BitmapFactory.Options options;
    String city;
    String country;
    String format;
    String numResults;
    String imageUrl;
    String bingRoot = "https://api.datamarket.azure.com/Bing/Search/Image?Query=%27";
    String key = "aa7k+1ynDhv9wK01uchT5Zb5wyV6LmiS6xKwblneys8";

    //AQuery aq;


    public ImageDownloader(View v, Activity root){
        options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        System.out.println("IMAGEDOWNLOADER CONSTRUCTOR");
        this.v = v;
        this.root = root;
        this.format = "&$format=json";
        imageUrl = null;
        bmd = null;
        numResults = "%27&$top=1";

        //aq = new AQuery(root, v);
    }

    @Override
    protected String doInBackground(String... params) {
        System.out.println("DO IN BAKGROUND");
        this.city = params[0];
        this.country = params[1];
        String location = city + ", " + country;
        System.out.println("LOCATION BEFORE ENCODE: " + city + ", " + country);
        String search = "";
        try {
            search = URLEncoder.encode(location, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("SEARCH NOT WORKING:" + search);
            return null;
        }
        String link = bingRoot + search + numResults + format;
        String stuff = loadImageFromNetwork(link);
        System.out.println("END OF BACKGROUND: " + stuff);
        return stuff;
    }


    //after getting the image, set the background as the returned image
    protected void onPostExecute(String result) {
        System.out.println("IN POST EXECUTE: " + result);
        if(result != null) {
            System.out.println("RESULT: " + result);
            System.out.println("IMAGEURL: " + imageUrl);

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

            System.out.println("SETTING BACKGROUND");
        } else {
            System.out.println("DAMMIT NULL");
            return;
        }
    }

    //load the image from the given link
    protected String loadImageFromNetwork(String link) {
        String searchRequestString = getSearchRequest(link);

        if(searchRequestString != null) {
            System.out.println("GOING TO PROCESSING");
            return processJSONRequest(searchRequestString);
            //processImage();
        }
        else {
            return null;
        }
    }


    //get the searh Request string based off of the link
    private String getSearchRequest(String link) {
        System.out.println("MADE IT TO GETSEARCHRQEUST");
        System.out.println("LINK: " + link);
        byte[] keyBytes = Base64.encode((key + ":" + key).getBytes(),  Base64.NO_WRAP);
        String keyEnc = new String(keyBytes);
        String basicKey = String.format("Basic %s", keyEnc);
        System.out.println("BASIC KEY: " + basicKey);

        //credentials = 'Basic ' + (':%s' % api_key).encode('base64').strip()

        try {
            System.out.println("LINK: " + link);
            System.out.println("JUST MADE URL THING IN SEARCH");

            StringBuilder sb = new StringBuilder();


            HttpClient client = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(link);
            request.setHeader("Content-type", "application/json");
            request.setHeader("Authorization", basicKey);
            ResponseHandler<String> rh = new BasicResponseHandler();
            String response = client.execute(request, rh);
            System.out.println("RESPONSE: " + response);
            return response;
        } catch (UnsupportedEncodingException e) {
            System.out.println(link + " cannot be encded in UTF-8");
            //e.printStackTrace();
        } catch (MalformedURLException e) {
            System.out.println("is not formatted as a url");
            //e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException getSearchResults");
            e.printStackTrace();
        }
        return null;
    }



    //Process JSON Request and get the links
    private String processJSONRequest(String searchRequestString) {
        //System.out.println("image search result: " + searchRequestString);
        StringBuilder sb = new StringBuilder();
        try {
            System.out.println("TRY");
            JSONObject responseObj = new JSONObject(searchRequestString);
            System.out.println("made responseobj");
            JSONObject response = responseObj.getJSONObject("d");
            JSONArray responseArray = response.getJSONArray("results");
            System.out.println("image search result numer: " + responseArray.length());
            this.imageUrl = responseArray.getJSONObject(0).getString("MediaUrl").toString();
            System.out.println("PROCESSJSONREQUEST: " + imageUrl);
            return imageUrl;
        } catch (JSONException e) {
            System.out.println(searchRequestString + " is a JSON Exception");
        }
        return null;
    }

    //have a thread to process the image
    private void processImage() {
        Thread t = new Thread(){
            public void run(){
                try{
                    System.out.println("GONNA LOAD IMAGE");
                    loadImage(imageUrl);
                }
                catch(Exception ex){
                    System.out.println("THREAD EXCEPTION");
                    return;
                }
            }
        };
        t.start();
    }

    //opening a stream to connect to the picture
    private void loadImage(String link) {
        InputStream inputStream = null;
        System.out.println("INSIDE LOAD IMAGE. TRYING HARD NOW");
        System.out.println("LINK: " + link);

        try{
            inputStream = (InputStream) new URL(link).getContent();
            Bitmap bm = BitmapFactory.decodeStream(inputStream);
            System.out.println("IS BITMAP NULL: " + (bm == null));
            bmd = new BitmapDrawable(v.getResources(), bm);

        } catch (MalformedURLException e) {
            System.out.println("MALFORMEDURL EXCEPTION");
        } catch (ProtocolException e) {
            System.out.println("PROTOCOL EXCEPTION");
        } catch (IOException e) {
            System.out.println("IOEXCEPTION");
        }
    }
}