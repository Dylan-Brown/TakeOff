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


public class FlightInfoView extends View {
    int CanvasWidth = 0;
    int CanvasHeight = 0;
    int x, y;
    boolean isFavorite = false;
    boolean ticket = false;
    Flight flight;
    final Firebase usersRef;


    public FlightInfoView(Context c) {
        super(c);
        usersRef = new Firebase("https://brilliant-inferno-6470.firebaseio.com/users");
    }

    // ADDRESS: FavFlights and usage
    public FlightInfoView(Context c, final String FlightInfo) {
        super(c);
        usersRef = new Firebase("https://brilliant-inferno-6470.firebaseio.com/users");

        // save the Flight information
        this.flight = Flight.parseFlight(FlightInfo);
        Log.e("FlightInfoView", "Displaying new flight: " + flight.toString());

        // Set up FireBase
        Firebase.setAndroidContext(c);

        // If there is a user, make sure they have a list of favorited flights, and check if this
        // flight in particular is among them
        if (usersRef.getAuth() != null) {
            final String uid = usersRef.getAuth().getUid();
            usersRef.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Map<String, Object> userData = (Map<String, Object>) snapshot.getValue();

                    if (!userData.containsKey("favoriteFlights")) {

                        ArrayList<Object> favFlights = new ArrayList<>();
                        userData.put("favoriteFlights", favFlights);
                        usersRef.child(uid).updateChildren(userData);

                    } else {

                        // TODO: Make sure this part works

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
                    Log.e("FlightInfoView",  "getAuth onCancelled for error: "
                            + firebaseError.getMessage());
                }
            });
        }

    }


    @Override
    protected void onDraw(Canvas canvas) {System.out.println("Inside OnDraw");

        // Draw the button
        CanvasWidth = canvas.getWidth();
        CanvasHeight = canvas.getHeight();
        super.onDraw(canvas);
        x = CanvasWidth / 2;
        y = CanvasHeight - CanvasHeight / 10;
        int radius;
        radius = CanvasHeight / 30;

        // write the flight info
        Paint paint = new Paint();
        canvas.drawPaint(paint);
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, CanvasWidth, CanvasHeight, paint);
        paint.setColor(Color.BLUE);
        paint.setTextSize(CanvasWidth / 15);
        String[] split = flight.humanReadable().split("\n");
        for (int i = 0; i < split.length;i++) {
            canvas.drawText(split[i], CanvasWidth / 10,
                    CanvasHeight / 10 + i * (CanvasHeight / 10 ), paint);
        }

        // Use Color.parseColor to define HTML colors
        if (!isFavorite) {
            paint.setColor(Color.parseColor("GREY"));
        } else {
            paint.setColor(Color.parseColor("YELLOW"));
        }
        canvas.drawCircle(x, y, radius, paint);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();

        // See if they are clicking the favorites button
        if (action == MotionEvent.ACTION_DOWN) {
            // Check if clicked within the right coordinates
            int xPos = (int)event.getRawX();
            int yPos = (int)event.getRawY();
            int radius = CanvasHeight / 30;

            // Check if a button is being clicked
            if (Math.abs(x - xPos) < (CanvasHeight / 30) && Math.abs((y + CanvasHeight / 28) -yPos)
                    < (CanvasHeight / 30)) {
                // The favorites button is being clicked

                // Make sure a user is logged in before taking action
                if (usersRef.getAuth() != null) {
                    final String uid = usersRef.getAuth().getUid();

                    if (isFavorite) {
                        // Remove the flight from the list of favs in FireBase
                        usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                Map<String, Object> userData = (Map<String, Object>) snapshot.getValue();
                                //  Make sure user has a list of favorite flights
                                if (!userData.containsKey("favoriteFlights")) {
                                    ArrayList<Object> favFlights = new ArrayList<>();
                                    userData.put("favoriteFlights", favFlights);
                                    // Add a favorites list to this user's information
                                    usersRef.child(uid).updateChildren(userData);
                                } else {
                                    ArrayList<Object> fav = (ArrayList<Object>) ((Map<String, Object>)
                                            snapshot.getValue()).get("favoriteFlights");
                                    // Remove the flight from the list
                                    Log.e("FlightInfoView", "onTouchEvent: removing favorite");
                                    fav.remove(flight.toString());
                                    userData.put("favoriteFlights", fav);
                                    // Update the internal FireBase datastructure
                                    usersRef.child(uid).updateChildren(userData);
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                // do nothing
                            }
                        });

                    } else {
                        // Add the to the user's list of favorites
                        usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                Map<String, Object> userData = (Map<String, Object>) snapshot.getValue();
                                if (!userData.containsKey("favoriteFlights")) {
                                    Log.e("FlightInfoView", "onTouchEvent: adding new favorite");
                                    ArrayList<Object> favFlights = new ArrayList<>();
                                    favFlights.add(flight.toString());
                                    userData.put("favoriteFlights", favFlights);
                                    usersRef.child(uid).updateChildren(userData);
                                } else {
                                    List<Object> favFlights = (List<Object>) ((Map<String, Object>)
                                            snapshot.getValue()).get("favoriteFlights");
                                    // Add the flight to the list
                                    Log.e("FlightInfoView", "onTouchEvent: adding new favorite");
                                    favFlights.add(flight.toString());
                                    userData.put("favoriteFlights", favFlights);
                                    usersRef.child(uid).updateChildren(userData);
                                }
                            }
                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                // Do nothing
                            }
                        });

                    }

                    // Change the favorite status
                    isFavorite = !isFavorite;

                } else {
                    Log.e("FlightInfoView", "onTouchEvent: No user logged in, taking no action.");
                    // No user logged in, do not take any action
                    // TODO: Add a Toast object to pop up, informing the user to make an account
                    // if they want to use favorites features

                }

            } else if  (xPos > x + CanvasWidth / 8 && xPos < x + CanvasWidth / 4 + CanvasWidth / 8
                    && yPos  > y - radius && yPos < y + radius) {
                Log.e("FlightInfoView", "onTouchEvent: BUY TICKET Button is being clicked.");
                // The BUY TICKET button has been clicked

                // TODO: Handle the BUY TICKET action

            } else if (xPos > CanvasWidth / 8 && xPos < CanvasWidth / 4 + CanvasWidth / 8
                    && yPos  > y - radius && yPos < y + radius) {
                Log.e("FlightInfoView", "onTouchEvent: GO BACK Button is being clicked.");
                // The GO BACK button has been clicked

                //we want to send the favorites information back to our Dashboard page
                ticket = true;
                //Intent intent = new Intent(this, ExternalFlightSearch.class);
                //intent.putExtra(EXTRA_MESSAGE2, FavFlights);
                //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Url.parse("http://www.google.com"));
                //startActivity(browserIntent);
                // TODO: Handle the GO BACK action

            } else {
                ticket = false;
            }
        }

        invalidate();
        return true;
    }

}
