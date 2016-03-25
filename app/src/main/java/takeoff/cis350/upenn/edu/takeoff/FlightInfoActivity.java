package takeoff.cis350.upenn.edu.takeoff;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FlightInfoActivity extends AppCompatActivity {
    FlightInfoView v;
    public int boardSize = 4;
    Flight Flight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If minimum Flight info passed, pass it, otherwise pass entire Flight String
        Bundle extras = getIntent().getExtras();
        Flight = (Flight) extras.get("FlightActual");
        v = new FlightInfoView (this, Flight.toString());
        setContentView(v);
    }

    // MENU FOR THIS ACTIVITY
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_flightinfo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void PurchaseTicket(MenuItem item) {
        String flight_details = Flight.humanReadable();
        String[] fd = flight_details.split("\n");
        String depart = fd[2].split(":")[1].trim();
        String arrive = fd[4].split(":")[1].trim();
        String f = fd[3].split(":")[1].trim();
        String r = fd[5].split(":")[1].trim();
        String date = fd[3].split(":")[1].trim();
        //we want to flip the date;
        String[] ddate = date.split("/");

        f = ddate[2].trim() + "-" + ddate[1].trim() + "-" + ddate[0].trim();

        String url = "https://www.google.com/flights/#search;f="+depart+",ZFV;t="+arrive+";d=" +f;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    public void ShareTicket(MenuItem item) {
        // get the email adress of the user.
        // if guest, then print toast
        //else
        String email = "";                                                                              //email address of the user. If guest, then return null;

        // setting email for testing purposes

        email = "anakagold@gmail.com";

        if (email != null) {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, email);
            intent.putExtra(Intent.EXTRA_SUBJECT, "TakeOff Flight Details");
            intent.putExtra(Intent.EXTRA_TEXT, Flight.humanReadable());

            try {
                startActivity(Intent.createChooser(intent, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
