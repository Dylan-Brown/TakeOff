package takeoff.cis350.upenn.edu.takeoff.user;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;

import takeoff.cis350.upenn.edu.takeoff.flight.Flight;

/**
 * Created by dylan on 4/base64/16.
 */
public class Group {

    private String admin;
    private String gid;
    private List<String> UIDs;
    private List<Flight> shared;

    /**
     * Constructor for the Group class; an administrator is determined by the User who created
     * the group.
     * @param admin the group creator
     */
    public Group(String admin, String name) {
        // set the admin for the group and initialize Arrays
        // TODO: Check that a group name is not used already
        this.admin = admin;
        this.gid = name;
        UIDs = new ArrayList<>();
        UIDs.add(admin);
        shared = new ArrayList<>();
    }

    /**
     * Invite another user to the group
     */
    public void invite() {
        Firebase usersRef = new Firebase("https://brilliant-inferno-6470.firebaseio.com/users");
        if (!usersRef.getAuth().getUid().equals(admin)) {
            // user is not the admin, does not have privledges to invite people to group
            // TODO: handle  (FIX COMPARISON)
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

    // turns this group into a string representation
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(admin + "\\*");
        sb.append(gid + "\\*");
        for (String user : UIDs) {
            sb.append(user + "_");
        }
        sb.append("\\*");
        for (Flight flight : shared) {
            sb.append(flight.toString() + "_");
        }
        return sb.toString();
    }

    // takes a string form of a group and parses it
    public static Group parseGroup(String o) {
        if (o == null) {
            return new Group("", "");
        }
        String[] info = o.split("\\*");
        String admin = info[0];
        String gid = info[1];
        List<String> userList = new ArrayList<String>();
        for (String user : info[2].split("_")) {
            userList.add(user);
        }
        List<Flight> flightList = new ArrayList<>();
        String[] flights = info[3].split("_");
        for (String flight : flights) {
            flightList.add(Flight.parseFlight(flight));
        }
        Group group = new Group(admin, gid);
        group.shared = flightList;
        group.UIDs = userList;
        return group;
    }

    public String humanReadable() {
        return this.toString();
    }


}
