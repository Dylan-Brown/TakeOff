package takeoff.cis350.upenn.edu.takeoff.ui.search;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class SearchParameterProcessor {

    SearchQuery sq = new SearchQuery();
    ArrayList<String> countries = new ArrayList<>();

    /**
     * Description: sets the departure date that the user chose
     * @param departureDateInput
     */
    public void setDepartureDate(String departureDateInput) {
        if (!departureDateInput.equals("")) {
            sq.date = parseDate(departureDateInput);
        }
    }

    /**
     * Description: sets the returning date that the user chose
     * @param returningDateInput
     */
    public void setReturningDate(String returningDateInput) {
        sq.isRoundtrip = !returningDateInput.equals("");
        if (!returningDateInput.equals("")) {
            sq.returnDate = parseDate(returningDateInput);
        }
    }

    /**
     * Description: sets the destination countries that the user chose
     * @param countries
     */
    public void setCountries(String countries) {
        String[] c = countries.split(",");
        for (int i = 0; i < c.length; i++) {
            this.countries.add(c[i].trim().replace(" ", "_"));
        }
    }

    /**
     * Description: sets the destination cities that the user chose
     * @param cities
     */
    public void setCities(String cities) {
        if (cities.length()>0){sq.destination=cities.split(",")[0];}
    }

    /**
     * Description: sets the max budget that the user chose
     * @param budget
     */
    public void setBudget(String budget) {
        if (!budget.equals("")) {
            sq.maxPrice = Double.parseDouble(budget);
        }
    }

    /**
     * Description: sets the number of tickets that the user chose
     * @param passengerCount
     */
    public void setPassengerCount(String passengerCount) {
        if (!passengerCount.equals("")) {
            sq.adultCount = Integer.parseInt(passengerCount);
        }
    }

    /**
     * Description: sets the max connections (in minutes/hours) that the user chose
     * @param maxConnectionDurationinHours
     */
    public void setMaxConnectionDuration(String maxConnectionDurationinHours) {
        if (!maxConnectionDurationinHours.equals("")) {
            sq.maxConnectionDuration = 60 * Integer.parseInt(maxConnectionDurationinHours);
        }
    }

    /**
     * Description: sets the desired cabin/class that the user chose
     * @param cabin
     */
    public void setCabin(String cabin) {
        sq.preferredCabin = parseCabin(cabin);
    }

    /**
     * Description: sets the alliance that the user chose
     * @param alliance
     */
    public void setAlliance(String alliance) {
        sq.alliance = alliance;
    }

    /**
     * Description: sets the airport codes of the destination that the user chose
     * @param airportCodes
     */
    public void setAirportCodes(String airportCodes) {
        if (parseLocations(airportCodes).size()>0) {
            sq.destination = parseLocations(airportCodes).get(0);
        }
    }

    /**
     * Description: sets if the user wants a non-stop flight
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
     * Description: sets if the user wants a refundable flight
     * @param refundable
     */
    public void setRefundable(boolean refundable) {
        sq.refundability = refundable;
    }

    /**
     * Description: changes the date format so it's usable for the json request
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
     * Description: changes the location so it's usable for the json request
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
     * Description: changes the cabin choice so it's usable for the json request
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
     * Description: changes the alliance choice so it's usable for the json request
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
     * Description: Gives access to the search query created
     * @return sq - the current search query created
     */
    public SearchQuery getQuery() {
        return sq;
    }
}

