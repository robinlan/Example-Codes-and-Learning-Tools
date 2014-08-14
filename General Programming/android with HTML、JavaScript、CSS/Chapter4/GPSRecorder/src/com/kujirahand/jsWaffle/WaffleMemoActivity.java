package com.kujirahand.jsWaffle;


import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WaffleMemoActivity extends Activity {
	
	public static WaffleMemoActivity mainInstance = null;
	public static String LOG_TAG = "jsWaffle";
	protected LinearLayout root;
	protected Handler handler = new Handler();
	public TextView textview;
	protected String targetPath = "/sdcard/data/memo";
	
	final int MENU_SAVE = 0;
	final int MENU_SAVE_AS = 1;
	final int MENU_LOAD = 2;
	final int MENU_CLOSE = 3;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	if (mainInstance == null) mainInstance = this; // set main instance
    	
    	// Initialize Window
    	onSetWindowFlags(getWindow());
    	
    	// Create WebView
    	buildMainView();
        
        // Set WebView to Main View
        setContentView(root);
    }
   
    /**
     * set Window Flags
     * @param w
     */
	public void onSetWindowFlags(Window w) {
		// has title
		// w.requestFeature(Window.FEATURE_NO_TITLE);
		// not fullscreen
        w.setFlags(
        		WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
	}
	
    /**
     * Create main view
     */
    protected void buildMainView() {
    	// レイアウト用パラメータ
        LinearLayout.LayoutParams containerParams = 
        		new LinearLayout.LayoutParams(
        				ViewGroup.LayoutParams.FILL_PARENT, 
        				ViewGroup.LayoutParams.FILL_PARENT,
        				0.0F);
        LinearLayout.LayoutParams txtviewParams = 
        		new LinearLayout.LayoutParams(
        				ViewGroup.LayoutParams.FILL_PARENT,
        				ViewGroup.LayoutParams.FILL_PARENT, 
        				1.0F);
        // ルートにレイアウトを追加
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.BLACK);
        root.setLayoutParams(containerParams);
        //
        textview = new TextView(this);
        textview.setLayoutParams(txtviewParams);
        root.addView(textview);
    }
    
    /**
     * show log to DDMS LogCat
     * @param msg
     */
	public void log(String msg) {
		Log.d(WaffleMemoActivity.LOG_TAG, msg);
	}
	public void log_error(String msg) {
		Log.e(WaffleMemoActivity.LOG_TAG, msg);
	}
	public void log_warn(String msg) {
		Log.w(WaffleMemoActivity.LOG_TAG, msg);
	}
	
    //-----------------------------------------------------------------
    // Activity Event
    //-----------------------------------------------------------------
    @Override
    protected void onStart() {
    	super.onStart();
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    }
    
    @Override
    protected void onPause() {
    	super.onStop();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    }
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    /*
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	        return true;
	    }
	    */
	    return super.onKeyDown(keyCode, event);
	}
	
	
	@Override
	 public boolean onCreateOptionsMenu(Menu menu) {
		// create
		MenuItem menuSave = menu.add("Save");
		MenuItem menuSaveAs = menu.add("Save As");
		MenuItem menuLoad = menu.add("Load");
		MenuItem menuClose = menu.add("Close");
		// icon
		menuSave.setIcon(android.R.drawable.ic_menu_save);
		menuSaveAs.setIcon(android.R.drawable.ic_menu_save);
		menuLoad.setIcon(android.R.drawable.ic_menu_view);
		menuClose.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	    return super.onPrepareOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case MENU_CLOSE:
			this.finish();
			break;
		case MENU_SAVE:
			saveTo("hoge.txt");
			break;
		case MENU_SAVE_AS:
			break;
		case MENU_LOAD:
			break;
		}
		return true;
	}
	
	public void setTargetPath(String dir) {
		targetPath = dir;
	}
	
	private void saveTo(String fname) {
		File target_dir = new File(targetPath);
		if (!target_dir.exists()) {
			try {
				target_dir.getParentFile().mkdirs();
			} catch (Exception e) {
			}
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	}
	
}
