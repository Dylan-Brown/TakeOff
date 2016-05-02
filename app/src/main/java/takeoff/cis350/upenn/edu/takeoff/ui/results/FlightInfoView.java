package takeoff.cis350.upenn.edu.takeoff.ui.results;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.graphics.Color;
import android.graphics.Paint;
import takeoff.cis350.upenn.edu.takeoff.flight.Flight;

/**
 * Description:
 * This class represents the View when user's click on an individual Flight in the Dashboard
 */
public class FlightInfoView extends View {

    private Flight flight;
    private int canvasWidth = 0;
    private int canvasHeight = 0;
    private int favButtonRadius = 0;
    private int favButtonXCoord, favButtonYCoord;
    private double[] buyBound;
    private double[] bckBound;
    private boolean isFavorite = false;
    private boolean ticket = false;

    /**
     * Description: The constructor for the class. Calls View's constructor and sets the FireBase reference.
     * Also uses the FlightInfo string to get relevant Flight information to display
     */
    public FlightInfoView(Context c, String flightInfo) {
        super(c);

        // save the Flight information
        this.flight = Flight.parseFlight(flightInfo);
    }

    /**
     * Description: Draw the info on the canvas
     * Could use a better method, such as viewgroup to embed more textviews here
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // get the canvas information
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();
        favButtonRadius = canvasHeight / 30;
        favButtonXCoord = canvasWidth / 2;
        favButtonYCoord = canvasHeight - canvasHeight / 10;

        Paint paint = new Paint();
        canvas.drawPaint(paint);
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, canvasWidth, canvasHeight, paint);
        paint.setColor(Color.BLUE);

        // write the flight info
        paint.setTextSize(canvasWidth / 15);
        String[] split = flight.humanReadable().split("\n");
        for (int i = 0; i < split.length;i++) {
            int textWidth = canvasWidth / 10;
            int textHeight = canvasHeight / 10 + i * (canvasHeight / 10 );
            canvas.drawText(split[i], textWidth, textHeight, paint);
        }
    }
}
