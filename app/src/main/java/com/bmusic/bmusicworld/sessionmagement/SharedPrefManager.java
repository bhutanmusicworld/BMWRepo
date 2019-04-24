package com.bmusic.bmusicworld.sessionmagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.bmusic.bmusicworld.MainActivity;
import com.bmusic.bmusicworld.model.User;


/**
 * Created by techm on 10/24/2017.
 */

public class SharedPrefManager {


    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences

    public void userRegister(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constant.KEY_ID, user.getId());
        editor.putString(Constant.KEY_NAME, user.getUserName());
        editor.putString(Constant.KEY_PHONE, user.getMobileNo());
        editor.putString(Constant.KEY_EMAIL, user.getEmail());
        editor.putString(Constant.KEY_USERTYPE, user.getUser_type());
        editor.putString(Constant.KEY_DOB, user.getDob());
        editor.putString(Constant.KEY_CREATED_DATE, user.getCreated_date());
        editor.apply();
    }
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constant.KEY_ID, user.getId());
        editor.putString(Constant.KEY_NAME, user.getUserName());
        editor.putString(Constant.KEY_PHONE, user.getMobileNo());
        editor.putString(Constant.KEY_EMAIL, user.getEmail());
        editor.putString(Constant.KEY_USERTYPE, user.getUser_type());
        editor.putString(Constant.KEY_DOB, user.getDob());
        editor.putString(Constant.KEY_CREATED_DATE, user.getCreated_date());
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constant.KEY_EMAIL, null) != null;
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString(Constant.KEY_ID, "-1"),
                sharedPreferences.getString(Constant.KEY_NAME, null),
                sharedPreferences.getString(Constant.KEY_PHONE, null),
                sharedPreferences.getString(Constant.KEY_EMAIL, null),
                sharedPreferences.getString(Constant.KEY_USERTYPE, null),
                sharedPreferences.getString(Constant.KEY_DOB, null),
                sharedPreferences.getString(Constant.KEY_CREATED_DATE, null)

        );
    }
    //this method will give the logged in user
    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, MainActivity.class));
    }

    public void setFirebaseToken(String token) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constant.KEY_TOKEN, token);
        editor.apply();
    }

    public String getFirebaseToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return  sharedPreferences.getString(Constant.KEY_TOKEN, null);
    }
}