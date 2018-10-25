package org.assignment.ctse.moneytracker.Model;

/**
 * Data model for a Borrower
 */
public class Borrower {

    int _id;
    String _name = "";
    String _amount = "";
    String _date = "";

    // the user that owns this borrower
    String _owner = "";

    // empty constructor
    public Borrower() {}

    /**
     * Borrower Constructor w/
     * id param
     *
     * @param id: id for table
     * @param name: name of Borrower
     * @param amount: amount of money being borrowed
     * @param date: date
     */
    public Borrower(int id, String name, String amount, String date, String owner) {
        this._id = id;
        this._name = name;
        this._amount = amount;
        this._date = date;
        this._owner = owner;
    }

    /**
     * Overloaded Borrower Constructor
     * w/out id param
     *
     * @param name: name of borrower
     * @param amount: amount of money being borrowed
     * @param date: date
     */
    public Borrower(String name, String amount, String date, String owner) {
        this._name = name;
        this._amount = amount;
        this._date = date;
        this._owner = owner;
    }

    public int getId() {
        return this._id;
    }

    public void set_id(int id) {
        this._id = id;
    }

    public String getName() {
        return this._name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getAmount() {
        return this._amount;
    }

    public void setAmount(String amount) {
        this._amount = amount;
    }

    public String getDate() {
        return this._date;
    }

    public void setDate(String date) {
        this._date = date;
    }

    public String getOwner() {
        return this._owner;
    }

    public void setOwner(String owner) {
        this._owner = owner;
    }
}
