package com.example.sdmusicplayer.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.AlbumColumns;
import android.provider.MediaStore.Audio.ArtistColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sdmusicplayer.R;
import com.example.sdmusicplayer.TracksBrowser;
import com.example.sdmusicplayer.info.ImageInfo;
import com.example.sdmusicplayer.info.ImageProvider;

/**
 *
 */
public class Utils {

	/**
     * @param context
     * @return if a Tablet is the device being used
     * 判断是否是平板
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
    
    /**
     * UP accordance without the icon
     * 
     * @param actionBar
     */
    public static void showUpTitleOnly(ActionBar actionBar) {
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE,
                ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE
                        | ActionBar.DISPLAY_SHOW_HOME);
    }
    
    /**
     * Replace the characters not allowed in file names with underscore
     * @param name
     * @return
     */
    public static String escapeForFileSystem(String name) {
        return name.replaceAll("[\\\\/:*?\"<>|]+", "_");
    }
    
    /**
     * Static utility function to download the file from the specified URL to the specified file.
     * @param urlString
     * @param outFile
     * @return true if the download succeeded false otherwise
     */
    public static boolean downloadFile(String urlString, File outFile) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;

        try {
            File dir = outFile.getParentFile();
            if (!dir.exists() && !dir.mkdirs())
                return false;

            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            final InputStream in =
                    new BufferedInputStream(urlConnection.getInputStream());
            out = new BufferedOutputStream(new FileOutputStream(outFile));

            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }

        } catch (final IOException e) {
            return false;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (final IOException e) {
                    return false;
                }
            }
        }
        return true;
    }
    
    // Returns if we're viewing an album
    public static boolean isAlbum(String mimeType) {
        return Audio.Albums.CONTENT_TYPE.equals(mimeType);
    }

    // Returns if we're viewing an artists albums
    public static boolean isArtist(String mimeType) {
        return Audio.Artists.CONTENT_TYPE.equals(mimeType);
    }

    // Returns if we're viewing a genre
    public static boolean isGenre(String mimeType) {
        return Audio.Genres.CONTENT_TYPE.equals(mimeType);
    }
    
    /**
     * @param artistName
     * @param id
     * @param key
     * @param context
     */
    public static void setArtistId(String artistName, long id, String key, Context context) {
        SharedPreferences settings = context.getSharedPreferences(key, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(artistName, id);
        editor.commit();
    }

    /**
     * @param artistName
     * @param key
     * @param context
     * @return artist ID
     */
    public static Long getArtistId(String artistName, String key, Context context) {
        SharedPreferences settings = context.getSharedPreferences(key, 0);
        return settings.getLong(artistName, 0);
    }
    
    /**
     * @param message
     */
    public static void showToast(int message, Toast mToast, Context context) {
        if (mToast == null) {
            mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
        mToast.setText(context.getString(message));
        mToast.show();
    }
    
    /**
     * Used to fit a Bitmap nicely inside a View
     * 
     * @param view
     * @param bitmap
     */
	@SuppressWarnings("deprecation")
    public static void setBackground(View view, Bitmap bitmap) {

        if (bitmap == null) {
            view.setBackgroundResource(0);
            return;
        }

        int vwidth = view.getWidth();
        int vheight = view.getHeight();
        int bwidth = bitmap.getWidth();
        int bheight = bitmap.getHeight();

        float scalex = (float)vwidth / bwidth;
        float scaley = (float)vheight / bheight;
        float scale = Math.max(scalex, scaley) * 1.0f;

        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap background = Bitmap.createBitmap(vwidth, vheight, config);

        Canvas canvas = new Canvas(background);

        Matrix matrix = new Matrix();
        matrix.setTranslate(-bwidth / 2, -bheight / 2);
        matrix.postScale(scale, scale);
        matrix.postTranslate(vwidth / 2, vheight / 2);

        canvas.drawBitmap(bitmap, matrix, null);

        view.setBackgroundDrawable(new BitmapDrawable(view.getResources(), background));
    }

	/**
     * @return A custom ContextMenu header
     */
    public static  View setHeaderLayout(String Type, Cursor cursor, Activity activity) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View header = inflater.inflate(R.layout.context_menu_header, null, false);

        // Artist image
        final ImageView mHanderImage = (ImageView)header.findViewById(R.id.header_image);
        String albumId="",artistName="",albumName="";

        if(Type == Constants.TYPE_ALBUM){
            albumName = cursor.getString(cursor.getColumnIndexOrThrow(AlbumColumns.ALBUM));
            artistName = cursor.getString(cursor.getColumnIndexOrThrow(AlbumColumns.ARTIST));
            albumId = cursor.getString(cursor.getColumnIndexOrThrow(BaseColumns._ID));
        }
        else{
            artistName = cursor.getString(cursor.getColumnIndexOrThrow(ArtistColumns.ARTIST));
        }
        
        
        ImageInfo mInfo = new ImageInfo();
        mInfo.type = Type;
        mInfo.size = Constants.SIZE_THUMB;
        mInfo.source = Constants.SRC_FIRST_AVAILABLE;
        mInfo.data = (Type == Constants.TYPE_ALBUM ? new String[]{ albumId , artistName, albumName } : new String[]{ artistName});
       
        ImageProvider.getInstance(activity).loadImage( mHanderImage, mInfo );

        // Set artist name
        TextView headerText = (TextView)header.findViewById(R.id.header_text);
        headerText.setText( (Type == Constants.TYPE_ALBUM ? albumName : artistName));
        headerText.setBackgroundColor((activity).getResources().getColor(R.color.transparent_black));
        return header;
    }
    
    public static void startTracksBrowser(String Type, long id, Cursor mCursor, Context context ) {
    	Bundle bundle = new Bundle();
        if( Type == Constants.TYPE_ARTIST ){
        	String artistName = mCursor.getString(mCursor.getColumnIndexOrThrow(ArtistColumns.ARTIST));
            String artistNulAlbums = mCursor.getString( mCursor.getColumnIndexOrThrow(ArtistColumns.NUMBER_OF_ALBUMS));        
            bundle.putString(Constants.MIME_TYPE, Audio.Artists.CONTENT_TYPE);
            bundle.putString(Constants.ARTIST_KEY, artistName);
            bundle.putString(Constants.NUMALBUMS, artistNulAlbums);
            Utils.setArtistId(artistName, id, Constants.ARTIST_ID, context);    	
        }
        else if( Type == Constants.TYPE_ALBUM ){            
            String artistName = mCursor.getString(mCursor.getColumnIndexOrThrow(AlbumColumns.ARTIST));
            String albumName = mCursor.getString(mCursor.getColumnIndexOrThrow(AlbumColumns.ALBUM));
            String albumId = mCursor.getString(mCursor.getColumnIndexOrThrow(BaseColumns._ID));
            bundle.putString(Constants.MIME_TYPE, Audio.Albums.CONTENT_TYPE);
            bundle.putString(Constants.ARTIST_KEY, artistName);
            bundle.putString(Constants.ALBUM_KEY, albumName);
            bundle.putString(Constants.ALBUM_ID_KEY, albumId);
            bundle.putBoolean(Constants.UP_STARTS_ALBUM_ACTIVITY, true);
        }
        /*else if( Type == Constants.TYPE_GENRE ){
            String genreKey = mCursor.getString(mCursor.getColumnIndexOrThrow(Audio.Genres.NAME));
            bundle.putString(Constants.MIME_TYPE, Audio.Genres.CONTENT_TYPE);
            bundle.putString(Constants.GENRE_KEY, genreKey);
        }
        else if( Type == Constants.TYPE_PLAYLIST ){
            String playlistName = mCursor.getString(mCursor.getColumnIndexOrThrow(PlaylistsColumns.NAME));
            bundle.putString(Constants.MIME_TYPE, Audio.Playlists.CONTENT_TYPE);
            bundle.putString(Constants.PLAYLIST_NAME, playlistName);
            bundle.putLong(BaseColumns._ID, id);
        }*/
        
        bundle.putLong(BaseColumns._ID, id);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClass(context, TracksBrowser.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
