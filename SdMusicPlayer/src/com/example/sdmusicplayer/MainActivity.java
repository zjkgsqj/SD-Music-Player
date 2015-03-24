package com.example.sdmusicplayer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import android.app.ActionBar;
import android.content.ComponentName;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.example.sdmusicplayer.adapters.PagerAdapter;
import com.example.sdmusicplayer.adapters.ScrollingTabsAdapter;
import com.example.sdmusicplayer.fragments.MainFragment;
import com.example.sdmusicplayer.utils.Utils;
import com.example.sdmusicplayer.widgets.ScrollableTabView;

public class MainActivity extends FragmentActivity implements ServiceConnection{

	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        if (!Utils.isTablet(this)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);     
        }
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);  
        
		setContentView(R.layout.activity_main);

        initActionBar();
		initViewPager();

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
        // Recently added tracks
        if(tabs_set.contains(getResources().getString(R.string.tab_recent)))
        	mPagerAdapter.addFragment(new MainFragment());
        // Artists
        if(tabs_set.contains(getResources().getString(R.string.tab_artists)))
        	mPagerAdapter.addFragment(new MainFragment());
        if(tabs_set.contains(getResources().getString(R.string.tab_albums)))
        	mPagerAdapter.addFragment(new MainFragment());
        // Albums


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
