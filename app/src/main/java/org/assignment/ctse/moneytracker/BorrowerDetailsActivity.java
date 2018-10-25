package org.assignment.ctse.moneytracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.assignment.ctse.moneytracker.Database.DatabaseHelper;

public class BorrowerDetailsActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrower_details);

        final String borrowerName = getIntent().getStringExtra("borrower_name");
        final String amountOwed = getIntent().getStringExtra("amount_owed");
        final String date = getIntent().getStringExtra("date");

        TextView borrowerNameTV = findViewById(R.id.borrower_name_tv);
        TextView amountOwedTV = findViewById(R.id.amount_owed_tv);
        TextView dateTV = findViewById(R.id.date_tv);
        Button backButton = findViewById(R.id.back_button);
        Button paidButton = findViewById(R.id.paid_button);

        borrowerNameTV.setText(borrowerName);
        amountOwedTV.setText(amountOwed);
        dateTV.setText(date);

        // Back action
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        paidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Removing Borrower from DB");
                try {
                    markBorrowerPaid(borrowerName,amountOwed,date);
                } catch (Exception e) {
                    Log.d(TAG, "Error removing Borrower from DB: " + e);
                }
                finish();
            }
        });
    }

    private void markBorrowerPaid(String name, String amount, String date) throws Exception {

        Log.d(TAG, "Removing " + name + " from database");

        try {
            DatabaseHelper db = new DatabaseHelper(this);
            db.removeBorrower(name,amount,date);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
