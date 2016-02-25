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

        flights=new Flight[4];
        flights[0]=new Flight();
        flights[0].arrivalCityCode="BOS";
        flights[0].departureCityCode="PHL";
        flights[0].departureDate="2016-03-14";
        flights[0].arrivalDate="2016-03-19";
        flights[0].airline="JETBLUE";
        flights[0].totalCost=100.00;
        flights[2]=new Flight();
        flights[2].arrivalCityCode="BOS";
        flights[2].departureCityCode="PHL";
        flights[2].departureDate="2016-03-12";
        flights[2].arrivalDate="2016-03-19";
        flights[2].airline="Delta";
        flights[2].totalCost=80.00;

        flights[3]=new Flight();
        flights[3].arrivalCityCode="BOS";
        flights[3].departureCityCode="PHL";
        flights[3].departureDate="2016-03-13";
        flights[3].arrivalDate="2016-03-19";
        flights[3].airline="Spirit";
        flights[3].totalCost=12.00;

        flights[1]=new Flight();
        flights[1].arrivalCityCode="LAX";
        flights[1].departureCityCode="BOS";
        flights[1].departureDate="2016-04-12";
        flights[1].arrivalDate="2016-04-19";
        flights[1].airline="British Airways";
        flights[1].totalCost=335.00;

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
                i.putExtra("cost", flights[position].totalCost);
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

