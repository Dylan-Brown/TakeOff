package takeoff.cis350.upenn.edu.takeoff;

// import com.android.volley.toolbox.*;
// import com.android.volley.*;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by tangson on 3/7/16.
 */

//curl -d @request.json --header "Content-Type: application/json"
// https://www.googleapis.com/qpxExpress/v1/trips/search?key=your_API_key_here

public class APIReader {
    public void Jsonreader() {
        try {
            Object obj = new FileReader("Dummydata");
        } catch (FileNotFoundException e) {
        }
        ;
    }
}
