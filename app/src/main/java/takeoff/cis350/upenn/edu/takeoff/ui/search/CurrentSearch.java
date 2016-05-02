package takeoff.cis350.upenn.edu.takeoff.ui.search;

/**
 * Created by anakagold on 4/29/16.
 */

/**
 * The class stores the information of the current search parameters.
 */
public class CurrentSearch {

    private static CurrentSearch cs = new CurrentSearch();
    private SearchParameterProcessor sq;

    private CurrentSearch() {
        sq = new SearchParameterProcessor();
    }

    public static CurrentSearch getInstance() {
        return cs;
    }

    /**
     * When we search for flight, we call this method to store
     * the search parameters
     * @param c
     */
    public void storeCurrentSearch(SearchParameterProcessor c) {
        this.sq = c;
    }

    /**
     * Returns the latest search parameters
     * @return
     */
    public SearchParameterProcessor getCurrentSearch() {
        return this.sq;
    }
}