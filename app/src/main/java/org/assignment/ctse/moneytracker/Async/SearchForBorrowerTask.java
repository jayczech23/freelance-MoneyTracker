package org.assignment.ctse.moneytracker.Async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.assignment.ctse.moneytracker.Database.DatabaseHelper;
import org.assignment.ctse.moneytracker.Model.Borrower;
import org.assignment.ctse.moneytracker.View.BorrowerAdapter;

import java.util.ArrayList;

/**
 * Async Task to retrieve searched
 * borrower and adjust Listview
 */
public class SearchForBorrowerTask extends AsyncTask<Object, Void, ArrayList<Borrower>> {

    private String TAG = this.getClass().getSimpleName();
    private String _searchString = "";

    ListView _listViewRef;
    ProgressDialog dialog;
    private Context mContext;
    ArrayList<Borrower> searchedBorrowerList = new ArrayList<Borrower>();

    public SearchForBorrowerTask(Context ctx, String searchString, ListView listViewRef) {
        this.mContext = ctx;
        this._listViewRef = listViewRef;
        this._searchString = searchString;
        dialog = new ProgressDialog(mContext);
    }

    @Override
    protected void onPreExecute() {
        dialog.setTitle("Searching for Borrower");
        dialog.show();
    }

    @Override
    protected ArrayList<Borrower> doInBackground(Object... objects) {

        // Retrieve list
        try {
            retrieveSearchedBorrowersFromDB();
        } catch (Exception e) {
            Log.d(TAG,"Error in search: " + e);
            try {
                DatabaseHelper db = new DatabaseHelper(mContext);
                // return all borrowers
                this.searchedBorrowerList = db.getAllBorrowers();
                Toast.makeText(mContext, "Borrower not found", Toast.LENGTH_SHORT).show();
            } catch (Exception err) {
                Log.e(TAG, err.toString());
            }
        }
        return this.searchedBorrowerList;
    }

    @Override
    protected void onPostExecute(ArrayList<Borrower> borrowers) {
        BorrowerAdapter borrowerAdapter = new BorrowerAdapter(this.mContext, this.searchedBorrowerList);
        _listViewRef.setAdapter(borrowerAdapter);
        dialog.dismiss();
    }


    /**
     * Retrieve list of borrowers from
     * the database
     */
    private void retrieveSearchedBorrowersFromDB() {

        Log.d(TAG, "Retrieving Searched Borrowers from DB");
        try {
            DatabaseHelper db = new DatabaseHelper(mContext);
            this.searchedBorrowerList = db.searchBorrower(this._searchString);

        } catch (Exception e) {
            Log.d(TAG, "Could not retrieve borrowers: " + e);
            Toast.makeText(mContext, "Borrower not found", Toast.LENGTH_SHORT).show();
        }
    }
}
