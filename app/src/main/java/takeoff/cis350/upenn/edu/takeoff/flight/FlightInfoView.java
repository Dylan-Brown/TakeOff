package takeoff.cis350.upenn.edu.takeoff.flight;

import android.content.Context;
import android.graphics.Canvas;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.util.Log;
import android.view.View;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.ui.WelcomeActivity;

/**
 * This class represents the View when user's click on an individual Flight in the Dashboard
 */
public class FlightInfoView extends View {

    private Flight flight;
    private int buttonRadius = 0;
    private int canvasWidth = 0;
    private int canvasHeight = 0;
    private int x, y;
    private boolean isFavorite = false;
    private boolean ticket = false;

    /**
     * The constructor for the class. Calls View's constructor and sets the FireBase reference
     */
    public FlightInfoView(Context c) {
        super(c);
    }

    /**
     * The constructor for the class. Calls View's constructor and sets the FireBase reference.
     * Also uses the FlightInfo string to get relevant Flight information to display
     */
    public FlightInfoView(Context c, String FlightInfo) {
        super(c);

        // save the Flight information
        this.flight = Flight.fromHumanReadable(FlightInfo);

        // if the user is logged in and determine if this flight is among their favorites
        final Firebase usersRef = WelcomeActivity.USER_FIREBASE;
        if (usersRef.getAuth() != null) {
            final String uid = usersRef.getAuth().getUid();

            usersRef.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Map<String, Object> userData = (Map<String, Object>) snapshot.getValue();
                    String fav = getResources().getString(R.string.firebase_fav);

                    if (!userData.containsKey(fav)) {
                        // the user does not have a list of favorite flights
                        ArrayList<Object> favFlights = new ArrayList<>();
                        userData.put(fav, favFlights);
                        usersRef.child(uid).updateChildren(userData);

                    } else {
                        // the user has favorites; check each favorited flight
                        ArrayList<Object> favFlights = (ArrayList<Object>)userData.get(fav);
                        for (Object o : favFlights) {
                            Flight f = Flight.parseFlight((String) o);
                            if (f.id.equals(flight.id)) {
                                isFavorite = true;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    // the request was cancelled by the user; do nthing
                }
            });
        }
    }

    /**
     * Draw the View on the canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // get the canvas information
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();
        buttonRadius = canvasHeight / 30;
        x = canvasWidth / 2;
        y = canvasHeight - canvasHeight / 10;
        int radius = canvasHeight / 30;

        Paint paint = new Paint();
        canvas.drawPaint(paint);
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, canvasWidth, canvasHeight, paint);
        paint.setColor(Color.BLUE);

        // write the flight info
        paint.setTextSize(canvasWidth / 15);
        String[] split = flight.humanReadable().split("\n");
        for (int i = 0; i < split.length;i++) {
            int textWidth = canvasWidth / 10;
            int textHeight = canvasHeight / 10 + i * (canvasHeight / 10 );
            canvas.drawText(split[i], textWidth, textHeight, paint);
        }

        // set the color of the button depending on if it is a favorite or not
        String color = (!isFavorite) ? "GREY" : "YELLOW";
        paint.setColor(Color.parseColor(color));

        // draw the favorites button
        canvas.drawCircle(x, y, radius, paint);
    }


    /**
     * Handle the event in which the favorites button was pushed
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            // Check if clicked within the right coordinates
            int x = (int)event.getRawX();
            int y = (int)event.getRawY();
            
            // favorite button bounds 
            double favXBound = Math.abs(this.x - x);
            double favYBound = Math.abs((this.y + canvasHeight / 28) - y);
            
            // buy button bounds 
            double buyXLow = canvasWidth / 8 + this.x;
            double buyXHi= canvasWidth / 4 + canvasWidth / 8 + this.x;
            double buyYLow = this.y - buttonRadius;
            double buyYHi = this.y + buttonRadius;

            // back button bounds 
            double backXLow = canvasWidth / 8;
            double backXHi = canvasWidth / 4 + canvasWidth / 8;
            double backYLow = this.y - buttonRadius;
            double backYHi = this.y + buttonRadius;

            // determine which button has been clicked, if any
            if (favXBound < buttonRadius && favYBound < buttonRadius) {
                // favorite button clicked; update the favorites list and change the favorite status
                if ( WelcomeActivity.USER_FIREBASE.getAuth() != null) {
                    final String uid =  WelcomeActivity.USER_FIREBASE.getAuth().getUid();
                    if (isFavorite) {
                        removeFavorite(uid);
                    } else {
                        addFavorite(uid);
                    }
                    isFavorite = !isFavorite;

                } else {
                    // no user logged in
                    String error = getResources().getString(R.string.please_sign_in);
                    (Toast.makeText(getContext(), error, Toast.LENGTH_SHORT)).show();
                }

            } else if  (buyXLow < x && x < buyXHi && buyYLow < y && y < buyYHi) {
                // buy button clicked
                buyTicket();

            } else if (backXLow < x && x < backXHi && y > backYLow && y < backYHi) {
                // the "Go Back" button has been clicked
                goBack();

            } else {
                // do nothing; no button has been pressed
                ticket = false;
            }
        }

        invalidate();
        return true;
    }

    /**
     * Go back to the dashboard and
     */
    public void goBack() {
        // TODO: Handle the GO BACK action
        ticket = true;
    }

    /**
     * Starts the motion to go purchase the ticket
     */
    public void buyTicket() {
        // TODO: Implement
    }

    /**
     * Make a call to Firebase to remove the favorite from the user's list of favorites
     * @param uid the unique identifier for the user's Firebase information
     */
    public void removeFavorite(final String uid) {

        final Firebase ref = WelcomeActivity.USER_FIREBASE.child(uid);
        final String favKey = getResources().getString(R.string.firebase_fav);

        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Map<String, Object> userData = (Map<String, Object>) snapshot.getValue();

                        if (userData != null && !userData.containsKey(favKey)) {
                            // the user does not have a list of favorite flights; set an empty list
                            ArrayList<Object> favFlights = new ArrayList<>();
                            userData.put(favKey, favFlights);
                            ref.updateChildren(userData);

                        } else {
                            // the user has a list of favorite flights; remove the flight
                            ArrayList<Object> fav = (ArrayList<Object>) userData.get(favKey);
                            fav.remove(flight.toString());
                            userData.put(favKey, fav);
                            ref.updateChildren(userData);
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        // the user cancelled the request; do nothing
                    }
                });

    }

    /**
     * Make a call to Firebase to add the flight to the user's list of favorites
     * @param uid the unique identifier for the user's Firebase information
     */
    public void addFavorite(final String uid) {

        final Firebase ref = WelcomeActivity.USER_FIREBASE.child(uid);
        final String favKey = getResources().getString(R.string.firebase_fav);

        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Map<String, Object> userData = (Map<String, Object>) snapshot.getValue();

                        if (userData != null && !userData.containsKey(favKey)) {
                            // the user does not have a list of favorite flights
                            ArrayList<Object> favFlights = new ArrayList<>();
                            favFlights.add(flight.toString());
                            userData.put(favKey, favFlights);
                            ref.updateChildren(userData);

                        } else {
                            // the user does have favorites; add this flight to the list
                            List<Object> favFlights = (List<Object>) userData.get(favKey);
                            favFlights.add(flight.toString());
                            userData.put(favKey, favFlights);
                            ref.updateChildren(userData);
                        }
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        // the user cancelled the request; do nothing
                    }
                });
    }

}
