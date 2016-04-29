package takeoff.cis350.upenn.edu.takeoff.ui.results;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.ui.search.Dashboard;
import takeoff.cis350.upenn.edu.takeoff.ui.search.SearchQuery;

/**
 * Class to handle the "Advanced Filter" feature of the dashboard
 */
public class FilterSearch extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    SearchQuery sq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Bundle extras = getIntent().getExtras();
        // TODO: Finish implementing this method
    }

    /**
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        // TODO: Implement
    }

    /**
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // TODO: Implement
    }

    /**
     *
     * @param adapterView
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // TODO: Implement
    }

    /**
     *
     * @param view
     */
    public void transitionToSearch (View view) {
        // go to Search
        // TODO: Update this to go to search
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }
}
