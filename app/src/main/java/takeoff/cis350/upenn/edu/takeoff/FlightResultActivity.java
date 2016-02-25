package takeoff.cis350.upenn.edu.takeoff;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by tangson on 2/25/16.
 */
public class FlightResultActivity extends Activity {
<<<<<<< HEAD

    //Hey Jason I created a new activity called FlightInfo which does what this Activity is supposed to do.
=======
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
>>>>>>> d80f6e2b409b94870f169d7e6980743bc3bbfee5
}
