package org.assignment.ctse.moneytracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.assignment.ctse.moneytracker.Async.RetrieveBorrowersTask;
import org.assignment.ctse.moneytracker.Async.SearchForBorrowerTask;
import org.assignment.ctse.moneytracker.Database.DatabaseHelper;
import org.assignment.ctse.moneytracker.Model.Borrower;
import org.assignment.ctse.moneytracker.R;

import java.util.ArrayList;

public class BorrowersListActivity extends AppCompatActivity {

    ListView borrowersListView;

    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onResume() {
        super.onResume();

        // retrieve list of all borrowers to be used to populate ListView
        borrowersListView = findViewById(R.id.borrowers_listview);
        new RetrieveBorrowersTask(this,this.borrowersListView).execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_borrowers_list);

        EditText searchEdit = findViewById(R.id.search_edit);
        FloatingActionButton addButton = findViewById(R.id.add_borrower_fab);
        Button logoutButton = findViewById(R.id.logout_button);

        // set 'Enter' for keyboard to 'Search' when editing
        searchEdit.setImeActionLabel("Search", KeyEvent.KEYCODE_ENTER);

        // logout action
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BorrowersListActivity.this, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        // search action
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView searchEdit, int actionId, KeyEvent keyEvent) {

                Log.d(TAG, "actionId: " + actionId);

                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == 66) {

                    // perform search
                    try {
                        search(searchEdit.getText().toString());
                    } catch (Exception e) {
                        Log.d(TAG,"Search failed with exception: " + e);
                        Toast.makeText(BorrowersListActivity.this, "Borrower Not found", Toast.LENGTH_SHORT).show();
                    }
                    searchEdit.setText("");
                }
                return true;
            }
        });

        // add borrower action
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show add borrower activity
                Intent intent = new Intent(BorrowersListActivity.this, AddBorrowerActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Search for specific borrower with given
     * search string
     *
     * @param searchString: borrower to search for
     * @throws Exception
     */
    private void search(String searchString) throws Exception {

        Log.d(TAG, "Performing Search with searchString: " + searchString);

        try {
            new SearchForBorrowerTask(this,searchString,this.borrowersListView).execute();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
