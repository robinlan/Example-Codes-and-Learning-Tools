package com.kujirahand.jsWaffle.plugins;

import java.util.Date;
import java.util.Vector;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;

import com.kujirahand.jsWaffle.WaffleActivity;
import com.kujirahand.jsWaffle.model.WafflePlugin;

public class GPSPlugin extends WafflePlugin
{
	Vector<GeoListener> listeners = new Vector<GeoListener>();
	
	// javascript interface
	public int getCurrentPosition(String callback_ok, String callback_ng, boolean accuracy_fine, int timeout, int maximumAge, int tag) {
		GeoListener geo = new GeoListener(waffle_activity);
		listeners.add(geo);
		geo.tag = tag;
		geo.callback_ok = callback_ok;
		geo.callback_ng = callback_ng;
		geo.timeout = timeout;
		geo.maximumAge = maximumAge;
		geo.report_count = 1;
		geo.accuracy_fine = accuracy_fine;
		geo.start(accuracy_fine);
		geo.flagLive = true;
		return listeners.size();
	}
	
	public int watchPosition(String callback_ok, String callback_ng, boolean accuracy_fine, int timeout, int maximumAge, int tag) {
		GeoListener geo = new GeoListener(waffle_activity);
		listeners.add(geo);
		geo.tag = tag;
		geo.callback_ok = callback_ok;
		geo.callback_ng = callback_ng;
		geo.report_count = -1;
		geo.accuracy_fine = accuracy_fine;
		geo.timeout = timeout;
		geo.maximumAge = maximumAge;
		geo.start(false);
		geo.flagLive = true;
		return listeners.size();
	}
	
	public void clearWatch(int watchId) {
		try {
			int index = watchId - 1;
			if (listeners.size() <= index || index < 0) return;
			GeoListener i = listeners.get(index);
			if (i == null) return;
			i.flagLive = false;
			i.stop();
			waffle_activity.log("clearWatchPosition:" + watchId);
			listeners.set(index, null);
		} catch (Exception e) {
			waffle_activity.log_error("[GPS ERROR]clearWatch:" + e.getMessage());
		}
	}
	public void clearWatchAll() {
		for (int i = 0; i < listeners.size(); i++) {
			clearWatch(i + 1);
		}
	}
	
	public void onPause() {
		// geolocation_listeners
		for (int i = 0; i < listeners.size(); i++) {
			GeoListener g = listeners.get(i);
			if (g == null) continue;
			g.stop();
		}
	}
	
	public void onResume() {
		// geolocation_listeners
		for (int i = 0; i < listeners.size(); i++) {
			GeoListener g = listeners.get(i);
			if (g == null) continue;
			if (g.flagLive) g.start();
		}
	}
	
	public void onPageStarted() {
		clearWatchAll();
		listeners.clear();
	}
	
	public void onDestroy() {
		clearWatchAll();
		listeners.clear();
	}
}

class GeoListener implements LocationListener
{
	public float min_dist = 1f;  // 1m
	public long report_count = 0;
	public Boolean flagLive = false;
	private static LocationManager locman = null;
	public int maximumAge = 5000;
	public int timeout = -1;
	public String callback_ok = "DroidWaffle._geolocation_fn_ok";
	public String callback_ng = "DroidWaffle._geolocation_fn_ng";
	public WaffleActivity waffle_activity;
	public int tag = -1;
	public boolean accuracy_fine = false;
	public boolean accuracy_fine_last = false;
	private Date last_report_time = null; // 最後に位置情報を報告した時間
	private String last_result = null; // 最後の報告結果
	
	final int ERROR_PERMISSION_DENIED = 1;
	final int ERROR_POSITION_UNAVAILABLE = 2;
	final int ERROR_TIMEOUT = 3;
	
	private Handler handler = new Handler();
	
	public GeoListener(WaffleActivity app) {
		this.waffle_activity = app;
	}
	
	public void start() {
		this.start(true);
	}
	
