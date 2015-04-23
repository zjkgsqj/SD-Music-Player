
package com.example.sdmusicplayer.fragments.list;

import java.io.File;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.Files.FileColumns;
import android.view.View;
import android.widget.AdapterView;

import com.example.sdmusicplayer.R;
import com.example.sdmusicplayer.SongListActivity;
import com.example.sdmusicplayer.TracksBrowser;
import com.example.sdmusicplayer.adapters.list.FolderAdapter;
import com.example.sdmusicplayer.fragments.base.ListViewFragment;
import com.example.sdmusicplayer.helpers.utils.MusicUtils;
import com.example.sdmusicplayer.utils.Constants;
import com.example.sdmusicplayer.utils.Utils;


public class FolderFragment extends ListViewFragment{
	
    public void setupFragmentData(){
        mAdapter = new FolderAdapter(getActivity(), R.layout.listview_song_item, null,
                										new String[] {}, new int[] {}, 0);
    	mProjection = new String[] {
                BaseColumns._ID, FileColumns.DATA, FileColumns.PARENT,
                "count(" + FileColumns.PARENT + ") as " + Constants.NUMSONGS
        };
    	StringBuilder Where = new StringBuilder(FileColumns.MEDIA_TYPE
				+ " = " + FileColumns.MEDIA_TYPE_AUDIO );
    	Where.append(") group by ( ").append(FileColumns.PARENT);
    	mWhere = Where.toString();
        mSortOrder = FileColumns.DATA;
        mUri = MediaStore.Files.getContentUri(Constants.EXTERNAL);
        mFragmentGroupId = 4;
        mType = Constants.TYPE_FOLDER;         
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
    	Bundle bundle = new Bundle();
    	Cursor mCursor = (Cursor) mListView.getItemAtPosition(position);
    	String filePath = mCursor.getString(mCursor.getColumnIndexOrThrow(FileColumns.DATA)); 
		String folderPath = MusicUtils.getFolderPath(filePath);
    	bundle.putString(FileColumns.PARENT, mCursor.getString(
    			mCursor.getColumnIndexOrThrow(FileColumns.PARENT)));
    	bundle.putString(Constants.SOURCE_TYPE, Constants.TYPE_FOLDER);
    	bundle.putString(Constants.FOLDER_NAME, MusicUtils.getFolderName(folderPath));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClass(getActivity(), SongListActivity.class);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }
}
