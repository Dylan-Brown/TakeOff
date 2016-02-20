package takeoff.cis350.upenn.edu.takeoff;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.*;
import android.app.*;
import android.view.*;

/**
 * Created by tangson on 2/19/16.
 */

/*PER FLIGHT*/

public class FlightResultActivity  extends ListActivity {

        // This is the Adapter being used to display the list's data
        SimpleCursorAdapter mAdapter;

        // These are the Contacts rows that we will retrieve
        static final String[] PROJECTION = new String[] {ContactsContract.Data._ID,
                ContactsContract.Data.DISPLAY_NAME};


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Create a progress bar to display while the list loads
        }

        // Called when a new Loader needs to be created
        public void onCreateLoader(int id, Bundle args) {
        }

        // Called when a previously created loader has finished loading
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            // Swap the new cursor in.  (The framework will take care of closing the
            // old cursor once we return.)
            mAdapter.swapCursor(data);
        }

        // Called when a previously created loader is reset, making the data unavailable
        public void onLoaderReset(Loader<Cursor> loader) {
            // This is called when the last Cursor provided to onLoadFinished()
            // above is about to be closed.  We need to make sure we are no
            // longer using it.
            mAdapter.swapCursor(null);
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            // Do something when a list item is clicked
        }
    }