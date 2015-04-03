package com.example.sdmusicplayer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;

import com.example.sdmusicplayer.adapters.PagerAdapter;
import com.example.sdmusicplayer.adapters.ScrollingTabsAdapter;
import com.example.sdmusicplayer.fragments.BottomActionBarFragment;
import com.example.sdmusicplayer.fragments.MainFragment;
import com.example.sdmusicplayer.fragments.albumFragment;
import com.example.sdmusicplayer.fragments.artistFragment;
import com.example.sdmusicplayer.fragments.folderFragment;
import com.example.sdmusicplayer.utils.Utils;
import com.example.sdmusicplayer.widgets.ScrollableTabView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;

public class MainActivity extends FragmentActivity implements ServiceConnection{

	ViewPager mViewPager;

    private SlidingUpPanelLayout mPanel;
    
	public static final String SAVED_STATE_ACTION_BAR_HIDDEN = "saved_state_action_bar_hidden";
    
	BottomActionBarFragment mBActionbar;
    
	private boolean isAlreadyStarted = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        if (!Utils.isTablet(this)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);     
        }
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);  
        
		setContentView(R.layout.activity_main);

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
        initActionBar();
		initViewPager();
		
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

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		/*if (id == R.id.action_settings) {
			return true;
		}*/
		return super.onOptionsItemSelected(item);
	}




	 /**
     * Initiate ViewPager and PagerAdapter
     */
    public void initViewPager() {
        // Initiate PagerAdapter
        PagerAdapter mPagerAdapter = new PagerAdapter(getSupportFragmentManager());

        //Get tab visibility preferences
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> defaults = new HashSet<String>(Arrays.asList(
        		getResources().getStringArray(R.array.tab_titles)
        	));
        Set<String> tabs_set = sp.getStringSet("tabs_enabled",defaults);
        //if its empty fill reset it to full defaults
        	//stops app from crashing when no tabs are shown
        	//TODO:rewrite activity to not crash when no tabs are chosen to show
        if(tabs_set.size()==0){
        	tabs_set = defaults;
        }
        
        //Only show tabs that were set in preferences
        if(tabs_set.contains(getResources().getString(R.string.tab_mine)))
        	mPagerAdapter.addFragment(new MainFragment());
        if(tabs_set.contains(getResources().getString(R.string.tab_artists)))
        	mPagerAdapter.addFragment(new artistFragment());
        if(tabs_set.contains(getResources().getString(R.string.tab_albums)))
        	mPagerAdapter.addFragment(new albumFragment());
        if(tabs_set.contains(getResources().getString(R.string.tab_folder)))
        	mPagerAdapter.addFragment(new folderFragment());

        // Initiate ViewPager
        ViewPager mViewPager = (ViewPager)findViewById(R.id.viewPager);
        mViewPager.setPageMargin(getResources().getInteger(R.integer.viewpager_margin_width));
        mViewPager.setPageMarginDrawable(R.drawable.viewpager_margin);
        mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());
        mViewPager.setAdapter(mPagerAdapter);
        //mViewPager.setCurrentItem(0);

        // Tabs
        initScrollableTabs(mViewPager);
    }
    
    /**
     * Initiate the tabs
     */
    public void initScrollableTabs(ViewPager mViewPager) {
        ScrollableTabView mScrollingTabs = (ScrollableTabView)findViewById(R.id.scrollingTabs);
        ScrollingTabsAdapter mScrollingTabsAdapter = new ScrollingTabsAdapter(this);
        mScrollingTabs.setAdapter(mScrollingTabsAdapter);
        mScrollingTabs.setViewPager(mViewPager);
    }
    
    /**
     * For the theme chooser
     */
    private void initActionBar() {
    	ActionBar actBar = getActionBar();
    	actBar.setDisplayUseLogoEnabled(true);
        actBar.setDisplayShowTitleEnabled(false);
    }
	
	@Override
	public void onServiceConnected(ComponentName arg0, IBinder arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onServiceDisconnected(ComponentName arg0) {
		// TODO Auto-generated method stub
		
	}

}
