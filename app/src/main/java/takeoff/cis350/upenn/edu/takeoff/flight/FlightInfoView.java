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

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * This class represents the View when user's click on an individual flight in the dashboard.
 */
public class FlightInfoView extends View {

    private Flight flight;
    private int canvasWidth = 0;
    private int canvasHeight = 0;
    private int x, y;
    private boolean isFavorite = false;
    private boolean ticket = false;
    private final Firebase usersRef;

    /**
     * The constructor for the class. Calls View's constructor and sets the FireBase reference
     */
    public FlightInfoView(Context c) {
        super(c);
        Firebase.setAndroidContext(c);
        usersRef = new Firebase("https://brilliant-inferno-6470.firebaseio.com/users");
    }

    /**
     * The constructor for the class. Calls View's constructor and sets the FireBase reference.
     * Also uses the FlightInfo string to get relevant Flight information to display
     */
    public FlightInfoView(Context c, String FlightInfo) {
        super(c);

        // save the Flight information
        this.flight = Flight.fromHumanReadable(FlightInfo);
        Log.i("FlightInfoView", "Displaying new flight: " + flight.humanReadable());

        // set up FireBase
        Firebase.setAndroidContext(c);
        usersRef = new Firebase("https://brilliant-inferno-6470.firebaseio.com/users");

        // if the user is logged in, determine if this flight is among their favorites
        if (usersRef.getAuth() != null) {
            final String uid = usersRef.getAuth().getUid();
            usersRef.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    // check if the user even has a favorite flights list
                    Map<String, Object> userData = (Map<String, Object>) snapshot.getValue();
                    if (!userData.containsKey("favoriteFlights")) {
                        // the user does not have a list of favorite flights
                        ArrayList<Object> favFlights = new ArrayList<>();
                        userData.put("favoriteFlights", favFlights);
                        usersRef.child(uid).updateChildren(userData);

                    } else {
                        // the user has a list of favorite flights
                        // TODO: Test this section further to be sure it works
                        ArrayList<Object> favFlights =
                                (ArrayList<Object>)userData.get("favoriteFlights");
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
        x = canvasWidth / 2;
        y = canvasHeight - canvasHeight / 10;
        int radius = canvasHeight / 30;

        // write the flight info
        Paint paint = new Paint();
        canvas.drawPaint(paint);
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, canvasWidth, canvasHeight, paint);
        paint.setColor(Color.BLUE);
        paint.setTextSize(canvasWidth / 15);
        String[] split = flight.humanReadable().split("\n");
        for (int i = 0; i < split.length;i++) {
            canvas.drawText(split[i], canvasWidth / 10,
                    canvasHeight / 10 + i * (canvasHeight / 10 ), paint);
        }

        // use Color.parseColor to define HTML colors
        if (!isFavorite) {
            paint.setColor(Color.parseColor("GREY"));
        } else {
            paint.setColor(Color.parseColor("YELLOW"));
        }

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
            int xPos = (int)event.getRawX();
            int yPos = (int)event.getRawY();
            int radius = canvasHeight / 30;

            // check if a button is being clicked
            if (Math.abs(x - xPos) < (canvasHeight / 30) && Math.abs((y + canvasHeight / 28) -yPos)
                    < (canvasHeight / 30)) {
                // the favorites button is being clicked

                // make sure a user is logged in before taking action
                if (usersRef.getAuth() != null) {
                    final String uid = usersRef.getAuth().getUid();

                    if (isFavorite) {
                        // Remove the flight from the list of favs in FireBase
                        usersRef.child(uid).addListenerForSingleValueEvent(
                                new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                Map<String, Object> userData =
                                        (Map<String, Object>) snapshot.getValue();

                                if (!userData.containsKey("favoriteFlights")) {
                                    // the user does not have a list of favorite flights
                                    ArrayList<Object> favFlights = new ArrayList<>();
                                    userData.put("favoriteFlights", favFlights);
                                    // add a favorites list to this user's information
                                    usersRef.child(uid).updateChildren(userData);

                                } else {
                                    // the user has a list of favorite flights
                                    ArrayList<Object> fav = (ArrayList<Object>)
                                            ((Map<String, Object>)
                                            snapshot.getValue()).get("favoriteFlights");
                                    // remove the flight from the list
                                    fav.remove(flight.toString());
                                    userData.put("favoriteFlights", fav);
                                    // update the internal FireBase data structure
                                    usersRef.child(uid).updateChildren(userData);
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                // the user cancelled the request; do nothing
                            }
                        });

                    } else {
                        // Add the to the user's list of favorites

                        usersRef.child(uid).addListenerForSingleValueEvent(
                                new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                Map<String, Object> userData = (Map<String, Object>)
                                        snapshot.getValue();
                                if (!userData.containsKey("favoriteFlights")) {
                                    // the user does not have a list of favorite flights
                                    ArrayList<Object> favFlights = new ArrayList<>();
                                    favFlights.add(flight.toString());
                                    userData.put("favoriteFlights", favFlights);
                                    usersRef.child(uid).updateChildren(userData);

                                } else {
                                    // the user does have a list of favorite flights
                                    List<Object> favFlights = (List<Object>) ((Map<String, Object>)
                                            snapshot.getValue()).get("favoriteFlights");
                                    // add the flight to the list
                                    favFlights.add(flight.toString());
                                    userData.put("favoriteFlights", favFlights);
                                    usersRef.child(uid).updateChildren(userData);
                                }
                            }
                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                // the user cancelled the request; do nothing
                            }
                        });

                    }

                    // change the favorite status
                    isFavorite = !isFavorite;

                } else {
                    // no user logged in, do not take any action
                    // TODO: Add a Toast object to pop up, informing the user to make an account
                }

            } else if  (xPos > x + canvasWidth / 8 && xPos < x + canvasWidth / 4 + canvasWidth / 8
                    && yPos  > y - radius && yPos < y + radius) {
                // the "Buy Ticket" button has been clicked

                // TODO: Handle the BUY TICKET action

            } else if (xPos > canvasWidth / 8 && xPos < canvasWidth / 4 + canvasWidth / 8
                    && yPos  > y - radius && yPos < y + radius) {
                // the "Go Back" button has been clicked

                // TODO: send the favorites information back to our Dashboard page
                // TODO: Handle the GO BACK action
                ticket = true;

            } else {
                // do nothing; no button is being pressed
                ticket = false;
            }
        }

        // redraw the canvas and return
        invalidate();
        return true;
    }

}
