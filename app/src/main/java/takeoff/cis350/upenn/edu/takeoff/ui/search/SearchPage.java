package takeoff.cis350.upenn.edu.takeoff.ui.search;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import takeoff.cis350.upenn.edu.takeoff.flight.QPXJSONReader;
import takeoff.cis350.upenn.edu.takeoff.flight.SearchQuerytoQPXReader;
import takeoff.cis350.upenn.edu.takeoff.R;

import takeoff.cis350.upenn.edu.takeoff.ui.WelcomeActivity;

/*

Jason:			      AIzaSyBWkE-Lhv0er0KlL6adTT2I1NYEzfjeMbA
Anaka: Take Off               AIzaSyAvcsE9zxl3GvGtSncJYQf9zmSrRwSyAJQ
Judy: Flight Engine	      AIzaSyDoavIZSjsa5TAWSa29u-W71v4wbADIEos
Dylan: flight-1223            AIzaSyB_4Rk4qn5CajLsU7T3Y_K9Sc3m6gFVa_w
Judy: 			AIzaSyAdM9ny3j-ahi526so97XHcE9LBA_iyrrU
*/
/**
 * This class is the page the user sees when they enter their requirements for the flight search
 */
public class SearchPage extends Activity implements OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String HTTP_LINK = "https://www.googleapis.com/qpxExpress/v1/trips/"
        + "search?key=AIzaSyB_4Rk4qn5CajLsU7T3Y_K9Sc3m6gFVa_w";
    private EditText departureDateText;
    private EditText returningDateText;
    private String departureDateInput;
    private String returningDateInput;
    private DatePickerDialog departureDatePickerDialog;
    private DatePickerDialog returningDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private MultiAutoCompleteTextView countriesAutoComp;
    private MultiAutoCompleteTextView citiesEditText;
    private EditText budgetEditText;
    private EditText ticketEditText;
    private EditText airportEditText;
    private EditText waitTimeEditText;

    // TODO: Do these need to be global variables? Do any of these?
    private int day;
    private int month;
    private int year;

    private String cabin;
    private String alliance;
    private boolean refundable;
    private boolean nonstop; //T = 0, F = 5
    private List<String> allCities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        // get the current Date
        Calendar newCalendar = Calendar.getInstance();
        day = newCalendar.get(Calendar.DAY_OF_MONTH);
        month = newCalendar.get(Calendar.MONTH) + 1;
        year = newCalendar.get(Calendar.YEAR);

        // set the date views
        departureDateInput = "";
        returningDateInput = "";
        dateFormatter = new SimpleDateFormat(getString(R.string.date_format), Locale.US);
        findDateViews();
        setDateField();

        // set-up the countries auto-complete feature
        countriesAutoComp = (MultiAutoCompleteTextView) findViewById(R.id.autocomplete_country);
        String[] countryArray = getResources().getStringArray(R.array.countries);
        countriesAutoComp.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        countriesAutoComp.setOnFocusChangeListener(getOnFocusChangeListener());
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countryArray);
        countriesAutoComp.setAdapter(adapter);
        countriesAutoComp.addTextChangedListener(countryTextWatcher);

        // set the cities, airport code, and wait time EditText setters
        citiesEditText = (MultiAutoCompleteTextView) findViewById(R.id.city_input);
        citiesEditText.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        citiesEditText.setOnFocusChangeListener(getOnFocusChangeListener());
        airportEditText = (EditText) findViewById(R.id.airport_input);
        airportEditText.setOnFocusChangeListener(getOnFocusChangeListener());
        waitTimeEditText = (EditText) findViewById(R.id.wait_time_input);
        waitTimeEditText.setOnFocusChangeListener(getOnFocusChangeListener());

        // set up the budget fields
        budgetEditText = (EditText) findViewById(R.id.budget_input);
        budgetEditText.addTextChangedListener(textWatcher);
        budgetEditText.setOnFocusChangeListener(getOnFocusChangeListener());
        ticketEditText = (EditText) findViewById(R.id.num_ticket_input);
        ticketEditText.setText("1");
        ticketEditText.setOnFocusChangeListener(getOnFocusChangeListener());

        // set the classes Spinner
        setClassSpinner();
        setAllianceSpinner();

        // default the extra option checkboxes to false
        refundable = false;
        nonstop = false;
    }

    /**
     * This method returns a lister that will hide the onScreen keyboard
     * when the specific views do not have the focus anymore
     * @return an OnFocusChangeListener
     */
    public View.OnFocusChangeListener getOnFocusChangeListener() {
        View.OnFocusChangeListener listener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        };
        return listener;
    }

    /**
     * When the focus is not in this designated view, this method hides the onScreen Keyboard
     * @param view
     */
    public void hideKeyboard(View view) {
        String ims = Activity.INPUT_METHOD_SERVICE;
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(ims);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * This method sets the options for the spinner for flight class options
     */
    private void setClassSpinner() {
        cabin = "";
        Spinner spinner = (Spinner) findViewById(R.id.class_spinner);
        spinner.setOnItemSelectedListener(this);

        // add each class option to the array
        List<String> list = new ArrayList<String>();
        list.add(getString(R.string.class_none));
        list.add(getString(R.string.class_coach));
        list.add(getString(R.string.class_p_coach));
        list.add(getString(R.string.class_business));
        list.add(getString(R.string.class_first));

        // set the adapter
        int layout = android.R.layout.simple_spinner_item;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, layout, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    /**
     * This method sets the options and variables for the Alliance spinner
     */
    private void setAllianceSpinner() {
        alliance = "";
        Spinner spinner = (Spinner) findViewById(R.id.alliance_spinner);
        spinner.setOnItemSelectedListener(this);

        // add each alliance option to the array
        List<String> list = new ArrayList<String>();
        list.add(getString(R.string.alliance_none));
        list.add(getString(R.string.alliance_oneworld));
        list.add(getString(R.string.alliance_star));
        list.add(getString(R.string.alliance_sky));

        // set  the adapter
        int layout = android.R.layout.simple_spinner_item;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, layout, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_page, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    /**
     * @param v
     *
     * Given a specific view, this method is activated when the view is clicked
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == departureDateText.getId()) {
            departureDatePickerDialog.show();
        } else if (v.getId() == returningDateText.getId()) {
            returningDatePickerDialog.show();
        } else if (v.getId() == R.id.clear_departure_date) {
            System.out.println("CLEARING DEPARTURE");
            departureDateText.setText("");
        } else if (v.getId() == R.id.clear_return_date) {
            returningDateText.setText("");
        } else {
        }

    }

    /**
     * Description: A TextWatcher to handle parsing changes in the city textfield
     */
    private final TextWatcher countryTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
            // Get the countries
            if (s.toString().contains(",")) {

                // get the list of countries as listed by the user
                allCities.clear();
                String userInput = s.toString();
                String[] countries = userInput.split(", ");

                for (int i = 0; i < countries.length; i++) {
                    try {
                        // get the array of cities for this specific country
                        String fieldName = countries[i].replace(",", "").trim().replace(" ", "_").toLowerCase();
                        Field field = R.array.class.getField(fieldName);
                        int resId = field.getInt(null);
                        Log.e("FEILD:", resId + "-" + field.toGenericString());
                        String[] cities = getResources().getStringArray(resId);

                        // add each city to the list of all cities to autocomplete
                        for (int j = 0; j < cities.length; j++) {
                            allCities.add(cities[j]);
                        }

                     } catch (Exception e) {
                        Log.e("afterTextChanged", e.getMessage());
                    }
                }

                // update the array adapter for the city
                ArrayAdapter<String> adapterCity =
                        new ArrayAdapter<String>(getBaseContext(),
                                android.R.layout.simple_list_item_1, allCities);
                citiesEditText.setAdapter(adapterCity);
                citiesEditText.setOnFocusChangeListener(getOnFocusChangeListener());

            }

        }
    };

    /**
     * Description: A TextWatcher to handle parsing changes in the
     * budget textfield
     */
    private final TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!s.toString().matches("^\\$(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?$")) {
                String userInput = "" + s.toString().replaceAll("[^\\d]", "");
                StringBuilder cashAmountBuilder = new StringBuilder(userInput);

                while (cashAmountBuilder.length() > 3 && cashAmountBuilder.charAt(0) == '0') {
                    cashAmountBuilder.deleteCharAt(0);
                }
                while (cashAmountBuilder.length() < 3) {
                    cashAmountBuilder.insert(0, '0');
                }
                cashAmountBuilder.insert(cashAmountBuilder.length() - 2, '.');

                budgetEditText.removeTextChangedListener(this);
                budgetEditText.setText(cashAmountBuilder.toString());

                budgetEditText.setTextKeepState("$" + cashAmountBuilder.toString());
                Selection.setSelection(budgetEditText.getText(), cashAmountBuilder.toString().length() + 1);

                budgetEditText.addTextChangedListener(this);
            }
        }
    };

    /**
     * This method is used to find the correct EditText views for the respective
     * departure and return dates.
     */
    public void findDateViews() {
        departureDateText = (EditText) findViewById(R.id.departure_date);
        departureDateText.setInputType(InputType.TYPE_NULL);
        departureDateText.requestFocus();
        returningDateText = (EditText) findViewById(R.id.returning_date);
        returningDateText.setInputType(InputType.TYPE_NULL);
    }

    /**
     * This method sets the dialouge associated with the date fields
     */
    private void setDateField() {
        departureDateText.setOnClickListener(this);
        returningDateText.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        departureDatePickerDialog = new DatePickerDialog(this, getDateSetListener(true),
                newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));

        returningDatePickerDialog = new DatePickerDialog(this, getDateSetListener(false),
                newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    /**
     * @param departure
     * @return an OnDateSetListener for one of the Date EditTexts
     * Description: Handles when the user clicks to enter dates and how to change the strings
     */
    public OnDateSetListener getDateSetListener(boolean departure) {
        OnDateSetListener listener = null;
        if (departure) {
            listener = new OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    departureDateText.setText(dateFormatter.format(newDate.getTime()));
                    departureDateInput = dateFormatter.format(newDate.getTime());

                    Log.e("OnDateSetListener", "DEPARTURE LISTENER: " + departureDateInput);
                }
            };
        } else {
            listener = new OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    returningDateText.setText(dateFormatter.format(newDate.getTime()));
                    returningDateInput = dateFormatter.format(newDate.getTime());

                    Log.e("OnDateSetListener", "RETURN LISTENER: " + returningDateInput);
                }
            };
        }
        return listener;
    }


    /**
     * @param view Description: Handles which checkboxes are clicked
     */
    public void onCheckboxClicked(View view) {
        // get the id of the checkbox
        int id = view.getId();
        switch (id) {
            case R.id.checkbox_refundable:
                refundable = ((CheckBox) view).isChecked();
                break;
            case R.id.checkbox_nonstop:
                nonstop = ((CheckBox) view).isChecked();
            default:
                break;
        }
    }


    //this is called when "search" is pressed and will transition over to Search Results
    public void transitionToSearch(View view) {

        //Get the inputs
        String countries = countriesAutoComp.getText().toString();
        String cities = citiesEditText.getText().toString();
        String airportCodes = airportEditText.getText().toString();
        String budget = budgetEditText.getText().toString();
        String passengerCount = ticketEditText.getText().toString();
        String maxConnectionDurationinHours = waitTimeEditText.getText().toString();

        //Setting the correct fields in the object in prep for search
        SearchParameterProcessor spp = new SearchParameterProcessor();
        spp.setDepartureDate(departureDateInput);
        spp.setReturningDate(returningDateInput);
        spp.setCountries(countries);
        spp.setBudget(budget.replace("$", ""));
        spp.setPassengerCount(passengerCount);
        spp.setMaxConnectionDuration(maxConnectionDurationinHours);
        spp.setCabin(cabin.toUpperCase());
        spp.setAlliance(alliance.toUpperCase());
        spp.setNonStop(nonstop);
        spp.setRefundable(refundable);
        spp.setAirportCodes(airportCodes);
        spp.setCities(getCityCodes(cities, countries));
        final SearchQuery sq = spp.getQuery();
        String request = SearchQuerytoQPXReader.makeJSONSearchObject(sq);

        new JSONAsyncTask(this.getApplicationContext()).execute(request);
        // Store the SearchQuery in FireBase
        final Firebase usersRef = WelcomeActivity.USER_FIREBASE;
        if (usersRef.getAuth() !=  null) {
            final String uid = usersRef.getAuth().getUid();
            usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    // User has favorites; get this information and replace search results
                    Map<String, Object> userInfo = (Map<String, Object>) snapshot.getValue();
                    if (!userInfo.containsKey("searchQueries")) {
                        ArrayList<Object> queries = new ArrayList<>();
                        userInfo.put("searchQueries", queries);
                    }
                    ArrayList<Object> prevQueries =
                            (ArrayList<Object>) userInfo.get("searchQueries");
                    prevQueries.add(sq.toString());
                    userInfo.put("searchQueries", prevQueries);
                    usersRef.child(uid).updateChildren(userInfo);
                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    // no favorites or some other error
                    String error = getString(R.string.error_no_favorites);
                    (Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT)).show();
                }
            });
        }



        //finish();
        //System.out.println("SearchPage: About to execute request...");


        //Intent intent = new Intent(this, Dashboard.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //this.startActivity(intent);



        /*
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        //no departure date
        if(departureDateInput.equals("")) {
            System.out.println("DATE INPUT");
            String msg = "Please do not leave date fields empty.";
            alertMessage(msg);
            return;
        }

        //valid dates if 2
        if(!returningDateText.getText().toString().equals("")) {
            if (!validDates(departureDateInput, returningDateInput)) {
                String msg = "Please make sure depature date is the same day or before return date" +
                        " and the dates have not passed.";
                alertMessage(msg);
                return;
            }
        }
        String[] arr = countriesAutoComp.getText().toString().trim().split(",");
        if(arr.length == 1 && arr[0] == "") {
            String msg = "Please enter at least one country.";
            alertMessage(msg);
            return;
        }
        String[] arr2 = citiesEditText.getText().toString().trim().split(",");
        if(arr2.length == 1 && arr2[0] == "") {
            String msg = "Please enter at least one city.";
            alertMessage(msg);
            return;
        }*/
    }


    public String getCityCodes(String cities, String countries) {
        String cityCodes = "";
        ArrayList<String> airportCodes = new ArrayList<>();
        try {

            // split the countries into  an  array
            ArrayList<String> cns = new ArrayList<>();
            String[] c = countries.split(",");
            for (int i = 0; i < c.length; i++) {
                cns.add(c[i].trim().replace(" ", "_"));
            }

            // get the arrays of the relevant countries
            ArrayList<String[]> countriesArrays = new ArrayList<>();
            String[] allCities = cities.trim().split(",");
            for (String cn : cns) {

                // get the field of cities in the country
                Field field = R.array.class.getField(cn);
                int resId = field.getInt(null);
                String[] countrysCities = getResources().getStringArray(resId);
                countriesArrays.add(countrysCities);

                for (int i = 0; i < allCities.length; i++) {
                    allCities[i] = allCities[i].trim();
                    for (int j = 0; j < countrysCities.length; j++) {
                        if (countrysCities[j].contains(allCities[i].trim())) {
                            // found a matching city; add the iata
                            int ind = countrysCities[j].indexOf("=") + 1;
                            String iata = countrysCities[j].substring(ind, countrysCities[j].length());
                            airportCodes.add(iata);
                        }
                    }

                }
            }

        } catch (Exception e) {
            Log.e("getCityCodes", e.getMessage());
        }

        // generate the cities string
        for (String iata : airportCodes) {
            cityCodes += iata + " ";
        }
        cityCodes =  cityCodes.trim();

        // log and return the results
        Log.e("getCityCodes", "Final result: " + cityCodes);
        return cityCodes;
    }



    /**
     * @param date - the string represent of the desired date in the format MM-dd-yyyy
     * @return integer array with the integers where [0] = month, [1] = day of month, [2] = year
     */
    private int[] parseDate(String date) {
        int[] dateArr = new int[3];
        String[] dateStrings = date.trim().split("-");
        for (int i = 0; i < 3; i++) {
            dateArr[i] = Integer.parseInt(dateStrings[i]);
        }
        return dateArr;
    }

    /**
     * @param message - the string to display for this alert
     *                Description - This message will invoke an alert with an okay button and the
     *                message on it
     */
    private void alertMessage(String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Invalid Input");
        alert.setMessage(message);
        alert.setPositiveButton("OK", null);
        alert.show();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("SPINNER");
        Spinner classSpinner = (Spinner) findViewById(R.id.class_spinner);
        classSpinner.setOnItemSelectedListener(this);
        cabin = parent.getItemAtPosition(position).toString();
        System.out.println(cabin);
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // Modified by Anaka
    public void DashBoardHistory(View view) {
        //start an intent
        Intent intent = new Intent(this, SearchHistoryWrapper.class);
        startActivity(intent);
    }


    /**
     * Created by anakagold on 4/8/16.
     */
    public class JSONAsyncTask extends AsyncTask<String, Void, JSONArray> {

        Context context;

        public JSONAsyncTask(Context context) {
            this.context = context.getApplicationContext();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JSONArray doInBackground(String... params) {
            try {
                Log.e("SearchPage", "doInBackground()");
                JSONObject json = new JSONObject(params[0]);
                System.out.println(json.toString(4));
                HttpPost httpPost = new HttpPost(HTTP_LINK);
                StringEntity SEJson = new StringEntity(json.toString());
                httpPost.setEntity(SEJson);
                httpPost.setHeader("Content-type", "application/json");
                HttpClient hc = HttpClientBuilder.create().build();
                ResponseHandler<String> rh = new BasicResponseHandler();
                HttpResponse response = hc.execute(httpPost);
                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    JSONObject jsonResponse = new JSONObject(EntityUtils.toString(entity));
                    JSONArray jsonArray =
                            jsonResponse.getJSONObject("trips").getJSONArray("tripOption");

                    return jsonArray;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("SearchPage","OH??");
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            try {
                Log.e("SearchPage", "IN POST EXECUTE");
                QPXJSONReader.getAPIResultsAsFlights(result);
                Log.e("SearchPage", "ASyncTask: IT WORKS!");
                finish();
                if(result != null) {
                    Log.e("SearchPage", "result.toString(2) = " + result.toString(2));
                }

            } catch (JSONException e) {
                Log.e("SearchPage", "ASyncTask: FAILED!");
            }
            Log.e("SearchPage", "ENDING SEARCH PAGE");
        }
    }
}

