
package com.example.sdmusicplayer.fragments.list;

import com.example.sdmusicplayer.adapters.list.ArtistListAdapter;
import com.example.sdmusicplayer.fragments.base.ListViewFragment;
import com.example.sdmusicplayer.utils.Constants;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Audio.AudioColumns;

import com.example.sdmusicplayer.R;

@SuppressLint("ValidFragment")
public class ArtistListFragment extends ListViewFragment {
	
    public ArtistListFragment(Bundle args) {
        setArguments(args);
    }

	@Override
    public void setupFragmentData(){
        mAdapter = new ArtistListAdapter(getActivity(), R.layout.listview_song_item, null,
                								new String[] {}, new int[] {}, 0);
    	mProjection = new String[] {
                BaseColumns._ID, MediaColumns.TITLE, AudioColumns.ALBUM, AudioColumns.ARTIST,
                AudioColumns.DURATION
        };
        StringBuilder where = new StringBuilder();
        where.append(AudioColumns.IS_MUSIC + "=1")
        					.append(" AND " + MediaColumns.TITLE + " != ''");
        long artist_id = getArguments().getLong(BaseColumns._ID);
        where.append(" AND " + AudioColumns.ARTIST_ID + "=" + artist_id);
        mWhere = where.toString();        
        mSortOrder = MediaColumns.TITLE;
        mUri = Audio.Media.EXTERNAL_CONTENT_URI;
        mFragmentGroupId = 88;
        mType = Constants.TYPE_ARTIST;
        mTitleColumn = MediaColumns.TITLE; 
    }
}
