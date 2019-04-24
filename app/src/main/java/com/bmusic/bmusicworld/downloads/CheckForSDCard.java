package com.bmusic.bmusicworld.downloads;

import android.os.Environment;

/**
 * Created by wafa on 8/26/2017.
 */
public class CheckForSDCard {
    //Check If SD Card is present or not method
    public boolean isSDCardPresent() {
        if (Environment.getExternalStorageState().equals(

                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
}
