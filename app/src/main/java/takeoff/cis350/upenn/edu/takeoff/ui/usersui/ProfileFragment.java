package takeoff.cis350.upenn.edu.takeoff.ui.usersui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import takeoff.cis350.upenn.edu.takeoff.R;

/**
 * The Fragment in the TabbingActivity to display User's profile information
 */
public class ProfileFragment extends Fragment {

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        return rootView;
    }
}
