package takeoff.cis350.upenn.edu.takeoff;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * Created by tangson on 2/23/16.
 * https://www.youtube.com/watch?v=DzpwvZ4S27g
 */
//THIS IS IT!!!

public class FavoriteListActivity extends ListActivity {
    Flight[] flights;///flight list will be bundled in. Get the flights from the favorite_list.

    FlightAdapter adapter;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("HIPPIE!","HI,THERE IS A LIST VIEW");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_list);
        listView=(ListView) findViewById(android.R.id.list);//this goes to the favorite_list
        adapter = new FlightAdapter(getApplicationContext(), R.layout.flight_item);
        for (Flight flight: flights){
            adapter.add(flight);
        }
        listView.setAdapter(adapter);

        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent i = new Intent(getApplicationContext(), FlightResultActivity.class);
                Log.e("HI", "HI!!");
                i.putExtra("departureDate", flights[position].departureDate);
                i.putExtra("arrivalDate", flights[position].arrivalDate);
                i.putExtra("departureCityCode", flights[position].departureCityCode);
                i.putExtra("arrivalCityCode", flights[position].arrivalCityCode);
                i.putExtra("cost", flights[position].cost);
                i.putExtra("airline", flights[position].airline);


                startActivity(i);
            }
        });
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

