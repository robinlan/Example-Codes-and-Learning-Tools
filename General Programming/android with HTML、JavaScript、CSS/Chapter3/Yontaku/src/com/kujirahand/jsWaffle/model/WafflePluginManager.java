package com.kujirahand.jsWaffle.model;

import java.util.Vector;

import com.kujirahand.jsWaffle.WaffleActivity;

import android.content.Intent;

public class WafflePluginManager {
	
	public Vector<IWafflePlugin> items = new Vector<IWafflePlugin>();
	private WaffleActivity waffle_activity;
	
	public WafflePluginManager(WaffleActivity waffle_activity) {
		this.waffle_activity = waffle_activity;
	}

	public void onPause() {
		waffle_activity.log("onPause");
    	for (IWafflePlugin plugin : items) {
			plugin.onPause();
		}
	}
	
	public void onResume() {
		waffle_activity.log("onResume");
    	for (IWafflePlugin plugin : items) {
			plugin.onResume();
		}
	}
	
	/*
	public void onUnload() {
		waffle_activity.log("onUnload");
    	for (IWafflePlugin plugin : items) {
			plugin.onUnload();
		}
	}
	*/
	
	public void onDestroy() { // Remove listener from system
		waffle_activity.log("onDestroy");
    	for (IWafflePlugin plugin : items) {
			plugin.onDestroy();
		}
	}
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		waffle_activity.log("onActivityResult:" + requestCode + "," + resultCode);
    	for (IWafflePlugin plugin : items) {
			plugin.onActivityResult(requestCode, resultCode, intent);
		}
	}
	/**
	 * Remove any listener 既に何かしらのリスナーが登録されているなら解除する
	 * @param url
	 */
	public void onPageStarted(String url) {
		waffle_activity.log("onPageStarted:" + url);
    	for (IWafflePlugin plugin : items) {
			plugin.onPageStarted(url);
		}
	}
	/**
	 * ページのロードが完了したとき
	 * @param url
	 */
	public void onPageFinished(String url) {
		// waffle_activity.log("onPageFinished:" + url);
    	for (IWafflePlugin plugin : items) {
			plugin.onPageFinished(url);
		}
	}
}
