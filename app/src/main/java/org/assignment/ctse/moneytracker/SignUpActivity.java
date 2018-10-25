package org.assignment.ctse.moneytracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.assignment.ctse.moneytracker.Database.DatabaseHelper;
import org.assignment.ctse.moneytracker.Model.CurrentUser;
import org.assignment.ctse.moneytracker.R;

public class SignUpActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button signInButton = findViewById(R.id.sign_in_button);
        final EditText usernameEdit = findViewById(R.id.username_input);
        EditText passwordEdit = findViewById(R.id.password_input);
        EditText confirmPassEdit = findViewById(R.id.confirm_password_input);

        final EditText[] passFields = {passwordEdit,confirmPassEdit};

        // SignIn action
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Signing in with new user.");
                String username = usernameEdit.getText().toString();

                // verify username is populated
                if (username.matches("")) {
                    Log.d(TAG, "Username cannot be empty");
                    Toast.makeText(SignUpActivity.this,"Please enter a Username",Toast.LENGTH_LONG).show();
                    return;
                }

                // verify password fields
                try {
                    if (isPasswordConfirmed(passFields)) {
                        Log.d(TAG, "Password confirmed");
                    } else {
                        Log.d(TAG, "Passwords don't match");
                        Toast.makeText(SignUpActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Log.d(TAG, "Error entering password: "+ e);
                    Toast.makeText(SignUpActivity.this,"Please enter a password and Confirmation", Toast.LENGTH_SHORT).show();
                    return;
                }

                // query database, and save User
                try {
                    saveNewUserToDB(username, passFields);
                } catch (Exception e) {
                    Log.d(TAG, "Error Saving New User to DB: " + e);
                    showToast("Could not save new user, please try again");
                    return;
                    }

                Intent intent = new Intent(SignUpActivity.this, BorrowersListActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }

    /**
     * Confirm passwords are the same
     *
     * @param passwordFields: fields to confirm
     * @return: bool specifying if password confirmed
     * @throws Exception: thrown if something error confirming password
     */
    private boolean isPasswordConfirmed(EditText[] passwordFields) throws Exception {

        String pass = passwordFields[0].getText().toString();
        String confPass = passwordFields[1].getText().toString();

        if (pass.matches("") || confPass.matches("")) {
            throw new Exception("A Password field is empty");
        }

        if (pass.equals(confPass)) {
            Log.d(TAG, "Password confirmed");
            return true;
        }
        return false;
    }

    /**
     * Create new user in database.
     *
     * @param username: User's name
     * @param fields: fields for password
     * @throws Exception: thrown if error saving to database
     */
    private void saveNewUserToDB(String username, EditText[] fields) throws Exception {

        String password = fields[0].getText().toString();
        DatabaseHelper db = new DatabaseHelper(SignUpActivity.this);
        CurrentUser newUser = new CurrentUser(username, password);

        try {
            db.getWritableDatabase();
            db.createNewUser(newUser);
        } catch (Exception e) {
            throw new Exception("Error saving user to database: " + e);
        }
    }

    private void showToast(String message) {
        Toast.makeText(SignUpActivity.this, message,Toast.LENGTH_SHORT).show();
    }
}
