package takeoff.cis350.upenn.edu.takeoff.ui.usersui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;

import takeoff.cis350.upenn.edu.takeoff.R;

/**
 * This class represents the Activity relevant to displaying a group's information. The name of the
 * group is displayed at the top of the page, and... TODO: finish class description
 */
public class GroupPage extends Activity {

    private TextView groupNameView;
    private ListView groupMembersView;
    private String[] groupMembers;
    private String groupName;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_page);

        // TODO: Get group name from intent message
        // String mesg = intent.getStringExtra("GROUP_MESSAGE");
        // Log.e("GroupPage", "onCreate: mesg is " + mesg);
        // group = Group.parseGroup(mesg);
        // Log.e("GroupPageActual", "Group message is " + mesg);
        groupName = "This_would_be_the_group_name";

        // TODO: Populate array with actual group members
        groupMembers = new String[10];
        Arrays.fill(groupMembers, "fake_group_member");

        groupNameView = (TextView) findViewById(R.id.textViewGroup);
        if (groupNameView == null) {
            groupNameView = new TextView(getApplicationContext());
        }
        groupNameView.setText(groupName);


        // TODO: Make the custom list view where each member's profile icon shows up
        // TODO: next to their name
        groupMembersView = (ListView) findViewById(R.id.member_list);
        int layout = android.R.layout.simple_list_item_single_choice;
        groupMembersView.setAdapter(new ArrayAdapter(this, layout, groupMembers));
    }

}
