package takeoff.cis350.upenn.edu.takeoff;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * A singleton class to store usernames and passwords locally
 */
public class UserDatabase {

    private static UserDatabase database = new UserDatabase();
    private ArrayList<String> usernames;
    private TreeMap<String, User> users;

    private UserDatabase() {
        usernames = new ArrayList<String>();
        users = new TreeMap<String, User>();
    }

    public static UserDatabase getInstance() {
        return database;
    }

    public boolean validate(String uname, String pass)  {
        for (String s : usernames)  {
            String format = uname  + ":" + pass;
            if (s.equals(format))  {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the User associated with a given username. Should not be called unless username has been
     * confirmed previouosly
     *
     * @param uname the username of the User
     * @return the User
     * @throws IllegalArgumentException if invalid username
     */
    public User getUser(String uname) {
        if (confirm(uname)) {
            return users.get(uname);
        }
        throw new IllegalArgumentException("UserDatabase: Invalid username!");
    }

    public boolean isUser(String uname) {
        if (confirm(uname)) {
            return true;
        }
        return false;
    }

    // Confirms that a username is in the database
    private boolean confirm(String uname) {
        for (String s : usernames)  {
            if (s.contains(uname))  {
                return true;
            }
        }
        return false;
    }

    // Add a new user to the database
    public void addUser(String uname, String pass)  {
        usernames.add(uname  + ":" + pass);
        users.put(uname, new User());
    }
}
