
package com.example.sdmusicplayer.fragments.grid;

import com.example.sdmusicplayer.adapters.grid.AlbumAdapter;
import com.example.sdmusicplayer.fragments.base.GridViewFragment;
import com.example.sdmusicplayer.utils.Constants;

import android.provider.BaseColumns;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.AlbumColumns;

import com.example.sdmusicplayer.R;

public class AlbumsFragment extends GridViewFragment {

    public void setupFragmentData(){
    	mAdapter = new AlbumAdapter(getActivity(), R.layout.gridview_items, null,
                					new String[] {}, new int[] {}, 0); 
    	mProjection = new String []{
                BaseColumns._ID, AlbumColumns.ALBUM, AlbumColumns.ARTIST, AlbumColumns.ALBUM_ART
        };
        mUri = Audio.Albums.EXTERNAL_CONTENT_URI;
        mSortOrder = Audio.Albums.DEFAULT_SORT_ORDER;
        mFragmentGroupId = 2;
        mType = Constants.TYPE_ALBUM;
    }
    
}
