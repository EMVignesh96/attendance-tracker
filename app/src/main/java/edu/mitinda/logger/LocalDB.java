package edu.mitinda.logger;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Vignesh on 03-10-2016.
 */

public class LocalDB {
    public static final String SP_NAME = "UserDetails";
    SharedPreferences localDB;

    public LocalDB(Context context) {
        localDB = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeData(User user) {
        SharedPreferences.Editor spEditor = localDB.edit();
        spEditor.putString("Username", user.username);
        spEditor.putString("Password", user.password);
        spEditor.commit();
    }

    public User getLoggedInUser() {
        String  username = localDB.getString("Username", "");
        String  password = localDB.getString("Password", "");

        User storedUser = new User(username, password);
        return storedUser;
    }

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor spEditior = localDB.edit();
        spEditior.putBoolean("loggedIn", loggedIn);
        spEditior.commit();
    }

    public boolean getUserLoggedIn() {
        if(localDB.getBoolean("loggedIn", false)) {
            return true;
        }
        else {
            return false;
        }
    }

    public void clearData() {
        SharedPreferences.Editor spEditor = localDB.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
