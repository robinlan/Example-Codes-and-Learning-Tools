package com.kujirahand.jsWaffle.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.kujirahand.jsWaffle.WaffleActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class IntentHelper {
	
	public static int request_code = 0;
	public static Uri last_intent_uri = null;
	public static String last_intent_type = null;
	
	public static boolean run(Context appContext, String url) {
		//TODO:URIの独自スキーマの定義
		// 各種  Intent の起動方法メモ
		// http://d.hatena.ne.jp/unagi_brandnew/20100309/1268115942
		// とても参考になる、今後追加すると良い
		if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("market://"))
		{
			Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			appContext.startActivity(browse);
			return true;
		}
		else if(url.startsWith("tel:"))
		{
			Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
			appContext.startActivity(dial);
			return true;
		}
		else if(url.startsWith("sms:"))
		{  
			Uri smsUri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, smsUri);
			intent.setType("vnd.android-dir/mms-sms");
		  	appContext.startActivity(intent);
		  	return true;
	  	}
		else if(url.startsWith("geo:"))
		{  
			Uri mapUri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);
		  	appContext.startActivity(intent);
		  	return true;
	  	}
		else if (url.startsWith("mailto:"))
		{
			// mailto:hoge@example.com?subject=test&body=hogehoge&attach=/sdcard/hoge.jpg
			Uri mailUri = Uri.parse(url);
			WaffleMailToDecoder dec = new WaffleMailToDecoder(url);
			String subject = dec.subject;
			String body = dec.body;
			String attach = dec.attach;
			String action = Intent.ACTION_SENDTO;
			if (attach != null) action = Intent.ACTION_SEND;
			Intent intentMail = new Intent(action, mailUri);
			intentMail.putExtra(Intent.EXTRA_SUBJECT, subject);
			intentMail.putExtra(Intent.EXTRA_TEXT, body);
			intentMail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			String mime = "text/plain";
			if (attach != null) { // attachfile
				Uri attachFileUri = WaffleUtils.checkFileUri(attach);
				mime = getContentType(attachFileUri.toString());
				intentMail.setType(mime);
				//TODO:添付ファイルをメールするとき、メールアドレスが入らない
				intentMail.putExtra(Intent.EXTRA_STREAM, attachFileUri);
				String[] tos = dec.mail.split(",");
				intentMail.putExtra(Intent.EXTRA_EMAIL, tos);
			}
			try {
				appContext.startActivity(intentMail);
			} catch (android.content.ActivityNotFoundException e) {
				WaffleActivity.mainInstance.log("ActivityNotFoundException:" + mime);
				intentMail = new Intent(Intent.ACTION_SENDTO, mailUri);
				intentMail.putExtra(Intent.EXTRA_SUBJECT, subject);
				intentMail.putExtra(Intent.EXTRA_TEXT, body);
				intentMail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				appContext.startActivity(intentMail);
			}
			return true;
		}
		else if (url.startsWith("file:") || url.startsWith("/sdcard/")) {
			return runFile(appContext, url);
		}
		else if (url.startsWith("camera:")) {
			return runCamera(appContext, url, MediaStore.ACTION_IMAGE_CAPTURE);
		}
		else if (url.startsWith("video:")) {
			return runCamera(appContext, url, MediaStore.ACTION_VIDEO_CAPTURE);
		}
		// could not set savefile (but set savedir) ... 保存ファイル名を指定できないのでカメラから分離する
		else if (url.startsWith("record:")) {
			return runCamera(appContext, url, MediaStore.Audio.Media.RECORD_SOUND_ACTION);
		}
		return false;
	}
	
	private static boolean runCamera(Context appContext, String url, String mediaType) {
		try {
			url = url.replace("camera:", "file:");
			url = url.replace("video:",  "file:");
			url = url.replace("record:", "file:");
			// set path
			Uri saveUri = Uri.parse(url);
			last_intent_uri = saveUri;
			last_intent_type = mediaType;
			// start activity
			Intent intent = new Intent();
			intent.setAction(mediaType);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, saveUri);
			Activity app = (Activity)(appContext);
			app.startActivityForResult(intent, request_code);
			return true;
		} catch (Exception e) {
			err("camera error:" + e.getMessage());
			return false;
		}
	}

	
	private static boolean runFile(Context appContext, String url) {
		if (!url.startsWith("file:")) {
			url = "file://" + url;
		}
		Uri uriFile = Uri.parse(url);
		if (url.startsWith("file:///android_asset")) {
			return false; // show in WaffleActivity
		}
		String ctype = getContentType(url);
		// run
		File f = new File(uriFile.getPath());
		if (f.exists()) {
			if (ctype == null) return false;
			try {
				Intent intentFile = new Intent(Intent.ACTION_VIEW);
				intentFile.setDataAndType(uriFile, ctype);
				appContext.startActivity(intentFile);
				return true;
			} catch (Exception e) {
				err("[Intent]" + e.getMessage());
				return false;
			}
		}
		err("[Intent]FileNotFound:" + url);
		return false;
	}
	
	public static void copyFile(InputStream input , OutputStream output) 
		throws IOException {
		int DEFAULT_BUFFER_SIZE = 1024 * 4;
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
		  output.write(buffer, 0, n);
		}
		input.close();
		output.close();
	}

	private static void err(String msg) {
		Log.e(WaffleActivity.LOG_TAG, msg);
	}
	private static void log(String msg) {
		Log.d(WaffleActivity.LOG_TAG, msg);
	}
	
	private static String getContentType(String url) {
		// Image
		if (url.endsWith(".png")) {
			return "image/*";
		}
		else if (url.endsWith(".jpg") || url.endsWith(".jpeg")) {
			return "image/*";
		}
		else if (url.endsWith(".gif") || url.endsWith(".gif")) {
			return "image/*";
		}
		// Video
		if (url.endsWith(".3gp")||url.endsWith(".3gpp")) {
			return "video/3gpp";
		}
		else if (url.endsWith(".mpeg") || url.endsWith(".mpg") || url.endsWith(".mp4")) {
			return "video/mpeg";
		}
		// text
		else if (url.endsWith(".txt")) {
			return "text/plain";
		}
		else if (url.endsWith(".html")||url.endsWith(".htm")) {
			return "text/html";
		}
		// else
		else if (url.endsWith(".pdf")) {
			return "application/pdf";
		}
		else {
			log("not support type:" + url);
			return null;
		}
	}
}
