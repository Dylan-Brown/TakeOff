package takeoff.cis350.upenn.edu.takeoff;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by judyweng on 2/24/2016.
 */
public class SearchParameters {

    //YYYY-MM-DD
    String date;
    String cabin;
    String earliestTime;
    String latestTime;
    String alliance;

    int budget;
    int quantity;
    int connectionDuration;
    int maxStop;

    Set<String> countries;
    Set<String> cities;
    Set<String> airports;

    boolean refundable;
    boolean roundtrip; //T = yes roundtrip, F = not roundtrip
    boolean directFlight; //T = yes directflight

    public SearchParameters(){

    }
    public SearchParameters(String date, Set<String> countries,
                            Set<String> cities, Set<String> airports, int budget, int quantity,
                            int connectionDuration,int maxStop,
                            String cabin, String alliance, boolean directFlight, boolean refundable,
                            boolean roundtrip){
        //Set Variables

        this.date = date;
        this.countries = countries;
        this.cities = cities;
        this.airports = airports;
        this.budget = budget;
        this.quantity = quantity;
        this.directFlight = directFlight;
        this.connectionDuration = connectionDuration;
        this.maxStop = maxStop;
        this.refundable = refundable;
        this.cabin = cabin;
        this.earliestTime = "";
        this.latestTime = "";
        this.alliance = alliance;
        this.roundtrip = roundtrip;
    }
}