package com.example.MyCalc;

import com.kujirahand.jsWaffle.WaffleActivity;
import com.kujirahand.jsWaffle.model.WaffleFlags;

public class MyCalc extends WaffleActivity {
    
    /** Set jsWaffle Setting flags */
    @Override
    protected void onSetWaffleFlags(WaffleFlags flags) {
    	super.onSetWaffleFlags(flags);
    	// set flags
    	flags.mainHtmlUrl = "file:///android_asset/www/index2.html";
    	flags.keepScreenNotSleep = false;
    	flags.useFullScreen = false;
    	flags.useVerticalScrollBar = false;
    	//flags.setWidth(320);
    }
    
    
    /** Please add the custom plug-in if it is necessary. */
    @Override
    protected void onAddPlugins() {
    	super.onAddPlugins();
    }

}