package takeoff.cis350.upenn.edu.takeoff.ui.results;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.flight.Flight;

/**
 * Description:
 * This class represents the Activity when user's click on an individual flight in the dashboard.
 */
public class FlightInfoActivity extends AppCompatActivity {

    private FlightInfoView view;
    private Flight flight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get the String holding this specific flight information from the bundle
        Bundle extras = getIntent().getExtras();
        String extraMesg = getString(R.string.dashboard_flight_mesg);
        String flightText = (String) extras.get(extraMesg);

        //Create Flight and view, then set the view
        flight = Flight.parseFlight(flightText );
        view = new FlightInfoView (this, flightText);
        setContentView(view);
    }

    /**
     * Description: Method called when the menu is created for this view
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_flightinfo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Description: The "Purchase Flight" menu item was clicked; the app will take the user to the external link
     * to find this flight
     */
    public void purchaseTicket(MenuItem item) {
        String flight_details = flight.humanReadable();
        String[] fd = flight_details.split("\n");
        String depart = fd[1].split(":")[1].trim();
        String arrive = fd[3].split(":")[1].trim();
        String date = fd[2].split(":")[1].trim();
        //we want to flip the date;
        String[] departDate = date.split("-");

        String flipped = departDate[2].trim() + "-" + departDate[1].trim() + "-" + departDate[0].trim();
        String url = "https://www.google.com/flights/#search;f="+depart+",ZFV;t="+arrive+";d="+flipped;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    /**
     * Description:  The "Share Flight" menu item  was clicked; the app will launch an installed and registered
     * email account
     */
    public void shareTicket(MenuItem item) {
        //Create an intent telling android it's supposed to go send an email
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_SUBJECT, "TakeOff Flight Details");
        intent.putExtra(Intent.EXTRA_TEXT, flight.humanReadable());
        try {
            startActivity(Intent.createChooser(intent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Description:  The "Go to Airbnb" menu item  was clicked; the app will launch the airbnb site
     */
    public void transitionToAirbnb(MenuItem item) {
        String url = "https://www.airbnb.com/";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}
