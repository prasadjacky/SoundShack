package com.prasad.musicplayer;

/**
 * Created by Shree Ganesh on 8/28/2014.
 */
public class SongDetail {

    private long Artist_ID;
    private long Album_ID;
    private String Song_Title;
    private String Artist;
    private String Song_Path;
    private String Song_Duration;
    private String Album_Artwork;
    private boolean isPlaying;

    public SongDetail(){

    }

    public SongDetail(long Artist_ID,long Album_ID,String Song_Title,String Artist, String Song_Path,String Song_Duration,String Album_Artwork,boolean isPlaying) {
        // TODO Auto-generated constructor stub
        this.Artist_ID = Artist_ID;
        this.Album_ID = Album_ID;
        this.Song_Title = Song_Title;
        this.Artist = Artist;
        this.Song_Path = Song_Path;
        this.Song_Duration = Song_Duration;
        this.Album_Artwork = Album_Artwork;
        this.isPlaying=isPlaying;
    }
    public long getArtist_ID() {
        return Artist_ID;
    }
    public void setArtist_ID(long artist_ID) {
        Artist_ID = artist_ID;
    }
    public long getAlbum_ID() {
        return Album_ID;
    }
    public void setAlbum_ID(long album_ID) {
        Album_ID = album_ID;
    }
    public String getSong_Title() {
        return Song_Title;
    }
    public void setSong_Title(String song_Title) {
        Song_Title = song_Title;
    }
    public String getSong_Path() {
        return Song_Path;
    }
    public void setSong_Path(String song_Path) {
        Song_Path = song_Path;
    }
    public String getSong_Duration(){
        return Song_Duration;
    }
    public void setSong_Duration(String Song_Duration){
        this.Song_Duration = Song_Duration;
    }
    public String getAlbum_Artwork() {
        return Album_Artwork;
    }
    public void setAlbum_Artwork(String album_Artwork) {
        Album_Artwork = album_Artwork;
    }
    public String getArtist() {
        return Artist;
    }
    public void setArtist(String artist) {
        Artist = artist;
    }
    public boolean isPlaying() { return isPlaying;  }
    public void setPlaying(boolean playing) {isPlaying = playing;
    }
}
