package takeoff.cis350.upenn.edu.takeoff;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by tangson on 2/23/16.
 * https://www.youtube.com/watch?v=DzpwvZ4S27g
 */
//THIS IS IT!!!

public class FlightListActivity extends ListActivity {
    Flight[] flights;///flight list will be bundled in. Get the flights from the favorite_list.
    String[] arrivalCityCodes;
    String[] departureCityCodes;
    String[] departureTime;
    String[] arrivalTime;
    FlightAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listView=(ListView) findViewById(R.id.list_view);//this goes to the favorite_list
        adapter = new FlightAdapter(getApplicationContext(), R.layout.flight_item);
        for (Flight flight: flights){
            adapter.add(flight);
        }
        listView.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    /*
    private class MyListAdapter extends ArrayAdapter<String> {
        private int layout;

        public MyListAdapter(Context context, int resource) {
            super(context, resource);
        }
    }*/
}

