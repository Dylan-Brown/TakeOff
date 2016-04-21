package takeoff.cis350.upenn.edu.takeoff.ui.usersui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.ui.WelcomeActivity;

/**
 * The Fragment in the TabbingActivity to display User's profile information
 */
public class ProfileFragment extends Fragment {

    private View rootView;
    private ImageView profilePic;
    private TextView username;

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        profilePic = (ImageView) getActivity().findViewById(R.id.userpic);
        username = (TextView) getActivity().findViewById(R.id.username);

        // if there is a userlogged-in, display their information
        if (WelcomeActivity.USER_FIREBASE.getAuth() != null) {
            AuthData auth = WelcomeActivity.USER_FIREBASE.getAuth();
            final String uid = auth.getUid();
            Firebase userRef = WelcomeActivity.USER_FIREBASE.child(uid);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    // get the username to display
                    HashMap<String, String> uData = (HashMap<String, String>) snapshot.getValue();
                    username.setText(uData.get(getString(R.string.firebase_uname)));

                    // get the user's profile image, if there is one
                    // TODO: Do this
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    // failed to retrieve user information
                    String error = getString(R.string.firebase_profile_fail);
                    (Toast.makeText(getContext(), error, Toast.LENGTH_SHORT)).show();
                }
            });

        }

        return rootView;
    }
}
