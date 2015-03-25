
package com.example.sdmusicplayer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.sdmusicplayer.R;

public class artistFragment extends Fragment{

	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState){
		View contextView = inflater.inflate(R.layout.fragment_main, container, false);
		return contextView;  
	}

}
