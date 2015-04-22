
package com.example.sdmusicplayer.fragments.list;

import com.example.sdmusicplayer.R;
import com.example.sdmusicplayer.adapters.list.ArtistAlbumAdapter;
import com.example.sdmusicplayer.fragments.base.ListViewFragment;
import com.example.sdmusicplayer.helpers.utils.MusicUtils;
import com.example.sdmusicplayer.utils.Constants;
import com.example.sdmusicplayer.utils.Utils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.AlbumColumns;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;

@SuppressLint("ValidFragment")
public class ArtistAlbumsFragment extends ListViewFragment{

    public ArtistAlbumsFragment(Bundle args) {
        setArguments(args);
    }    

    public void setupFragmentData(){
        mAdapter =new ArtistAlbumAdapter(getActivity(), R.layout.listview_song_item, null,
                									new String[] {}, new int[] {}, 0);
    	mProjection = new String[] {
                BaseColumns._ID, AlbumColumns.ALBUM, AlbumColumns.NUMBER_OF_SONGS,
                AlbumColumns.ARTIST
        };
        mSortOrder =  Audio.Albums.DEFAULT_SORT_ORDER;
        long artistId = getArguments().getLong((BaseColumns._ID));
        mUri = Audio.Artists.Albums.getContentUri(Constants.EXTERNAL, artistId);
        mFragmentGroupId = 7;
        mType = Constants.TYPE_ALBUM;     
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
    	Utils.startTracksBrowser(mType, id, mCursor, getActivity());
    }

    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        menu.add(mFragmentGroupId, PLAY_SELECTION, 0, getResources().getString(R.string.play_all));
        menu.add(mFragmentGroupId, ADD_TO_PLAYLIST, 0, getResources().getString(R.string.add_to_playlist));
        menu.add(mFragmentGroupId, SEARCH, 0, getResources().getString(R.string.search));
        mCurrentId = mCursor.getString( mCursor.getColumnIndexOrThrow( BaseColumns._ID ) );       
        menu.setHeaderView( Utils.setHeaderLayout( mType, mCursor, getActivity() ) );
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	if(item.getGroupId() == mFragmentGroupId){
	        switch (item.getItemId()) {
	            case PLAY_SELECTION: {
	                long[] list = MusicUtils.getSongListForAlbum(getActivity(),
	                        Long.parseLong(mCurrentId));
	                MusicUtils.playAll(getActivity(), list, 0);
	                break;
	            }
	            case ADD_TO_PLAYLIST: {
	                Intent intent = new Intent(Constants.INTENT_ADD_TO_PLAYLIST);
	                long[] list = MusicUtils.getSongListForAlbum(getActivity(),
	                        Long.parseLong(mCurrentId));
	                intent.putExtra(Constants.INTENT_PLAYLIST_LIST, list);
	                getActivity().startActivity(intent);
	                break;
	            }
	            case SEARCH: {
	                MusicUtils.doSearch(getActivity(), mCursor, mCursor.getColumnIndexOrThrow(AlbumColumns.ALBUM));
	                break;
	            }
	            default:
	                break;
	        }
	        return true;
    	}
        return super.onContextItemSelected(item);
    }*/
}
