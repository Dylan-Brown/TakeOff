package takeoff.cis350.upenn.edu.takeoff;

import java.security.Timestamp;
import java.sql.Time;

/**
 * Created by tangson on 3/17/16.
 */

public class SearchQuery {
    int no_of_Slices=1; //number of total trips; one-way is 1 slice, roundtrip is 2 slices

    Time timeOfSearch;
    String origin=" ";
    String destination=" ";
    String date=""; //in the format of YYYY-MM-DD
    boolean isRoundtrip;
    String returnDate=""; //in the format of YYYY-MM-DD, only present if roundtrip
    int adultCount=1;	//number of adults (i.e. passengers)

    String preferredCabin=""; //COACH, PREMIUM_COACH, BUSINESS, FIRST
    double maxPrice=99999; //in USD
    boolean refundability=false; //is refundable or not
    int numberOfSolutions=20; //maximum number of search results
    int maxStops=5; //number of connections, direct flights = 0
    String earliestTime=""; //in the format of HH:MM (24 hours)
    String latestTime=""; //in the format of HH:MM (24 hours)
    int maxConnectionDuration=4320; //maximum amount in minutes
    String alliance=""; //NONE, STAR, ONEWORLD, XXX,XXX

    public SearchQuery() {
    };

    // returns the query in a String with a : separating them
    @Override
    public String toString() {
        String toReturn = " ";
        //not stroring the time now.
        toReturn += "" + no_of_Slices + ":";
        toReturn += origin + ":";
        toReturn += destination + ":";
        toReturn += date + ":";
        if (isRoundtrip){
            toReturn += "true" + ":";
        }
        else {
            toReturn += "false" + ":";
        }
        toReturn += returnDate + ":";
        toReturn += adultCount + ":";
        toReturn += preferredCabin + ":";
        toReturn += maxPrice + ":";
        if (refundability){
            toReturn += "true" + ":";
        }
        else {
            toReturn += "false" + ":";
        }
        toReturn += numberOfSolutions + ":";
        toReturn += maxStops + ":";
        toReturn += earliestTime + ":";
        toReturn += latestTime + ":";
        toReturn += maxConnectionDuration + ":";
        toReturn += alliance + ":";

        return toReturn;
    }

    public String humanReadable() {
        StringBuilder sb = new StringBuilder();
        sb.append("Departure Date: " + date + "\n");
        sb.append("Airport Code: " + origin + "\n");
        if (destination != null & !destination.isEmpty()) {
            sb.append("Destination: " + destination + "\n");
        }
        // TODO: Add more when relevant
        // Departure Date*, Return Date, Countries, Cities, Airport Code*
        return sb.toString();
    }

    public static SearchQuery parseSearchQuery(String s) {

        SearchQuery sq = new SearchQuery();

        String[] temp = s.split(":");

        sq.no_of_Slices = Integer.parseInt(temp[0].trim());
        //time not in search query
        sq.origin= temp[1];
        sq.destination= temp[2].trim();
        sq.date= temp[3].trim(); //in the format of YYYY-MM-DD
        if (temp[4].equals("true")) {
            sq.isRoundtrip = true;
        }
        else {
            sq.isRoundtrip = false;
        }
        sq.returnDate = temp[5]; //in the format of YYYY-MM-DD, only present if roundtrip
        sq.adultCount= Integer.parseInt(temp[6]);	//number of adults (i.e. passengers)
        sq.preferredCabin = temp[7]; //COACH, PREMIUM_COACH, BUSINESS, FIRST
        sq.maxPrice = Double.parseDouble(temp[8]); //in USD
        if (temp[9].equals("true")) {
            sq.refundability = true;
        }
        else {
            sq.refundability = false;
        }
        sq.numberOfSolutions = Integer.parseInt(temp[10]); //maximum number of search results
        sq.maxStops = Integer.parseInt(temp[11]); //number of connections, direct flights = 0
        sq.earliestTime= temp[12]; //in the format of HH:MM (24 hours)
        sq.latestTime = temp[13]; //in the format of HH:MM (24 hours)
        sq.maxConnectionDuration = Integer.parseInt(temp[14]); //maximum amount in minutes
        if (temp.length > 15)
            sq.alliance = temp[15]; //NONE, STAR, ONEWORLD, XXX,XXX

        return sq;
    }
}
