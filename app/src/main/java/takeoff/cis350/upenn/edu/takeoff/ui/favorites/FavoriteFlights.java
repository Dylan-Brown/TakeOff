package takeoff.cis350.upenn.edu.takeoff.ui.favorites;

import java.util.*;

import takeoff.cis350.upenn.edu.takeoff.flight.Flight;

/**
 * Created by tangson on 2/19/16.
 */
public class FavoriteFlights {
    private Map Favorites;
    public Integer uniqueID;

    public FavoriteFlights(int uniqueID){
        Favorites=new LinkedHashMap<Integer, Flight>(); //Flight.stringID, Flight
        this.uniqueID=uniqueID;
    }

    public boolean add(Flight flight){
        if (!Favorites.containsKey(flight.getId())){
            Favorites.put(flight.getId(), flight);
            return true;
        }
        else return false;
    }

    public boolean remove(Flight flight){
        return (null!=Favorites.remove(flight.getId()));
    }

    public Set getFlights() {
        return (Set) Favorites.values();
    }
}
