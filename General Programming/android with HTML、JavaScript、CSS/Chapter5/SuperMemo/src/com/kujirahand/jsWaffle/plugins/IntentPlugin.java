package com.kujirahand.jsWaffle.plugins;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;

import com.kujirahand.jsWaffle.WaffleActivityFullScreen;
import com.kujirahand.jsWaffle.WaffleActivitySub;
import com.kujirahand.jsWaffle.model.WafflePlugin;
import com.kujirahand.jsWaffle.utils.IntentHelper;
import com.kujirahand.jsWaffle.utils.WaffleUtils;

public class IntentPlugin extends WafflePlugin {
	//
	public final static int ACTIVITY_REQUEST_CODE_BARCODE = 0xFF0001;
	public final static int ACTIVITY_REQUEST_CODE_CONTACT = 0xFF0002;
	public final static int ACTIVITY_REQUEST_CODE_GALLARY = 0xFF0003;
	public final static int ACTIVITY_REQUEST_CODE_VOICERECOG = 0xFF0004;
	
	// Callback string
	private String intent_startActivity_callback = null;
	private String intent_startActivity_callback_barcode = null;
	private String intent_startActivity_callback_gallery = null;
	private String intent_startActivity_callback_voicerec = null;
	
	//
	public final static int RESULT_CANCELED = 0x000000;

	/**
	 * Start Intent
	 * @param url
	 */
	public boolean startIntent(String url) {
		IntentHelper.request_code = -1;
		return _startIntent(url, false);
	}
	public boolean startIntentForResult(String url, String callback, int requestCode) {
		IntentHelper.request_code = requestCode;
		this.intent_startActivity_callback = callback;
		return _startIntent(url, false);
	}
	/**
	 * Start Intent with FullScreen
	 * @param url
	 */
	public boolean startIntentFullScreen(String url) {
		return _startIntent(url, true);
	}
	private boolean _startIntent(String url, boolean bFull) {
		// android_asset?
		if (url.startsWith("file:///android_asset/")) {
			try {
				Intent i;
				if (bFull) {
					i = new Intent(waffle_activity, WaffleActivityFullScreen.class);
				} else {
					i = new Intent(waffle_activity, WaffleActivitySub.class);
				}
				i.putExtra("url", url);
				i.setAction(Intent.ACTION_VIEW);
				waffle_activity.startActivityForResult(i, 1);
				return true;
			} catch (Exception e) {
				waffle_activity.log_error("assets:" + e.getMessage());
				return false;
			}
		}
		// other
		else {
			return IntentHelper.run(waffle_activity, url);
		}
	}
	/**
	 * Start Intent (and Request Result)
	 * @param intent
	 * @param requestCode
	 */
	public void intent_startActivityForResult(Intent intent, int requestCode, String callbackName) {
		try {
			this.intent_startActivity_callback = callbackName;
			waffle_activity.startActivityForResult(intent, requestCode);
		} catch (Exception e) {
			waffle_activity.log_error("activityError:" + e.getMessage());
		}
	}
	
