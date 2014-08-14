package com.example.Tips;
import com.kujirahand.jsWaffle.model.WafflePlugin;
public class TestPlugin extends WafflePlugin {
	public String getMessage() {
		return "Hello!";
	}
	
	public void executeJS() {
		waffle_activity.callJsEvent("alert('3+5='+(3+5))");
	}
	public void executeJS2() {
		webview.loadUrl("javascript:alert('3+5='+(3+5))");
	}
}
