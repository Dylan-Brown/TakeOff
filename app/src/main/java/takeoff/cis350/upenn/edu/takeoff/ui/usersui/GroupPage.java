package takeoff.cis350.upenn.edu.takeoff.ui.usersui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.user.Group;

/**
 * Created by dylan on 4/8/16.
 */
public class GroupPage extends Activity {

    private Group group;
    private TextView displayInfo;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        System.out.println("IN GROUP PAGE");
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String mesg = intent.getStringExtra("GROUP_MESSAGE");
        group = Group.parseGroup(mesg);
        Log.e("GroupPageActual", "Group message is " + mesg);

        displayInfo = (TextView) findViewById(R.id.textViewGroup);
        if (displayInfo == null)
            displayInfo = new TextView(getApplicationContext());
        displayInfo.setText("this is where the group info would go");
        setContentView(displayInfo);

    }




}
