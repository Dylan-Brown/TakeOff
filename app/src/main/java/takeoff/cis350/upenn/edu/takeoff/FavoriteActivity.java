package takeoff.cis350.upenn.edu.takeoff;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.Collection;

/**
 * Created by tangson on 2/19/16.
 * https://www.youtube.com/watch?v=ZEEYYvVwJGY
 */
public class FavoriteActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User user=new User();
        Flight[] favoriteFlights = user.getFavorites();
        ArrayAdapter adapter=new ArrayAdapter(getListView().getContext(), android.R.layout.simple_list_item_1, favoriteFlights);
        getListView().setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if (id==R.id.action_settings){return true;}
    return super.onOptionsItemSelected(item);
    }

    @Override
    private class MyListAdapter extends ArrayAdapter<String>{
        private int layout;
    }
}
