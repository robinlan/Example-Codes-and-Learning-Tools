package com.kujirahand.jsWaffle.model;

import com.kujirahand.jsWaffle.WaffleActivity;

import android.content.Intent;
import android.webkit.WebView;

public class WafflePlugin implements IWafflePlugin {
	
	protected WebView webview;
	protected WaffleActivity waffle_activity;
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	}

	@Override
	public void onDestroy() {
	}

	@Override
	public void onPause() {
	}

	@Override
	public void onResume() {
	}
	
	@Override
	public void onPageStarted(String url) {
	}
	
	@Override
	public void onPageFinished(String url) {
	}
	
	@Override
	public void setContext(WaffleActivity app) {
		this.waffle_activity = app;
	}

	@Override
	public void setWebView(WebView web) {
		this.webview = web;
	}
}
