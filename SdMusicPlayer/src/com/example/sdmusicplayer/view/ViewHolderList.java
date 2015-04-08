/**
 * 
 */

package com.example.sdmusicplayer.view;

import com.example.sdmusicplayer.R;

import android.view.View;
import android.widget.TextView;

/**
 * @author Andrew Neal
 */
public class ViewHolderList {

    public final TextView mViewHolderDuration;

    public final TextView mViewHolderLineOne;

    public final TextView mViewHolderLineTwo;

    public ViewHolderList(View view) {
        mViewHolderDuration = (TextView)view.findViewById(R.id.listview_item_duration);
        mViewHolderLineOne = (TextView)view.findViewById(R.id.listview_item_line_one);
        mViewHolderLineTwo = (TextView)view.findViewById(R.id.listview_item_line_two);

    }
}
