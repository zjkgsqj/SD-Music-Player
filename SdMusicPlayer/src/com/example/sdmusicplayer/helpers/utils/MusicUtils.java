package com.example.sdmusicplayer.helpers.utils;

import java.util.Arrays;
import java.util.Formatter;
import java.util.Locale;
import java.util.Random;

import com.example.sdmusicplayer.R;
import com.example.sdmusicplayer.service.MusicService;
import com.example.sdmusicplayer.service.aidl.IMusicService;
import com.example.sdmusicplayer.utils.Constants;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio;

public class MusicUtils {

	// Used to make number of albums/songs/time strings
    private final static StringBuilder sFormatBuilder = new StringBuilder();
    private static final Object[] sTimeArgs = new Object[5];
    private final static Formatter sFormatter = new Formatter(sFormatBuilder, Locale.getDefault());
    private final static long[] sEmptyList = new long[0];
    
    public static IMusicService mService = null;
    private static Equalizer mEqualizer = null;    
    private static BassBoost mBoost = null;
    
	
	/**
	 * 格式化播放时长
     * @param context
     * @param secs
     * @return time String
     */
    public static String makeTimeString(Context context, long secs) {

        String durationformat = context.getString(secs < 3600 ? R.string.durationformatshort
                : R.string.durationformatlong);

        /*
         * Provide multiple arguments so the format can be changed easily by
         * modifying the xml.
         */
        sFormatBuilder.setLength(0);

        final Object[] timeArgs = sTimeArgs;
        timeArgs[0] = secs / 3600;
        timeArgs[1] = secs / 60;
        timeArgs[2] = secs / 60 % 60;
        timeArgs[3] = secs;
        timeArgs[4] = secs % 60;

        return sFormatter.format(durationformat, timeArgs).toString();
    }
    
    /**
     * @param context
     * @param cursor
     */
    public static void shuffleAll(Context context, Cursor cursor) {

        long[] list = getRandomSongListForCursor(cursor);
        playAll(context, list, -1, false);
    }

    /**
     * @param context
     * @param cursor
     */
    public static void playAll(Context context, Cursor cursor) {
        playAll(context, cursor, 0, false);
    }

    /**
     * @param context
     * @param cursor
     * @param position
     */
    public static void playAll(Context context, Cursor cursor, int position) {
        playAll(context, cursor, position, false);
    }

    /**
     * @param context
     * @param list
     * @param position
     */
    public static void playAll(Context context, long[] list, int position) {
        playAll(context, list, position, false);
    }

    /**
     * @param context
     * @param cursor
     * @param position
     * @param force_shuffle
     */
    private static void playAll(Context context, Cursor cursor, int position, boolean force_shuffle) {

        long[] list = getSongListForCursor(cursor);
        playAll(context, list, position, force_shuffle);
    }

    /**
     * @param cursor
     * @return
     */
    public static long[] getSongListForCursor(Cursor cursor) {
        if (cursor == null) {
            return sEmptyList;
        }
        int len = cursor.getCount();
        long[] list = new long[len];
        cursor.moveToFirst();
        int colidx = -1;
        try {
            colidx = cursor.getColumnIndexOrThrow(Audio.Playlists.Members.AUDIO_ID);
        } catch (IllegalArgumentException ex) {
            colidx = cursor.getColumnIndexOrThrow(BaseColumns._ID);
        }
        for (int i = 0; i < len; i++) {
            list[i] = cursor.getLong(colidx);
            cursor.moveToNext();
        }
        return list;
    }
    
    /**
     * @param cursor
     * @return
     */
    public static long[] getRandomSongListForCursor(Cursor cursor) {
        if (cursor == null) {
            return sEmptyList;
        }
        int len = cursor.getCount();
        long[] list = new long[len];
        cursor.moveToFirst();
        int colidx = -1;
        try {
            colidx = cursor.getColumnIndexOrThrow(Audio.Playlists.Members.AUDIO_ID);
        } catch (IllegalArgumentException ex) {
            colidx = cursor.getColumnIndexOrThrow(BaseColumns._ID);
        }
        for (int i = 0; i < len; i++) {
            list[i] = cursor.getLong(colidx);
            cursor.moveToNext();
        }
        int index;
        Random random = new Random();
        for (int i = list.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            if (index != i)
            {
            	list[index] ^= list[i];
            	list[i] ^= list[index];
            	list[index] ^= list[i];
            }
        }
        return list;
    }
    
