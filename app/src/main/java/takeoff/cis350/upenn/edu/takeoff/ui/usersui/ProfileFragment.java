package takeoff.cis350.upenn.edu.takeoff.ui.usersui;

import android.graphics.Bitmap;
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

import java.util.HashMap;
import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.ui.WelcomeActivity;

/**
 * The Fragment in the TabbingActivity to display User's profile information. We display a profile
 * image for the user, as well as their username. Additioanlly, there is a toolbar to view a list
 * of groups to which the user belongs, or to start a new group. Most features in this fragment have
 * default values and guest users cannot view or create groups.
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
    public View onCreateView(LayoutInflater inflater, ViewGroup vGroup, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, vGroup, false);
        profilePic = (ImageView) rootView.findViewById(R.id.userpic);
        username = (TextView) rootView.findViewById(R.id.username_tv);

        // if there is a user logged in, display their information
        if (WelcomeActivity.USER_FIREBASE.getAuth() != null) {
            AuthData auth = WelcomeActivity.USER_FIREBASE.getAuth();
            Firebase userRef = WelcomeActivity.USER_FIREBASE.child(auth.getUid());

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    // get the username to display
                    HashMap<String, String> uData = (HashMap<String, String>) snapshot.getValue();
                    username.setText(uData.get(getString(R.string.firebase_uname)));

                    // get the user's profile image, if there is one
                    String pic = uData.get(getString(R.string.firebase_pic));
                    if (pic != null) {
                        Bitmap bmPic = ImageOps.stringToBitmap(pic);
                        profilePic.setImageBitmap(bmPic);
                    }
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

    public void setProfilePictrue(Bitmap image, final String base64Image) {
        profilePic.setImageBitmap(image);

        AuthData auth = WelcomeActivity.USER_FIREBASE.getAuth();
        final Firebase userRef = WelcomeActivity.USER_FIREBASE.child(auth.getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // get the username to display
                HashMap<String, Object> uData = (HashMap<String, Object>) snapshot.getValue();
                uData.put(getString(R.string.firebase_pic), base64Image);
                userRef.updateChildren(uData);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // failed to retrieve user information
                String error = getString(R.string.firebase_profile_fail);
                (Toast.makeText(getContext(), error, Toast.LENGTH_SHORT)).show();
            }
        });




        profilePic.invalidate();
    }
}
