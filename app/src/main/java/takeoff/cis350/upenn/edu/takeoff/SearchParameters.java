package takeoff.cis350.upenn.edu.takeoff;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by judyweng on 2/24/2016.
 */
public class SearchParameters {

    private int departureMonth;
    private int departureDay;
    private int departureYear;

    private int arrivalMonth;
    private int arrivalDay;
    private int arrivalYear;

    private double maxBudget;
    private int maxConFlight;

    private Set<String> countries;
    private Set<String> cities;

    private boolean refundable;


    public SearchParameters() {
        countries = new HashSet<>();
        cities = new HashSet<>();
    }

    public SearchParameters(String departureDate, String arrivalDate, double budget, String countries,
                            String cities) {
        //Set Variables
        int[] departureArr = parseDate(departureDate);
        setDepartureDate(departureArr);
        int[] arrivalArr = parseDate(arrivalDate);
        setArrivalDate(arrivalArr);
        maxBudget = budget;
        this.countries = parseLocations(countries);
        this.cities = parseLocations(cities);
    }

    public SearchParameters(String departureDate, String arrivalDate, double budget, String countries,
                            String cities, int connectingF, boolean refundable) {
        this(departureDate, arrivalDate, budget, countries, cities);

        maxConFlight = connectingF;
        refundable = refundable;
    }


    /**
     * @param s - String holding all the desired set of locations
     * @return Set of strings with each location parsed
     */

    private Set<String> parseLocations(String s) {
        Set<String> locations = new HashSet<>();
        String[] set = s.split(",");
        for (String x : set) {
            locations.add(x);
        }
        return locations;
    }

    /**
     * @param date - the string represent of the desired date in the format MM-dd-yyyy
     * @return integer array with the integers where [0] = month, [1] = day of month, [2] = year
     */
    private int[] parseDate(String date) {
        int[] dateArr = new int[3];
        String[] dateStrings = date.trim().split("-");
        for (int i = 0; i < 3; i++) {
            dateArr[i] = Integer.parseInt(dateStrings[i]);
        }
        return dateArr;
    }

    /**
     * @param dateArr - integer array holding the values of the departure date
     *                Description: Sets the correct global variables for the departure date
     */
    private void setDepartureDate(int[] dateArr) {
        departureMonth = dateArr[0];
        departureDay = dateArr[1];
        departureYear = dateArr[2];
    }

    /**
     * @param date - String representation of the new departure date
     *             Description - allows future changes to the departure date
     */
    protected void changeDepartureDate(String date) {
        int[] departureArr = parseDate(date);
        setDepartureDate(departureArr);
    }

    /**
     * @param dateArr - integer array holding the values of the arrival date
     *                Description: Sets the correct global variables for the arrival date
     */
    private void setArrivalDate(int[] dateArr) {
        arrivalMonth = dateArr[0];
        arrivalDay = dateArr[1];
        arrivalYear = dateArr[2];
    }

    /**
     * @param date - String representation of the new arrival date
     *             Description - allows future changes to the arrival date
     */
    protected void changeArrivalDate(String date) {
        int[] departureArr = parseDate(date);
        setDepartureDate(departureArr);
    }

    /**
     * @param r - the boolean flag for refundable tickets
     *          Description - allows future changes to the refundable tag
     */
    protected void changeRefundable(boolean r) {
        this.refundable = r;
    }

    /**
     * @param n - the boolean flag for maximum desired connecting flights
     *          Description - allows future changes to the desired connecting flights
     */
    protected void changeCFlight(int n) {
        maxConFlight = n;
    }

    /**
     * @param b - the boolean flag for maximum budget
     *          Description - allows future changes to the maximum budget
     */
    protected void changeBudget(double b) {
        maxBudget = b;
    }

    /*
    All the get methods
     */
    protected String getDepartureDate() {
        return departureMonth + "-" + departureDay + "-" + departureYear;
    }

    protected String getArrivalDate() {
        return arrivalMonth + "-" + arrivalDay + "-" + arrivalYear;
    }

    protected int getDepartureMonth() {
        return departureMonth;
    }

    protected int getDepartureDay() {
        return departureDay;
    }

    protected int getDepartureYear() {
        return departureYear;
    }

    protected int getArrivalMonth() {
        return arrivalMonth;
    }

    protected int getArrivalDay() {
        return arrivalDay;
    }

    protected int getArrivalYear() {
        return arrivalYear;
    }


    protected boolean isRefundable() {
        return refundable;
    }

    protected Set<String> getCountries() {
        Set<String> c = new HashSet<>();
        c.addAll(countries);
        return c;
    }

    protected Set<String> getCities() {
        Set<String> c = new HashSet<>();
        c.addAll(cities);
        return c;
    }

    protected double getBudget() {
        return maxBudget;
    }

    protected int getMaxConFlight() {
        return maxConFlight;
    }
}

//radio button
//class names -> preferred class
//connecting flights
//earliest departure time, latest departure time
//earliest arrival time, latest arrival time
//optional alliance -> spinner