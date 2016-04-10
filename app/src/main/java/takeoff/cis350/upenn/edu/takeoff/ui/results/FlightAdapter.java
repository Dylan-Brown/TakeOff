package takeoff.cis350.upenn.edu.takeoff.ui.results;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.*;

import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.flight.Flight;
import takeoff.cis350.upenn.edu.takeoff.flight.SubFlight;

/**
 * Created by tangson on 2/23/16.
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
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.flight_item, parent, false);
            handler = new DataHandler();
            handler.departureCityCode = (TextView) row.findViewById(R.id.departureTextView);
            handler.arrivalCityCode = (TextView) row.findViewById(R.id.arrivalTextView);

            handler.arrivalDate = (TextView) row.findViewById(R.id.arrivalDateTextView);
            handler.departureDate = (TextView) row.findViewById(R.id.departureDateTextView);

            handler.arrivalTime = (TextView) row.findViewById(R.id.arrivalTimeTextView);
            handler.departureTime = (TextView) row.findViewById(R.id.departureTimeTextView);

            handler.cost = (TextView) row.findViewById(R.id.costTextView);
            handler.connections = (TextView) row.findViewById(R.id.conxnTextView);
            row.setTag(handler);
        } else {
            handler = (DataHandler) row.getTag();
        }
        Flight flight;
        flight = (Flight) this.getItem(position);
        String connectionCities = getConCities(flight);
        handler.departureCityCode.setText(flight.getDepartureCityCode());
        handler.arrivalCityCode.setText(flight.getArrivalCityCode());

        handler.arrivalDate.setText(flight.getArrivalDate());
        handler.departureDate.setText(flight.getDepartureDate());

        handler.cost.setText(String.valueOf(flight.getCost()));

        handler.departureTime.setText(flight.getDepartureTime());
        handler.arrivalTime.setText(flight.getArrivalTime());

        if (flight.getNumOfConnections() == 1) {
            handler.connections.setText("1 Conxn\n" +connectionCities);
        } else if (flight.getNumOfConnections() == 0) {
            handler.connections.setText("Direct");
        } else if (flight.getNumOfConnections() > 1) {

            handler.connections.setText(flight.getNumOfConnections() + "Conxns\n" + connectionCities);
        }
        else {      handler.connections.setText("BOOP\n");
        }
        return row;
    }

    /**
     *
     * @param flight
     * @return
     */
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
}