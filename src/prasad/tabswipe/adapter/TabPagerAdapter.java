package prasad.tabswipe.adapter;

import com.prasad.musicplayer.NowPlaying;
import com.prasad.musicplayer.AllSongs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
/**
 * Created by Shree Ganesh on 9/9/2014.
 */
public class TabPagerAdapter extends FragmentPagerAdapter {
    public TabPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        switch(index){
            case 0:
                return new NowPlaying();
            case 1:
                return new AllSongs();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
