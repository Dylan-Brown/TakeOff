package takeoff.cis350.upenn.edu.takeoff.flight;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.*;

import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.flight.Flight;
import takeoff.cis350.upenn.edu.takeoff.flight.SubFlight;
import takeoff.cis350.upenn.edu.takeoff.ui.WelcomeActivity;

/**
 * Created by tangson on 2/23/16.
 *
 * TODO: have a prevent-duplicate favorites in the addFavorites method.
 */
public class FlightAdapter extends ArrayAdapter {


    List list = new ArrayList();
    private TextView arrivalDate;

    /**
     * Default constructor for the class
     */
    public FlightAdapter(Context context, int resource) {
        super(context, resource);
    }

    /**
     *
     */
    static class DataHandler {
        TextView departureCityCode;
        TextView departureDate;
        TextView arrivalCityCode;
        TextView arrivalDate;
        TextView departureTime;
        TextView arrivalTime;
        TextView cost;
        TextView connections;


        TextView retDepartureCityCode;
        TextView retDepartureDate;
        TextView retArrivalCityCode;
        TextView retArrivalDate;
        TextView retDepartureTime;
        TextView retArrivalTime;
        TextView retConnections;

    }

    /**
     *
     * @param object
     */
    @Override
    public void add(Object object) {
        super.add(object);
        list.add(object);
    }

    /**
     *
     * @return
     */
    @Override
    public int getCount() {
        return this.list.size();
    }

    /**
     *
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        DataHandler handler;
        final Flight flight = (Flight) this.getItem(position);



        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (flight.isReturnTrip()) {
                row = inflater.inflate(R.layout.flight_item_round, parent, false);
            } else {
                row = inflater.inflate(R.layout.flight_item_one, parent, false);
            }

            final ImageButton favoriteButton = (ImageButton) row.findViewById(R.id.favoriteButton);
            favoriteButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(WelcomeActivity.USER_FIREBASE.getAuth() !=  null) {
//                  favoriteButton.setBackgroundResource(android.R.drawable.star_off);
                        favoriteButton.setBackgroundResource(android.R.drawable.star_on);
                        addFavorite(WelcomeActivity.FIREBASE.getAuth().getUid(), flight);
                    } else {
                        Context c = getContext();
                        String message = c.getString(R.string.please_sign_in);
                        Toast toast = Toast.makeText(c,
                                message, Toast.LENGTH_LONG);
                        toast.show();
                    }

                }
            });

            handler = new DataHandler();
            handler.departureCityCode = (TextView) row.findViewById(R.id.departureTextView);
            handler.arrivalCityCode = (TextView) row.findViewById(R.id.arrivalTextView);

            handler.arrivalDate = (TextView) row.findViewById(R.id.arrivalDateTextView);
            handler.departureDate = (TextView) row.findViewById(R.id.departureDateTextView);

            handler.arrivalTime = (TextView) row.findViewById(R.id.arrivalTimeTextView);
            handler.departureTime = (TextView) row.findViewById(R.id.departureTimeTextView);

            handler.cost = (TextView) row.findViewById(R.id.costTextView);
            handler.connections = (TextView) row.findViewById(R.id.conxnTextView);

            if (flight.isReturnTrip()) {

                handler.retDepartureCityCode = (TextView) row.findViewById(R.id.retDepartureTextView);
                handler.retArrivalCityCode = (TextView) row.findViewById(R.id.retArrivalTextView);

                handler.retArrivalDate = (TextView) row.findViewById(R.id.retArrivalDateTextView);
                handler.retDepartureDate = (TextView) row.findViewById(R.id.retDepartureDateTextView);

                handler.retArrivalTime = (TextView) row.findViewById(R.id.retArrivalTimeTextView);
                handler.retDepartureTime = (TextView) row.findViewById(R.id.retDepartureTimeTextView);

                handler.retConnections = (TextView) row.findViewById(R.id.retConxnTextView);
            }
            row.setTag(handler);
        } else {
            handler = (DataHandler) row.getTag();
        }

        String connectionCities = getConCities(flight);
        handler.departureCityCode.setText(flight.getDepartureCityCode());
        handler.arrivalCityCode.setText(flight.getArrivalCityCode());

        handler.arrivalDate.setText(flight.getArrivalDate().replace("X", "-"));
        handler.departureDate.setText(flight.getDepartureDate().replace("X", "-"));

        handler.cost.setText(String.valueOf(flight.getCost()));

        handler.departureTime.setText(flight.getDepartureTime().replace("X", "-"));
        handler.arrivalTime.setText(flight.getArrivalTime().replace("X", "-"));

        if (flight.getNumOfConnections() == 1) {
            handler.connections.setText("1 Conxn\n" + connectionCities);
        } else if (flight.getNumOfConnections() == 0) {
            handler.connections.setText("Direct");
        } else if (flight.getNumOfConnections() > 1) {

            handler.connections.setText(flight.getNumOfConnections() + "Conxns\n" + connectionCities);
        } else {
            handler.connections.setText("BOOP\n");
        }

        if (flight.isReturnTrip()) {
            handler.retDepartureCityCode.setText(flight.retDepartureCityCode);
            handler.retArrivalCityCode.setText(flight.retArrivalCityCode);

            handler.retArrivalDate.setText(flight.retArrivalDate);
            handler.retDepartureDate.setText(flight.retDepartureDate);

            handler.retDepartureTime.setText(flight.retDepartureTime);
            handler.retArrivalTime.setText(flight.retArrivalTime);

            if (flight.retNumOfConnections == 1) {
                handler.retConnections.setText("1 Conxn\n" + connectionCities);
            } else if (flight.retNumOfConnections == 0) {
                handler.retConnections.setText("Direct");
            } else if (flight.retNumOfConnections > 1) {

                handler.retConnections.setText(flight.getNumOfConnections() + "Conxns\n" + connectionCities);
            } else {
                handler.retConnections.setText("BOOP\n");
            }

        }

        return row;
    }

    /**
     * @param flight
     * @return
     **/
    private String getConCities(Flight flight) {
        List destinations = new ArrayList<String>();
        String destinationString = "";
        for (SubFlight sf : flight.getSubFlights()) {
            destinations.add(sf.getArrivalCityCode());
        }
        for (int i = 0; i < destinations.size() - 1; i++) {
            destinationString += destinations.get(i) + "\n";
        }

        return destinationString;
    }


    private void addFavorite(final String uid, final Flight flight) {

        final Firebase ref = WelcomeActivity.USER_FIREBASE.child(uid);
        final String favKey = "favoriteFlights";

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