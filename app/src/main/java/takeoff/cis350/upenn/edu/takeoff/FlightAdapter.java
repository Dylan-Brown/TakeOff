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

    public FlightAdapter(Context context, int resource){
        super(context, resource);
    }
static class DataHandler{
    TextView departureCityCode;
    TextView departureTime;
    TextView arrivalCityCode;
    TextView arrivalTime;
}

    @Override
    public void add(Object object){
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
        row=convertView;
        DataHandler handler;
        if (convertView==null){
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.flight_item,parent, false);
            handler = new DataHandler();
            handler.departureCityCode = (TextView) row.findViewById(R.id.departureTextView);
            handler.arrivalCityCode = (TextView) row.findViewById(R.id.arrivalTextView);
            handler.arrivalTime = (TextView) row.findViewById(R.id.arrivalDateTextView);
            handler.departureTime = (TextView) row.findViewById(R.id.departureDateTextView);
            row.setTag(handler);
        }
        else{
            handler= (DataHandler) row.getTag();
        }
        Flight flight;
        flight = (Flight) this.getItem(position);
        handler.departureCityCode.setText(flight.departureCityCode);
        handler.arrivalCityCode.setText(flight.arrivalCityCode);
        handler.arrivalTime.setText(flight.arrivalTime.toString());
        handler.departureTime.setText(flight.departureTime.toString());
        return row;
    }
}
