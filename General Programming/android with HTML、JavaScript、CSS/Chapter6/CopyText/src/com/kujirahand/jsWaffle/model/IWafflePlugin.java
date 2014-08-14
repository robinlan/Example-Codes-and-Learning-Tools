package com.kujirahand.jsWaffle.model;

import com.kujirahand.jsWaffle.WaffleActivity;
import android.content.Intent;
import android.webkit.WebView;


/**
 * jsWaffle Plugin Interfalce
 * @author kujira
 *
 */

public interface IWafflePlugin {
	/* initialize method */
	void setWebView(WebView web);
	void setContext(WaffleActivity app);
	/* activity events */
	void onPause();
	void onResume();
	// void onUnload(); // Android1.6 not supported
	void onDestroy(); // To remove listener from system
	void onActivityResult(int requestCode, int resultCode, Intent intent);
	/* webview event */
	void onPageStarted(String url);
	void onPageFinished(String url);
}
