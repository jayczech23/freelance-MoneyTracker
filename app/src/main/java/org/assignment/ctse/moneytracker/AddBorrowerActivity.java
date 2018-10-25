package org.assignment.ctse.moneytracker;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.assignment.ctse.moneytracker.Database.DatabaseHelper;
import org.assignment.ctse.moneytracker.Model.Borrower;
import org.assignment.ctse.moneytracker.Model.MyApp;
import org.assignment.ctse.moneytracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddBorrowerActivity extends AppCompatActivity {

    String TAG = this.getClass().getSimpleName();
    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_borrower);

        EditText borrowerNameEdit = findViewById(R.id.borrower_input);
        EditText amountEdit = findViewById(R.id.amount_input);
        final EditText dateEdit = findViewById(R.id.date_input);
        Button addBorrowerButton = findViewById(R.id.confirm_add_borrower_btn);

        final EditText[] fields = {borrowerNameEdit, amountEdit, dateEdit};

        // configure date edit to auto populate today's date
        dateEdit.setHint("Ex: 01/02/2017");

        // confirm add borrower action
        addBorrowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    verifyFields(fields);
                    addBorrowerToDatabase(fields);
                    Toast.makeText(AddBorrowerActivity.this, "Borrower Added", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (Exception e) {
                    Log.d(TAG, "Exception thrown when adding borrower: " + e);
                    makeToast("Please populate all fields.");
                }
            }
        });

        // set Date Picker Listener
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel(dateEdit);
            }
        };

        // date field action (Show Date Picker)
        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Show Date Picker");
                new DatePickerDialog(AddBorrowerActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    void verifyFields(EditText[] fields) throws Exception {

        Log.d(TAG, "Verifying fields for adding new borrower");

        for(int i=0; i < fields.length; i++) {

            if (fields[i].getText().toString().matches("")) {
                throw new Exception("ERROR: All fields must be populated");
            }
        }
    }

    private void updateDateLabel(EditText dateEdit) {
        String format = "MM/dd/yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        dateEdit.setText(simpleDateFormat.format(myCalendar.getTime()));
    }

    void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    void addBorrowerToDatabase(EditText[] fields) throws Exception {

        DatabaseHelper db = new DatabaseHelper(this);

        String borrowerName = fields[0].getText().toString();
        String amount = fields[1].getText().toString();
        String date = fields[2].getText().toString();
        String owner = MyApp.getCurrentUser().getUsername();

        Borrower newBorrower = new Borrower(borrowerName,amount,date, owner);

        Log.d(TAG, "Attempting to add Borrower to database: " + newBorrower.getName());
        try {
            db.createNewBorrower(newBorrower);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
