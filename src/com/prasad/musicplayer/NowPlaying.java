package com.prasad.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Shree Ganesh on 9/9/2014.
 */
public class NowPlaying extends Fragment implements Runnable,View.OnClickListener,SeekBar.OnSeekBarChangeListener,MediaPlayer.OnCompletionListener,View.OnLongClickListener/*,AudioManager.OnAudioFocusChangeListener */{

    final Handler myHandle = new Handler();
    private RelativeLayout relativeLayout;
    private View view;
    private Intent intent;
    private FragmentActivity fragmentActivity;
    private MainActivity mainActivity;

    private String TAG = "Sound Shack";
    public static ImageButton play,next,prev,shuffle,repeat,playlist;
    public static SeekBar songProgress;
    public static TextView displayCurrentSong,tvSeekStart,tvSeekEnd;
    public static MediaPlayer player;
    public static String PLAYER_STATUS="STOPPED";
    public static ImageView ivArt;

    //songs info
    public static String currentSong;
    public static String nextSong;
    public static int currentSongPosition;
    public static int totalSongs;
    public static ArrayList<String> mySongs;
    public static ArrayList<SongDetail> songDetails;
    public static String artWork="";

    public static Thread seek;
    public static String playlist_status = "not_executed";

    //Player status
    public static String SHUFFLE = "OFF";
    public static String REPEAT = "OFF";
    public static String PLAYLIST_STATUS = "NOT_LOADED";
    public static String art="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        relativeLayout = (RelativeLayout) inflater.inflate(R.layout.now_playing,container,false);
        return  relativeLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG,"Initializing");
        initialize();
        setHasOptionsMenu(true);
        super.onActivityCreated(savedInstanceState);
    }

    private void initialize() {
        // TODO Auto-generated method stub

        Log.i(TAG,"Initializing NowPlaying");
        //initialize thread
        if(PLAYLIST_STATUS.equals("NOT_LOADED")){
            mySongs = new ArrayList<String>();
            songDetails = new ArrayList<SongDetail>();
        }

        view = getView();
        fragmentActivity = super.getActivity();
        intent = fragmentActivity.getIntent();

        //initializing controls
        //player = new MediaPlayer();
        //player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        play = (ImageButton) view.findViewById(R.id.play_button);
        play.setBackgroundResource(R.drawable.play_orange);

        next = (ImageButton) view.findViewById(R.id.next_button);
        next.setBackgroundResource(R.drawable.button_next);

        prev = (ImageButton) view.findViewById(R.id.prev_button);
        prev.setBackgroundResource(R.drawable.button_prev);

        shuffle = (ImageButton) view.findViewById(R.id.shuffle_button);
        shuffle.setBackgroundResource(R.drawable.shuffle_2);

        repeat = (ImageButton) view.findViewById(R.id.repeat_button);
        repeat.setBackgroundResource(R.drawable.repeat_2);

        displayCurrentSong = (TextView) view.findViewById(R.id.text_shown);
        tvSeekStart = (TextView) view.findViewById(R.id.tvSeekStart);
        tvSeekEnd = (TextView) view.findViewById(R.id.tvSeekEnd);

        ivArt = (ImageView) view.findViewById(R.id.ivArtwork);


        songProgress = (SeekBar)view.findViewById(R.id.seek_bar);

        //Adding click listeners
        play.setOnClickListener(this);
        next.setOnClickListener(this);
        prev.setOnClickListener(this);
        shuffle.setOnClickListener(this);
        repeat.setOnClickListener(this);

        next.setOnLongClickListener(this);
        prev.setOnLongClickListener(this);

        songProgress.setOnSeekBarChangeListener(this);
        songProgress.setEnabled(false);
        //player.setOnCompletionListener(this);

        currentSong = "";
        currentSongPosition = 0;
        totalSongs = 0;
        nextSong = "";

        TelephonyManager mgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        if(mgr != null) {
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }
    public void run() {
        int currentPosition = player.getCurrentPosition();
        long maxPosition = player.getDuration();
        while(player!=null &&currentPosition<maxPosition){
            try{
                currentPosition = player.getCurrentPosition();
                Thread.sleep(100);
            }catch(InterruptedException ie){
                ie.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }
            updateTime();
        }
    }

    private void updateTime(){
        myHandle.post(myRunnable);
    }

    final Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            String secondString = "";
            String timer = "";
            int minutes = (player.getCurrentPosition() % (1000 * 60 * 60)) / (1000 * 60);
            int seconds = ((player.getCurrentPosition() % (1000 * 60 * 60)) % (1000 * 60) / 1000);
            if (seconds < 10)
                secondString = "0" + seconds;
            else
                secondString = "" + seconds;
            timer = timer + minutes + ":" + secondString;
            tvSeekStart.setText(timer);
        }
    };

    PhoneStateListener phoneStateListener = new PhoneStateListener(){
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                //Incoming call: Pause music
                if(player!=null) {
                    if(player.isPlaying())
                        player.pause();
                }
            } else if(state == TelephonyManager.CALL_STATE_IDLE) {
                //Not in call: Play music
                if(player!=null) {
                    if(!player.isPlaying())
                        player.start();
                }else{
                	Toast.makeText(fragmentActivity, "No Song Playing!", Toast.LENGTH_SHORT).show();
                	player = new MediaPlayer();
                    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                }
            } else if(state == TelephonyManager.CALL_STATE_OFFHOOK) {
                //A call is dialing, active or on hold
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu,menu);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.play_button:
                if (currentSong.equals("")) {
                    //Intent i = new Intent(fragmentActivity,Tab2.class);
                    //startActivityForResult(i,100);
                    fragmentActivity.getActionBar().setSelectedNavigationItem(1);
                } else {
                    System.out.println("Alter media state");
                    if(player.isPlaying()){
                        System.out.println("Pause song.");
                        MusicPlayback.pause();
                        play.setBackgroundResource(R.drawable.play_orange);
                    }else{
                        System.out.println("Play song.");
                        MusicPlayback.play();
                        play.setBackgroundResource(R.drawable.pause_orange);
                    }
                    //Toast.makeText(NowPlaying.this,"Playing " + currentSong, Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.next_button:
                if (currentSong.equals("")) {
                    Toast.makeText(fragmentActivity, "No song playing!", Toast.LENGTH_SHORT).show();
                }else{
                    if(currentSongPosition < (songDetails.size() - 1)){
                        //MusicPlayback.play_song(currentSongPosition + 1);
                        playSong(currentSongPosition + 1);
                        currentSongPosition = currentSongPosition + 1;
                    }else{
                        // play first song
                        //MusicPlayback.play_song(0);
                        playSong(0);
                        currentSongPosition = 0;
                    }
                }
                break;

            case R.id.prev_button:
                if (currentSong.equals("")) {
                    Toast.makeText(fragmentActivity,"No song playing!", Toast.LENGTH_SHORT).show();
                }else{
                    if(currentSongPosition > 0){
                        //MusicPlayback.play_song(currentSongPosition - 1);
                        playSong(currentSongPosition - 1);
                        currentSongPosition = currentSongPosition - 1;
                    }else{
                        // play last song
                        //MusicPlayback.play_song(songDetails.size() - 1);
                        playSong(songDetails.size() - 1);
                        currentSongPosition = songDetails.size() - 1;
                    }
                }
                break;

            case R.id.shuffle_button:
                if(SHUFFLE.equals("ON")){
                    Toast.makeText(fragmentActivity,"Shuffle Off",Toast.LENGTH_SHORT).show();
                    SHUFFLE = "OFF";
                    shuffle.setBackgroundResource(R.drawable.shuffle_2);
                    MusicPlayback.SongPlayed = null;
                    //shuffle.setBackgroundDrawable(getResources().getDrawable(R.drawable.shuffle_2));
                }else if(SHUFFLE.equals("OFF")){
                    Toast.makeText(fragmentActivity,"Shuffle On",Toast.LENGTH_SHORT).show();
                    SHUFFLE="ON";
                    REPEAT = "OFF";
                    player.setLooping(false);
                    repeat.setBackgroundResource(R.drawable.repeat_2);
                    shuffle.setBackgroundResource(R.drawable.shuffle_pressed_orange);
                    MusicPlayback.SongPlayed = new ArrayList<Integer>();
                    //shuffle.setBackgroundDrawable(getResources().getDrawable(R.drawable.shuffle_2_pressed));
                }
                break;

            case R.id.repeat_button:
                if(REPEAT.equals("ON")){
                    Toast.makeText(fragmentActivity,"Repeat Off",Toast.LENGTH_SHORT).show();
                    REPEAT = "OFF";
                    player.setLooping(false);
                    repeat.setBackgroundResource(R.drawable.repeat_2);
                    //repeat.setBackgroundDrawable(getResources().getDrawable(R.drawable.repeat_2_pressed));
                }else if(REPEAT.equals("OFF")){
                    Toast.makeText(fragmentActivity,"Repeat On",Toast.LENGTH_SHORT).show();
                    REPEAT="ON";
                    SHUFFLE="OFF";
                    player.setLooping(true);
                    shuffle.setBackgroundResource(R.drawable.shuffle_2);
                    repeat.setBackgroundResource(R.drawable.repeat_pressed_orange);
                    //repeat.setBackgroundDrawable(getResources().getDrawable(R.drawable.repeat_2));
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==100){
            currentSongPosition = data.getExtras().getInt("songIndex");
            MusicPlayback.play_song(currentSongPosition);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.mAllSongs:
                fragmentActivity.getActionBar().setSelectedNavigationItem(1);
                break;

             case R.id.mExit:
                songProgress.setProgress(0);
                if(player.isPlaying()){
                    player.stop();
                    player.reset();
                    player.release();
                    myHandle.removeCallbacks(myRunnable);
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                break;
            //case R.id.mFullScreen:
            //requestWindowFeature(Window.FEATURE_NO_TITLE);
            //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            // break;
        }
        return false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        // TODO Auto-generated method stub
        try {
            if (player.isPlaying()) {
                if (fromUser)
                    player.seekTo(progress);
            }
        } catch (Exception e) {
            Log.e("seek bar", "" + e);
            seekBar.setEnabled(false);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
        //player.pause();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
        //player.start();
    }

    @Override
    public boolean onLongClick(View view) {
        int seek,seekInc,seekDec;
        switch(view.getId()){
            case R.id.next_button:
                Toast.makeText(fragmentActivity,"Long Press Next",Toast.LENGTH_SHORT).show();
                /*seek=(int)(0.05)*player.getDuration();
                seekInc=(int)(0.05)*player.getDuration();
                while(next.isPressed()){
                    while(player.isPlaying() && seek<=player.getDuration()) {
                        player.seekTo(seek);
                        seek += seekInc;
                    }
                }*/
                break;
            case R.id.prev_button:
                Toast.makeText(fragmentActivity,"Long Press Prev",Toast.LENGTH_SHORT).show();
                /*seek=player.getCurrentPosition()-5;
                seekDec=(int)(0.05)*player.getDuration();
                if(seek<0){
                    seek=0;
                }
                while(prev.isPressed()){
                    while(player.isPlaying() && seek>=0) {
                        player.seekTo(seek);
                        seek += seekDec;
                    }
                }*/
                break;
        }
        return true;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
       /*player = new MediaPlayer();
       player.setAudioStreamType(AudioManager.STREAM_MUSIC);
       player.setOnCompletionListener(this);

       MusicPlayback.next();*/
        if(REPEAT.equals("ON")){
            //if repeat is on play same song
            //MusicPlayback.play_song(currentSongPosition);
            playSong(currentSongPosition);
        }else if(SHUFFLE.equals("ON")){
            //if shuffle is on - play a random song
            Random rand = new Random();
            currentSongPosition = rand.nextInt((totalSongs - 1) - 0 + 1);
            //MusicPlayback.play_song(currentSongPosition);
            playSong(currentSongPosition);
        }else{
            //no repeat & no shuffle
            if(currentSongPosition<totalSongs-1){
                currentSongPosition++;
                //MusicPlayback.play_song(currentSongPosition);
                playSong(currentSongPosition);
            }else{
                //MusicPlayback.play_song(0);
                playSong(0);
                currentSongPosition = 0;
            }
        }
    }

    public static void playSong(final int songIndex){
        try{                
        		player.reset();
                player.setDataSource(songDetails.get(songIndex).getSong_Path()); 
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
						// TODO Auto-generated method stub
						if(REPEAT.equals("ON")){
				            //if repeat is on play same song
				            //MusicPlayback.play_song(currentSongPosition);
				            playSong(currentSongPosition);
				        }else if(SHUFFLE.equals("ON")){
				            //if shuffle is on - play a random song
				            Random rand = new Random();
				            currentSongPosition = rand.nextInt((totalSongs - 1) - 0 + 1);
				            //MusicPlayback.play_song(currentSongPosition);
				            playSong(currentSongPosition);
				        }else{
				            //no repeat & no shuffle
				            if(currentSongPosition<totalSongs-1){
				                currentSongPosition++;
				                //MusicPlayback.play_song(currentSongPosition);
				                playSong(currentSongPosition);
				            }else{
				                //MusicPlayback.play_song(0);
				                playSong(0);
				                currentSongPosition = 0;
				            }
				        }
					}
				});
                
                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                    currentSong = songDetails.get(songIndex).getSong_Title();
                    displayCurrentSong.setText(currentSong);
                    artWork = songDetails.get(songIndex).getAlbum_Artwork();
                    if(artWork != null){
                        art = artWork;
                        ivArt.setImageURI(Uri.fromFile(new File(artWork)));
                    }else{
                        ivArt.setImageResource(R.drawable.default_cover_orange);
                    }
                    songProgress.setMax(player.getDuration());
                    songProgress.setEnabled(true);
                    songProgress.setProgress(0);
                    currentSongPosition = songIndex;
                    System.out.println("Track no.:"+(currentSongPosition+1));
                    System.out.println("Song Playing :" + currentSong);
                    System.out.println("Artwork :"+artWork);
                    long maxPosition = player.getDuration();
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
                    tvSeekEnd.setText(timer);
                    play.setBackgroundResource(R.drawable.pause_orange);
                    new Thread(new NowPlaying()).start();
                }
            });            
            player.prepare();
            //Tab1.player.start();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   /* public void initMediaPlayer(){
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if(currentSongPosition>0)
        {
            try {
                player.setDataSource(songDetails.get(currentSongPosition).getSong_Path());
                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                        currentSong = songDetails.get(currentSongPosition).getSong_Title();
                        displayCurrentSong.setText(currentSong);
                        artWork = songDetails.get(currentSongPosition).getAlbum_Artwork();
                        if(artWork != null){
                            art = artWork;
                            ivArt.setImageURI(Uri.fromFile(new File(artWork)));
                        }else{
                            ivArt.setImageResource(R.drawable.default_cover_orange);
                        }
                        songProgress.setMax(player.getDuration());
                        songProgress.setEnabled(true);
                        songProgress.setProgress(0);
                        //currentSongPosition = songIndex;
                        System.out.println("Track no.:"+(currentSongPosition+1));
                        System.out.println("Song Playing :" + currentSong);
                        System.out.println("Artwork :"+artWork);
                        long maxPosition = player.getDuration();
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
                        tvSeekEnd.setText(timer);
                        play.setBackgroundResource(R.drawable.pause_orange);
                        new Thread(new NowPlaying()).start();
                    }
                });
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //player.start();
        }else{
            Toast.makeText(getActivity(),"No Music Playing",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (player == null) initMediaPlayer();
                else if (!player.isPlaying()) player.start();
                player.setVolume(1.0f, 1.0f);
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (player.isPlaying()) player.stop();
                player.release();
                player = null;
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (player.isPlaying()) player.pause();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (player.isPlaying()) player.setVolume(0.1f, 0.1f);
                break;
        }
    }*/
    
}
