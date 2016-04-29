package takeoff.cis350.upenn.edu.takeoff.ui.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import takeoff.cis350.upenn.edu.takeoff.R;
import takeoff.cis350.upenn.edu.takeoff.ui.search.Dashboard;
import takeoff.cis350.upenn.edu.takeoff.ui.search.SearchQuery;

/**
 * Class to handle the "Advanced Filter" feature of the dashboard
 */
public class FilterSearch extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    SearchQuery sq;
    private EditText budgetMax;
    private EditText budgetMin;
    private EditText Quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        // Getting info of the budget
        budgetMax = (EditText) findViewById(R.id.max);
        budgetMax.addTextChangedListener(textWatcher1);
        budgetMax.setOnFocusChangeListener(getOnFocusChangeListener());

        budgetMin = (EditText) findViewById(R.id.min);
        budgetMin.addTextChangedListener(textWatcher2);
        budgetMin.setOnFocusChangeListener(getOnFocusChangeListener());

        Quantity = (EditText) findViewById(R.id.quantity);
        Quantity.setText("1");
        Quantity.setOnFocusChangeListener(getOnFocusChangeListener());


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

    /**
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // TODO: Implement
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

        //if (budgetMax.getText().)
        // go to Search
        Log.e("Inside transition", "here");
        Log.e("MAX : ", budgetMax.getText().toString());
        Log.e("MIN : ", budgetMin.getText().toString());
        Log.e("Inside transition", "here");
        Toast.makeText(FilterSearch.this, "Dylan Help. Need to get current search Query", Toast.LENGTH_SHORT).show();
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
