package takeoff.cis350.upenn.edu.takeoff.ui.favorites;

import java.util.*;

import takeoff.cis350.upenn.edu.takeoff.flight.Flight;

/**
 * Created by tangson on 2/19/16.
 */
public class FavoriteFlights {

    private Map favorites;
    public Integer uniqueID;

    /**
     * Constructor for  the class
     * @param uniqueID
     */
    public FavoriteFlights(int uniqueID){
        // Flight.stringID, Flight
        favorites = new LinkedHashMap<Integer, Flight>();
        this.uniqueID=uniqueID;
    }

    public boolean add(Flight flight){
        if (!favorites.containsKey(flight.getId())){
            favorites.put(flight.getId(), flight);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Remove a Flight from the  list of FavoriteFlights
     * @param flight the Flight to be removed
     * @return true if the Flight was removed from the list, false otherwise
     */
    public boolean remove(Flight flight){
        return (null != favorites.remove(flight.getId()));
    }

    /**
     * Gets the list of FavoriteFlights of a user
     * @return the list of FavoriteFlights
     */
    public Set getFlights() {
        return (Set) favorites.values();
    }
}
