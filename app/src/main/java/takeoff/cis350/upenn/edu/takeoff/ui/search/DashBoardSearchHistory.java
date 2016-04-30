package takeoff.cis350.upenn.edu.takeoff.ui.search;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.support.v4.app.SharedElementCallback;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.flight.Flight;
import takeoff.cis350.upenn.edu.takeoff.flight.JSONAsyncTask;
import takeoff.cis350.upenn.edu.takeoff.flight.QPXJSONReader;
import takeoff.cis350.upenn.edu.takeoff.flight.SearchQuerytoQPXReader;
import takeoff.cis350.upenn.edu.takeoff.ui.WelcomeActivity;

public class DashBoardSearchHistory extends ListFragment {

    List<Flight> flightResults;
    List<SearchQuery> searches;
    DummySearchQueryHistory history = new DummySearchQueryHistory();
    String[] stringHistory;

    ListView lv;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.lv = (ListView) getView().findViewById(android.R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                try {
                    String s = (String) lv.getItemAtPosition(position);
                    SearchQuery sq = SearchQuery.fromHumanReadable(s);
                    for (SearchQuery q : searches) {
                        if (SearchQuery.compare(sq, q)) {
                            Log.e("DashboardSearchHistory", "Found Search Query");
                             String jsonSearch = SearchQuerytoQPXReader.makeJSONSearchObject(sq);
                             new SearchPage().new JSONAsyncTask(getContext()).execute(jsonSearch);

                        }
                    }
                } catch (Exception e) {
                    // bla
                }

            }
        });

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
                        String[] stringHistory = new String[queries.size()];

                        for (int i = 0; i < stringHistory.length; i++) {
                            String sqString = (String) queries.get(i);
                            SearchQuery sq = SearchQuery.parseSearchQuery(sqString);
                            if (sq != null && sq.humanReadable() != null) {
                                stringHistory[i] = sq.humanReadable();
                                searches.add(sq);
                                // Log.e("SearchQueries", "query is " + sq.humanReadable());
                            } else {
                                stringHistory[i] = "";
                            }



                            // Log.e("SearchQueries", "human readable query is " + sq);
                        }
                        // Log.e("SearchQueries", "Setting adapter");
                        setAdapter(stringHistory);
                    }
                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    String error = (String) getText(R.string.dashboard_no_results);
                    (Toast.makeText(getContext(), error, Toast.LENGTH_SHORT)).show();
                }
            });
        }

        // int layout = android.R.layout.simple_list_item_single_choice;
        // setListAdapter(new ArrayAdapter(getActivity(), layout, stringHistory));
        // setAdapter(stringHistory);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vGroup, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard_history, vGroup, false);
        searches = new ArrayList<>();
        return rootView;
    }

    /**
     * Set the list adapter for a list of String representations of search queries
     * @param info the list of String representations of search queries
     */
    private void setAdapter(String[] info) {
        int layout = android.R.layout.simple_list_item_1;
        // Log.e("SearchQueries", "Setting adapterL layout is " +  layout);
        // Log.e("SearchQueries", "Setting adapterL activity is " +  getActivity());
        for (int i =  0; i < info.length; i++) {
            // Log.e("ForLoop", "info[i] is " + info[i]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), layout, info);
        lv.setAdapter(adapter);
    }
}
