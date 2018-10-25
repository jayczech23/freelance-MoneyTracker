package org.assignment.ctse.moneytracker.Async;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.assignment.ctse.moneytracker.BorrowerDetailsActivity;
import org.assignment.ctse.moneytracker.Database.DatabaseHelper;
import org.assignment.ctse.moneytracker.Model.Borrower;
import org.assignment.ctse.moneytracker.View.BorrowerAdapter;

import java.util.ArrayList;
import java.util.List;

public class RetrieveBorrowersTask extends AsyncTask<Object, Void, ArrayList<Borrower>>  {

    private String TAG = this.getClass().getSimpleName();

    ListView _listViewRef;
    ProgressDialog progressDialog;
    private Context mContext;
    ArrayList<Borrower> borrowersBuffer = new ArrayList<Borrower>();

    public RetrieveBorrowersTask(Context ctx, ListView listViewRef) {
        this.mContext = ctx;
        this._listViewRef = listViewRef;
        progressDialog = new ProgressDialog(mContext);
  }

    @Override
    protected void onPreExecute() {
        progressDialog.setTitle("Retrieving Borrowers");
        progressDialog.show();
    }

    @Override
    protected ArrayList<Borrower> doInBackground(Object... objects) {

        // Retrieve List of borrowers here and return
        try {
            retrieveAllBorrowersFromDB();
        } catch (Exception e) {
            Log.d(TAG, "ERROR Retrieving Borrowers Async: " + e);
            Toast.makeText(mContext, "Borrower not Found", Toast.LENGTH_SHORT).show();
        }
        return this.borrowersBuffer;
    }

    @Override
    protected void onPostExecute(ArrayList<Borrower> borrowers) {

        // TODO: Create adapters here
        BorrowerAdapter borrowerAdapter = new BorrowerAdapter(this.mContext,
                this.borrowersBuffer);
        _listViewRef.setAdapter(borrowerAdapter);
        progressDialog.dismiss();
    }

    /**
     * Get all the borrowers from the DB
     *
     * @throws Exception: thrown if error occurs when querying database
     */
    private void retrieveAllBorrowersFromDB() throws Exception {

        Log.d(TAG, "Retrieving Borrowers from Database");
        try {
            DatabaseHelper db = new DatabaseHelper(mContext);
            this.borrowersBuffer = db.getAllBorrowers();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
