package takeoff.cis350.upenn.edu.takeoff;

/**
 * Created by tangson on 2/20/16.
 */
import java.lang.String;
import java.util.Collection;
import java.util.Set;

public class User {
    private static int COUNTER=0;
    FavoriteFlights favoriteFlights;

    public User (){
        // TODO: Actually use a unique ID for the flight
        favoriteFlights=new FavoriteFlights(COUNTER=+1);
    }
    public boolean addFavorite(Flight flight){
        return favoriteFlights.add(flight);
    }

    public boolean removeFavorite(Flight flight){
        return favoriteFlights.remove(flight);
    }

    public Flight[] getFavorites(){
        Set set=favoriteFlights.getFlights();
        return (Flight[]) set.toArray(new Flight[set.size()]);
    }
}
