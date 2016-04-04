package com.prasad.musicplayer;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Shree Ganesh on 9/6/2014.
 */
public class SongListAdapter extends BaseAdapter {
    private Activity activity;
    private static ArrayList<SongDetail> data;
    private static LayoutInflater inflater=null;

    public SongListAdapter(Activity a,ArrayList<SongDetail> d){
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // title
        TextView artist = (TextView)vi.findViewById(R.id.artist); // artist name
        TextView duration = (TextView)vi.findViewById(R.id.duration); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image


        String songPath = data.get(position).getSong_Path();
        String songTitle = data.get(position).getSong_Title();
        String songDuration = data.get(position).getSong_Duration();
        long temp = Long.parseLong(songDuration);

        long minutes = TimeUnit.MILLISECONDS.toMinutes(temp);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(temp)/10;
        String songArtist = data.get(position).getArtist();
        String artWork = data.get(position).getAlbum_Artwork();

        // Setting all values in listview
        title.setText(songTitle);
        artist.setText(songArtist);
        duration.setText(minutes+":"+seconds);
        if(artWork==null){
            //artWork = data.get(position).getAlbum_Artwork();
            thumb_image.setImageResource(R.drawable.default_cover_orange);
        }else{
            thumb_image.setImageURI(Uri.fromFile(new File(artWork)));
        }
        return vi;
    }

}
