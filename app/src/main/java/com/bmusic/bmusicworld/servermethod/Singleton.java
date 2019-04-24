package com.bmusic.bmusicworld.servermethod;

import java.util.ArrayList;

import dm.audiostreamer.MediaMetaData;

/**
 * Created by TECHMIT on 2/12/2018.
 */

public class Singleton {

    private static volatile Singleton instance = null;
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                // Double check
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
    public ArrayList<MediaMetaData> modelList = new ArrayList<MediaMetaData>();
}