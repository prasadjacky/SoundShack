package com.prasad.musicplayer;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Shree Ganesh on 9/9/2014.
 */
public class AllSongs extends Fragment  implements AdapterView.OnItemClickListener{

    private RelativeLayout relativeLayout;
    private View view;
    private FragmentActivity fragmentActivity;
    public ArrayAdapter<String> songs;
    public static SongListAdapter adapter;
    public static ListView tvPlaylist,list;
    public ContentResolver resolver;
    public String sortOrder;
    public Cursor cursor;
    public String selection;
    public static ArrayList<SongDetail> songDetails;

    public static ImageView ivCurrentSong;
    public String TAG = "Sound Shack";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        relativeLayout = (RelativeLayout) inflater.inflate(R.layout.songs_list,container,false);
        return relativeLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public synchronized void run() {
                if (NowPlaying.PLAYLIST_STATUS.equals("NOT_LOADED")) {
                    loadAllSongs();
                }
            }
        });
        initialize();
        super.onActivityCreated(savedInstanceState);
    }

    public void loadAllSongs(){
        Log.i(TAG, "Loading Songs in the background");
        if (android.os.Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.i(TAG, "Media Mounted : true");
            new LoadSongsInBackground().execute("LOAD_SONGS");
        }else {
            Log.i(TAG,"Media Mounted : false");
        }
    }

    public void initialize(){
        Log.i(TAG,"Initializing Playlist activity_home...");
        Log.i(TAG, "Playlist Status : " + NowPlaying.PLAYLIST_STATUS);
        fragmentActivity = super.getActivity();
        view = getView();
        list  = (ListView) view.findViewById(R.id.list);
        songDetails = new ArrayList<>();
        SongDetail blankList = new SongDetail();
        blankList.setSong_Title("Loading Songs...");
        blankList.setAlbum_Artwork(null);
        blankList.setSong_Duration("0");
        blankList.setArtist("Loading Songs...");
        ArrayList<SongDetail> blankPlaylist = new ArrayList<SongDetail>();
        blankPlaylist.add(blankList);
        adapter = new SongListAdapter(getActivity(), blankPlaylist);
        list.setAdapter(adapter);
        //ivCurrentSong = (ImageView) view.findViewById(R.id.ivCurrentSong);
        /*if (NowPlaying.songDetails != null) {
            NowPlaying.totalSongs = NowPlaying.songDetails.size();
            adapter = new SongListAdapter(getActivity(), NowPlaying.songDetails);
            list.setAdapter(adapter);
            Toast.makeText(getActivity(), NowPlaying.totalSongs + " songs loaded.", Toast.LENGTH_SHORT).show();
            list.setOnItemClickListener(this);

            Log.i(TAG,"Playlist Status : "+ NowPlaying.PLAYLIST_STATUS);
        }else{
            Toast.makeText(getActivity(),"No songs loaded.", Toast.LENGTH_SHORT).show();
        }*/
    }

    private void addSongDetailsToList(long Artist_ID,long Album_ID,String Song_Title,String Artist,String Song_Path,String Duration,String Album_Artwork,boolean isPlaying){
        songDetails.add(new SongDetail(Artist_ID, Album_ID, Song_Title,Artist, Song_Path, Duration, Album_Artwork,isPlaying));
    }

    public ArrayList<SongDetail> getSongsDetailList(){
        Log.i(TAG,"Fetching Songs from MediaStore");
        selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        resolver = getActivity().getContentResolver();
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ALBUM
        };
        cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, sortOrder);

        if(cursor.moveToFirst()){
            do{
                long duration = Long.parseLong(cursor.getString(5));
                if(duration>0){
                    long Artist_ID = cursor.getLong(0);
                    String Artist = cursor.getString(1);
                    String Duration = cursor.getString(5);
                    long Album_ID = cursor.getLong(6);
                    String Song_Title = cursor.getString(4).substring(0, cursor.getString(4).length()-4);
                    String Song_Path = cursor.getString(3);
                    String Album_Artwork ="";
                    String albumid = cursor.getString(6);
                    Cursor cur = getActivity().getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]
                            {MediaStore.Audio.Albums.ALBUM_ART}, MediaStore.Audio.Albums._ID + "=?", new String[]{albumid}, null);
                    if(cur.moveToFirst()){
                        Album_Artwork = cur.getString(cur.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                    }
                    cur.close();
                    addSongDetailsToList(Artist_ID, Album_ID, Song_Title, Artist, Song_Path, Duration, Album_Artwork,false);
                }
            }while(cursor.moveToNext());
            cursor.close();
            return songDetails;
        }else{
            //mySongs.add("No songs loaded");
            cursor.close();
            return null;
        }

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        String songTitle = NowPlaying.songDetails.get(position).getSong_Title();
        Log.i(TAG,"Song Title :" + songTitle);
        int songIndex = position;
        /*ImageView ivCurrentSong = (ImageView) view.findViewById(R.id.ivCurrentSong);
        ivCurrentSong.setImageResource(R.drawable.play);
        ivCurrentSong.setVisibility(View.VISIBLE);*/
        /*Intent in = new Intent(getActivity(),Tab1.class);
        in.putExtra("songIndex",songIndex);
        getActivity().setResult(100,in);
        getActivity().finish();*/
        //ivCurrentSong = (ImageView)view.findViewById(R.id.ivCurrentSong);
        //ivCurrentSong.setBackgroundResource(R.drawable.play);
        //MusicPlayback.play_song(songIndex);
        NowPlaying.playSong(songIndex);
        fragmentActivity.getActionBar().setSelectedNavigationItem(0);
    }

    private class LoadSongsInBackground extends AsyncTask<String,Void,ArrayList<SongDetail>> {
        ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Loading songs...");
            //this.dialog.show();
        }

        @Override
        protected ArrayList<SongDetail> doInBackground(String... params) {
            if(params[0].equals("LOAD_SONGS")){
                return getSongsDetailList();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<SongDetail> songDetails) {
            super.onPostExecute(songDetails);
            if(dialog.isShowing()){
                dialog.dismiss();
            }
            if(!songDetails.isEmpty()){
                NowPlaying.PLAYLIST_STATUS= "LOADED";
                NowPlaying.songDetails = songDetails;
                NowPlaying.totalSongs = songDetails.size();
                adapter = new SongListAdapter(getActivity(), songDetails);
                list.setAdapter(adapter);
                Toast.makeText(getActivity(), NowPlaying.totalSongs + " songs loaded.", Toast.LENGTH_SHORT).show();
                list.setOnItemClickListener(AllSongs.this);
                Log.i(TAG,"Playlist Status : "+ NowPlaying.PLAYLIST_STATUS);
                Log.i(TAG,"All songs loaded.");
            }else{
                Log.i(TAG,"No songs loaded.");
            }
        }
    }
}
