package takeoff.cis350.upenn.edu.takeoff;

        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.content.Intent;
        import android.widget.AdapterView;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //Keep the default board size as 4
    int boardSize = 4;
    //Extra Message
    public final static String EXTRA_MESSAGE = "spinnerValue";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //MY CODE

    public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {

    }

    public void login(View view){
        // So Login is a default Activity. Dylan, you can make it facy IF you want
        // I'd say leave it till the end :D
        Intent intent = new  Intent (this, LogInActivity.class);
        //start Activity intent
        startActivity(intent);
    }

    public void guest (View view) {
        // Goes to my Dashboard
        Intent intent = new  Intent (this, SearchInfo.class);
        //start Activity intent
        // TODO: Fix! This causes the app to crash when clicking button
        startActivity(intent);
    }

    public void sign (View view) {
        // DYLAN, YOU NEED TO IMPLEMENT THIS
        // This is the sign in page
        // Change the Activity call later :)
        Intent intent = new  Intent (this, SignUpActivity.class);
        //start Activity intent
        startActivity(intent);
    }
}
