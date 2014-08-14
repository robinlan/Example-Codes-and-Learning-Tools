package com.kujirahand.jsWaffle.plugins;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Hashtable;

import com.kujirahand.jsWaffle.WaffleActivity;
import com.kujirahand.jsWaffle.model.WafflePlugin;
import com.kujirahand.jsWaffle.utils.WaffleUtils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Vibrator;
import android.text.ClipboardManager;
import android.widget.Toast;

/*
 * Javaクラスのメソッドのみが登録される
 */
public class ABasicPlugin extends WafflePlugin
{
	//
	public final static int DIALOG_TYPE_DEFAULT = 0;
	public final static int DIALOG_TYPE_YESNO = 0x10;
	public final static int DIALOG_TYPE_SELECT_LIST = 0x11;
	public final static int DIALOG_TYPE_CHECKBOX_LIST = 0x12;
	public final static int DIALOG_TYPE_DATE = 0x13;
	public final static int DIALOG_TYPE_TIME = 0x14;
	public final static int DIALOG_TYPE_PROGRESS = 0x15;
	public static int dialogType = DIALOG_TYPE_DEFAULT;
	public static String dialogTitle = null;
	
	
	//---------------------------------------------------------------
	// Interface
	//---------------------------------------------------------------
	/**
	 * Get Waffle Version Info
	 * @return version string
	 */
	public double getWaffleVersion() {
		return WaffleActivity.WAFFLE_VERSON;
	}
	/**
	 * Log
	 */
	public void log(String msg) {
		WaffleActivity.mainInstance.log(msg);
	}
	public void log_error(String msg) {
		WaffleActivity.mainInstance.log_error(msg);
	}
	public void log_warn(String msg) {
		WaffleActivity.mainInstance.log_warn(msg);
	}
	/**
	 * Get android resource string
	 * @return resource string
	 */
	public String getResString(String name) {
		int id = waffle_activity.getResources().getIdentifier(name, "string", 
				waffle_activity.getPackageName());
		if (id == 0) {
			return "";
		}
		return waffle_activity.getResources().getString(id);
	}
	
	/**
	 * Get last error jsWaffle
	 * @return error string
	 */
	public String getLastError() {
		return WaffleActivity.mainInstance.last_error_str;
	}
		
	/**
	 * beep
	 */
	public void beep() {
		if (beep_tone == null) {
			Uri ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			beep_tone = RingtoneManager.getRingtone(waffle_activity, ringtone);
			if (beep_tone == null) {
				ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
				beep_tone = RingtoneManager.getRingtone(waffle_activity, ringtone);
			}
		}
		if (beep_tone != null) {
			if (beep_tone.isPlaying()) return;
			beep_tone.play();
		}
	}
	Ringtone beep_tone = null;
	/**
	 * ring
	 */
	public void ring() {
		if (ring_tone == null) {
			Uri ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			ring_tone = RingtoneManager.getRingtone(waffle_activity, ringtone);
		}
		if (ring_tone != null) {
			if (ring_tone.isPlaying()) return;
			ring_tone.play();
		}
	}
	Ringtone ring_tone = null;
	
