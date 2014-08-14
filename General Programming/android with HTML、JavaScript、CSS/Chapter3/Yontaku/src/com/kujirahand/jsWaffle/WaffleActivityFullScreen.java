package com.kujirahand.jsWaffle;

import com.kujirahand.jsWaffle.model.WaffleFlags;
import android.content.Intent;

public class WaffleActivityFullScreen extends WaffleActivity {
    /** Set jsWaffle Setting flags */
    @Override
    protected void onSetWaffleFlags(WaffleFlags flags) {
    	super.onSetWaffleFlags(flags);
    	
    	// Check Intent Parameter
        Intent i = getIntent();
        String url = i.getStringExtra("url");
        if (url != null) {
        	flags.mainHtmlUrl = url;
        }
        else {
        	flags.mainHtmlUrl = "file:///android_asset/www/index.html";
        }
        
        flags.useFullScreen = true;
    }
}
