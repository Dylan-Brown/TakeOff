package takeoff.cis350.upenn.edu.takeoff.ui.results;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import takeoff.cis350.upenn.edu.takeoff.R;

/**
 * Class for displaying the information of a specific Flight
 */
public class FlightInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flightresult_page);
    }

}