	/**
	 * Get Barcode
	 * @param callbackName
	 * @param mode (null|QR_CODE_MODE|ONE_D_MODE|DATA_MATRIX_MODE)
	 * @return tried
	 */
	public boolean scanBarcode(String callbackName, String mode) {
		if (!intent_exists("com.google.zxing.client.android.SCAN")) {
			return false;
		}
		intent_startActivity_callback_barcode = callbackName;
		try {
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			if (mode == "QR_CODE_MODE" || mode == "ONE_D_MODE" || mode == "DATA_MATRIX_MODE") {
				intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
			}
			waffle_activity.startActivityForResult(intent, ACTIVITY_REQUEST_CODE_BARCODE);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Pickup Image From Gallery
	 */
	public boolean pickupImageFromGallery(String callbackName) {
		intent_startActivity_callback_gallery = callbackName;
		try {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			waffle_activity.startActivityForResult(
					intent, ACTIVITY_REQUEST_CODE_GALLARY);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * RecognizerIntent / recognizeSpeech
	 * @param callbackName
	 * @param option Language(ja_JP || en)
	 * @return tried
	 */
	public boolean recognizeSpeech(String callbackName, String option) {
		// Log.d("locale","locale=" + Locale.ENGLISH.toString());
		Intent intent = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		if (option != null && option != "") {
			intent.putExtra(
					RecognizerIntent.EXTRA_LANGUAGE,
					option);
		}
		/* intent.putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                "Please Speak!"); */
		intent_startActivity_callback_voicerec = callbackName;
		try {
			waffle_activity.startActivityForResult(intent, ACTIVITY_REQUEST_CODE_VOICERECOG);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Intent Exists?
	 * @param intentName (ex: com.kujirahand.jsWaffle.xxx)
	 */
	public boolean intent_exists(String intentName) {
		final Intent intent = new Intent(intentName);
		final PackageManager packMan = waffle_activity.getPackageManager();
		List<ResolveInfo> list = packMan.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return (list.size() > 0);
	}
	
	/**
	 * new Intent
	 */
	public Intent intent_new(String action, String uri) {
		Intent intent = new Intent(action, Uri.parse(uri));
		return intent;
	}
	public void intent_setClassName(Intent intent, String packageName, String className) {
		intent.setClassName(packageName, className);
	}
	public void intent_putExtra(Intent intent, String name, String value) {
		intent.putExtra(name, value);
	}
	public String intent_getExtra(Intent intent, String name) {
		return intent.getStringExtra(name);
	}
	/**
	 * Start Intent
	 * @param intent
	 */
	public void intent_startActivity(Intent intent) {
		try {
			waffle_activity.startActivity(intent);
		} catch (Exception e) {
			waffle_activity.log_error("activityError:" + e.getMessage());
		}
	}
	
	
	
	//-----------------------------------------------------------------
	// @see intent_startActivityForResult
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		String param;
		if (requestCode == ACTIVITY_REQUEST_CODE_BARCODE && intent_startActivity_callback_barcode != null) {
			String contents = "";
			String format = "text";
			if (intent != null) {
				contents = intent.getStringExtra("SCAN_RESULT");
		        format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				if (contents != null) contents = URLEncoder.encode(contents);
			}
	        param = intent_startActivity_callback_barcode + "('" + contents + "','" + format + "')";
			waffle_activity.callJsEvent(param);
		
		} else if (requestCode == ACTIVITY_REQUEST_CODE_GALLARY) {
			String fname = intent.getData().toString();
	        param = intent_startActivity_callback_gallery + "('" + fname + "')";
			waffle_activity.callJsEvent(param);
		
		} else if (requestCode == ACTIVITY_REQUEST_CODE_VOICERECOG && 
				resultCode != RESULT_CANCELED) {
			String voice_str = "";
			ArrayList<String> voice_results = intent.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
			if (voice_results.size() > 0) {
				/*
				for (int i = 0; i < voice_results.size(); i++) {
					 voice_str += voice_results.get(i);
		        }
		        */
				voice_str = voice_results.get(0);
			}
			param = intent_startActivity_callback_voicerec + "('" + voice_str + "')";
			waffle_activity.callJsEvent(param);
		
		} else if (
			requestCode == IntentHelper.request_code && 
			(IntentHelper.last_intent_type == MediaStore.ACTION_IMAGE_CAPTURE ||
					IntentHelper.last_intent_type == MediaStore.ACTION_VIDEO_CAPTURE)
		) {
			cameraResult(requestCode, resultCode, intent);
		
		} else {
			if (intent_startActivity_callback == null) return;
			param = intent_startActivity_callback + "(" + requestCode + "," + resultCode + ")";
			waffle_activity.callJsEvent(param);
		}
	}
	
	public void cameraResult(int requestCode, int resultCode, Intent intent) {
		// Android 2.x (not use EXTRA_OUTPUT)
		if (intent != null) {
			Uri imageurl = intent.getData();
			if (imageurl != null && resultCode != 0) {
				try {
					String s_src = imageurl.toString();
					String s_des = IntentHelper.last_intent_uri.toString();
					if (!s_src.equals(s_des)) {
						WaffleUtils.copyFileFromName(
								imageurl.toString(),
								IntentHelper.last_intent_uri.toString(),
								waffle_activity);
					}
				} catch (Exception e) {
					resultCode = 0;
				}
			}
		}
		if (intent_startActivity_callback == null) return;
		String param = intent_startActivity_callback + "(" + requestCode + "," + resultCode + ")";
		waffle_activity.callJsEvent(param);
	}
}
