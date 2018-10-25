package org.assignment.ctse.moneytracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.assignment.ctse.moneytracker.Database.DatabaseHelper;
import org.assignment.ctse.moneytracker.Model.CurrentUser;
import org.assignment.ctse.moneytracker.R;

public class SignInActivity extends AppCompatActivity {

    String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        final EditText userNameEdit = findViewById(R.id.username_edit);
        final EditText passwordEdit = findViewById(R.id.password_edit);
        Button loginButton = findViewById(R.id.login_btn);
        TextView signUpLink = findViewById(R.id.signup_button);

        final EditText[] userFields = {userNameEdit,passwordEdit};

        // login button action
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // verify values in fields
                try {
                    verifyFieldsPopulated(userFields);
                } catch (Exception e) {
                    Log.d(TAG, "Exception thrown when trying to login: "+ e);
                    Toast.makeText(SignInActivity.this,
                            "Please enter a Username and Password, or Sign-In",
                            Toast.LENGTH_SHORT).show();
                }

                // attempt to login existing user
                String username = userNameEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                try {
                    attemptToLoginExistingUser(username,password);
                    // If a user is found, show main activity
                    Intent intent = new Intent(SignInActivity.this, BorrowersListActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    Log.d(TAG, "Exception thrown when trying to LOGIN: " + e);
                    Toast.makeText(SignInActivity.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                }

                // clear fields after attempt
                clearFields(userFields);
            }
        });

        // sign-in button action
        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Navigating to create new user (Sign-up)");
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Verify the required fields are
     * populated
     *
     * @param fields: Username and password EditText elements
     * @throws Exception: thrown if one of the fields are empty
     */
    private void verifyFieldsPopulated(EditText[] fields) throws Exception {

        // make sure fields are populated
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getText().toString().matches("")) {
                throw new Exception(" Field cannot be empty: " + fields[i].getId());
            }
        }
    }

    /**
     * Attempt to login user with current credentials
     * Check database to verify
     *
     * @param username: entered username
     * @param password: entered password
     * @throws Exception: thrown when user is not found or password is not correct
     */
    private void attemptToLoginExistingUser(String username, String password) throws Exception {

        DatabaseHelper db = new DatabaseHelper(SignInActivity.this);

        try {
            db.getCurrentUser(username,password);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Clear fields for
     * UI update.
     *
     * @param fields
     */
    private void clearFields(EditText[] fields) {
        for (int i=0; i < fields.length; i++) {
            fields[i].setText("");
        }
    }
}