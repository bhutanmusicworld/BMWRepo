package com.bmusic.bmusicworld.extra;





import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bmusic.bmusicworld.R;

/**
 * Created by TECHMIT on 12/16/2017.
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;
import android.widget.ListView;

import dm.audiostreamer.CurrentSessionCallback;
import dm.audiostreamer.MediaMetaData;
public class SongList extends AppCompatActivity implements CurrentSessionCallback {

    private ArrayList<MediaMetaData> songList;
    private ListView songView;
    Context  context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        songList = new ArrayList<MediaMetaData>();
        context.getApplicationContext();
    }

    public ArrayList<MediaMetaData> getSongList() {

        Cursor musicCursor =context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,MediaStore.Audio.Media.DATA + " like ? ",
                new String[] {"%myBmusic%"},null);


        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int dur = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DURATION);
            int lab = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM);
            int art = musicCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);

            int url=musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            //add songs to list
            do {
                int thisId = musicCursor.getInt(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thislab = musicCursor.getString(lab);
                String thisurl = musicCursor.getString(url);
                String thisdur = musicCursor.getString(dur);
                String thisart = musicCursor.getString(art);

                String[] units =thisdur.split(":");
                //will break the string up into an array
                int minutes = Integer.parseInt(units[0]);

                // int minutes = Integer.parseInt(units[0]); //first element
                int seconds = Integer.parseInt(units[1]);
                int duration = 60 * minutes + seconds; //add up our value

                String time = String.valueOf(duration); //mm:ss

                MediaMetaData metadata=new MediaMetaData();
                String Id= String.valueOf(thisId);
                metadata.setMediaId(Id);
                metadata.setMediaTitle(thisTitle);
                metadata.setMediaArtist(thisArtist);
                metadata.setMediaUrl(thisurl);
                metadata.setMediaAlbum(thislab);
                metadata.setMediaAlbum(thisart);
                metadata.setMediaDuration(time);
                songList.add(metadata);
               // songList.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }

         return songList;

    }

    @Override
    public void updatePlaybackState(int i) {

    }

    @Override
    public void playSongComplete() {

    }

    @Override
    public void currentSeekBarPosition(int i) {

    }

    @Override
    public void playCurrent(int i, MediaMetaData mediaMetaData) {

    }

    @Override
    public void playNext(int i, MediaMetaData mediaMetaData) {

    }

    @Override
    public void playPrevious(int i, MediaMetaData mediaMetaData) {

    }
}
