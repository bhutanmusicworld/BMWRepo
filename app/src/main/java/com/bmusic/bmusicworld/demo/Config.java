package com.bmusic.bmusicworld.demo;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by wafa on 10/6/2017.
 */

public class Config {
    //Here Check Internet Connection

    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final String PUSH_NOTIFY = "pushNotify";
    public static final String PUSH_NOTIFY_PLAY = "pushNotify";
    public static boolean isInternetOn(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo data = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifi != null & data != null) && (wifi.isConnected() | data.isConnected()))
        {
            return true;
        } else
        {
            return false;
        }
    }
}
