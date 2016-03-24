package takeoff.cis350.upenn.edu.takeoff;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import java.lang.Math;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.view.View;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.os.Bundle;
import android.view.View;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class FlightInfoView extends View {
    int CanvasWidth = 0;
    int CanvasHeight = 0;
    int x, y;
    boolean isFavorite = false;
    String FlightInfo = "Anaka";
    ArrayList<String> FavFlights;
    boolean ticket = false;
    public final static String EXTRA_MESSAGE2 = "FavFlight";
    final Firebase usersRef = new Firebase("https://brilliant-inferno-6470.firebaseio.com/users");


    public FlightInfoView(Context c) {
        super(c);
    }

    public FlightInfoView(Context c, String FlightInfo, final ArrayList<String> FavFlights) {
        super(c);
        this.FlightInfo = FlightInfo;
        this.FavFlights = FavFlights;

        // TODO: Get the list of favortite flights from firebase, determine if the flight is among the favorites

        final String uid = usersRef.getAuth().getUid();
        usersRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Map<String, Object> userData = (Map<String, Object>) snapshot.getValue();
                if (!userData.containsKey("favoriteFlights")) {
                    HashMap<String, Object> favFlights = new HashMap<>();
                    userData.put("favoriteFlights", favFlights);
                    usersRef.child(uid).updateChildren(userData);
                } else {
                    Map<String, Object> userFavs = (Map<String, Object>)
                            ((Map<String, Object>) snapshot.getValue()).get("favoriteFlights");
                    for (Object o : userFavs.values()) {
                        Flight f = (Flight) o;
                        if (FavFlights.contains(f)) {
                            isFavorite = true;
                            break;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // do nothing
            }
        });


        //Decode the flight info provided.
        //And then print it on the screen.
        //Draw the butt
    }

    @Override
    protected void onDraw(Canvas canvas) {
        System.out.println("Inside OnDraw");

        // Draw the picture first


        // Draw the button first
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
        paint.setColor(Color.RED);
        paint.setTextSize(CanvasWidth / 15);
        String[] split = FlightInfo.split("\n");
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

        //Drawing the BUY TICKET button outline
        paint.setColor(Color.parseColor("GREY"));
        canvas.drawRect(x + 1 * CanvasWidth / 8, y - radius,
                x + 1 * CanvasWidth / 4 + 1 * CanvasWidth / 8, y + radius, paint);

        //Writing BUY TICKET
        paint.setColor(Color.parseColor("RED"));
        paint.setTextSize(CanvasWidth / 22);

        canvas.drawText("BUY TICKET", x + 1 * CanvasWidth / 8 + 5, y + radius - 50, paint);

        //Drawing the GO BACK button outline
        paint.setColor(Color.parseColor("GREY"));
        if (ticket) {
            paint.setColor(Color.parseColor("RED"));
        }
        canvas.drawRect(1 * CanvasWidth / 8, y - radius,
                1 * CanvasWidth / 4 + 1 * CanvasWidth / 8, y + radius, paint);

        //Writing GO BACK
        paint.setColor(Color.parseColor("RED"));
        paint.setTextSize(CanvasWidth / 22);

        canvas.drawText("GO BACK", 1 * CanvasWidth / 8 + 8, y + radius - 50, paint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("Enter onTouch");
        int action = event.getActionMasked();

        // See if they are clicking the favorites button
        if (action == MotionEvent.ACTION_DOWN) {
            /*if this location is within the scope of our favorites button,
             * we act accordingly*/
            int xPos = (int)event.getRawX();
            int yPos = (int)event.getRawY();
            int radius = CanvasHeight / 30;

            //if the favorites button is clicked, then this happens
            if (Math.abs(x - xPos) < (CanvasHeight / 30) &&
                    Math.abs((y + CanvasHeight / 28) -yPos) < (CanvasHeight / 30)) {
                System.out.print("Enter Here");
                // add this flight to favorites or delete it from favorites
                //delete from favorites
                if (isFavorite) {                                                                           // if the current  flight is in FireBase
                    FavFlights.remove(FlightInfo);                                                          //  Remove the current flight from FireBase

                    // remove the flight  from the list of favs in FireBase
                    final String uid = usersRef.getAuth().getUid();
                    usersRef.child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            Map<String, Object> userData = (Map<String, Object>) snapshot.getValue();
                            if (!userData.containsKey("favoriteFlights")) {
                                HashMap<String, Object> favFlights = new HashMap<>();
                                userData.put("favoriteFlights", favFlights);
                                usersRef.child(uid).updateChildren(userData);
                            } else {
                                Map<String, Object> userFavs = (Map<String, Object>)
                                        ((Map<String, Object>) snapshot.getValue()).get("favoriteFlights");
                                for (String s : userFavs.keySet()) {
                                    if (((Flight)userFavs.get(s)).equals(FlightInfo)) {
                                        userFavs.remove(s);
                                        usersRef.child(uid).child("favoriteFlights").setValue(userFavs);
                                        break;
                                    }
                                }
                            }
                        }
                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            // do nothing
                        }
                    });

                }
                //add to favorites
                else {
                    FavFlights.add(FlightInfo);                                                             // Add the current flight to FireBase
                    System.out.println("Size of the favlist in View" + FavFlights.size());

                    // remove the flight  from the list of favs in FireBase
                    final String uid = usersRef.getAuth().getUid();
                    usersRef.child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            Map<String, Object> userData = (Map<String, Object>) snapshot.getValue();
                            if (!userData.containsKey("favoriteFlights")) {
                                HashMap<String, Object> favFlights = new HashMap<>();
                                // TODO: Put unique id for flight or  something
                                favFlights.put("", FlightInfo);
                                userData.put("favoriteFlights", favFlights);
                                usersRef.child(uid).updateChildren(userData);
                            } else {
                                Map<String, Object> userFavs = (Map<String, Object>)
                                        ((Map<String, Object>) snapshot.getValue()).get("favoriteFlights");
                                // TODO: Put unique id for flight or  something
                                userFavs.put("", FlightInfo);
                                usersRef.child(uid).child("favoriteFlights").setValue(userFavs);
                            }
                        }
                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            // do nothing
                        }
                    });

                }
                isFavorite = !isFavorite;
            }


            //if the BUY TICKET button is clicked then this happens
            else if  (xPos > x + 1 * CanvasWidth / 8 && xPos < x + 1 * CanvasWidth / 4 + 1 * CanvasWidth / 8
                    && yPos  > y - radius && yPos < y + radius) {
            }

            //if the GO BACK button is clicked then this happens
            else if  (xPos > 1 * CanvasWidth / 8 && xPos < 1 * CanvasWidth / 4 + 1 * CanvasWidth / 8
                    && yPos  > y - radius && yPos < y + radius) {
                //we want to send the favorites information back to our Dashboard page
                ticket = true;
                //Intent intent = new Intent(this, ExternalFlightSearch.class);
                //intent.putExtra(EXTRA_MESSAGE2, FavFlights);
                //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Url.parse("http://www.google.com"));
                //startActivity(browserIntent);
            }
            else ticket = false;
        }
        invalidate();
        return true;
    }

}
