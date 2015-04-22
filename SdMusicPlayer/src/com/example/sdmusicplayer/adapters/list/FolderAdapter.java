
package com.example.sdmusicplayer.adapters.list;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore.Files.FileColumns;

import com.example.sdmusicplayer.R;
import com.example.sdmusicplayer.adapters.base.ListViewAdapter;
import com.example.sdmusicplayer.helpers.utils.MusicUtils;
import com.example.sdmusicplayer.utils.Constants;


public class FolderAdapter extends ListViewAdapter {

	public FolderAdapter(Context context, int layout, Cursor c,	String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
	}

	@Override
	public void setupViewData(Cursor mCursor) { 	
		int numsongs = mCursor.getInt(mCursor.getColumnIndexOrThrow(Constants.NUMSONGS)); 
		String filePath = mCursor.getString(mCursor.getColumnIndexOrThrow(FileColumns.DATA)); 
		String folderPath = MusicUtils.getFolderPath(filePath);
		mLineDuration = MusicUtils.makeAlbumsLabel(mContext, 0, numsongs,true);
		mLineOneText = MusicUtils.getFolderName(folderPath);
    	mLineTwoText = folderPath;
        mImageData = new String[]{ mLineTwoText };        
        mPlayingId = MusicUtils.getCurrentAudioId();
        mCurrentId = mCursor.getLong(mCursor.getColumnIndexOrThrow(BaseColumns._ID));      
        mListType = Constants.TYPE_FOLDER;   	
	}

}
