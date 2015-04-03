package com.example.sdmusicplayer.utils;

public final class Constants {

	public final static String VISUALIZATION_TYPE = "visualization_type";
	//Image Loading Constants
	public final static String TYPE_ARTIST = "artist", TYPE_ALBUM = "album", TYPE_GENRE = "genre", TYPE_SONG = "song",
			TYPE_PLAYLIST  = "playlist", ALBUM_SUFFIX = "albartimg", ARTIST_SUFFIX = "artstimg", 
			PLAYLIST_SUFFIX = "plylstimg", GENRE_SUFFIX = "gnreimg", SRC_FIRST_AVAILABLE = "first_avail",
			SRC_LASTFM = "last_fm", SRC_FILE = "from_file", SRC_GALLERY = "from_gallery",
			SIZE_NORMAL = "normal", SIZE_THUMB = "thumb";
	// Bundle & Intent type
    public final static String MIME_TYPE = "mimetype", INTENT_ACTION = "action", DATA_SCHEME = "file";
    
    // Playlists
    public final static long PLAYLIST_UNKNOWN = -1, PLAYLIST_ALL_SONGS = -2, PLAYLIST_QUEUE = -3,
            PLAYLIST_NEW = -4, PLAYLIST_FAVORITES = -5, PLAYLIST_RECENTLY_ADDED = -6;
}
