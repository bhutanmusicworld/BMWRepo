package com.bmusic.bmusicworld.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by wafa on 8/18/2017.
 */

public class Track implements Parcelable {
    private  String id;
    private  String title;
    private  String StreamUrl;
    private  String ArtAtWork;
    private  String length;
    private  String songimg;
    private  String gener;
    boolean isDownload=false;
    boolean isDownloaded=false;
    boolean isbackplay = false ;
    boolean isnotplay = false ;
    private  String lable;
    private  String release;
    private  int progress;
   public ArrayList<Track> mylist;
    private Bitmap bitmap;

    public ArrayList<Track> getMylist() {
        return mylist;
    }

    public void setMylist(ArrayList<Track> mylist) {
        this.mylist = mylist;
    }

    public Track(String title, String artAtWork, String length, String lable, Bitmap bitmap,String gener) {
        this.title = title;
        this.ArtAtWork = artAtWork;
        this.length = length;
        this.lable = lable;
        this.bitmap = bitmap;
        this.gener = gener;
    }

    public Track(String id, String title, String streamUrl, String artAtWork, String songimg) {
        this.id = id;
        this.title = title;
        this.StreamUrl = streamUrl;
        this.ArtAtWork = artAtWork;
        this.songimg = songimg;
    }
    public Track(String id, String title, String streamUrl, String songimg) {
        this.id = id;
        this.title = title;
        this.StreamUrl = streamUrl;
        this.songimg = songimg;
    }

    public Track() {

    }

    public String getGener() {
        return gener;
    }

    public void setGener(String gener) {
        this.gener = gener;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStreamUrl() {
        return StreamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        StreamUrl = streamUrl;
    }

    public String getArtAtWork() {
        return ArtAtWork;
    }

    public void setArtAtWork(String artAtWork) {
        ArtAtWork = artAtWork;
    }

    public String getSongimg() {
        return songimg;
    }

    public void setSongimg(String songimg) {
        this.songimg = songimg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.songimg);
        parcel.writeString(this.StreamUrl);
        parcel.writeString(this.title);


    }
    public static final Parcelable.Creator<Track> CREATOR
            = new Parcelable.Creator<Track>() {
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean download) {
        isDownload = download;
    }

    public boolean isnotplay() {
        return isnotplay;
    }

    public void setIsnotplay(boolean isnotplay) {
        this.isnotplay = isnotplay;
    }

    private Track(Parcel in) {
        this.id = in.readString();
        this.songimg = in.readString();
        this.StreamUrl = in.readString();
        this.title = in.readString();

    }

    public boolean isbackplay() {
        return isbackplay;
    }

    public void setIsbackplay(boolean isbackplay) {
        this.isbackplay = isbackplay;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}