package com.kujirahand.jsWaffle.model;

import com.kujirahand.jsWaffle.WaffleActivity;
import android.view.Display;

public class WaffleFlags {
	// Main URL
	public String mainHtmlUrl = "file:///android_asset/www/index.html";
	// Window Setting
	public boolean useFullScreen = false;
	public boolean keepScreenNotSleep = false;
	public boolean noTitle = true;
	// Scroll bar
	public boolean useVerticalScrollBar = false;
	public boolean useHorizontalScrollBar = false;
	// Zoom Controls
	public boolean useBuiltInZoomControls = false;
	public int initialScale = 100;
	
	/** calc scale method */
	public void setWidth(int width) {
		Display p = waffle_activity.getWindowManager().getDefaultDisplay();
		int w = p.getWidth();
		double per = (double)w / width * 100.0;
		this.initialScale = (int)per;
		waffle_activity.log("scale="+initialScale);
	}
	public void setHeight(int height) {
		Display p = waffle_activity.getWindowManager().getDefaultDisplay();
		int h = p.getHeight();
		double per = (double)h / height * 100.0;
		this.initialScale = (int)per;
		waffle_activity.log("scale="+initialScale);
	}
	
	private WaffleActivity waffle_activity = null;
	public WaffleFlags(WaffleActivity activity) {
		waffle_activity = activity;
	}
}
