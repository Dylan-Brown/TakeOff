package takeoff.cis350.upenn.edu.takeoff.flight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import takeoff.cis350.upenn.edu.takeoff.R;

/**
 * Created by tangson on 2/25/16.
 */
public class FlightResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flightresult_page);

        Intent intent = getIntent();
        String departureDate = intent.getStringExtra("departureDate");
        String arrivalDate = intent.getStringExtra("arrivalDate");
        String departureCityCode = intent.getStringExtra("departureCityCode");
        String arrivalCityCode = intent.getStringExtra("arrivalCityCode");
        String airline = intent.getStringExtra("airline");
        String cost = intent.getStringExtra("cost");

        TextView arrivaltextView = (TextView) findViewById(R.id.arrivalCityCode);
        arrivaltextView.setText(arrivalCityCode);

        TextView departuretextView = (TextView) findViewById(R.id.rdepartureCityCode);
        departuretextView.setText(departureCityCode);

    }
}
