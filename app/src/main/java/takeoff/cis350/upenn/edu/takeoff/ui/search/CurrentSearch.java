package takeoff.cis350.upenn.edu.takeoff.ui.search;

/**
 * Created by anakagold on 4/29/16.
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

    public void storeCurrentSearch(SearchParameterProcessor c) {
        this.sq = c;
    }

    public SearchParameterProcessor getCurrentSearch() {
        return this.sq;
    }
}