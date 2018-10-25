package org.assignment.ctse.moneytracker.Model;

/**
 * Data Model for a User
 */
public class CurrentUser {

    // Need Username, and Password

    int _id;
    String _username = "";
    String _password = "";

    /**
     * Constructor with table-id
     *
     * @param id
     * @param username
     * @param password
     */
    public CurrentUser(int id, String username, String password) {
        this._id = id;
        this._username = username;
        this._password = password;
    }

    /**
     * Overloaded constructor w/out id
     *
     * @param username
     * @param password
     */
    public CurrentUser(String username, String password) {
        this._username = username;
        this._password = password;
    }

    public int getId() {
        return this._id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getUsername() {
        return this._username;
    }

    public void setUsername(String username) {
        this._username = username;
    }

    // fixme this may not be necessary
    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        this._password = password;
    }
}
