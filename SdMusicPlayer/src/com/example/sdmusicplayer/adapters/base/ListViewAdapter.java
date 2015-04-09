package com.example.sdmusicplayer.adapters.base;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.MediaColumns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

import com.example.sdmusicplayer.R;
import com.example.sdmusicplayer.helpers.utils.MusicUtils;
import com.example.sdmusicplayer.utils.Constants;
import com.example.sdmusicplayer.view.ViewHolderList;

public class ListViewAdapter extends SimpleCursorAdapter {
	
    private WeakReference<ViewHolderList> holderReference;
    
    protected Context mContext;
    
    private int left, top;    
    
    public String mListType = null,  mLineOneText = null, mLineTwoText = null ,mLineDuration = null;
    
    public String[] mImageData = null;
    
    public long mPlayingId = 0, mCurrentId = 0;
    
    public boolean showContextEnabled = true;
    
    //private ImageProvider mImageProvider;

    /**
     * Used to quickly show our the ContextMenu
     */
    /*private final View.OnClickListener showContextMenu = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.showContextMenu();
        }
    };*/
    
    public ListViewAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        mContext = context;
        // Helps center the text in the Playlist/Genre tab
        left = mContext.getResources().getDimensionPixelSize(
                R.dimen.listview_items_padding_left_top);
        top = mContext.getResources().getDimensionPixelSize(
                R.dimen.listview_items_padding_gp_top);
        
    	//mImageProvider = ImageProvider.getInstance( (Activity) mContext );
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view = super.getView(position, convertView, parent);

        Cursor mCursor = (Cursor) getItem(position);
        setupViewData(mCursor);

        final ViewHolderList viewholder;
        if ( view != null ) {
            viewholder = new ViewHolderList(view);
            holderReference = new WeakReference<ViewHolderList>(viewholder);
            view.setTag(holderReference.get());
        } else {
            viewholder = (ViewHolderList)convertView.getTag();
        }

        if( mLineOneText != null ){
            holderReference.get().mViewHolderLineOne.setText(mLineOneText);
        }
        else{
        	holderReference.get().mViewHolderLineOne.setVisibility(View.GONE);
        }

        if( mLineTwoText != null ){
            holderReference.get().mViewHolderLineTwo.setText(mLineTwoText);
        }
        else{
            holderReference.get().mViewHolderLineOne.setPadding(left, top, 0, 0);
            holderReference.get().mViewHolderLineTwo.setVisibility(View.GONE);
        }
        if( mLineDuration != null){
        	holderReference.get().mViewHolderDuration.setText(mLineDuration);
        }
        else{
        	holderReference.get().mViewHolderDuration.setVisibility(View.GONE);
        }
        
        /*if( mImageData != null ){

            ImageInfo mInfo = new ImageInfo();
            mInfo.type = mListType;
            mInfo.size = Constants.SIZE_THUMB;
            mInfo.source = Constants.SRC_FIRST_AVAILABLE;
            mInfo.data = mImageData;
            
            //mImageProvider.loadImage( viewholder.mViewHolderImage, mInfo ); 
        }
        else{
            holderReference.get().mViewHolderImage.setVisibility(View.GONE);
        }*/
              
        return view;
    }
    
    //public abstract void setupViewData( Cursor mCursor ); 
    public void setupViewData( Cursor mCursor ){
    	mLineOneText = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaColumns.TITLE));    	
    	mLineTwoText = mCursor.getString(mCursor.getColumnIndexOrThrow(AudioColumns.ARTIST)); 
    	long duration = mCursor.getInt(mCursor.getColumnIndexOrThrow(AudioColumns.DURATION))/1000;
    	mLineDuration = MusicUtils.makeTimeString(mContext, duration);
    	//mLineDuration = duration / 60 + ":" + duration % 60; 
        mImageData = new String[]{ mLineTwoText };        
        //mPlayingId = MusicUtils.getCurrentAudioId();
        mCurrentId = mCursor.getLong(mCursor.getColumnIndexOrThrow(BaseColumns._ID));      
        mListType = Constants.TYPE_ARTIST;   	
    }
    
}
