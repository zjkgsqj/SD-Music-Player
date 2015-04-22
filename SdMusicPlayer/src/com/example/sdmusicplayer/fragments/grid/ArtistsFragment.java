
package com.example.sdmusicplayer.fragments.grid;

import com.example.sdmusicplayer.adapters.grid.ArtistAdapter;
import com.example.sdmusicplayer.fragments.base.GridViewFragment;
import com.example.sdmusicplayer.utils.Constants;

import android.provider.BaseColumns;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.ArtistColumns;

import com.example.sdmusicplayer.R;

public class ArtistsFragment extends GridViewFragment{

    public void setupFragmentData(){
    	mAdapter = new ArtistAdapter(getActivity(), R.layout.gridview_items, null, 
    									new String[] {}, new int[] {}, 0); 
    	mProjection = new String []{
                BaseColumns._ID, ArtistColumns.ARTIST, ArtistColumns.NUMBER_OF_ALBUMS
        };
        mUri = Audio.Artists.EXTERNAL_CONTENT_URI;
        mSortOrder = Audio.Artists.DEFAULT_SORT_ORDER;
        mFragmentGroupId = 1;
        mType = Constants.TYPE_ARTIST;
    }
}
