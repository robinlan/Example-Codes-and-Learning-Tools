package com.kujirahand.jsWaffle.plugins;

import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;

import com.kujirahand.jsWaffle.WaffleActivity;
import com.kujirahand.jsWaffle.model.WafflePlugin;

public class AccelPlugin extends WafflePlugin {

	Vector<AccelListener> listeners = new Vector<AccelListener>();
	
	// javascript interfalce
	/**
	 * Set Sensor event callback
	 * @param funcname
	 * @param requestCode
	 */
	public int setAccelCallback(String funcname, int requestCode) {
		AccelListener ac = new AccelListener(waffle_activity);
		listeners.add(ac);
		ac.sensour_callback_funcname = funcname;
		ac.requestCode = requestCode;
		ac.start();
		return listeners.size();
	}
	
	public void clearAccelAll() {
		for (int i = 0; i < listeners.size(); i++) {
			clearAccel(i + 1);
		}
	}
	
	public void clearAccel(int watchId) {
		int index = watchId - 1;
		if (listeners.size() <= 0) return;
		if (!(0 <= index && index < listeners.size())) {
			return;
		}
		AccelListener ac = listeners.get(index);
		if (ac == null) return;
		if (ac.isLive) {
			try {
				ac.stop();
			} 
			catch (Exception e) {
				waffle_activity.log_error("[Accel Error] clearAccel Error");
			}
		}
		listeners.set(index, null);
	}
	
	/**
	 * Set Shake Event callback
	 */
	public void setShakeCallback(String shake_callback_fn, String shake_end_callback_fn, double shake_freq, double shake_end_freq, int requestCode) {
		AccelListener ac = new AccelListener(waffle_activity);
		listeners.add(ac);
		// shake
		ac.shake_callback_funcname = shake_callback_fn;
		ac.shake_freq = shake_freq;
		// shake end
		ac.shake_end_callback_funcname = shake_end_callback_fn;
		ac.shake_end_freq = shake_end_freq;
		//
		ac.requestCode = requestCode;
		ac.start();
	}
	
	public void onPause() {
		for (AccelListener ac : listeners) {
			if (ac != null) {
				ac.stop();
			}
		}
	}
	
	public void onResume() {
		for (AccelListener ac : listeners) {
			if (ac != null) {
				ac.start();
			}
		}
	}
	
	public void onPageStarted() {
		clearAccelAll();
		listeners.clear();
	}
	
	public void onDestroy() {
		clearAccelAll();
		listeners.clear();
	}
}

class AccelListener implements SensorEventListener
{
	SensorManager sensorMan;
	WaffleActivity context;
	public Boolean isLive = false;
	
	public static final int SHAKE_STATUS_READY = 0;
	public static final int SHAKE_STATUS_SHAKING = 1;
	
	public int requestCode;
	public String sensour_callback_funcname = null;
	public String shake_callback_funcname = null;
	public String shake_end_callback_funcname = null;
	public double shake_freq = 20.0f;
	public double shake_end_freq = 8.0f;
	public int shake_status = SHAKE_STATUS_READY;
	
	public AccelListener(WaffleActivity context) {
		this.context = context;
		sensorMan = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
	}
	
	public void start() {
		List<Sensor> sensors = sensorMan.getSensorList(Sensor.TYPE_ACCELEROMETER);
		if (sensors.size() > 0) {
			Sensor se = sensors.get(0);
			isLive = sensorMan.registerListener(this, se, SensorManager.SENSOR_DELAY_GAME);
		}
		shake_status = SHAKE_STATUS_READY;
	}
	
	public void stop() {
		if (isLive) {
			sensorMan.unregisterListener(this);
			isLive = false;
		}
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	private long lastTime = 0;
    private float[] currentOrientationValues = {0.0f, 0.0f, 0.0f};
    private float[] currentAccelerationValues = {0.0f, 0.0f, 0.0f};

	@Override
	public void onSensorChanged(SensorEvent event) {
    	float accelX = event.values[0];
    	float accelY = event.values[1];
    	float accelZ = event.values[2];
        switch(event.sensor.getType()) {
        case Sensor.TYPE_ACCELEROMETER:
            // 傾き（ハイカット）
            currentOrientationValues[0] = event.values[0] * 0.1f + currentOrientationValues[0] * (1.0f - 0.1f);
            currentOrientationValues[1] = event.values[1] * 0.1f + currentOrientationValues[1] * (1.0f - 0.1f);
            currentOrientationValues[2] = event.values[2] * 0.1f + currentOrientationValues[2] * (1.0f - 0.1f);
            // 加速度（ローカット）
            currentAccelerationValues[0] = event.values[0] - currentOrientationValues[0];
            currentAccelerationValues[1] = event.values[1] - currentOrientationValues[1];
            currentAccelerationValues[2] = event.values[2] - currentOrientationValues[2];
            
            // 振ってる？　絶対値（あるいは２乗の平方根）の合計がいくつ以上か？
            float targetValue = 
                Math.abs(currentAccelerationValues[0]) + 
                Math.abs(currentAccelerationValues[1]) +
                Math.abs(currentAccelerationValues[2]);
            if(targetValue > shake_freq) {
            	/*
            	currentOrientationValues[0] = 0;
            	currentOrientationValues[1] = 0;
            	currentOrientationValues[2] = 0;
            	currentAccelerationValues[0] = 0;
            	currentAccelerationValues[1] = 0;
            	currentAccelerationValues[2] = 0;
            	*/
            	//振ったときの処理
            	if (shake_status == SHAKE_STATUS_READY) {
            		if (shake_callback_funcname != null) {
            			context.callJsEvent(shake_callback_funcname + "(" + requestCode + ")");
            			shake_status = SHAKE_STATUS_SHAKING;
            		}
            	}
            }
            else if(targetValue < shake_end_freq) {
            	// 振ってないときの処理
            	if (shake_status == SHAKE_STATUS_SHAKING) {
            		if (shake_end_callback_funcname != null) {
            			context.callJsEvent(shake_end_callback_funcname + "(" + requestCode + ")");
            			shake_status = SHAKE_STATUS_READY;
            		}
            	}
            }
            /*
            // 傾きは？３つの絶対値（あるいは２乗の平方根）のうちどれがいちばんでかいか？
            if(Math.abs(currentOrientationValues[0]) > 7.0f) {
                //orientation.setText("横");
            } else if(Math.abs(currentOrientationValues[1]) > 7.0f) {
                //orientation.setText("縦");
            } else if(Math.abs(currentOrientationValues[2]) > 7.0f) {
                //orientation.setText("水平");
            } else {
                //orientation.setText("");
            }
            */
            break;
        default:
        }
        // send event
        if (sensour_callback_funcname == null || sensour_callback_funcname == "") return;
        long now = SystemClock.uptimeMillis();
        long diff = now - lastTime;
        if (diff > 100) {
			String accel_str = sensour_callback_funcname + 
				"(" + accelX + "," + accelY + "," + accelZ + "," + requestCode + ")";
			context.callJsEvent(accel_str);
			lastTime = now;
        }
	}

}