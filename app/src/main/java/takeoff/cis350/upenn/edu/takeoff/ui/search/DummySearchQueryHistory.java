package takeoff.cis350.upenn.edu.takeoff.ui.search;

import java.util.ArrayList;

import takeoff.cis350.upenn.edu.takeoff.ui.search.SearchQuery;

/**
 * Created by anakagold on 3/24/16.
 */
public class DummySearchQueryHistory {

    ArrayList<SearchQuery> searchHistory = new ArrayList<SearchQuery>();
    String[] stringHistory2 = new String[20];

    // Create a new class late that imitates this class but takes in a List of Queries

    public DummySearchQueryHistory() {

        for (int i = 0; i < 20; i++) {
            // make like 20 copies;
            SearchQuery SQ = new SearchQuery();

            SQ.no_of_Slices = 1; //number of total trips; one-way is 1 slice, roundtrip is 2 slices
            //update this later.
            //SQ.timeOfSearch ;
            SQ.origin = "PHL";
            SQ.destination = "MAA";
            SQ.date = "2017/01/01"; //in the format of YYYY-MM-DD
            SQ.isRoundtrip = false;
            SQ.returnDate = "2017/02/02"; //in the format of YYYY-MM-DD, only present if roundtrip
            SQ.adultCount = 1;    //number of adults (i.e. passengers)

            String preferredCabin = ""; //COACH, PREMIUM_COACH, BUSINESS, FIRST
            double maxPrice = 99999; //in USD
            boolean refundability = false; //is refundable or not
            int numberOfSolutions = 499; //maximum number of search results
            int maxStops = 5; //number of connections, direct flights = 0
            String earliestTime = ""; //in the format of HH:MM (24 hours)
            String latestTime = ""; //in the format of HH:MM (24 hours)
            int maxConnectionDuration = 4320; //maximum amount in minutes
            String alliance = ""; //NONE, STAR, ONEWORLD, XXX,XXX


            searchHistory.add(SQ);

            String temp1 = "";
            temp1 += "Origin : " + SQ.origin + "\n";
            temp1 += "Destination : " + SQ.destination + "\n";
            temp1 += " Travel date : " + SQ.date + "\n";
            temp1 += "Return date : " + SQ.returnDate + "\n";
            stringHistory2[i] = temp1;
        }
    }
}
