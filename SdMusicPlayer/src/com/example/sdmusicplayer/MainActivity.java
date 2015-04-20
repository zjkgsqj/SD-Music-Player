package com.example.sdmusicplayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;

import com.example.sdmusicplayer.adapters.ScrollingTabsAdapter;
import com.example.sdmusicplayer.fragments.AlbumsFragment;
import com.example.sdmusicplayer.fragments.BottomActionBarFragment;
import com.example.sdmusicplayer.fragments.MainFragment;
import com.example.sdmusicplayer.fragments.albumFragment;
import com.example.sdmusicplayer.fragments.artistFragment;
import com.example.sdmusicplayer.fragments.folderFragment;
import com.example.sdmusicplayer.helpers.utils.MusicUtils;
import com.example.sdmusicplayer.service.MusicService;
import com.example.sdmusicplayer.service.ServiceToken;
import com.example.sdmusicplayer.service.aidl.IMusicService;
import com.example.sdmusicplayer.utils.Utils;
import com.example.sdmusicplayer.widgets.ScrollableTabView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;
import com.viewpagerindicator.TabPageIndicator;

public class MainActivity extends FragmentActivity implements ServiceConnection{

	ViewPager mViewPager;
	private ServiceToken mToken;
    private SlidingUpPanelLayout mPanel;   
	public static final String SAVED_STATE_ACTION_BAR_HIDDEN = "saved_state_action_bar_hidden";    
	BottomActionBarFragment mBActionbar;   
	private boolean isAlreadyStarted = false;
	private List<String> titles = new ArrayList<String>() ;
	
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
		/*int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}*/
		return super.onOptionsItemSelected(item);
	}




	 /**
     * Initiate ViewPager and PagerAdapter
     */
    public void initViewPager() {
        // Initiate PagerAdapter
        //PagerAdapter mPagerAdapter = new PagerAdapter(getSupportFragmentManager());

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
        
/*        //Only show tabs that were set in preferences
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
        initScrollableTabs(mViewPager);*/
        
        ViewPager pager = (ViewPager)findViewById(R.id.viewPager);  
		//构造适配器  
        List<Fragment> fragments=new ArrayList<Fragment>(); 
        if(tabs_set.contains(getResources().getString(R.string.tab_mine))){
        	fragments.add(new MainFragment());
        	titles.add(getResources().getString(R.string.tab_mine));
        }
        if(tabs_set.contains(getResources().getString(R.string.tab_artists))){
        	fragments.add(new artistFragment());
        	titles.add(getResources().getString(R.string.tab_artists));
        }
        if(tabs_set.contains(getResources().getString(R.string.tab_albums))){
        	fragments.add(new AlbumsFragment());
        	titles.add(getResources().getString(R.string.tab_albums));
        }
        if(tabs_set.contains(getResources().getString(R.string.tab_folder))){
        	fragments.add(new folderFragment());        	
        	titles.add(getResources().getString(R.string.tab_folder));
        }
		FragmentPagerAdapter pagerAdapter = new TabPageIndicatorAdapter(getSupportFragmentManager(),fragments);
		pager.setAdapter(pagerAdapter);
		//设置缓存页面，当前页面的相邻N各页面都会被缓存
		pager.setOffscreenPageLimit(titles.size()-1);
		//实例化TabPageIndicator然后设置ViewPager与之关联  
        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);  
        indicator.setViewPager(pager);  
		
        //对ViewPager设置监听，用indicator设置就行了  
        indicator.setOnPageChangeListener(new OnPageChangeListener() {  
              
            @Override  
            public void onPageSelected(int arg0) {  
                //Toast.makeText(getApplicationContext(), TITLE[arg0], Toast.LENGTH_SHORT).show();  
            }  
              
            @Override  
            public void onPageScrolled(int arg0, float arg1, int arg2) {  
                  
            }  
              
            @Override  
            public void onPageScrollStateChanged(int arg0) {  
                  
            }  
        }); 
    }
    
    /** 
     * ViewPager适配器 
     * 
     */  
    public class TabPageIndicatorAdapter extends FragmentPagerAdapter {  
    	private List<Fragment> mFragments;
        public TabPageIndicatorAdapter(FragmentManager fm,List<Fragment> fragments) {  
            super(fm);  
            mFragments = fragments;              
        }  
  
        @Override  
        public Fragment getItem(int position) {  
            //新建一个Fragment来展示ViewPager item的内容，并传递参数  
        	Fragment fragment = mFragments.get(position); 
        	/*Bundle args = new Bundle();
            args.putInt("TabNum", position);
            args.putString("arg", TITLE.get(position));    
            fragment.setArguments(args);*/
            return fragment;  
        }  
  
        @Override  
        public CharSequence getPageTitle(int position) {  
            return titles.get(position % titles.size());  
        }  
  
        @Override  
        public int getCount() {  
            return titles.size();  
        }  
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
