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
 * This class represents the Activity when user's click on an individual flight in the dashboard.
 */
public class FlightInfoActivity extends AppCompatActivity {

    private FlightInfoView view;
    private Flight flight;

    /**
     * Method is called when the user clicks on an individual flight in the dashboard
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the Flight information from the bundle
        //System.out.println("------------------ONCREATE OF FLIGHTINFORACTIVITY----------");
        Bundle extras = getIntent().getExtras();
        String extraMesg = getString(R.string.dashboard_flight_mesg);
        //System.out.println("------------------GETTING EXTRA OF THE MSG: " + extraMesg + "----------");
        String flightText = (String) extras.get(extraMesg);
        //System.out.println(flightText );
        flight = Flight.parseFlight(flightText );
        /*System.out.println("------------------PARSED FLIGHT----------");
        System.out.println("------------------SANITY CHECK FOR TOSTRING----------");
        System.out.println(flight.toString());
        System.out.println(flightText );
        System.out.println(flight.toString().equals(flightText));
        System.out.println("------------------SANITY CHECK FOR TOSTRING DONE----------");*/
        view = new FlightInfoView (this, flightText);
        setContentView(view);
    }

    /**
     * Method called when the menu is created for this view
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_flightinfo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * The "Purchase Flight" menu item was clicked; the app will take the user to the external link
     * to find this flight
     */
    public void PurchaseTicket(MenuItem item) {
        String flight_details = flight.humanReadable();
        String[] fd = flight_details.split("\n");
        String depart = fd[1].split(":")[1].trim();
        String arrive = fd[3].split(":")[1].trim();
        String date = fd[2].split(":")[1].trim();
        //we want to flip the date;
        String[] ddate = date.split("/");

        String f = ddate[2].trim() + "-" + ddate[1].trim() + "-" + ddate[0].trim();
        System.out.println(depart + " " + arrive + " " + f);
        String url = "https://www.google.com/flights/#search;f="+depart+",ZFV;t="+arrive+";d=" +f;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    /**
     * The "Share Flight" menu item  was clicked; the app will
     */
    public void ShareTicket(MenuItem item) {

        // TODO: Call FireBase to get the user's email
        String email = "anakagold@gmail.com";

        if (email != null) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, email);
            intent.putExtra(Intent.EXTRA_SUBJECT, "TakeOff Flight Details");
            intent.putExtra(Intent.EXTRA_TEXT, flight.humanReadable());
            try {
                startActivity(Intent.createChooser(intent, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "There are no email clients installed.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void airbnb(MenuItem item) {
        String flight_details = flight.humanReadable();
        String[] fd = flight_details.split("\n");
        String depart = fd[1].split(":")[1].trim();
        String arrive = fd[3].split(":")[1].trim();
        String date = fd[2].split(":")[1].trim();
        //we want to flip the date;
      //  String[] ddate = date.split("/");

//        String f = ddate[2].trim() + "-" + ddate[1].trim() + "-" + ddate[0].trim();
       // System.out.println(depart + " " + arrive + " " + f);
        String url = "https://www.airbnb.com/";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent); //but at least we have something tru
    }

}
