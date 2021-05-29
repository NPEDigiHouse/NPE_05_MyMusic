package com.example.npe_05_mymusic.Models.songs;

public class SongsModel {
    private String title, artist, genre, song_url, cover_url;

    public SongsModel(String title, String artist, String genre, String song_url, String cover_url) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.song_url = song_url;
        this.cover_url = cover_url;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }

    public String getSong_url() {
        return song_url;
    }

    public String getCover_url() {
        return cover_url;
    }
}
