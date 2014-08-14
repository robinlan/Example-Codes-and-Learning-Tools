package com.kujirahand.jsWaffle.utils;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

public abstract class ContactAccessor {
	 public abstract Intent getContactPickerIntent();
	 public abstract HashMap<String, String> getContactData(Activity a, Intent data);
	 private static ContactAccessor sInstance;

	 public static ContactAccessor getInstance() {
	    if (sInstance == null) {
	        String className;
	        int sdkVersion = Integer.parseInt(Build.VERSION.SDK);
	        if (sdkVersion < 5/* Build.VERSION_CODES.ECLAIR */) {
	            className = "ContactAccessor3_4";
	        } else {
	            className = "ContactAccessor5";
	        }
	        try {
	       	 	Class<?> clazz = 
	            	Class.forName("com.kujirahand.jsWaffle.utils." + className)
	                .asSubclass(ContactAccessor.class);
	                 sInstance = (ContactAccessor)clazz.newInstance();
	        } catch (Exception e) {
	             throw new IllegalStateException(e);
	        }
	        return sInstance;
	    }
	    else {
	    	return sInstance;
	    }
	}
}
