package takeoff.cis350.upenn.edu.takeoff.user;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;

import takeoff.cis350.upenn.edu.takeoff.flight.Flight;

/**
 * Created by dylan on 4/7/16.
 */
public class Group {

    private User admin;
    private String gid;
    private List<String> UIDs;
    private List<Flight> shared;

    /**
     * Constructor for the Group class; an administrator is determined by the User who created
     * the group.
     * @param admin the group creator
     */
    public Group(User admin, String name) {
        // set the admin for the group and initialize Arrays
        // TODO: Check that a group name is not used already
        this.admin = admin;
        this.gid = name;
        UIDs = new ArrayList<>();
        UIDs.add(admin.getUID());
        shared = new ArrayList<>();
    }

    /**
     * Invite another user to the group
     */
    public void invite() {
        Firebase usersRef = new Firebase("https://brilliant-inferno-6470.firebaseio.com/users");
        if (!usersRef.getAuth().getUid().equals(admin.getUID())) {
            // user is not the admin, does not have privledges to invite people to group
            // TODO: handle
        } else {
            // user is admin adn does have privledges to invite people to group
            // TODO: handle
        }

    }

    /**
     * Share a flight with the group. No action is taken if the list of flights exceeds the maximum
     * number of shared Flights
     * @param flight
     */
    public void share(Flight flight) {
        if (shared.size() >= 20) {
            // number of shared Flights exceeds maximum
            // TODO: Handle
        } else  {
            // add the flight to the group's list of flights
            shared.add(flight);
            // TODO: finish updating firebase
        }
    }

    public List<String> getUsers() {
        // TODO: Get usernames or other info to display for GroupPage

        return null;
    }


}
