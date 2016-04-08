package takeoff.cis350.upenn.edu.takeoff.ui.usersui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import takeoff.cis350.upenn.edu.takeoff.R;

/**
 * Created by dylan on 4/7/16.
 */
public class GroupPageActivity extends ListActivity {

    private final String GROUP_MESSAGE = "GROUP_MESSAGE";
    ListView groupView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadGroupPage();
        // TODO: Set the view
    }

    protected void loadGroupPage() {
        System.out.println("IN DASHBOARD LOADING");
        groupView = new GroupPageView(getApplicationContext());
        String[] groups = new String[10];
        for (int i = 0;  i < groups.length; i++) {
            groups[i] = "mock_group_" + i;
        }

        // for now, displaying empty information
        setListAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, groups));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                groups);



        groupView.setAdapter(adapter);

        // set the view
        setContentView(R.layout.activity_group_page_view);




    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new  Intent(this, GroupPage.class);
        Log.e("GroupPage", "Here!!!");

        // Parse the TextView to a group name
        // TODO: Actually make this work
        String groupInfo = ((TextView) v).getText().toString();
        Log.e("GroupPage", "groupInfo: " + groupInfo);

        // add  the relevant extras
        //

        // Start the FlightInfoActivity
        intent.putExtra(GROUP_MESSAGE, groupInfo);
        startActivity(intent);
    }

}
