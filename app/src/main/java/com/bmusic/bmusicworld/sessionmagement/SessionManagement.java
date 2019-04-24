package com.bmusic.bmusicworld.sessionmagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.bmusic.bmusicworld.MainActivity;
import com.bmusic.bmusicworld.register.MainLogin;

import java.util.HashMap;


public class SessionManagement {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "MyLoginPrefrence";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    public static final String KEY_NUMBER = "mobile_number";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_ID = "id";
    public static final String KEY_USERID = "userId";
    public static final String KEY_IMAGE="useimage";
    public static final String KEY_LANGUAGE="language";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // Constructor
    public SessionManagement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession( String id,String number, String password){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        //storind id in pref
        editor.putString(KEY_ID, id);
        // Storing number in pref
        editor.putString(KEY_NUMBER, number);

        // Storing password in pref
        editor.putString(KEY_PASSWORD, password);

        // commit changes
        editor.commit();
    }

    public void setUserId( String id){
        // Storing login value as TRUE
        editor.putString(KEY_USERID, id);
        // commit changes
        editor.commit();
    }

    public String getUserId( ){
        // Storing login value as TRUE
        return pref.getString(KEY_USERID, null);

    }
    public void setLanguage(String lang)
    {
        editor.putString(KEY_LANGUAGE,lang);
        editor.commit();
    }
    public String getLanguage()
    {
        return pref.getString(KEY_LANGUAGE,null);
    }


    public void setUserImage(String image)
    {
        editor.putString(KEY_IMAGE,image);
        editor.commit();
    }
    public String getUserImage()
    {
        return pref.getString(KEY_IMAGE,null);
    }
    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context,MainLogin.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }



    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        // user id
        user.put(KEY_ID, pref.getString(KEY_ID, null));


        // user number
        user.put(KEY_NUMBER, pref.getString(KEY_NUMBER, null));
        // user password
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        // return user
        return user;
    }
    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        // After logout redirect user to Loing Activity
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){

        return pref.getBoolean(IS_LOGIN, false);
    }

    public void setLogin(Boolean isLogin){
        editor.putBoolean(IS_LOGIN, isLogin);
        editor.commit();
    }
}