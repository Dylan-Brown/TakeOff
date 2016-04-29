package takeoff.cis350.upenn.edu.takeoff.ui.search;

/**
 * TODO: Write the class description
 */
public class SearchQuery {

    public int no_of_Slices=1;              // number of total trips; one-way is 1, roundtrip is 2
    public String origin=" ";
    public String destination="";
    public String date="";                  // in the format of YYYY-MM-DD
    public boolean isRoundtrip;
    public String returnDate="";            // in the format of YYYY-MM-DD, only if roundtrip
    public int adultCount=1;	            // number of adults (i.e. passengers)

    public String preferredCabin="";        // COACH, PREMIUM_COACH, BUSINESS, FIRST
    public double maxPrice=99999;           // in USD
    public boolean refundability=false;     // is refundable or not
    public int numberOfSolutions=20;        // maximum number of search results
    public int maxStops=5;                  // number of connections, direct flights = 0
    public String earliestTime="";          // in the format of HH:MM (24 hours)
    public String latestTime="";            // in the format of HH:MM (24 hours)
    public int maxConnectionDuration=4320;  // maximum amount in minutes
    public String alliance="";              // NONE, STAR, ONEWORLD, XXX, ...

    /**
     * Empty constructor
     */
    public SearchQuery() {
    };

    // returns the query in a String with a : separating them
    @Override
    public String toString() {
        String sq = no_of_Slices + ":";
        sq += origin + ":";
        sq += destination + ":";
        sq += date + ":";
        if (isRoundtrip){
            sq += "true" + ":";
        } else {
            sq += "false" + ":";
        }
        sq += returnDate + ":";
        sq += adultCount + ":";
        sq += preferredCabin + ":";
        sq += maxPrice + ":";
        if (refundability){
            sq += "true" + ":";
        } else {
            sq += "false" + ":";
        }
        sq += numberOfSolutions + ":";
        sq += maxStops + ":";
        sq += earliestTime + ":";
        sq += latestTime + ":";
        sq += maxConnectionDuration + ":";
        sq += alliance + ":";

        return sq;
    }

    /**
     * Creates a human-readable String that summarizes the contents of a given search query
     * @return the human readable String
     */
    public String humanReadable() {
        String hr = "Departure Date: " + date + "\n";
        hr += "Airport Code: " + origin + "\n";
        if (destination != null & !destination.isEmpty()) {
            hr += "Destination: " + destination + "\n";
        }
        return hr;
    }

    /**
     * Takes in a String that represents a SearchQuery and parses it back into a SearchQuery object
     * @param s the String that represents a SearchQuery
     * @return
     */
    public static SearchQuery parseSearchQuery(String s) {
        if (s == null || s.getClass() != String.class) {
            return null;
        }

        SearchQuery sq = new SearchQuery();
        String[] temp = s.split(":");

        sq.no_of_Slices = Integer.parseInt(temp[0].trim());
        //time not in search query
        sq.origin= temp[1];
        sq.destination= temp[2].trim();
        sq.date= temp[3].trim(); //in the format of YYYY-MM-DD
        if (temp[4].equals("true")) {
            sq.isRoundtrip = true;
        } else {
            sq.isRoundtrip = false;
        }
        sq.returnDate = temp[5]; //in the format of YYYY-MM-DD, only present if roundtrip
        sq.adultCount= Integer.parseInt(temp[6]);	//number of adults (i.e. passengers)
        sq.preferredCabin = temp[7]; //COACH, PREMIUM_COACH, BUSINESS, FIRST
        sq.maxPrice = Double.parseDouble(temp[8]); //in USD
        if (temp[9].equals("true")) {
            sq.refundability = true;
        } else {
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
