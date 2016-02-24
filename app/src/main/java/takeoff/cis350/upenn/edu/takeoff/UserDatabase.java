package takeoff.cis350.upenn.edu.takeoff;

import java.util.ArrayList;

/**
 * A singleton class to store usernames and passwords locally
 */
public class UserDatabase {

    private static UserDatabase database = new UserDatabase();
    private ArrayList<String> usernames;

    private UserDatabase() {
        usernames = new ArrayList<String>();
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

    public void addUser(String uname, String pass)  {
        usernames.add(uname  + ":" + pass);
    }
}
