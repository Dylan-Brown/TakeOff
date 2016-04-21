package takeoff.cis350.upenn.edu.takeoff.ui.favorites;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.flight.Flight;

/**
 * The activity representing where we display all favorited flights of a given user
 */
public class FavoriteActivity extends ListActivity {

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // todo: actually get favorites? or is this class outdated?
        Flight[] favoriteFlights = new Flight[20];
        ArrayAdapter adapter = new ArrayAdapter(getListView().getContext(), android.R.layout.simple_list_item_1, favoriteFlights);
        getListView().setAdapter(adapter);
    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.action_settings) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
