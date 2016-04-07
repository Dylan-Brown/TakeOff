package takeoff.cis350.upenn.edu.takeoff.ui.favorites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.ui.search.Dashboard;
import takeoff.cis350.upenn.edu.takeoff.ui.search.SearchQuery;

/**
 * Created by dylan on 3/25/16.
 */
public class FilterSearch extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    SearchQuery sq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Bundle extras = getIntent().getExtras();
        //String temp = (String) extras.get("searchQuery");
        //sq = SearchQuery.parseSearchQuery(temp);                                                      //this line is throwing error. i am trying to get the Search Query from DashBoard
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void transitionToSearch (View view) {
        //for now, just go to DashBoard
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }
}
