package takeoff.cis350.upenn.edu.takeoff.ui.search;

import android.util.Log;

import java.lang.reflect.Field;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import takeoff.cis350.upenn.edu.takeoff.R;

/**
 * Created by judyweng on 3/23/2016.
 */
public class SearchParameterProcessor {

    SearchQuery sq = new SearchQuery();
    ArrayList<String> airportCodes = new ArrayList<>();
    ArrayList<String> countries = new ArrayList<>();

    /**
     *
     * @param departureDateInput
     */
    public void setDepartureDate(String departureDateInput) {
        if (!departureDateInput.equals("")) {
            sq.date = parseDate(departureDateInput);
        }
    }

    /**
     *
     *
     * @param returningDateInput
     */
    public void setReturningDate(String returningDateInput) {
        sq.isRoundtrip = !returningDateInput.equals("");
        if (!returningDateInput.equals("")) {
            sq.returnDate = parseDate(returningDateInput);
        }
    }

    /**
     *
     * @param countries
     */
    public void setCountries(String countries) {
        Log.e("SPP", "COUNTRIES BEING SEARCHED: " + countries);
        String[] c = countries.split(",");
        for (int i = 0; i < c.length; i++) {
            this.countries.add(c[i].trim().replace(" ", "_"));
        }
    }

    /**
     *
     * @param cities
     */
    public void setCities(String cities) {
        if (cities.length()>0){sq.destination=cities.split(",")[0];}
    }

    /**
     *
     * @param budget
     */
    public void setBudget(String budget) {
        if (!budget.equals("")) {
            sq.maxPrice = Double.parseDouble(budget);
        }
    }

    /**
     *
     * @param passengerCount
     */
    public void setPassengerCount(String passengerCount) {
        if (!passengerCount.equals("")) {
            sq.adultCount = Integer.parseInt(passengerCount);
        }
    }

    /**
     *
     * @param maxConnectionDurationinHours
     */
    public void setMaxConnectionDuration(String maxConnectionDurationinHours) {
        if (!maxConnectionDurationinHours.equals("")) {
            sq.maxConnectionDuration = 60 * Integer.parseInt(maxConnectionDurationinHours);
        }
    }

    /**
     *
     * @param cabin
     */
    public void setCabin(String cabin) {
        sq.preferredCabin = parseCabin(cabin);
    }

    /**
     *
     * @param alliance
     */
    public void setAlliance(String alliance) {
        sq.alliance = alliance;
    }

    /**
     *
     * @param airportCodes
     */
    public void setAirportCodes(String airportCodes) {
        if (parseLocations(airportCodes).size()>0) {
            sq.destination = parseLocations(airportCodes).get(0);
        }
    }

    /**
     *
     * @param nonstop
     */
    public void setNonStop(boolean nonstop) {
        if (nonstop) {
            sq.maxStops = 0;
        } else {
            sq.maxStops = 5;
        }
    }

    /**
     *
     * @param refundable
     */
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
     *
     * @param date - the string represent of the desired date in the format MM-dd-yyyy
     * @return string - the string represent of the desired date in the format yyyy-MM-dd
     */
    private String parseDate(String date) {
        int[] dateArr = new int[3];
        String[] dateStrings = date.trim().split("-");
        for (int i = 0; i < 3; i++) {
            dateArr[i] = Integer.parseInt(dateStrings[i]);
        }
        return "" + dateArr[2] + "-" + dateArr[0] + "-" + dateArr[1];
    }

    /**
     *
     * @param s
     * @return
     */
    private List<String> parseLocations(String s) {
        List<String> locations = new ArrayList<>();
        String[] set = s.split(",");
        for (String x : set) {
            locations.add(x);
        }
        return locations;
    }

    /**
     *
     * @param input
     * @return
     */
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

    /**
     *
     * @param input
     * @return
     */
    private String parseAlliance(String input) {
        if (input.equals("")) {
            return input;
        } else if (input.equals("NONE")) {
            return " ";
        } else if (input.equals("STAR ALLIANCE")) {
            return "STAR";
        } else if (input.equals("ONEWORLD") || input.equals("SKYTEAM")) {
            return input;
        } else {
            throw new IllegalArgumentException("Alliance type " + input + " is invalid");
        }
    }

    /**
     *
     * @return
     */
    public SearchQuery getQuery() {
        Calendar calendar = Calendar.getInstance();
        // getTime() returns current time in milliseconds
        Date now = calendar.getTime();
        // passed the milliseconds to constructor of Timestamp class
        sq.timeOfSearch = new Time(now.getTime());
        return sq;
    }
}

