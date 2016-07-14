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

public class MusicPlayback{

    public static String artWork="";
    public static ArrayList<Integer> SongPlayed;
    public String TAG = "Sound Shack";

    public synchronized static void play() {
        // TODO Auto-generated method stub
        NowPlaying.player.start();
    }

    public static void pause() {
        // TODO Auto-generated method stub
        NowPlaying.player.pause();
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
}
