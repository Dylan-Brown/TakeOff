package takeoff.cis350.upenn.edu.takeoff.ui.search;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.flight.Flight;
import takeoff.cis350.upenn.edu.takeoff.flight.QPXJSONReader;

/**
 * Class to handle the "Advanced Filter" feature of the dashboard
 */
public class FilterSearch extends FragmentActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    SearchQuery sq;
    private EditText budgetMax;
    private EditText budgetMin;
    private EditText Quantity;
    private SearchParameterProcessor spp = CurrentSearch.getInstance().getCurrentSearch();
    private ArrayList<Flight> flightResult;
    private Flight[] filteredList;
    private String cabin;
    private String alliance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_search);
        flightResult = QPXJSONReader.getFlightResultsFromMostRecentSearch();
        /*for (Flight f : flightResult) {
            System.out.println(f.toString());
        }*/

        filteredList = new Flight[flightResult.size()];

        // Getting info of the budget
        budgetMax = (EditText) findViewById(R.id.max);
        budgetMax.addTextChangedListener(textWatcher1);
        budgetMax.setOnFocusChangeListener(getOnFocusChangeListener());

        budgetMin = (EditText) findViewById(R.id.min);
        budgetMin.addTextChangedListener(textWatcher2);
        budgetMin.setOnFocusChangeListener(getOnFocusChangeListener());

        /*Quantity = (EditText) findViewById(R.id.quantity);
        Quantity.setText("1");
        Quantity.setOnFocusChangeListener(getOnFocusChangeListener());*/
        setClassSpinner();
        setAllianceSpinner();

        //Bundle extras = getIntent().getExtras();
        // TODO: Finish implementing this method
    }

    /**
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        // TODO: Implement
    }

    private void setClassSpinner() {
        //Log.e("ENTER")
        cabin = "";
        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
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

    private void setAllianceSpinner() {
        alliance = "";
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
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

    /**
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("SPINNER");
        Spinner classSpinner = (Spinner) findViewById(R.id.spinner2);
        classSpinner.setOnItemSelectedListener(this);
        cabin = parent.getItemAtPosition(position).toString();
        System.out.println(cabin);
    }

    /**
     *
     * @param adapterView
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // TODO: Implement
    }

    /**
     *
     * @param view
     */
    public void transitionToSearch (View view) {
        boolean check = true;
        double budget = spp.getQuery().maxPrice;
        String bMax = budgetMax.getText().toString().replace("$", "");
        String bMin = budgetMin.getText().toString().replace("$", "");
        Double bmax = Double.parseDouble(bMax);
        Double bmin = Double.parseDouble(bMin);
        //int quantity = Integer.parseInt(Quantity.getText().toString());

        //if (budgetMax.getText().)
        // go to Search
        Log.e("MAX : ", budgetMax.getText().toString());
        Log.e("MIN : ", budgetMin.getText().toString());
        Log.e("Checking spp", "Meh" + spp.getQuery().adultCount);
        Log.e("Size of my Flight List", "is " + flightResult.size());

        // Checking if the inputted budget is valid
        if(budgetMax.getText() == null || budgetMax.getText().toString().equals("")
                || budgetMin.getText() == null || budgetMin.getText().toString().equals("")) {
            Toast.makeText(FilterSearch.this, "Please Enter Valid Budget Value", Toast.LENGTH_SHORT).show();
            check = false;
        }
        else {
            if (bmin > budget || budget < bmax || bmin > bmax) {
                Toast.makeText(FilterSearch.this, "Your initial budget isn't in this price range. "
                        + "Enter valid min and max budget", Toast.LENGTH_SHORT).show();
                check = false;
            }
        }

        // Checking for cabin
        Log.e("Cabin of spp : ", spp.getQuery().preferredCabin);
        spp.setAlliance(alliance.toUpperCase());
        if (spp.getQuery().preferredCabin.toUpperCase().equals("")) {
            spp.setCabin(cabin.toUpperCase());
            Toast.makeText(FilterSearch.this, "CABIN CHANGED", Toast.LENGTH_SHORT).show();
        }
        int  i = 0;
        // We only have to add if
        if (check) {
            for (Flight f : flightResult) {
                //if the price of this flight isn't in our range, we delte it
                if (f.getCost() > bmin && f.getCost() < bmax) {
                    Log.e("Cost of flight added: ", "" +f.getCost());
                    filteredList[i] = f;
                    i++;
                }
            }


            // get the Dashboard fragmetn
            String tag = getString(R.string.dashboard_tag);
            Dashboard dash;
            if ((dash = (Dashboard) this.getSupportFragmentManager().findFragmentByTag(tag)) != null) {
                dash.filterAdapter(filteredList);
            }
            // call the method
            finish();
        }
    }


    // Extra classes/methods

    /**
     * Description: A TextWatcher to handle parsing changes in the
     * budget textfield
     */
    private final TextWatcher textWatcher1 = new TextWatcher() {

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

                budgetMax.removeTextChangedListener(this);
                budgetMax.setText(cashAmountBuilder.toString());

                budgetMax.setTextKeepState("$" + cashAmountBuilder.toString());
                Selection.setSelection(budgetMax.getText(), cashAmountBuilder.toString().length() + 1);

                budgetMax.addTextChangedListener(this);
            }
        }
    };

    /**
     * Description: A TextWatcher to handle parsing changes in the
     * budget textfield
     */
    private final TextWatcher textWatcher2 = new TextWatcher() {

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

                budgetMin.removeTextChangedListener(this);
                budgetMin.setText(cashAmountBuilder.toString());

                budgetMin.setTextKeepState("$" + cashAmountBuilder.toString());
                Selection.setSelection(budgetMin.getText(), cashAmountBuilder.toString().length() + 1);

                budgetMin.addTextChangedListener(this);
            }
        }
    };

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
}
