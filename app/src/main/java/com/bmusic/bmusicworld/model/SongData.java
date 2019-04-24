package com.bmusic.bmusicworld.model;

/**
 * Created by wafa on 8/16/2017.
 */

public class SongData {
    private String id;
    private String songs_title;
    private String songs_length;
    private String release_date;
    private String album_id;
    private String music_by;
    private String artist_id;
    private String language;
    private String lable;
    private String status;
    private String created_date;
    private String created_on;
    private String updated_date;
    private String updated_on;
    private String song_name;
    private String img_name;

    public SongData() {
    }

    public SongData(String id, String songs_title, String songs_length, String album_id, String music_by, String artist_id, String song_name, String img_name) {
        this.id = id;
        this.songs_title = songs_title;
        this.songs_length = songs_length;
        this.album_id = album_id;
        this.music_by = music_by;
        this.artist_id = artist_id;
        this.song_name = song_name;
        this.img_name = img_name;
    }

    public SongData(String songs_title, String img_name) {
        this.songs_title = songs_title;
        this.img_name = img_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSongs_title() {
        return songs_title;
    }

    public void setSongs_title(String songs_title) {
        this.songs_title = songs_title;
    }

    public String getSongs_length() {
        return songs_length;
    }

    public void setSongs_length(String songs_length) {
        this.songs_length = songs_length;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getMusic_by() {
        return music_by;
    }

    public void setMusic_by(String music_by) {
        this.music_by = music_by;
    }

    public String getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }

    public String getSong_name() {
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }

    public String getImg_name() {
        return img_name;
    }

    public void setImg_name(String img_name) {
        this.img_name = img_name;
    }
}
