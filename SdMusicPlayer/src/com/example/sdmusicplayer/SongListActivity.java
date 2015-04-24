package com.example.sdmusicplayer;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.Audio.Playlists;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Files.FileColumns;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.sdmusicplayer.adapters.base.ListViewAdapter;
import com.example.sdmusicplayer.adapters.list.PlaylistAdapter;
import com.example.sdmusicplayer.adapters.list.SonglistAdapter;
import com.example.sdmusicplayer.fragments.BottomActionBarFragment;
import com.example.sdmusicplayer.helpers.utils.MusicUtils;
import com.example.sdmusicplayer.service.MusicService;
import com.example.sdmusicplayer.service.ServiceToken;
import com.example.sdmusicplayer.service.aidl.IMusicService;
import com.example.sdmusicplayer.utils.Constants;
import com.example.sdmusicplayer.utils.Utils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

public class SongListActivity extends FragmentActivity implements ServiceConnection{

	private ServiceToken mToken;
	private SlidingUpPanelLayout mPanel;
	public static final String SAVED_STATE_ACTION_BAR_HIDDEN = "saved_state_action_bar_hidden";
	public static final int FILTER_SIZE = 1 * 1024 * 1024;// 1MB
	public static final int FILTER_DURATION = 1 * 60 * 1000;// 1分钟
	private BottomActionBarFragment mBActionbar;
	private boolean isAlreadyStarted = false;
	protected ListViewAdapter mAdapter;
	protected ListView mListView;
	protected Cursor mCursor;
	private Bundle bundle;
	private String mType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		if (!Utils.isTablet(this)){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);     
		}
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);         
		setContentView(R.layout.activity_song_list);

		Intent intent = getIntent();
        bundle = savedInstanceState != null ? savedInstanceState : intent.getExtras();
        mType = bundle.getString(Constants.SOURCE_TYPE);
		mBActionbar =(BottomActionBarFragment) getSupportFragmentManager().findFragmentById(R.id.bottomactionbar_new);		  
		mBActionbar.setUpQueueSwitch(this);        
		mPanel = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
		mPanel.setAnchorPoint(0);        
		mPanel.setDragView(findViewById(R.id.bottom_action_bar_dragview));
		//mPanel.setShadowDrawable(getResources().getDrawable(R.drawable.above_shadow));
		mPanel.setAnchorPoint(0.0f);
		mPanel.setPanelSlideListener(new PanelSlideListener() {
			@Override
			public void onPanelSlide(View panel, float slideOffset) {
				if (slideOffset < 0.2) {
					mBActionbar.onCollapsed();
					if (!getActionBar().isShowing()) {
						getActionBar().show();
					}                    
				} else {
					mBActionbar.onExpanded();
					if (getActionBar().isShowing()) {
						getActionBar().hide();
					}
				}
			}
			@Override
			public void onPanelExpanded(View panel) {  
			}           
			@Override
			public void onPanelCollapsed(View panel) {
			}
			@Override
			public void onPanelAnchored(View panel) {
			}
			@Override
			public void onPanelHidden(View panel) {
			}
		});

		String startedFrom = getIntent().getStringExtra("started_from");
		if(startedFrom!=null){
			ViewTreeObserver vto = mPanel.getViewTreeObserver();
			vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					if(!isAlreadyStarted){
						//mPanel.expandPane();
						isAlreadyStarted=true;
					}
				}
			});
		}
		
		initActionBar();
		initSongList();		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			super.onBackPressed();
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}   

	/**
	 * Set the ActionBar title
	 */
	private void initActionBar() {
		Utils.showUpTitleOnly(getActionBar());
	}
	
	private void initSongList() {
		if(mCursor==null){
			queryCursorByType();
		}
		mListView = (ListView) findViewById(R.id.songList);
		if(Constants.TYPE_PLAYLIST.equals(mType)){
			mAdapter =new PlaylistAdapter(this, R.layout.listview_song_item, mCursor,
					new String[] {}, new int[] {}, 0);
			mListView.setAdapter(mAdapter);
			mListView.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
						long id) {
					Cursor mCursor = (Cursor) mListView.getItemAtPosition(position);
					Long playlistId = mCursor.getLong(mCursor.getColumnIndexOrThrow(Playlists._ID)); 
					String playlistName = mCursor.getString(mCursor.getColumnIndexOrThrow(Playlists.NAME)); 
					Bundle bundle = new Bundle();
	            	bundle.putString(Constants.SOURCE_TYPE, Constants.TYPE_PLAYLIST_LIST);
	            	bundle.putString(Constants.PLAYLIST_NAME, playlistName);
	            	bundle.putLong(Constants.PLAYLIST_ID, playlistId);
	            	Intent mIntent = new Intent(SongListActivity.this, SongListActivity.class);
	            	mIntent.putExtras(bundle);
					startActivity(mIntent);				
				}			
			});
		}else{
			mAdapter =new SonglistAdapter(this, R.layout.listview_song_item, mCursor,
					new String[] {}, new int[] {}, 0);
			mListView.setAdapter(mAdapter);
			mListView.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
						long id) {
					MusicUtils.playAll(SongListActivity.this, mCursor, position);				
				}			
			});
		}
	}

	/**
	 * 根据类型获取cursor
	 */
	private void queryCursorByType(){
		ContentResolver resolver = getContentResolver();
		StringBuilder mSelection = new StringBuilder();
		/*// 过滤大小小于1M，时长小于1分钟的音频
		mSelection.append(Media.SIZE + " > " + FILTER_SIZE);
		mSelection.append(" and " + Media.DURATION + " > " + FILTER_DURATION);*/
		if(Constants.TYPE_FOLDER.equals(mType)){
			String folder = bundle.getString(FileColumns.PARENT);
			mSelection.append(FileColumns.MEDIA_TYPE).append(" = ").append(FileColumns.MEDIA_TYPE_AUDIO);
			mSelection.append(" and ").append(FileColumns.PARENT).append(" = ").append(folder);
			mCursor = resolver.query(Files.getContentUri(Constants.EXTERNAL),
					null, mSelection.toString(), null, Audio.Media.DEFAULT_SORT_ORDER);
		}else if(Constants.TYPE_SONG.equals(mType)){
			mCursor = resolver.query(Audio.Media.EXTERNAL_CONTENT_URI,
					null, null, null, Audio.Media.DEFAULT_SORT_ORDER);
		}else if(Constants.TYPE_RECENT_ADD.equals(mType)){
			int X = MusicUtils.getIntPref(this, Constants.NUMWEEKS, 5) * 3600 * 24 * 7;
			mSelection.append(MediaColumns.TITLE + " != ''");
			mSelection.append(" AND " + AudioColumns.IS_MUSIC + "=1");
			mSelection.append(" AND " + MediaColumns.DATE_ADDED + ">"
	                + (System.currentTimeMillis() / 1000 - X));
			String mSortOrder = MediaColumns.DATE_ADDED + " DESC";
			mCursor = resolver.query(Audio.Media.EXTERNAL_CONTENT_URI,
					null, mSelection.toString(), null, mSortOrder);
		}else if(Constants.TYPE_PLAYLIST.equals(mType)){
			String[] mProjection = new String[] {
                    Playlists._ID, Playlists.NAME,
                    " count(" + Playlists.Members._ID + ") as " + Constants.NUMSONGS
            };
			mSelection.append(" 1=1 ) group by ( ").append(Playlists.Members._ID);
			mCursor = resolver.query(Audio.Playlists.EXTERNAL_CONTENT_URI,
					mProjection, mSelection.toString(), null, Playlists.NAME);
		}else if(Constants.TYPE_PLAYLIST_LIST.equals(mType)){			
			Long mPlaylistId = bundle.getLong(Constants.PLAYLIST_ID);
			mCursor = resolver.query(Playlists.Members.getContentUri(Constants.EXTERNAL,
					mPlaylistId),null, null, null, Playlists.Members.AUDIO_ID);			
		}
	}
	
	@Override
	public void onServiceConnected(ComponentName name, IBinder obj) {
		MusicUtils.mService = IMusicService.Stub.asInterface(obj);
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		MusicUtils.mService = null;
	}

    @Override
    protected void onStart() {
        // Bind to Service
        mToken = MusicUtils.bindToService(this, this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicService.META_CHANGED);
        this.setTitle();
        super.onStart();
    }

    @Override
    protected void onStop() {
        // Unbind
        if (MusicUtils.mService != null)
            MusicUtils.unbindFromService(mToken);
        //TODO: clear image cache
        super.onStop();
    }
    
    @Override
    protected void onDestroy() { 
    	if (mAdapter != null && mAdapter.getCursor() != null) {
            mAdapter.getCursor().close();  
        }
    	super.onDestroy();   
    }

	@Override
	public void onBackPressed() {
		if (mPanel != null &&
				(mPanel.getPanelState() == PanelState.EXPANDED || mPanel.getPanelState() == PanelState.ANCHORED)) {
			mPanel.setPanelState(PanelState.COLLAPSED);
		}
		else{
			super.onBackPressed();
		}
	} 
	
	private void setTitle() {
		if(bundle != null){
			mType = bundle.getString(Constants.SOURCE_TYPE);
			if(Constants.TYPE_FOLDER.equals(mType)){
				setTitle(getString(R.string.folder_page_title) 
						+ bundle.getString(Constants.FOLDER_NAME));
			}else if(Constants.TYPE_RECENT_ADD.equals(mType)){
				setTitle(getString(R.string.label_recent_add));
			}else if(Constants.TYPE_PLAYLIST.equals(mType)){
				setTitle(getString(R.string.label_play_list));
			}else if(Constants.TYPE_PLAYLIST_LIST.equals(mType)){
				setTitle(getString(R.string.playlist_page_title) 
						+ bundle.getString(Constants.PLAYLIST_NAME));
			}
		}
	}

}
