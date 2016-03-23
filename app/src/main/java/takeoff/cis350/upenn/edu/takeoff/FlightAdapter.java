package takeoff.cis350.upenn.edu.takeoff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.*;

/**
 * Created by tangson on 2/23/16.
 */
public class FlightAdapter extends ArrayAdapter {
    List list = new ArrayList();
    private TextView arrivalDate;

    public FlightAdapter(Context context, int resource) {
        super(context, resource);
    }

    static class DataHandler {
        TextView departureCityCode;
        TextView departureDate;
        TextView arrivalCityCode;
        TextView arrivalDate;
        TextView cost;
    }

    @Override
    public void add(Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

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
            handler.cost = (TextView) row.findViewById(R.id.costTextView);
            row.setTag(handler);
        } else {
            handler = (DataHandler) row.getTag();
        }
        Flight flight;
        flight = (Flight) this.getItem(position);
        handler.departureCityCode.setText(flight.departureCityCode);
        handler.arrivalCityCode.setText(flight.arrivalCityCode);
        handler.arrivalDate.setText(flight.arrivalDate);
        handler.departureDate.setText(flight.departureDate);
        handler.cost.setText(String.valueOf(flight.cost));
        return row;
    }
}
