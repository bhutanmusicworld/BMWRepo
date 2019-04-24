package com.bmusic.bmusicworld.downloads;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

import dm.audiostreamer.MediaMetaData;

/**
 * Created by wafa on 8/26/2017.
 */


public class SongsManager extends Activity{
    Context context;
    // SDCard Path
    File home;
    // final String MEDIA_PATH = new String("/storage/emulated/0/Bmusic/");
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> list;
    // Constructor
    public SongsManager(){

    }



    /**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     * */

     //if(home.isDirectory())
    public ArrayList<HashMap<String, String>> getFolder() {
       home = new File(
                Environment.getExternalStorageDirectory() + "/"
                        +".Bmusic");
        //.Bmusic
        if (home.isDirectory())
        {
        list= getPlayList();
        }else
        {
           // Toast.makeText(SongsManager.this,"No songs Exist in Bmusic App.please Download First",Toast.LENGTH_LONG).show();
        }
        return list;
    }
    public ArrayList<HashMap<String, String>> getPlayList(){
        try {
            if (home.listFiles(new FileExtensionFilter()).length > 0) {
                for (File file : home.listFiles(new FileExtensionFilter())) {
                    HashMap<String, String> song = new HashMap<String, String>();
                    song.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
                    song.put("songPath", file.getPath());


                    // Adding each song to SongList
                    songsList.add(song);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
            // return songs list array
            return songsList;
        }
    public ArrayList<MediaMetaData> getFolder2() {
        ArrayList<MediaMetaData> songList = new ArrayList<>();
        home = new File(
                Environment.getExternalStorageDirectory() + "/"
                        +".Bmusic");
        if (home.isDirectory())
        {
            songList= getPlayList2();
        }else
        {
            // Toast.makeText(SongsManager.this,"No songs Exist in Bmusic App.please Download First",Toast.LENGTH_LONG).show();
        }
        return songList;
    }
    public ArrayList<MediaMetaData> getPlayList2(){
        ArrayList<MediaMetaData> songList = new ArrayList<>();
        if (home.listFiles(new FileExtensionFilter()).length > 0) {
            for (File file : home.listFiles(new FileExtensionFilter())) {
                String thisTitle= file.getName().substring(0, (file.getName().length() - 4));
                String thisurl=  file.getPath();
                MediaMetaData metadata=new MediaMetaData();
               // String Id= String.valueOf(thisId);
               // metadata.setMediaId(Id);
                metadata.setMediaUrl(thisurl);
                metadata.setMediaTitle(thisTitle);
                Log.e("songPath",thisurl);
//                metadata.setMediaArtist(thisArtist);
//
//                metadata.setMediaAlbum(thislab);
//                // metadata.setMediaAlbum(thisart);
//                metadata.setMediaDuration(thisdur);
                songList.add(metadata);

            }
        }
        // return songs list array
        return songList;
    }
}
    /**
     * Class to filter files which are having .mp3 extension
     * */
    class FileExtensionFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3") || name.endsWith(".MP3"));
        }
    }