    /**
     * 从position位置开始播放歌曲，force_shuffle为是否随机播放
     * @param context
     * @param list
     * @param position
     * @param force_shuffle
     */
    private static void playAll(Context context, long[] list, int position, boolean force_shuffle) {
        if (list.length == 0 || mService == null) {
            return;
        }
        try {
            if (force_shuffle) {
                mService.setShuffleMode(MusicService.SHUFFLE_NORMAL);
            }
            long curid = mService.getAudioId();
            int curpos = mService.getQueuePosition();
            if (position != -1 && curpos == position && curid == list[position]) {
                // The selected file is the file that's currently playing;
                // figure out if we need to restart with a new playlist,
                // or just launch the playback activity.
                long[] playlist = mService.getQueue();
                if (Arrays.equals(list, playlist)) {
                    // we don't need to set a new list, but we should resume
                    // playback if needed
                    mService.play();
                    return;
                }
            }
            if (position < 0) {
                position = 0;
            }
            mService.open(list, force_shuffle ? -1 : position);
            mService.play();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void releaseEqualizer(){
    	if(mEqualizer != null){
    		mEqualizer.release();
    	}
    	if(mBoost != null){
    		mBoost.release();
    	}
    }
    /**
     * 初始化均衡器
     * @param media player from music service.
     */
    public static void initEqualizer(MediaPlayer player, Context context){
    	releaseEqualizer();
    	int id = player.getAudioSessionId();
    	mEqualizer = new Equalizer(1,id);
    	mBoost = new BassBoost(1, id);
    	updateEqualizerSettings(context);
    }

    public static int[] getEqualizerFrequencies(){
    	short numBands = mEqualizer.getNumberOfBands()<=6?mEqualizer.getNumberOfBands():6;	
    	int[] freqs= new int[numBands];
    	if(mEqualizer != null){
			for( int i = 0; i <= numBands-1 ; i++ ){
				int[] temp = mEqualizer.getBandFreqRange((short)i);
				freqs[i] = ((temp[1]-temp[0])/2)+temp[0];
			}
			return freqs;
    	}
    	return null;
    }
    
	public static void updateEqualizerSettings(Context context){
        SharedPreferences mPreferences = context.getSharedPreferences(Constants.SD_PREFERENCES, Context.MODE_WORLD_READABLE
                | Context.MODE_WORLD_WRITEABLE);

    	if(mBoost != null){
    		mBoost.setEnabled(mPreferences.getBoolean("simple_eq_boost_enable", false));
    		mBoost.setStrength ((short)(mPreferences.getInt("simple_eq_bboost",0)*10));
    	}
    	
    	if(mEqualizer != null){
	    	mEqualizer.setEnabled(mPreferences.getBoolean("simple_eq_equalizer_enable", false));
	    	short numBands = mEqualizer.getNumberOfBands()<=6?mEqualizer.getNumberOfBands():6;
	    	short r[] = mEqualizer.getBandLevelRange();
	        short min_level = r[0];
	        short max_level = r[1];
	    	for( int i = 0; i <= (numBands -1) ; i++ ){
	            int new_level = min_level + (max_level - min_level) * mPreferences.getInt("simple_eq_seekbars"+String.valueOf(i),100) / 100; 
	            mEqualizer.setBandLevel ((short)i, (short)new_level);
	    	}
    	}
    }

    /**
     * @param mContext
     * @return
     */
    public static int getCardId(Context mContext) {
        ContentResolver res = mContext.getContentResolver();
        Cursor c = res.query(Uri.parse("content://media/external/fs_id"), null, null, null, null);
        int id = -1;
        if (c != null) {
            c.moveToFirst();
            id = c.getInt(0);
            c.close();
        }
        return id;
    }
    
    /**
     * @param context
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    public static Cursor query(Context context, Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        return query(context, uri, projection, selection, selectionArgs, sortOrder, 0);
    }
    
    /**
     * @param context
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @param limit
     * @return
     */
    public static Cursor query(Context context, Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder, int limit) {
        try {
            ContentResolver resolver = context.getContentResolver();
            if (resolver == null) {
                return null;
            }
            if (limit > 0) {
                uri = uri.buildUpon().appendQueryParameter("limit", "" + limit).build();
            }
            return resolver.query(uri, projection, selection, selectionArgs, sortOrder);
        } catch (UnsupportedOperationException ex) {
            return null;
        }
    }
    
    /**
     * @return duration of a track
     */
    public static long getDuration() {
        if (mService != null) {
            try {
                return mService.duration();
            } catch (RemoteException e) {
            }
        }
        return 0;
    }
    
    /**
     * @return current album ID
     */
    public static long getCurrentAlbumId() {

        if (mService != null) {
            try {
                return mService.getAlbumId();
            } catch (RemoteException ex) {
            }
        }
        return -1;
    }

    /**
     * @return current artist ID
     */
    public static long getCurrentArtistId() {

        if (MusicUtils.mService != null) {
            try {
                return mService.getArtistId();
            } catch (RemoteException ex) {
            }
        }
        return -1;
    }

    /**
     * @return current track ID
     */
    public static long getCurrentAudioId() {

        if (MusicUtils.mService != null) {
            try {
                return mService.getAudioId();
            } catch (RemoteException ex) {
            }
        }
        return -1;
    }

    /**
     * @return current artist name
     */
    public static String getArtistName() {

        if (mService != null) {
            try {
                return mService.getArtistName();
            } catch (RemoteException ex) {
            }
        }
        return null;
    }

    /**
     * @return current album name
     */
    public static String getAlbumName() {

        if (mService != null) {
            try {
                return mService.getAlbumName();
            } catch (RemoteException ex) {
            }
        }
        return null;
    }

    /**
     * @return current track name
     */
    public static String getTrackName() {

        if (mService != null) {
            try {
                return mService.getTrackName();
            } catch (RemoteException ex) {
            }
        }
        return null;
    }
}
