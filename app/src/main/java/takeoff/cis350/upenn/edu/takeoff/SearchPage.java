package takeoff.cis350.upenn.edu.takeoff;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class SearchPage extends Activity implements OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText departureDateText;
    private EditText arrivalDateText;
    private String departureDateInput;
    private String arrivalDateInput;
    private DatePickerDialog departureDatePickerDialog;
    private DatePickerDialog arrivalDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    private MultiAutoCompleteTextView countriesAutoComp;
    private EditText citiesEditText;
    private EditText budgetEditText;
    private EditText connectingEditText;

    private int day;
    private int month;
    private int year;

    //Represents which classes the user wants in the folloring order:
    //Economy, Business, First Class
    private String flightClass;
    private boolean refundable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        //Current Date
        Calendar newCalendar = Calendar.getInstance();
        day = newCalendar.get(Calendar.DAY_OF_MONTH);
        month = newCalendar.get(Calendar.MONTH)+1;
        year = newCalendar.get(Calendar.YEAR);
        System.out.println("day: " + day + " month: " + month + " year: " + year);

        //Dates
        departureDateInput = "";
        arrivalDateInput = "";
        dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        findDateViews();
        setDateField();

        //Making the autocomplete Text
        countriesAutoComp=(MultiAutoCompleteTextView)findViewById(R.id.autocomplete_country);
        String[] countryArray = getResources().getStringArray(R.array.countries_array);
        countriesAutoComp.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        countriesAutoComp.setOnFocusChangeListener(getOnFocusChangeListener());
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countryArray);
        countriesAutoComp.setAdapter(adapter);

        //Cities
        citiesEditText = (EditText) findViewById(R.id.city_input);
        citiesEditText.setOnFocusChangeListener(getOnFocusChangeListener());

        //Connecting Flights
        connectingEditText= (EditText) findViewById(R.id.connecting_flight_input);
        connectingEditText.setOnFocusChangeListener(getOnFocusChangeListener());

        //Budget
        budgetEditText = (EditText) findViewById(R.id.budget_input);
        budgetEditText.addTextChangedListener(textWatcher);
        budgetEditText.setOnFocusChangeListener(getOnFocusChangeListener());

        //Classes Spinner
        setClassSpinner();

        refundable = false;
    }

    public View.OnFocusChangeListener getOnFocusChangeListener(){
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
        flightClass = "";

        Spinner spinner = (Spinner) findViewById(R.id.class_spinner);
        spinner.setOnItemSelectedListener(this);

        List<String> list = new ArrayList<String>();
        list.add("Select Class");
        list.add("Coach");
        list.add("Premium Coach");
        list.add("Business");
        list.add("First");

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
        if(v.getId() == departureDateText.getId()) {
            departureDatePickerDialog.show();
        }
        /*
        else if(v.getId() == arrivalDateText.getId()) {
            arrivalDatePickerDialog.show();
        }*/
        else {
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
     * @param view
     * Description: When the focus is not in this designated view, the onScreen Keyboard disappears
     * Called by the Countries, Cities, and Budget EditText View
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * Description: This method is used to find the correct EditText views for the respective
     * departure and arrival dates.
     */
    public void findDateViews() {
        departureDateText = (EditText) findViewById(R.id.departure_date);
        departureDateText.setInputType(InputType.TYPE_NULL);
        departureDateText.requestFocus();

        //arrivalDateText = (EditText) findViewById(R.id.arrival_date);
        System.out.println("HERE");
        //arrivalDateText.setInputType(InputType.TYPE_CLASS_NUMBER);
        System.out.println("FINDDATEVIEWS");
    }

    private void setDateField() {
        departureDateText.setOnClickListener(this);
        //rrivalDateText.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        departureDatePickerDialog = new DatePickerDialog(this, getDateSetListener(true),
                newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        //arrivalDatePickerDialog = new DatePickerDialog(this, getDateSetListener(false),
                //newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        System.out.println("SETDATEFIELD");
    }


    /**
     *
     * @param departure
     * @return an OnDateSetListener for one of the Date EditTexts
     * Description: Handles when the user clicks to enter dates and how to change the strings
     */
    public OnDateSetListener getDateSetListener(boolean departure){
        OnDateSetListener listener = null;
        if(departure){
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
        } /*else {
            listener = new OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    arrivalDateText.setText(dateFormatter.format(newDate.getTime()));
                    arrivalDateInput = dateFormatter.format(newDate.getTime());
                    System.out.println("ARRIVAL LISTENER");
                    System.out.println(arrivalDateInput);;
                }
            };
        }*/
        System.out.println("GETLISTENER");
        return listener;
    }


    /**
     *
     * @param view
     * Description: Handles which checkboxes are clicked
     */
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        int id = view.getId();
        switch (id) {
            case R.id.checkbox_refundable:
                refundable = ((CheckBox)view).isChecked();
                break;
            default:
                break;
        }

    }



    //this is called when "search" is pressed and will transition over to Search Results
    public void transitionToSearch(View view) {
        System.out.println("DEPARTURE SEARCH");
        System.out.println(departureDateInput);
        //System.out.println("ARRIVAL SEARCH");
        //System.out.println(arrivalDateInput);
        System.out.println("COUNTRIES AUTO COMPLETE");
        System.out.println(countriesAutoComp.getText().toString());
        System.out.println("CITIES INPUT");
        System.out.println(citiesEditText.getText().toString());
        System.out.println("BUDGET: " + budgetEditText.getText().toString());
        System.out.println("CONNECTING FLIGHT: " + connectingEditText.getText().toString());
        System.out.println("REFUNDABLE: " + refundable);
        System.out.println("SPINNER CLASS: " + flightClass);


        /*AlertDialog.Builder alert = new AlertDialog.Builder(this);

        if(departureDateInput.equals("") || arrivalDateInput.equals("")) {
            System.out.println("DATE INPUT");
            String msg = "Please do not leave date fields empty.";
            alertMessage(msg);
            return;
        }
        if(!validDates(departureDateInput, arrivalDateInput)){
            String msg = "Please make sure depature date is the same day or before arrival date" +
                    " and the dates have not passed.";
            alertMessage(msg);
            return;
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
        }
        String b = budgetEditText.getText().toString();
        System.out.println("B: " + b.substring(1, b.length()));
        double budget = Double.parseDouble(b.substring(1, b.length()));
        System.out.println("B: " + budget);
        if(budget <= 0) {
            String msg = "Please enter a buget.";
            alertMessage(msg);
            return;
        }*/
    }

    /**
     *
     * @param departure - string representing departure date in the format MM-dd-yyyy
     * @param arrival - string representing arrival date in the format MM-dd-yyyy
     * @return boolean indicating whether the departure and arrival dates are valid inputs
     */
    private boolean validDates(String departure, String arrival) {
        int[] departureArr = parseDate(departure);
        int[] arrivalArr = parseDate(arrival);
        //entered in years that have passed
        if(departureArr[2] < this.year || arrivalArr[2] < this.year) {
            return false;
        }
        //departure year after arrival year
        if(departureArr[2] > arrivalArr[2]) {
            return false;
        }
        //same year
        else if(departureArr[2] == arrivalArr[2]) {
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
                if(departureArr[0] == this.month &&
                        (departureArr[1] < this.day || arrivalArr[1] < this.day)) {
                    return false;
                }
                //departure day after arrival day
                if(departureArr[1] > arrivalArr[1]) {
                    return false;
                }
                return true;
            }
        }
        else {
            return true;
        }
        return true;
    }

    /**
     *
     * @param date - the string represent of the desired date in the format MM-dd-yyyy
     * @return integer array with the integers where [0] = month, [1] = day of month, [2] = year
     */
    private int[] parseDate(String date){
        int[] dateArr = new int[3];
        String[] dateStrings = date.trim().split("-");
        for(int i = 0; i < 3; i++) {
            dateArr[i] = Integer.parseInt(dateStrings[i]);
        }
        return dateArr;
    }

    /**
     *
     * @param message - the string to display for this alert
     * Description - This message will invoke an alert with an okay button and the
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
        System.out.println("ONITEMSELECTED");
        System.out.println("SPINNER");
        Spinner classSpinner = (Spinner) findViewById(R.id.class_spinner);
        classSpinner.setOnItemSelectedListener(this);
        flightClass = parent.getItemAtPosition(position).toString();

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
