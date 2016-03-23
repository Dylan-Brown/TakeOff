package takeoff.cis350.upenn.edu.takeoff;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by judyweng on 3/23/2016.
 */
public class SearchParameterProcessor {

    SearchQuery sq = new SearchQuery();

    public void setDepartureDate(String departureDateInput) {
        sq.date = parseDate(departureDateInput);
    }

    public void setReturningDate(String returningDateInput) {
        sq.returnDate = parseDate(returningDateInput);
        sq.isRoundtrip = returningDateInput.equals("");
    }

    public void setCountries(String countries) {
    }

    public void setCities(String cities) {
    }

    public void setBudget(String budget) {
        sq.maxPrice = Integer.parseInt(budget);
    }

    public void setPassengerCount(String passengerCount) {
        sq.adultCount = Integer.parseInt(passengerCount);
    }

    public void setMaxConnectionDuration(String maxConnectionDurationinHours) {
        sq.maxConnectionDuration = Integer.parseInt(maxConnectionDurationinHours);
    }

    public void setCabin(String cabin) {
        sq.preferredCabin = parseCabin(cabin);
    }

    public void setAlliance(String alliance) {
        sq.alliance = alliance;
    }

    public void setairportCodes(String airportCodes) {
        sq.destination = parseLocations(airportCodes).get(0);
    }

    public void setNonStop(boolean nonstop) {
        if (nonstop) {
            sq.maxStops = 0;
        } else {
            sq.maxStops = 5;
        }
    }

    public void setRefundable(boolean refundable) {
        sq.refundability = refundable;
    }

/*
    public SearchParameterProcessor(String departureDate, String returnDate, String countries,
                                    String cities, String airportCodes, String budget,
                                    String quantity, String waitTime, String flightClass,
                                    String alliance, boolean nonstop, boolean refundable) {


        int parsedBudget;
        if (budget.equals("")) {
            parsedBudget = 99999;
        } else {
            parsedBudget = Integer.parseInt(budget);
        }

        int maxStop;
        int connectionDuration;
        if (nonstop) {
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
        if (roundtrip) {
            parsedReturnDate = parseDate(returnDate);
        }

        List<String> parsedCountries = parseLocations(countries);
        List<String> parsedCities = parseLocations(cities);
        List<String> parsedAirportCodes = parseLocations(airportCodes);

        String cabin = parseCabin(flightClass);
        String parsedAlliance = parseAlliance(alliance);

        SearchParameters sp = new SearchParameters(parsedDepartureDate, parsedCountries,
                parsedCities, parsedAirportCodes, parsedBudget, parsedQuantity, connectionDuration,
                maxStop, cabin, parsedAlliance, nonstop, refundable, roundtrip);


    }
*/

    /**
     * @param date - the string represent of the desired date in the format MM-dd-yyyy
     * @return string - the string represent of the desired date in the format yyyy-MM-dd
     */
    private String parseDate(String date) {
        int[] dateArr = new int[3];
        String[] dateStrings = date.trim().split("-");
        for (int i = 0; i < 3; i++) {
            dateArr[i] = Integer.parseInt(dateStrings[i]);
        }
        return "" + dateArr[2] + "-" + dateArr[1] + "-" + dateArr[0];
    }

    private List<String> parseLocations(String s) {
        List<String> locations = new ArrayList<>();
        String[] set = s.split(",");
        for (String x : set) {
            locations.add(x);
        }
        return locations;
    }

    private String parseCabin(String input) {
        if (input.equals("")) {
            return input;
        } else if (input.equals("NONE")) {
            return "";
        } else if (input.equals("PREMIUM COACH")) {
            return "PREMIUM_COACH";
        } else if (input.equals("COACH") || input.equals("BUSINESS") || input.equals("FIRST")) {
            return input;
        } else {
            throw new IllegalArgumentException("Class of " + input + " is invalid");
        }
    }

    private String parseAlliance(String input) {
        if (input.equals("")) {
            return input;
        } else if (input.equals("NONE")) {
            return "";
        } else if (input.equals("STAR ALLIANCE")) {
            return "STAR";
        } else if (input.equals("ONEWORLD") || input.equals("SKYTEAM")) {
            return input;
        } else {
            throw new IllegalArgumentException("Alliance type " + input + " is invalid");
        }
    }

    public SearchQuery getQuery() {
        return sq;
    }
}