	public void ring_stop() {
		if (ring_tone == null) {
			return;
		}
		if (ring_tone.isPlaying()) ring_tone.stop();
	}
	
	
	/**
	 * vibrate
	 * @param msec
	 */
	public void vibrate(long msec) {
		long pattern = msec;
		if (pattern == 0) pattern = 500;
        Vibrator vibrator = (Vibrator) waffle_activity.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern);
	}
	/**
	 * makeToast
	 * @param msg
	 */
	public void makeToast(String msg) {
		Toast.makeText(waffle_activity, msg, Toast.LENGTH_SHORT).show();
	}
	
	
	/**
	 * play media file (for BGM)
	 * @param filename
	 * @param loopMode
	 * @paran audioType (music || ring)
	 * @return MediaPlayer
	 */
	public MediaPlayer createPlayer(String soundfile, int loopMode, String audioType) {
		MediaPlayer mp = new MediaPlayer();
		try {
			if (audioType == "music") {
				mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
			}
			else if (audioType == "ring" || audioType == null || audioType == "") {
				mp.setAudioStreamType(AudioManager.STREAM_RING);
			}
			//
			Uri uri = WaffleUtils.checkFileUri(soundfile);
			String path = uri.getPath();
			if (path.startsWith("/android_asset/")) {
				path = path.substring(15);
				AssetFileDescriptor fd = waffle_activity.getAssets().openFd(path);
				mp.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
			} else {
				mp.setDataSource(path);
			}
			mp.prepare();
			mp.setLooping(loopMode == 1);
		} catch (IOException e) {
			log_error("[audio]" + e.getMessage() + "/" + soundfile);
			return null;
		}
		return mp;
	}
	public void playPlayer(MediaPlayer mp) {
		if (mp == null) return;
		mp.seekTo(0);
		mp.start();
	}
	public void stopPlayer(MediaPlayer mp) {
		if (mp == null) return;
		if (mp.isPlaying()) {
			mp.stop();
		}
	}
	public boolean isPlayingSound(MediaPlayer mp) {
		if (mp == null) return false;
		return (mp.isPlaying());
	}
	public void unloadPlayer(MediaPlayer mp) {
		if (mp == null) return;
		try {
			mp.release();
			mp = null;
		} catch (Exception e) {
		}
	}
	
	/**
	 * play sound file (for Realtime play or loop) ... OGG is best!
	 */
	private SoundPool pool = null;
	public int loadSoundPool(String filename) {
		int res = -1;
		if (pool == null) pool = new SoundPool(5, AudioManager.STREAM_RING, 0);
		try {
			Uri uri = WaffleUtils.checkFileUri(filename);
			String path = uri.getPath();
			if (path.startsWith("/android_asset/")) {
				path = path.substring(15);
				AssetFileDescriptor fd = waffle_activity.getAssets().openFd(path);
				if (fd == null) throw new IOException("FileOpenError:" + path);
				res = pool.load(fd, 1);
			} else {
				res = pool.load(path, 1);
			}
			if (res >= 0) return res;
			log_error("[loadSoundPool]error:" + filename);
			return -1;
		} catch (Exception e) {
			log_error("[audio]" + e.getMessage());
			return -1;
		}
	}
	public void playSoundPool(int id, int loop) {
		if (pool == null) {
			log_error("SoundPool not ready");
			return;
		}
		AudioManager am = (AudioManager)waffle_activity.getSystemService(Activity.AUDIO_SERVICE);
		int v = am.getStreamVolume(AudioManager.STREAM_RING);
		int max_v = am.getStreamMaxVolume(AudioManager.STREAM_RING);
		float vf = (float)v / (float)max_v;
		// volume (0-1.0)
		log("volume=" + vf);
		pool.play(id, vf, vf, 1, loop, 1);
	}
	public void stopSoundPool(int id) {
		if (pool == null) {
			log_error("SoundPool not ready");
			return;
		}
		pool.stop(id);
	}
	public void unloadSoundPool(int id) {
		if (pool == null) {
			log_error("SoundPool not ready");
			return;
		}
		pool.unload(id);
	}	
	
	/**
	 * finish activity
	 */
	public void finish() {
		waffle_activity.finish();
	}
		
	/**
	 * Add menu
	 */
	public void setMenuItem(int itemNo, boolean visible, String title, String iconName) {
		waffle_activity.setMenuItem(itemNo, visible, title, iconName);
	}
	public static String menu_item_callback_funcname = null;
	public void setMenuItemCallback(String callback_fn) {
		menu_item_callback_funcname = callback_fn;
	}
	
	/**
	 * Dialog
	 */
	public void setPromptType(int no, String title) {
		dialogType = no;
		dialogTitle = title;
	}
	
	/**
	 * capture screen and save to file
	 * @param filename
	 * @param format png or jpeg
	 * @return
	 */
	public boolean snapshotToFile(String filename, String format) {
		// snapshot
		Bitmap bmp = null;
		try {
			webview.setDrawingCacheEnabled(true);
			bmp = Bitmap.createBitmap(webview.getDrawingCache());
			webview.setDrawingCacheEnabled(false);
		} catch (Exception e) {
			log_error("snapshot failed: " + e.getMessage());
			return false;
		}
		if (bmp == null) {
			log_error("snapshot failed: bmp = null");
			return false;
		}
		// save to file
		Bitmap.CompressFormat fmt = Bitmap.CompressFormat.PNG;
		format = format.toLowerCase();
		if (format == "jpeg" || format == "image/jpeg") {
			fmt = Bitmap.CompressFormat.JPEG;
		}
		try {
			byte[] w = bmp2data(bmp, fmt, 80/*middle*/);
			boolean b = writeDataFile(filename, w);
			bmp.recycle(); // recycle !!
			return b;
		} catch (Exception e) {
			log_error("snapshot failed:" + e.getMessage());
			return false;
		}
	}
	 private static byte[] bmp2data(Bitmap src, Bitmap.CompressFormat format, int quality) {
		 ByteArrayOutputStream os=new ByteArrayOutputStream();
		 src.compress(format,quality,os);
		 return os.toByteArray();
	 }
	 private boolean writeDataFile(String filename, byte[] w) throws Exception {
		 OutputStream out = WaffleUtils.getOutputStream(filename, waffle_activity);
		 if (out == null) throw new Exception("FileOpenError:" + filename);
		 try {
			 out.write(w, 0, w.length);
			 out.close();
			 return true;
		 } catch (Exception e) {
			 out.close();
		 }
		 return false;
	 }
	 
	 /**
	  * get data from url sync
	  */
	 public boolean httpGet(final String url, final String callback_ok, final String callback_ng, final String tag) {
		 new Thread(new Runnable() {
				@Override
				public void run() {
					String result = WaffleUtils.httpGet(url);
					String query = "";
					if (result != null) {
						result = URLEncoder.encode(result);
						query = callback_ok + "('" + result + "'," + tag +  ")";
					} else {
						query = callback_ng + "('" + WaffleUtils.httpLastError + "'," + tag +  ")";
					}
					callJsEvent(query);
				}
			 }).start();
		 return true;
	 }
	 
	 public boolean httpPostJSON(final String url, final String json, final String callback, final int tag) {
		 new Thread(new Runnable() {
				@Override
				public void run() {
					String result = WaffleUtils.httpPostJSON(url, json);
					// escape result
					result = result.replace("\\", "\\\\");
					result = result.replace("\"", "\\\"");
					// result
					String query = callback + "(\"" + (result) + "\"," + tag +  ")";
					callJsEvent(query);
				}
			 }).start();
		 return true;
	 }
	 /**
	  * download file
	  * @param url
	  * @param filename
	  * @param callback
	  * @param tag
	  */
	 public void httpDownload(final String url, final String filename, final String callback, final int tag) {
		 new Thread(new Runnable() {
			@Override
			public void run() {
				boolean b = WaffleUtils.httpDownloadToFile(url, filename, waffle_activity);
				String query = callback + "(" + (b ? "true" : "false") + "," + tag +  ")";
				callJsEvent(query);
			}
		 }).start();
	 }
	 
	 public void clipboardSetText(String text) {
		 ClipboardManager cm = (ClipboardManager)waffle_activity.getSystemService(Activity.CLIPBOARD_SERVICE);
		 cm.setText(text);
	 }
	 public String clipboardGetText() {
		 ClipboardManager cm = (ClipboardManager)waffle_activity.getSystemService(Activity.CLIPBOARD_SERVICE);
		 return cm.getText().toString();
	 }
	 
	//---------------------------------------------------------------
	// Event Wrapper
	//---------------------------------------------------------------
	
	private Hashtable<String, EventList> eventList = null;
	public void addEventListener(String eventName, String callback, int tag) {
		if (eventList == null) eventList = new Hashtable<String, EventList>();
		EventList e = eventList.get(eventName);
		if (e == null) {
			e = new EventList();
			eventList.put(eventName, e);
		}
		EventListItem i = new EventListItem();
		i.callback = callback;
		i.tag = tag;
		e.list.add(i);
	}
	public void removeEventListener(String eventName, int tag) {
		if (eventList == null) return;
		EventList e = eventList.get(eventName);
		if (e == null) return;
		for (int i = 0; i < e.list.size(); i++) {
			EventListItem item = e.list.get(i);
			if (item.tag == i) {
				e.list.remove(i);
				break;
			}
		}
	}
	
	private void doEventListener(String eventName, String paramStr) {
		if (eventList == null) return;
		EventList e = eventList.get(eventName);
		if (e == null) return;
		for (int i = 0; i < e.list.size(); i++) {
			EventListItem item = e.list.get(i);
			String args = "";
			if (paramStr != null) {
				args = item.tag + "," + paramStr;
			} else {
				args = Integer.toString(item.tag);
			}
			String p = String.format("%s(%s)", item.callback, args);
			callJsEvent(p);
		}
	}
	
	@Override
	public void onPause() { doEventListener("pause", null); }
	@Override
	public void onResume() { doEventListener("resume", null); }
	@Override
	public void onDestroy() { doEventListener("destroy", null); }
	@Override
	public void onPageStarted(String url) {
		// remove listener
		if (eventList != null) {
			eventList.clear();
			eventList = null;
		}
	}
	@Override
	public void onPageFinished(String url){ doEventListener("pageFinished", url); }
		
	//---------------------------------------------------------------
	// Private method
	//---------------------------------------------------------------
    public void callJsEvent(String query) {
        waffle_activity.callJsEvent(query);
    }
    class EventListItem {
    	String callback;
    	int tag;
    }
	class EventList {
		ArrayList<EventListItem> list = new ArrayList<EventListItem>();
	}
	

}
