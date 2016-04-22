package takeoff.cis350.upenn.edu.takeoff.ui.search;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
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
import java.util.List;
import java.util.Map;

import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.flight.Flight;
import takeoff.cis350.upenn.edu.takeoff.ui.WelcomeActivity;

public class DashBoardSearchHistory extends ListFragment {

    List<Flight> flightResults;
    DummySearchQueryHistory history = new DummySearchQueryHistory();
    String[] stringhistory = history.stringHistory2;

    ListView lv;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.lv = getListView();

        // Retrieve the search queries from the database
        final Firebase usersRef = WelcomeActivity.USER_FIREBASE;
        final String sqs = getString(R.string.firebase_sqs);

        if (usersRef.getAuth() !=  null) {
            final String uid = usersRef.getAuth().getUid();

            usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Map<String, Object> userInfo = (Map<String, Object>) snapshot.getValue();

                    if (!userInfo.containsKey(sqs)) {
                        // user has no previous search queries; create the array
                        ArrayList<Object> queries = new ArrayList<>();
                        userInfo.put(sqs, queries);

                    } else {
                        // get user's previouos search queries
                        ArrayList<Object> queries = (ArrayList<Object>) userInfo.get(sqs);
                        String[] queryStrings = new String[queries.size()];
                        for (int i = 0; i < queries.size(); i++) {
                            String sqString = (String) queries.get(i);
                            SearchQuery sq = SearchQuery.parseSearchQuery(sqString);
                            queryStrings[i++] = sq.humanReadable();

                            Log.e("SearchQueries", "query is " + sqString);
                            Log.e("SearchQueries", "human readable query is " + sq);
                        }
                        stringhistory = queryStrings;
                        setAdapter(queryStrings);
                    }
                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    String error = (String) getText(R.string.dashboard_no_results);
                    (Toast.makeText(getContext(), error, Toast.LENGTH_SHORT)).show();
                }
            });
        }

        int layout = android.R.layout.simple_list_item_single_choice;
        setListAdapter(new ArrayAdapter(getActivity(), layout, stringhistory));
        setAdapter(stringhistory);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vGroup, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard_history, vGroup, false);
        return rootView;
    }

    /**
     * Set the list adapter for a list of String representations of search queries
     * @param info the list of String representations of search queries
     */
    private void setAdapter(String[] info) {
        int layout = android.R.layout.simple_list_item_1;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), layout, info);
        this.lv.setAdapter(adapter);
    }
}
