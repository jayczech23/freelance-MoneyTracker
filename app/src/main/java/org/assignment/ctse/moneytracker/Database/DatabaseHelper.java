package org.assignment.ctse.moneytracker.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.assignment.ctse.moneytracker.Model.Borrower;
import org.assignment.ctse.moneytracker.Model.CurrentUser;
import org.assignment.ctse.moneytracker.Model.MyApp;

import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {

    private String TAG = this.getClass().getSimpleName();
    private static Context appContext;
    private static final String DATABASE_NAME = "Tracker.sqlite";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERS = "USERS";
    private static final String TABLE_BORROWERS = "BORROWERS";
    private final static String KEY_ID = "id";

    // columns for USERS table
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    // columns for BORROWERS table
    private static final String KEY_OWNER_NAME = "owner_name";
    private static final String KEY_BORROWER_NAME = "borrower_name";
    private static final String KEY_AMOUNT_OWED = "amount_owed";
    private static final String KEY_DATE = "date";


    // constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.appContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // create USER table
        String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_USERNAME + " TEXT," +
                KEY_PASSWORD + " TEXT" +
                ")";
        db.execSQL(CREATE_USERS_TABLE);

        // todo: create BORROWER table.
        String CREATE_BORROWERS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_BORROWERS +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_BORROWER_NAME + " TEXT," +
                KEY_AMOUNT_OWED + " TEXT," +
                KEY_DATE + " TEXT," +
                KEY_OWNER_NAME + " TEXT" +
                ")";
        db.execSQL(CREATE_BORROWERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " +  TABLE_BORROWERS);
        onCreate(db);
    }

    /**
     * Create a new 'Borrower' in
     * database
     *
     * @param borrower: Borrower object to be added
     * @throws Exception: thrown if insert fails
     */
    public void createNewBorrower(Borrower borrower) throws Exception {

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_BORROWER_NAME, borrower.getName());
            values.put(KEY_AMOUNT_OWED, borrower.getAmount());
            values.put(KEY_DATE, borrower.getDate());
            values.put(KEY_OWNER_NAME, borrower.getOwner());
            Log.d(TAG, "Adding new borrower to db");

            db.insert(TABLE_BORROWERS, null, values);
            db.close();

        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Retrieve all Borrowers from
     * Table
     *
     * @return: List Borrower objects
     * @throws Exception: thrown if query fails
     */
    public ArrayList<Borrower> getAllBorrowers() throws Exception {

        ArrayList<Borrower> borrowerList = new ArrayList<>();
        String owner = MyApp.getCurrentUser().getUsername();
        String[] args = {owner};

        // query database for all entries in BORROWERS table owned by current user
        String queryString = "SELECT * FROM " +
                TABLE_BORROWERS + " WHERE " +
                KEY_OWNER_NAME + "=?";

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(queryString, args);
            if (cursor.moveToFirst()) {
                do {
                    Borrower borrower = new Borrower();
                    borrower.set_id(Integer.parseInt(cursor.getString(0)));
                    borrower.setName(cursor.getString(1));
                    borrower.setAmount(cursor.getString(2));
                    borrower.setDate(cursor.getString(3));
                    borrower.setOwner(owner);
                    borrowerList.add(borrower);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            throw new Exception(e);
        }

        return borrowerList;
    }

    /**
     * Search for specified Borrower
     *
     * @param searchString: Name of Borrower to search for
     * @return: Borrower object if found
     * @throws Exception: thrown if not found
     */
    public ArrayList<Borrower> searchBorrower(String searchString) throws Exception {

        ArrayList<Borrower> foundBorrowers = new ArrayList<>();

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String owner = MyApp.getCurrentUser().getUsername();
            String[] args = { searchString, owner};

            String queryString = "SELECT * FROM " + TABLE_BORROWERS + " WHERE " +
                    KEY_BORROWER_NAME + "=?" + " AND " +
                    KEY_OWNER_NAME + "=?";

            // query
            Cursor cursor = db.rawQuery(queryString, args);
            if (cursor.moveToFirst()) {
                do {
                    Borrower borrower = new Borrower();
                    borrower.set_id(Integer.parseInt(cursor.getString(0)));
                    borrower.setName(cursor.getString(1));
                    borrower.setAmount(cursor.getString(2));
                    borrower.setDate(cursor.getString(3));
                    borrower.setOwner(owner);
                    foundBorrowers.add(borrower);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            throw new Exception(e);
        }

        if (foundBorrowers.isEmpty()) {
            throw new Exception("Could not find borrower with given search string");
        }

        return foundBorrowers;
    }

    /**
     * Remove Borrower from Database
     * (PAID)
     *
     * @param name
     * @param amount
     * @param date
     * @throws Exception
     */
    public void removeBorrower(String name, String amount, String date) throws Exception {

        String owner = MyApp.getCurrentUser().getUsername();

        String[] args = {name, amount, date, owner};
        String whereClause =
                KEY_BORROWER_NAME + "=?" + " AND " +
                KEY_AMOUNT_OWED + "=?" + " AND " +
                KEY_DATE + "=?" + " AND " +
                KEY_OWNER_NAME + "=?";

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_BORROWERS,whereClause,args);
            db.close();
        } catch (Exception e) {
            Log.d(TAG, "Failed to delete borrower");
            throw new Exception(e);
        }
    }

    /**
     * Create a new User in
     * database
     *
     * @param user: 'CurrentUser' object to be added
     * @throws Exception: thrown if insert fails
     */
    public void createNewUser(CurrentUser user) throws Exception {

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_USERNAME, user.getUsername());
            values.put(KEY_PASSWORD, user.getPassword());

            Log.d(TAG, "Adding new user: " + user.getUsername());

            db.insert(TABLE_USERS,null,values);
            MyApp.setCurrentUser(user);
            db.close();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Retrieve and authenticate the
     * specified User with the given
     * username and password
     *
     * @param username: user trying to login
     * @param password: user's password to authenticate
     * @throws Exception: thrown if username and password combination don't match
     */
    public void getCurrentUser(String username, String password) throws Exception {

        SQLiteDatabase db = this.getReadableDatabase();
        String[] args = {password,username};

        String queryString = "SELECT * FROM "+ TABLE_USERS + " WHERE " +
                KEY_PASSWORD + "=?" +
                " AND " +
                KEY_USERNAME + "=?";

       Cursor cursor = db.rawQuery(queryString,args);
       try {
           cursor.moveToFirst();

           String currentUsername = cursor.getString(1);
           String currentPassword = cursor.getString(2);
           Log.d(TAG, "Found user: " + currentUsername);

           // store user as app's current user
           CurrentUser currentUser = new CurrentUser(currentUsername,currentPassword);
           MyApp.setCurrentUser(currentUser);

           Log.d(TAG, "App's current user: " + MyApp.getCurrentUser().getUsername());
           cursor.close();
       } catch (Exception e) {
           Log.d(TAG, "Exception thrown when getting current user: " + e);
           throw new Exception("Could not retrieve user from DB");
       }
    }
}
