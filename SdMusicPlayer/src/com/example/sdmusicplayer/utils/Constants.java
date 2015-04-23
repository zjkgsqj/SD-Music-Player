package com.example.sdmusicplayer.utils;

public final class Constants {

	// Last.fm API
    public final static String LASTFM_API_KEY = "0bec3f7ec1f914d7c960c12a916c8fb3";
    // Storage Volume
    public final static String EXTERNAL = "external";
    
	public final static String VISUALIZATION_TYPE = "visualization_type";
	//Image Loading Constants
	public final static String TYPE_ARTIST = "artist", 
							   TYPE_ALBUM = "album", 
							   TYPE_GENRE = "genre", 
						       TYPE_SONG = "song",
							   TYPE_PLAYLIST  = "playlist", 
						       TYPE_FOLDER  = "folder", 
							   ALBUM_SUFFIX = "albartimg", 
							   ARTIST_SUFFIX = "artstimg", 
							   PLAYLIST_SUFFIX = "plylstimg", 
							   GENRE_SUFFIX = "gnreimg", 
							   SRC_FIRST_AVAILABLE = "first_avail",
							   SRC_LASTFM = "last_fm", 
							   SRC_FILE = "from_file", 
							   SRC_GALLERY = "from_gallery",
							   SIZE_NORMAL = "normal", 
							   SIZE_THUMB = "thumb";
	// Bundle & Intent type
    public final static String MIME_TYPE = "mimetype", 
    		                   INTENT_ACTION = "action",
    		                   SOURCE_TYPE = "sourcetype",
    		                   DATA_SCHEME = "file";
    
    // Playlists
    public final static long PLAYLIST_UNKNOWN = -1, 
				    		 PLAYLIST_ALL_SONGS = -2, 
				    		 PLAYLIST_QUEUE = -3,
				             PLAYLIST_NEW = -4, 
				             PLAYLIST_FAVORITES = -5, 
				             PLAYLIST_RECENTLY_ADDED = -6;
    
    public final static String SD_PREFERENCES = "sdpreferences";
    
 // SharedPreferences
    public final static String ARTIST_KEY = "artist",
    		ALBUM_KEY = "album", 
    		ALBUM_ID_KEY = "albumid", 
    		NUMALBUMS = "num_albums",
    		NUMSONGS = "num_songs",
            GENRE_KEY = "genres", 
            ARTIST_ID = "artistid", 
            NUMWEEKS = "numweeks",
            BUILD_DEPENDS = "build_depends",
            PLAYLIST_NAME_FAVORITES = "Favorites", 
            PLAYLIST_NAME = "playlist", 
            WIDGET_STYLE="widget_type",
            UP_STARTS_ALBUM_ACTIVITY = "upStartsAlbumActivity";
    
    
    public final static String FOLDER_PATH = "folder_path";
    public final static String FOLDER_NAME = "folder_name";
    
    // Genres
    public final static String[] GENRES_DB = {
            "Blues", "Classic Rock", "Country", "Dance", "Disco", "Funk", "Grunge", "Hip-Hop",
            "Jazz", "Metal", "New Age", "Oldies", "Other", "Pop", "R&B", "Rap", "Reggae", "Rock",
            "Techno", "Industrial", "Alternative", "Ska", "Death Metal", "Pranks", "Soundtrack",
            "Euro-Techno", "Ambient", "Trip-Hop", "Vocal", "Jazz+Funk", "Fusion", "Trance",
            "Classical", "Instrumental", "Acid", "House", "Game", "Sound Clip", "Gospel", "Noise",
            "AlternRock", "Bass", "Soul", "Punk", "Space", "Meditative", "Instrumental Pop",
            "Instrumental Rock", "Ethnic", "Gothic", "Darkwave", "Techno-Industrial", "Electronic",
            "Pop-Folk", "Eurodance", "Dream", "Southern Rock", "Comedy", "Cult", "Gangsta",
            "Top 40", "Christian Rap", "Pop/Funk", "Jungle", "Native American", "Cabaret",
            "New Wave", "Psychedelic", "Rave", "Showtunes", "Trailer", "Lo-Fi", "Tribal",
            "Acid Punk", "Acid Jazz", "Polka", "Retro", "Musical", "Rock & Roll", "Hard Rock",
            "Folk", "Folk-Rock", "National Folk", "Swing", "Fast Fusion", "Bebob", "Latin",
            "Revival", "Celtic", "Bluegrass", "Avantgarde", "Gothic Rock", "Progressive Rock",
            "Psychedelic Rock", "Symphonic Rock", "Slow Rock", "Big Band", "Chorus",
            "Easy Listening", "Acoustic", "Humour", "Speech", "Chanson", "Opera", "Chamber Music",
            "Sonata", "Symphony", "Booty Bass", "Primus", "Porn Groove", "Satire", "Slow Jam",
            "Club", "Tango", "Samba", "Folklore", "Ballad", "Power Ballad", "Rhythmic Soul",
            "Freestyle", "Duet", "Punk Rock", "Drum Solo", "A capella", "Euro-House", "Dance Hall",
            "Goa", "Drum & Bass", "Club-House", "Hardcore", "Terror", "Indie", "Britpop",
            "Negerpunk", "Polsk Punk", "Beat", "Christian Gangsta Rap", "Heavy Metal",
            "Black Metal", "Crossover", "Contemporary Christian", "Christian Rock ", "Merengue",
            "Salsa", "Thrash Metal", "Anime", "JPop", "Synthpop"
    };
}
