
package com.example.sdmusicplayer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sdmusicplayer.R;
import com.example.sdmusicplayer.SongListActivity;
import com.example.sdmusicplayer.utils.Constants;

public class MainFragment extends Fragment{

	private TextView localMusic, myFavorite, recentPlay, playList;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState){
		View contextView = inflater.inflate(R.layout.fragment_main, container, false);
		localMusic = (TextView)contextView.findViewById(R.id.label_local_music);
		myFavorite = (TextView)contextView.findViewById(R.id.label_my_favorite);
		recentPlay = (TextView)contextView.findViewById(R.id.label_recent_play);
		playList = (TextView)contextView.findViewById(R.id.label_play_list);
		localMusic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	Bundle bundle = new Bundle();
            	bundle.putString(Constants.SOURCE_TYPE, Constants.TYPE_SONG);
            	Intent mIntent = new Intent(getActivity(), SongListActivity.class);
            	mIntent.putExtras(bundle);
				startActivity(mIntent);
            }
        });
		myFavorite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               
            }
        });
		recentPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               
            }
        });
		playList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               
            }
        });
		
		
		
		
		return contextView;  
	}

}
