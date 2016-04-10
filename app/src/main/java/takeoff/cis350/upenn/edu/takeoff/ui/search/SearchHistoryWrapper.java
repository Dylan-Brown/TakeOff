package takeoff.cis350.upenn.edu.takeoff.ui.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import takeoff.cis350.upenn.edu.takeoff.R;

/**
 * Class to wrap the SearchHistory activity
 */
public class SearchHistoryWrapper extends AppCompatActivity {

    /**
     *
     */
    public SearchHistoryWrapper() {

    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history_wrapper);

        FrameLayout frame = (FrameLayout) findViewById(R.id.hist_wrapper_frame);
        Fragment historyFragment = new DashBoardSearchHistory();

    }
}
