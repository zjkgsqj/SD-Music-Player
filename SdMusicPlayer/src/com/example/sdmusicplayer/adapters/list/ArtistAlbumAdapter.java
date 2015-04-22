
package com.example.sdmusicplayer.adapters.list;

import com.example.sdmusicplayer.adapters.base.ListViewAdapter;
import com.example.sdmusicplayer.helpers.utils.MusicUtils;
import com.example.sdmusicplayer.utils.Constants;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio.AlbumColumns;
public class ArtistAlbumAdapter extends ListViewAdapter {

    public ArtistAlbumAdapter(Context context, int layout, Cursor c, String[] from, int[] to,
            int flags) {
        super(context, layout, c, from, to, flags);
    }

    public void setupViewData( Cursor mCursor ){
    	mLineOneText = mCursor.getString(mCursor.getColumnIndexOrThrow(AlbumColumns.ALBUM));    	
        int songs_plural = mCursor.getInt(mCursor.getColumnIndexOrThrow(AlbumColumns.NUMBER_OF_SONGS));
    	mLineTwoText =MusicUtils.makeAlbumsLabel(mContext, 0, songs_plural, true );    	
        String artistName = mCursor.getString(mCursor.getColumnIndexOrThrow(AlbumColumns.ARTIST));        
        String albumId = mCursor.getString(mCursor.getColumnIndexOrThrow(BaseColumns._ID));
        mImageData = new String[]{ albumId , artistName, mLineOneText };        
        mPlayingId = MusicUtils.getCurrentAlbumId();
        mCurrentId = Long.parseLong(albumId);        
        mListType = Constants.TYPE_ALBUM;   	
    }
}
