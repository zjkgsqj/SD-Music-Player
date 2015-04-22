
package com.example.sdmusicplayer.adapters.grid;

import com.example.sdmusicplayer.adapters.base.GridViewAdapter;
import com.example.sdmusicplayer.helpers.utils.MusicUtils;
import com.example.sdmusicplayer.utils.Constants;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.ArtistColumns;


public class ArtistAdapter extends GridViewAdapter {

    public ArtistAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    public void setupViewData(Cursor mCursor){

    	mLineOneText = mCursor.getString(mCursor.getColumnIndexOrThrow(ArtistColumns.ARTIST));
        int albums_plural = mCursor.getInt(mCursor.getColumnIndexOrThrow(ArtistColumns.NUMBER_OF_ALBUMS));
        boolean unknown = mLineOneText == null || mLineOneText.equals(MediaStore.UNKNOWN_STRING);
        mLineTwoText = MusicUtils.makeAlbumsLabel(mContext, albums_plural, 0, unknown);        
        mGridType = Constants.TYPE_ARTIST;        
        mImageData = new String[]{mLineOneText};
        mPlayingId = MusicUtils.getCurrentArtistId();
        mCurrentId = mCursor.getLong(mCursor.getColumnIndexOrThrow(BaseColumns._ID));
        
    }
}
