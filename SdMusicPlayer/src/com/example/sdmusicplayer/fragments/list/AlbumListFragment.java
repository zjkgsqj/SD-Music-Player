package com.example.sdmusicplayer.fragments.list;

import com.example.sdmusicplayer.R;
import com.example.sdmusicplayer.adapters.list.AlbumListAdapter;
import com.example.sdmusicplayer.fragments.base.ListViewFragment;
import com.example.sdmusicplayer.utils.Constants;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Audio.AudioColumns;

@SuppressLint("ValidFragment")
public class AlbumListFragment extends ListViewFragment {

	public AlbumListFragment(Bundle args) {    
		setArguments(args);    
	}

	@Override
	public void setupFragmentData() {
        mAdapter = new AlbumListAdapter(getActivity(), R.layout.listview_song_item, null,
                								new String[] {}, new int[] {}, 0);
    	mProjection = new String[] {
                BaseColumns._ID, MediaColumns.TITLE, AudioColumns.ALBUM, AudioColumns.ARTIST,
                AudioColumns.DURATION
        };
        StringBuilder where = new StringBuilder();
        where.append(AudioColumns.IS_MUSIC + "=1")
        					.append(" AND " + MediaColumns.TITLE + " != ''");
        long albumId = getArguments().getLong(BaseColumns._ID);
        where.append(" AND " + AudioColumns.ALBUM_ID + "=" + albumId);
        mWhere = where.toString();        
        mSortOrder = Audio.Media.TRACK + ", " + Audio.Media.DEFAULT_SORT_ORDER;
        mUri = Audio.Media.EXTERNAL_CONTENT_URI;
        mFragmentGroupId = 89;
        mType = Constants.TYPE_ALBUM;
        mTitleColumn = MediaColumns.TITLE; 
	}

}