	public void start(boolean accuracy_fine) {
		waffle_activity.log(
			String.format(
				"GeoListener: acc:%s, timeout:%s, maximumAge:%s, repeat:%s", 
				this.accuracy_fine ? "true":"false",
				timeout, 
				maximumAge, 
				report_count
			)
		);
		accuracy_fine_last = accuracy_fine;
		// check maximumAge
		if (last_report_time != null) {
			if (report_count == 1) {
				Date now = new Date();
				if (now.getTime() - last_report_time.getTime() < maximumAge) {
					jsCallSuccess(last_result);
					report_count = 0;
					flagLive = false;
					return;
				}
			}
		}
		
		// select provider
		Criteria crit = new Criteria();
		if (accuracy_fine) {
			crit.setAccuracy(Criteria.ACCURACY_FINE); // GPS
		} else {
			crit.setAccuracy(Criteria.ACCURACY_COARSE); // Faster
		}
		String providerName = getLocMan().getBestProvider(crit, true);
		LocationProvider p = null;
		if (providerName != null && providerName != "") {
			p = getLocMan().getProvider(providerName);
		}
		if (p == null) {
			jsThrowError("no provider", ERROR_POSITION_UNAVAILABLE);
			return;
		}
		
		// register
		waffle_activity.log("[GPS] set provider:" + providerName);
		getLocMan().requestLocationUpdates(providerName, maximumAge, min_dist, this);
		checkTimeout();
	}
	
	private void checkTimeout() {
		if (timeout <= 0) return;
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (report_count > 0) report_count--;
				// check timeout
				Boolean flag_ok = true;
				Date now = new Date();
				if (flagLive == false) return;
				if (last_report_time == null) {
					flag_ok = false;
				}
				else if (now.getTime() - last_report_time.getTime() > timeout) {
					flag_ok = false;
				}
				if (!flag_ok) {
					// raise error
					jsThrowError("timeout", ERROR_TIMEOUT);
				}
				// repeat ?
				if (report_count != 0) {
					last_report_time = new Date(); // set last timeout
					handler.postDelayed(this, timeout);
				}
				else {
					flagLive = false;
					stop();
				}
			}
		}, timeout);
	}
	
	public void jsThrowError(String msg, int code) {
		waffle_activity.log_error(msg);
		String s = String.format("%s({message:'%s', code:%s},%s)", callback_ng, msg, code, tag);
		waffle_activity.callJsEvent(s);
	}
	public void jsCallSuccess(String param) {
		String q = callback_ok + "(" + param + "," + tag + ")";
		waffle_activity.callJsEvent(q);
		last_result = param;
	}

	public void stop() {
		try {
			getLocMan().removeUpdates(this);
		}
		catch (Exception e) {
		}
	}
	
	@Override
	public void onLocationChanged(Location location) {
		last_report_time = new Date();
		// Check Stop
		if (report_count > 0) {
			report_count--;
			if (report_count <= 0) {
				flagLive = false;
				stop();
			}
		}
		
		// Call Event
		// (HTML5 geolocations 互換)
		String param = "{" +
		"coords:{" +
			"latitude:"  + location.getLatitude() + "," + // 緯度
			"longitude:" + location.getLongitude() + "," + // 経度
			"altitude:" + location.getAltitude() + "," + // 標高
			"accuracy:" + location.getAccuracy() + "," + // 精度
			"heading:" + location.getBearing() + "," + // 方向
			"speed:" + location.getSpeed() + "," + // 速度
		"}," +
		"timestamp:" + location.getTime() + // 時間
		"}";
		jsCallSuccess(param);
		
		if (accuracy_fine != accuracy_fine_last) {
			stop();
			start(accuracy_fine);
			waffle_activity.log("[Location] change provider");
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		switch (status) {
		case LocationProvider.AVAILABLE:
			waffle_activity.log("LocationStatus:AVAILABLE");
			break;
		case LocationProvider.OUT_OF_SERVICE:
			waffle_activity.log("LocationStatus:OUT_OF_SERVICE");
			break;
		case LocationProvider.TEMPORARILY_UNAVAILABLE:
			waffle_activity.log("LocationStatus:TEMPORARILY_UNAVAILABLE");
			break;
		default:
			waffle_activity.log("LocationStatus:Unknown");
			break;
		}
	}
	
	private LocationManager getLocMan() {
		if (locman != null) {
			return locman;
		}
		locman = (LocationManager)waffle_activity.getSystemService(Activity.LOCATION_SERVICE);
		return locman;
	}
}
