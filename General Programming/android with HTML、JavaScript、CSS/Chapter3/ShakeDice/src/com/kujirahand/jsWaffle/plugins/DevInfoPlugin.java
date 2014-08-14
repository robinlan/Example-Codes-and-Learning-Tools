package com.kujirahand.jsWaffle.plugins;

import java.io.File;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;
import android.view.Display;

import com.kujirahand.jsWaffle.model.WafflePlugin;

public class DevInfoPlugin extends WafflePlugin {
	
	public String getDisplayInfo() {
		Display p = waffle_activity.getWindowManager().getDefaultDisplay();
		DisplayMetrics m = new DisplayMetrics();
		p.getMetrics(m);
		return String.format(
			"{" + 
			"width:%d,height:%d," + 
			"orientation:%d," + // 0:0deg, 1:90deg, 2:180deg, 3:270deg
			"refreshRate:%f,pixelFormat:%d,"+
			"xdpi:%f, ydpi:%f,"+
			"density:%f, scaledDensity:%f"+
			"}", 
			p.getWidth(), p.getHeight(),
			p.getOrientation(), // 
			p.getRefreshRate(), p.getPixelFormat(),
			m.xdpi, m.ydpi,
			m.density, m.scaledDensity
		);
	}
	
	/**
	 * システムのメモリ情報を取得する
	 * @return
	 */
	public long getSystemMemory() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long memorySize = stat.getAvailableBlocks() * stat.getBlockSize();
		return memorySize;
	}
	
	public String getMemoryInfo() {
		ActivityManager man = ((ActivityManager)
			waffle_activity.getSystemService(Activity.ACTIVITY_SERVICE));
		ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
		man.getMemoryInfo(info);
		String s = String.format(
				"{'availMem':%d, 'lowMemory':%s, 'threshold':%d}", 
				info.availMem,
				((info.lowMemory) ? "true" : "false"),
				info.threshold);
		return s;
	}
	
	public boolean hasSDCard() {
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}
	
	public String getSDCardPath() {
		File targetdir = Environment.getExternalStorageDirectory();
		return targetdir.getPath();
	}
	
/*
Log.d("build","BOARD:" + Build.BOARD);
//Log.d("build","BOOTLOADER:" + Build.BOOTLOADER);	//Android 1.6未対応
Log.d("build","BRAND:" + Build.BRAND);
Log.d("build","CPU_ABI:" + Build.CPU_ABI);
//Log.d("build","CPU_ABI2:" + Build.CPU_ABI2);		//Android 1.6未対応
Log.d("build","DEVICE:" + Build.DEVICE);
Log.d("build","DISPLAY:" + Build.DISPLAY);
Log.d("build","FINGERPRINT:" + Build.FINGERPRINT);
//Log.d("build","HARDWARE:" + Build.HARDWARE);		//Android 1.6未対応
Log.d("build","HOST:" + Build.HOST);
Log.d("build","ID:" + Build.ID);
Log.d("build","MANUFACTURER:" + Build.MANUFACTURER);
Log.d("build","MODEL:" + Build.MODEL);
Log.d("build","PRODUCT:" + Build.PRODUCT);
//Log.d("build","RADIO:" + Build.RADIO);				//Android 1.6未対応
Log.d("build","TAGS:" + Build.TAGS);
Log.d("build","TIME:" + Build.TIME);
Log.d("build","TYPE:" + Build.TYPE);
//Log.d("build","UNKNOWN:" + Build.UNKNOWN);			//Android 1.6未対応
Log.d("build","USER:" + Build.USER);
Log.d("build","VERSION.CODENAME:" + Build.VERSION.CODENAME);
Log.d("build","VERSION.INCREMENTAL:" + Build.VERSION.INCREMENTAL);
Log.d("build","VERSION.RELEASE:" + Build.VERSION.RELEASE);
Log.d("build","VERSION.SDK:" + Build.VERSION.SDK);
Log.d("build","VERSION.SDK_INT:" + Build.VERSION.SDK_INT);        
*/	
	
	
	public int getAndroidVersionInt() {
		String a = Build.VERSION.SDK;
		return Integer.parseInt(a);
	}
	
	public String getAndroidVersionRelease() {
		return Build.VERSION.RELEASE;
	}
	
	/**
	 * device unique id
	 * @return
	 */
	public String getAndroidId() {
		String android_id = null;
		try {
			android_id = Secure.getString(waffle_activity.getContentResolver(), Secure.ANDROID_ID);
		} catch (Exception e) {
		}
		try {
			// for Android 1.5
			if (android_id == null) {
				android_id = android.provider.Settings.System.getString(
						waffle_activity.getContentResolver(), android.provider.Settings.System.ANDROID_ID);
			}
		} catch (Exception e) {
		}
		return android_id;
	}
	
}
