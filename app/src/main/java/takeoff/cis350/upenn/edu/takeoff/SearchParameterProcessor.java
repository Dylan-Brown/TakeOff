package takeoff.cis350.upenn.edu.takeoff;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by judyweng on 3/23/2016.
 */
public class SearchParameterProcessor {

    List<SearchParameters> spList;

    public SearchParameterProcessor(String departureDate, String returnDate, String countries,
                                    String cities, String airportCodes, String budget,
                                    String quantity, String waitTime, String flightClass,
                                    String alliance, boolean nonstop, boolean refundable){
        spList = new ArrayList<SearchParameters>();
        int parsedQuantity = Integer.parseInt(quantity);

        int parsedBudget;
        if(budget.equals("")) {
            parsedBudget = 99999;
        } else {
            parsedBudget = Integer.parseInt(budget);
        }

        int maxStop;
        int connectionDuration;
        if(nonstop) {
            maxStop = 0;
            connectionDuration = 0;
        } else {
            maxStop = 5;
            if (waitTime.equals("")) {
                connectionDuration = 4320;
            } else {
                connectionDuration = Integer.parseInt(waitTime) * 60;
            }
        }

        //dates
        boolean roundtrip = !returnDate.equals("");
        String parsedReturnDate = "";
        String parsedDepartureDate = parseDate(departureDate);
        if(roundtrip){
            parsedReturnDate = parseDate(returnDate);
        }

        Set<String> parsedCountries = parseLocations(countries);
        Set<String> parsedCities = parseLocations(cities);
        Set<String> parsedAirportCodes = parseLocations(airportCodes);

        String cabin = parseCabin(flightClass);
        String parsedAlliance = parseAlliance(alliance);

        SearchParameters departure = new SearchParameters(parsedDepartureDate,parsedCountries,
                parsedCities, parsedAirportCodes,parsedBudget, parsedQuantity, connectionDuration,
                maxStop, cabin, parsedAlliance,nonstop, refundable, roundtrip);
        spList.add(departure);

        if(roundtrip) {
            SearchParameters returning = new SearchParameters(parsedReturnDate,parsedCountries,
                    parsedCities, parsedAirportCodes,parsedBudget, parsedQuantity, connectionDuration,
                    maxStop, cabin, parsedAlliance,nonstop, refundable, roundtrip);
            spList.add(returning);
        }
    }

    /**
     *
     * @param date - the string represent of the desired date in the format MM-dd-yyyy
     * @return string - the string represent of the desired date in the format yyyy-MM-dd
     */
    private String parseDate(String date){
        int[] dateArr = new int[3];
        String[] dateStrings = date.trim().split("-");
        for(int i = 0; i < 3; i++) {
            dateArr[i] = Integer.parseInt(dateStrings[i]);
        }
        return "" + dateArr[2] + "-" + dateArr[1] + "-" + dateArr[0];
    }

    private Set<String> parseLocations(String s) {
        Set<String> locations = new HashSet<>();
        String[] set = s.split(",");
        for(String x : set) {
            locations.add(x);
        }
        return locations;
    }

    private String parseCabin(String input) {
        if(input.equals("")){
            return input;
        } else if(input.equals("NONE")){
            return "";
        } else if(input.equals("PREMIUM COACH")){
            return "PREMIUM_COACH";
        } else if(input.equals("COACH") || input.equals("BUSINESS") || input.equals("FIRST")){
            return input;
        }
        else {
            throw new IllegalArgumentException("Class of " + input + " is invalid");
        }
    }

    private String parseAlliance(String input) {
        if(input.equals("")){
            return input;
        } else if(input.equals("NONE")){
            return "";
        } else if(input.equals("STAR ALLIANCE")){
            return "STAR";
        } else if(input.equals("ONEWORLD") || input.equals("SKYTEAM")){
            return input;
        }
        else {
            throw new IllegalArgumentException("Alliance type " + input + " is invalid");
        }
    }

    public List<SearchParameters> getSearchParams() {
        return spList;
    }
}

