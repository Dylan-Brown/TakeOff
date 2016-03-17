package takeoff.cis350.upenn.edu.takeoff;

/**
 * Created by tangson on 3/17/16.
 */

public class SearchQuery {
    int no_of_Slices=1; //number of total trips; one-way is 1 slice, roundtrip is 2 slices
    String origin="";
    String destination="";
    String date=""; //in the format of YYYY-MM-DD
    int adultCount=1;	//number of adults (i.e. passengers)

    String preferredCabin=""; //COACH, PREMIUM_COACH, BUSINESS, FIRST
    int maxPrice=99999; //in USD
    boolean refundability=false; //is refundable or not
    int numberOfSolutions=499; //maximum number of search results
    int maxStops=5; //number of connections, direct flights = 0
    String earliestTime=""; //in the format of HH:MM (24 hours)
    String latestTime=""; //in the format of HH:MM (24 hours)
    int maxConnectionDuration=4320; //maximum amount in minutes
    String alliance=""; //STAR, ONEWORLD

    public SearchQuery() {
    };
}
