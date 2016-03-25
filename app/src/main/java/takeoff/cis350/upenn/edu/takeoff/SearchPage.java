package takeoff.cis350.upenn.edu.takeoff;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class SearchPage extends Activity implements OnClickListener, AdapterView.OnItemSelectedListener {
    private String APIKey="AIzaSyB_4Rk4qn5CajLsU7T3Y_K9Sc3m6gFVa_w";
    private EditText departureDateText;
    private EditText returningDateText;
    private String departureDateInput;
    private String returningDateInput;
    private DatePickerDialog departureDatePickerDialog;
    private DatePickerDialog returningDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    private MultiAutoCompleteTextView countriesAutoComp;
    private EditText citiesEditText;
    private EditText budgetEditText;
    private EditText ticketEditText;
    private EditText airportEditText;
    private EditText waitTimeEditText;

    private int day;
    private int month;
    private int year;

    private String cabin;
    private String alliance;
    private boolean refundable;
    private boolean nonstop; //T = 0, F = 5

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Here!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        //Current Date
        Calendar newCalendar = Calendar.getInstance();
        day = newCalendar.get(Calendar.DAY_OF_MONTH);
        month = newCalendar.get(Calendar.MONTH) + 1;
        year = newCalendar.get(Calendar.YEAR);
        System.out.println("day: " + day + " month: " + month + " year: " + year);

        //Dates
        departureDateInput = "";
        returningDateInput = "";
        dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        findDateViews();
        setDateField();

        //Making the autocomplete Text
        countriesAutoComp = (MultiAutoCompleteTextView) findViewById(R.id.autocomplete_country);
        String[] countryArray = getResources().getStringArray(R.array.countries_array);
        countriesAutoComp.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        countriesAutoComp.setOnFocusChangeListener(getOnFocusChangeListener());
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countryArray);
        countriesAutoComp.setAdapter(adapter);

        //Cities
        citiesEditText = (EditText) findViewById(R.id.city_input);
        citiesEditText.setOnFocusChangeListener(getOnFocusChangeListener());

        airportEditText = (EditText) findViewById(R.id.airport_input);
        airportEditText.setOnFocusChangeListener(getOnFocusChangeListener());

        waitTimeEditText = (EditText) findViewById(R.id.wait_time_input);
        waitTimeEditText.setOnFocusChangeListener(getOnFocusChangeListener());

        //Budget
        budgetEditText = (EditText) findViewById(R.id.budget_input);
        budgetEditText.addTextChangedListener(textWatcher);
        budgetEditText.setOnFocusChangeListener(getOnFocusChangeListener());
        ticketEditText = (EditText) findViewById(R.id.num_ticket_input);
        ticketEditText.setText("1");
        ticketEditText.setOnFocusChangeListener(getOnFocusChangeListener());

        //Classes Spinner
        setClassSpinner();
        setAllianceSpinner();

        refundable = false;
        nonstop = false;
    }

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

    private void setClassSpinner() {
        cabin = "";

        Spinner spinner = (Spinner) findViewById(R.id.class_spinner);
        spinner.setOnItemSelectedListener(this);

        List<String> list = new ArrayList<String>();
        list.add("None");
        list.add("Coach");
        list.add("Premium Coach");
        list.add("Business");
        list.add("First");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setAllianceSpinner() {
        alliance = "";

        Spinner spinner = (Spinner) findViewById(R.id.alliance_spinner);
        spinner.setOnItemSelectedListener(this);

        List<String> list = new ArrayList<String>();
        list.add("None");
        list.add("Oneworld");
        list.add("Star Alliance");
        list.add("SkyTeam");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
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
     * Description: A TextWatcher to handle parsing changes in the TextFields
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
     * @param view Description: When the focus is not in this designated view, the onScreen Keyboard disappears
     *             Called by the Countries, Cities, and Budget EditText View
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * Description: This method is used to find the correct EditText views for the respective
     * departure and return dates.
     */
    public void findDateViews() {
        departureDateText = (EditText) findViewById(R.id.departure_date);
        departureDateText.setInputType(InputType.TYPE_NULL);
        departureDateText.requestFocus();

        returningDateText = (EditText) findViewById(R.id.returning_date);
        returningDateText.setInputType(InputType.TYPE_NULL);

    }

    private void setDateField() {
        departureDateText.setOnClickListener(this);
        returningDateText.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        departureDatePickerDialog = new DatePickerDialog(this, getDateSetListener(true),
                newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        returningDatePickerDialog = new DatePickerDialog(this, getDateSetListener(false),
                newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        System.out.println("SETDATEFIELD");
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

                    System.out.println("DEPARTURE LISTENER");
                    System.out.println(departureDateInput);
                }
            };
        } else {
            listener = new OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    returningDateText.setText(dateFormatter.format(newDate.getTime()));
                    returningDateInput = dateFormatter.format(newDate.getTime());
                    System.out.println("returning LISTENER");
                    System.out.println(returningDateInput);
                }
            };
        }
        System.out.println("GETLISTENER");
        return listener;
    }


    /**
     * @param view Description: Handles which checkboxes are clicked
     */
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
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


        String countries = countriesAutoComp.getText().toString();
        String cities = citiesEditText.getText().toString();
        String airportCodes = airportEditText.getText().toString();
        String budget = budgetEditText.getText().toString();
        String passengerCount = ticketEditText.getText().toString();
        String maxConnectionDurationinHours = waitTimeEditText.getText().toString();

        SearchParameterProcessor spp = new SearchParameterProcessor();
        spp.setDepartureDate(departureDateInput);
        spp.setReturningDate(returningDateInput);
        spp.setCountries(countries);
        spp.setCities(cities);
        spp.setBudget(budget.replace("$", ""));
        spp.setPassengerCount(passengerCount);
        spp.setMaxConnectionDuration(maxConnectionDurationinHours);
        spp.setCabin(cabin.toUpperCase());
        spp.setAlliance(alliance.toUpperCase());
        spp.setNonStop(nonstop);
        spp.setRefundable(refundable);
        spp.setairportCodes(airportCodes);
        SearchQuery sq = spp.getQuery();                                                                  // This SearchQuery needs to be passed to FireBase
        String request = QPXAPIReader.makeJSONObjectFromSearchQuery(sq);
       /* try {
            QPXAPIReader.executeAPIRequest(sq, "AIzaSyB_4Rk4qn5CajLsU7T3Y_K9Sc3m6gFVa_w", this);
        } catch (Exception e) {
        }
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);*/
        System.out.println("SearchPage: About to execute request...");
        new JSONAsyncTask(this.getApplicationContext()).execute(request);
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

    /**
     * @param departure - string representing departure date in the format MM-dd-yyyy
     * @param returning - string representing return date in the format MM-dd-yyyy
     * @return boolean indicating whether the departure and return dates are valid inputs
     */
    private boolean validDates(String departure, String returning) {
        int[] departureArr = parseDate(departure);
        int[] arrivalArr = parseDate(returning);
        //entered in years that have passed
        if (departureArr[2] < this.year || arrivalArr[2] < this.year) {
            return false;
        }
        //departure year after arrival year
        if (departureArr[2] > arrivalArr[2]) {
            return false;
        }
        //same year
        else if (departureArr[2] == arrivalArr[2]) {
            //entered in months that have passed in this year
            if (departureArr[2] == this.year &&
                    (departureArr[0] < this.month || arrivalArr[0] < this.month)) {
                return false;
            }

            //departure month after arrival month
            if (departureArr[0] > arrivalArr[0]) {
                return false;
            }

            //same month, same year
            else if (departureArr[0] == arrivalArr[0]) {

                //entered in days that have passed for this month
                if (departureArr[0] == this.month &&
                        (departureArr[1] < this.day || arrivalArr[1] < this.day)) {
                    return false;
                }
                //departure day after arrival day
                if (departureArr[1] > arrivalArr[1]) {
                    return false;
                }
                return true;
            }
        } else {
            return true;
        }
        return true;
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
        Intent intent = new Intent(this, DashBoardSearchHistory.class);                                 //Give me the last 20 searches from FireBase
        startActivity(intent);
    }


    private class JSONAsyncTask extends AsyncTask<String, Void, JSONArray> {
        ProgressDialog pd;
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
                JSONObject json = new JSONObject(params[0]);
                System.out.println(json.toString(4));
                HttpPost httpPost = new HttpPost("https://www.googleapis.com/qpxExpress/v1/trips/search?key="+APIKey);
                StringEntity SEJson = new StringEntity(json.toString());
                httpPost.setEntity(SEJson);
                httpPost.setHeader("Content-type", "application/json");
                HttpClient hc = HttpClientBuilder.create().build();
                ResponseHandler<String> rh = new BasicResponseHandler();
                HttpResponse response = hc.execute(httpPost);
                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();
                System.out.println("ATTEMPT??");

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    JSONObject jsonResponse = new JSONObject(EntityUtils.toString(entity));
                    JSONArray jsonArray = jsonResponse.getJSONObject("trips").getJSONArray("tripOption");

                    return jsonArray;
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... progress) {
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            try {

                QPXAPIParser.getAPIResultsAsFlight(result);
                System.out.println("ASyncTask: IT WORKS!");
                System.out.println(result.toString(2));
            } catch (JSONException e) {
            }
            Intent intent = new Intent(context, Dashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}