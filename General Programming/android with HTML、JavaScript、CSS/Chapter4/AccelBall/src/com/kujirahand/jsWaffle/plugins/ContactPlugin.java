package com.kujirahand.jsWaffle.plugins;

import java.util.HashMap;

import android.content.Intent;

import com.kujirahand.jsWaffle.model.WafflePlugin;
import com.kujirahand.jsWaffle.utils.ContactAccessor;

public class ContactPlugin extends WafflePlugin {
	
	private String callbackStr = null;
	private int tag;
	
	private ContactAccessor contactAccessor;
	
	public void pickupContact(String callback, int tag) {
		this.callbackStr = callback;
		this.tag = tag;
		// select SDK Version
		contactAccessor = ContactAccessor.getInstance();
		Intent it = contactAccessor.getContactPickerIntent();
		waffle_activity.startActivityForResult(it, IntentPlugin.ACTIVITY_REQUEST_CODE_CONTACT);
	}
	
	public void getContact(Intent intent) {
		HashMap<String, String> map = contactAccessor.getContactData(waffle_activity, intent);
		String json = String.format("{name:'%s',email:'%s',number:'%s'}",
				map.get("name"),
				map.get("email"),
				map.get("number")
		);
		waffle_activity.callJsEvent(callbackStr +"(" + tag + "," + json + ")");
	}
	
	public void failed() {
		waffle_activity.callJsEvent(callbackStr + "(" + tag + ", null)");
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == 0) {
			failed(); return;
		}
		if (requestCode == IntentPlugin.ACTIVITY_REQUEST_CODE_CONTACT && callbackStr != null) {
			getContact(intent);
		}
	}
}
