package takeoff.cis350.upenn.edu.takeoff.ui.search;

import android.content.Context;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.flight.SearchQuerytoQPXReader;
import takeoff.cis350.upenn.edu.takeoff.ui.WelcomeActivity;

public class SearchHistory extends ListFragment {

    List<SearchQuery> searches;
    ListView lv;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.lv = (ListView) getView().findViewById(android.R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                try {
                    //Perform the search that was just clicked on
                    String s = (String) lv.getItemAtPosition(position);
                    SearchQuery sq = SearchQuery.fromHumanReadable(s);
                    for (SearchQuery q : searches) {
                        if (SearchQuery.compare(sq, q)) {
                            String jsonSearch = SearchQuerytoQPXReader.makeJSONSearchObject(sq);
                            SearchPage sp= new SearchPage();
                            sp.new JSONAsyncTask(getContext()).execute(jsonSearch);
                        }
                    }
                } catch (Exception e) {
                    //Decide proper thing to do
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
                        // get user's previous search queries
                        ArrayList<Object> queries = (ArrayList<Object>) userInfo.get(sqs);
                        String[] stringHistory = new String[queries.size()];

                        for (int i = 0; i < stringHistory.length; i++) {
                            String sqString = (String) queries.get(i);
                            SearchQuery sq = SearchQuery.parseSearchQuery(sqString);
                            if (sq != null && sq.humanReadable() != null) {
                                stringHistory[i] = sq.humanReadable();
                                searches.add(sq);
                            } else {
                                stringHistory[i] = "";
                            }
                        }
                        //Set the adapter
                        setAdapter(stringHistory);
                    }
                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    String error = (String) getText(R.string.dashboard_no_results);
                    (Toast.makeText(getContext(), error, Toast.LENGTH_SHORT)).show();
                }
            });
        } else {
            //If not logged in, simply show that the user has to be signed in to use this
            Context c = getContext();
            String message = c.getString(R.string.please_sign_in);
            Toast toast = Toast.makeText(c,
                    message, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vGroup, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, vGroup, false);
        searches = new ArrayList<>();
        return rootView;
    }

    /**
     * Set the list adapter for a list of String representations of search queries
     * @param info the list of String representations of search queries
     */
    private void setAdapter(String[] info) {
        int layout = android.R.layout.simple_list_item_1;
        for (int i =  0; i < info.length; i++) {
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), layout, info);
        lv.setAdapter(adapter);
    }
}
