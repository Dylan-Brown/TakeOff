package takeoff.cis350.upenn.edu.takeoff.ui.favorites;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.flight.Flight;
import takeoff.cis350.upenn.edu.takeoff.ui.WelcomeActivity;
import takeoff.cis350.upenn.edu.takeoff.ui.search.Dashboard;

/**
 * The class representing the Favorites fragment of TabbedActivity
 */
public class FavoritesFragment extends ListFragment {

    private final Firebase usersRef = WelcomeActivity.USER_FIREBASE;
    List<Flight> flightResults;
    ListView listView;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVariables();
        loadFavorites();
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        return rootView;
    }

    /**
     *Sets the global vairables
     */
    private void setVariables() {

        // get the flight results
        Dashboard dash = (Dashboard)
                getActivity().getSupportFragmentManager().findFragmentByTag("dashboard");
        if((listView = getListView()) == null) {
            return;
        }
        flightResults = dash.getFlightResults();

        // form the results array
        String[] flights;
        if (flightResults != null) {
            flights = new String[flightResults.size()];
        } else {
            flights = new String[10];
            Arrays.fill(flights, "");
        }

        // set the adapters
        int layout1 = android.R.layout.simple_list_item_single_choice;
        int layout2 = android.R.layout.simple_list_item_1;
        setListAdapter(new ArrayAdapter(getActivity(), layout1, flights));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), layout2, flights);
        listView.setAdapter(adapter);
    }

    /**
     * If the user is signed in, this method attempts to load that user's favorites
     */
    private void loadFavorites() {
        // try authenticating the user
        if (usersRef.getAuth() !=  null) {
            String uid = usersRef.getAuth().getUid();
            String fav = getString(R.string.firebase_fav);
            usersRef.child(uid).child(fav).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            // TODO: See if this works
                            if(flightResults == null) {
                                flightResults = new ArrayList<>();
                            }
                        }
                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            // notify the user no favorites were found
                            String error = getString(R.string.error_no_favorites);
                            (Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT)).show();
                        }
                    });
        }
    }

    /**
     *
     * @param info
     */
    private void setAdapter(String[] info) {
        int layout = android.R.layout.simple_list_item_1;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), layout, info);
        listView.setAdapter(adapter);
    }
}
