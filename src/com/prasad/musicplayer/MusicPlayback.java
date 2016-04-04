package com.prasad.musicplayer;

/**
 * Created by Shree Ganesh on 8/15/2014.
 */

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayback extends Activity{

    public static String artWork="";
    public static ArrayList<Integer> SongPlayed;
    public String TAG = "Sound Shack";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);       
    }

    public synchronized static void play() {
        // TODO Auto-generated method stub
        System.out.println("play song...");
        if (!NowPlaying.player.isPlaying()) {
            System.out.println("new player started...");
            NowPlaying.player.start();
        } else {
            System.out.println("no song playing...");
        }
    }

    public static void pause() {
        // TODO Auto-generated method stub
        System.out.println("pause song...");
        if (NowPlaying.player.isPlaying()) {
            NowPlaying.player.pause();
        } else {

        }
    }
/*
    public synchronized static void next() {
        System.out.println("Next song.");
        if(Tab1.SHUFFLE.equals("ON")){
            int pos = 0;
            boolean song_played=false;
            Random r = new Random();
            while(true){
                pos = r.nextInt(Tab1.totalSongs);
                if (pos>0 && pos<= Tab1.totalSongs){
                    for (int i = 0;i<SongPlayed.size();i++){
                        if(pos==SongPlayed.get(i)) {
                            song_played = true;
                            System.out.println("Song already played");
                            break;
                        }
                    }
                }
                if (song_played==false){
                    System.out.println("New song selected");
                    break;
                }
            }
            System.out.println("Random Song pos : "+pos);
            Tab1.currentSongPosition = pos;
            System.out.println("Track no.:" + Tab1.currentSongPosition);
            play_song(Tab1.currentSongPosition);
            SongPlayed.add(pos);
        }else {
            if ((Tab1.currentSongPosition + 1) < Tab1.totalSongs) {
                Tab1.currentSongPosition += 1;
                System.out.println("Track no.:" + Tab1.currentSongPosition);
                play_song(Tab1.currentSongPosition);
            } else {
                Tab1.currentSongPosition = 0;
                play_song(Tab1.currentSongPosition);
            }
        }
    }

    public synchronized static void prev() {
        System.out.println("Prev song.");
        if(Tab1.SHUFFLE.equals("ON")){
            int pos = 0;
            boolean song_played=false;
            Random r = new Random();
            while(true){
                pos = r.nextInt((Tab1.totalSongs - 1) - 0 + 1);
                if (pos>0 && pos<= Tab1.totalSongs){
                    for (int i = 0;i<SongPlayed.size();i++){
                        if(pos==SongPlayed.get(i)) {
                            song_played = true;
                            System.out.println("Song already played");
                            break;
                        }
                    }
                }
                if (song_played==false){
                    System.out.println("New song selected");
                    break;
                }
            }
            System.out.println("Random Song pos : "+pos);
            Tab1.currentSongPosition = pos;
            System.out.println("Track no.:" + Tab1.currentSongPosition);
            play_song(Tab1.currentSongPosition);
            SongPlayed.add(pos);
        }else {
            if (Tab1.currentSongPosition - 1 < 0) {
                Tab1.currentSongPosition = Tab1.totalSongs - 1;
                play_song(Tab1.currentSongPosition);
            } else {
                Tab1.currentSongPosition -= 1;
                System.out.println("Track no.:" + Tab1.currentSongPosition);
                play_song(Tab1.currentSongPosition);
            }
        }
    }
*/
    public static void play_song(final int songIndex){
        try{
            NowPlaying.player.reset();
            NowPlaying.player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                    NowPlaying.currentSong = NowPlaying.songDetails.get(songIndex).getSong_Title();
                    NowPlaying.displayCurrentSong.setText(NowPlaying.currentSong);
                    artWork = NowPlaying.songDetails.get(songIndex).getAlbum_Artwork();
                    if(artWork != null){
                        NowPlaying.art = artWork;
                        NowPlaying.ivArt.setImageURI(Uri.fromFile(new File(artWork)));
                    }else{
                        NowPlaying.ivArt.setImageResource(R.drawable.default_cover_orange);
                    }
                    NowPlaying.songProgress.setMax(NowPlaying.player.getDuration());
                    NowPlaying.songProgress.setEnabled(true);
                    NowPlaying.songProgress.setProgress(0);
                    NowPlaying.currentSongPosition = songIndex;
                    System.out.println("Track no.:"+(NowPlaying.currentSongPosition+1));
                    System.out.println("Song Playing :" + NowPlaying.currentSong);
                    System.out.println("Artwork :"+artWork);
                    long maxPosition = NowPlaying.player.getDuration();
                    // long minutes = TimeUnit.MILLISECONDS.toMinutes(maxPosition);
                    // long seconds = TimeUnit.MILLISECONDS.toSeconds(maxPosition)/10;
                    String secondString = "";
                    String timer = "";
                    //long minutes = (player.getCurrentPosition())/(1000*60);
                    //long seconds = (player.getCurrentPosition())/(1000);
                    int minutes = (int) (maxPosition % (1000 * 60 * 60)) / (1000 * 60);
                    int seconds = (int) ((maxPosition % (1000 * 60 * 60)) % (1000 * 60) / 1000);
                    if (seconds < 10)
                        secondString = "0" + seconds;
                    else
                        secondString = "" + seconds;
                    timer = timer + minutes + ":" + secondString;
                    //int minutes = (int)(maxPosition % (1000*60*60)) / (1000*60);
                    //int seconds = (int) ((maxPosition % (1000*60*60)) % (1000*60) / 1000);
                    NowPlaying.tvSeekEnd.setText(timer);
                    NowPlaying.play.setBackgroundResource(R.drawable.pause_orange);
                    new Thread(new NowPlaying()).start();
                }
            });
            NowPlaying.player.setDataSource(NowPlaying.songDetails.get(songIndex).getSong_Path());
            NowPlaying.player.prepare();
            //Tab1.player.start();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
