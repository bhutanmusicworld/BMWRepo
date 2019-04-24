package com.bmusic.bmusicworld.model;

/**
 * Created by wafa on 8/22/2017.
 */

public class PlayList {
    private  String playlistid;
    private  String playlistname;
    private  String songid;
    private  String playlistimage;
    private  String songcount;

    public String getPlaylistimage() {
        return playlistimage;
    }

    public void setPlaylistimage(String playlistimage) {
        this.playlistimage = playlistimage;
    }

    public String getSongcount() {
        return songcount;
    }

    public void setSongcount(String songcount) {
        this.songcount = songcount;
    }

    public PlayList() {
    }

    public PlayList(String playlistid, String playlistname, String songid) {
        this.playlistid = playlistid;
        this.playlistname = playlistname;
        this.songid = songid;
    }

    public String getPlaylistid() {
        return playlistid;
    }

    public void setPlaylistid(String playlistid) {
        this.playlistid = playlistid;
    }

    public String getPlaylistname() {
        return playlistname;
    }

    public void setPlaylistname(String playlistname) {
        this.playlistname = playlistname;
    }

    public String getSongid() {
        return songid;
    }

    public void setSongid(String songid) {
        this.songid = songid;
    }
}
