package com.example.sdmusicplayer.adapters.list;

import com.example.sdmusicplayer.adapters.base.ListViewAdapter;
import com.example.sdmusicplayer.helpers.utils.MusicUtils;
import com.example.sdmusicplayer.utils.Constants;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Audio.AudioColumns;

public class ArtistListAdapter extends ListViewAdapter {

	public ArtistListAdapter(Context context, int layout, Cursor c,	String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
	}

	@Override
	public void setupViewData(Cursor mCursor) {
    	mLineOneText = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaColumns.TITLE));    	
    	mLineTwoText = mCursor.getString(mCursor.getColumnIndexOrThrow(AudioColumns.ALBUM));  
    	long duration = mCursor.getInt(mCursor.getColumnIndexOrThrow(AudioColumns.DURATION))/1000;
    	mLineDuration = MusicUtils.makeTimeString(mContext, duration);
        mImageData = new String[]{ mLineTwoText };        
        mPlayingId = MusicUtils.getCurrentAudioId();
        mCurrentId = mCursor.getLong(mCursor.getColumnIndexOrThrow(BaseColumns._ID));
        mListType = Constants.TYPE_ARTIST;
	}
}
