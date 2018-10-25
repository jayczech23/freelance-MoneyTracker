package org.assignment.ctse.moneytracker.Model;


import android.app.Application;


/**
 * Class for storing Current User
 * of app to set owner of borrowers in
 * database
 */
public class MyApp extends Application{

    private static CurrentUser _currentUser;

    public static CurrentUser getCurrentUser() {
        return _currentUser;
    }

    public static void setCurrentUser(CurrentUser user) {
        _currentUser = user;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
