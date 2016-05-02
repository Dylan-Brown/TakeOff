package takeoff.cis350.upenn.edu.takeoff.ui.search;

/**
 * Created by anakagold on 4/29/16.
 */

/**
 * TODO: WRITE CLASS DESCRIPTION, WHAT THIS CLASS IS FOR
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
     * TODO: WRITE METHOD HEADER, DESCRIPTION + WHEN ITS USED RIGHT NOW
     * @param c
     */
    public void storeCurrentSearch(SearchParameterProcessor c) {
        this.sq = c;
    }

    /**
     * TODO: WRITE METHOD HEADER, DESCRIPTION + WHEN ITS USED RIGHT NOW
     * @return
     */
    public SearchParameterProcessor getCurrentSearch() {
        return this.sq;
    }
}