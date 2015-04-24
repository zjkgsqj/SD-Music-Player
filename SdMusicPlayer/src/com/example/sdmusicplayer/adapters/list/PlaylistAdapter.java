
package com.example.sdmusicplayer.adapters.list;

import com.example.sdmusicplayer.adapters.base.ListViewAdapter;
import com.example.sdmusicplayer.helpers.utils.MusicUtils;
import com.example.sdmusicplayer.utils.Constants;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Audio.PlaylistsColumns;

public class PlaylistAdapter extends ListViewAdapter {    
    
    public PlaylistAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    public void setupViewData( Cursor mCursor ){
    	mLineOneText = mCursor.getString(mCursor.getColumnIndexOrThrow(PlaylistsColumns.NAME));
    	int numsongs = mCursor.getInt(mCursor.getColumnIndexOrThrow(Constants.NUMSONGS)); 
    	mLineTwoText = MusicUtils.makeAlbumsLabel(mContext, 0, numsongs,true);
    }    
}
