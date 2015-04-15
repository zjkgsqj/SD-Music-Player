package com.example.sdmusicplayer;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
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
import com.example.sdmusicplayer.fragments.BottomActionBarFragment;
import com.example.sdmusicplayer.helpers.utils.MusicUtils;
import com.example.sdmusicplayer.service.MusicService;
import com.example.sdmusicplayer.service.ServiceToken;
import com.example.sdmusicplayer.service.aidl.IMusicService;
import com.example.sdmusicplayer.utils.Utils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

public class SongListActivity extends FragmentActivity implements ServiceConnection{

	private ServiceToken mToken;
	private SlidingUpPanelLayout mPanel;
	public static final String SAVED_STATE_ACTION_BAR_HIDDEN = "saved_state_action_bar_hidden";
	private BottomActionBarFragment mBActionbar;
	private boolean isAlreadyStarted = false;
	protected ListViewAdapter mAdapter;
	protected ListView mListView;
	protected Cursor mCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		if (!Utils.isTablet(this)){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);     
		}
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);         
		setContentView(R.layout.activity_song_list);

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
			ContentResolver resolver = getContentResolver();
			mCursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
					null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		}
		mListView = (ListView) findViewById(R.id.songList);
		mAdapter =new ListViewAdapter(this, R.layout.listview_song_item, mCursor,
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

}
