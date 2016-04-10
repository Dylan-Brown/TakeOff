package takeoff.cis350.upenn.edu.takeoff.ui.favorites;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
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
import takeoff.cis350.upenn.edu.takeoff.ui.search.Dashboard;

/**
 * The class representing the Favorites fragment of TabbedActivity
 */
public class FavoritesFragment extends ListFragment {

    private final Firebase usersRef =
            new Firebase("https://brilliant-inferno-6470.firebaseio.com/users");
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
     *
     */
    private void setVariables() {
        Dashboard dash = (Dashboard) getActivity().getSupportFragmentManager().findFragmentByTag("dashboard");
        listView = getListView();
        if(listView == null) {
            return;
        }
        flightResults = dash.getFlightResults();

        String[] flights;
        if (flightResults != null) {
            flights = new String[flightResults.size()];
        } else {
            flights = new String[10];
            Arrays.fill(flights, "");
        }
        setListAdapter(new ArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_single_choice, flights));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                flights);
        listView.setAdapter(adapter);
    }


    private void loadFavorites() {


        if (usersRef.getAuth() !=  null) {
            Log.e("Dashboard", "Authorized");

            String uid = usersRef.getAuth().getUid();
            usersRef.child(uid).child("favoriteFlights").addListenerForSingleValueEvent(
                    new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            // User has favorites; get this information and replace search results
                            Log.e("Favorites Fragment", "onDataChange");
                            if(flightResults == null) {
                                flightResults = new ArrayList<Flight>();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            // Internally display error message, externally claim nothing found
                            Log.e("Dashboard", "The read failed: " + firebaseError.getMessage());
                            Toast toast = Toast.makeText(getActivity(),
                                    "No favorites found.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });

        } else {
            // No authenticated user (guest session or some error) - no favorites data
            Toast toast = Toast.makeText(getActivity(), "No favorites found.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     *
     * @param info
     */
    private void setAdapter(String[] info) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                info);
        listView.setAdapter(adapter);
    }
}
