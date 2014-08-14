/***

jsWaffle for Android (JavaScript Wrapper)

***/
/**
 * @projectDescription jsWaffle for Android (JavaScript Wrapper)
 * @author	kujirahand.com (http://kujirahand.com)
 * @see http://d.aoikujira.com/jsWaffle/wiki/
 * @namespace jsWaffle.droid
 */

(function(self){

// already included ?
if (typeof(self.droid) != 'undefined' || self.droid) return;

self.droid = {};

// helper method
if (typeof(self.$) == 'undefined') {
	
	self.$ = function (id) {
		var obj = document.getElementById(id);
		// extends dom obj
		if (obj != null) {
			obj.hide = function() { this.style.display = "none";  };
			obj.show = function() { this.style.display = "block"; };
		}
		return obj;
	};
	
}

var func_bank = {
		
	items : [],
		
	getTag : function () {
		return this.items.length;
	},
	
	setItem : function (tag, callback) {
		this.items[tag] = callback;
	},
	
	registerItem : function (callback) {
		var tag = this.getTag();
		this.setItem(tag, callback);
		return tag;
	},
	
	getItem : function (tag) {
		return this.items[tag];
	},
	
	___ : 0
};

var end_of_define_method = {
	droid : function () { return 0; },
	cross : function () { return 0; }
};

var plugin_defineDroidMethod = function (pluin_info, method_list) {
	var plugin_name  = pluin_info.pluginName;
	var plugin_empty = (typeof(self[plugin_name]) == 'undefined' || !self[plugin_name]);
	
	if (plugin_empty) {
		self[plugin_name] = {}; // dummy
	}
	
	// insert method to droid
	for (var method_name in method_list) {
		var method_info = method_list[method_name];
		if (plugin_empty) {
			self.droid[method_name] = method_info['cross'];
		} else {
			self.droid[method_name] = method_info['droid'];
		}
	}
};

plugin_defineDroidMethod(
	{ pluginName:'_base', className:'ABasicPlugin' },
	{
		/** @id droid.getWaffleVersion */
		getWaffleVersion : {
			droid : function () { return _base.getWaffleVersion(); },
			cross : function () { return 999.999; }
		},
		
		/** @id droid.log */
		log : {
			droid : function (msg) { _base.log(msg); },
			cross : function (msg) { console.log(msg); }
		},
		/** @id droid.log_error */
		log_error : {
			droid : function (msg) { _base.log_error(msg); },
			cross : function (msg) { console.error(msg); }
		},
		/** @id droid.log_warn */
		log_warn : {
			droid : function (msg) { _base.log_warn(msg); },
			cross : function (msg) { console.warn(msg); }
		},
		/** @id droid.getLastError */
		getLastError : {
			droid : function () { return String(_base.getLastError()); },
			cross : function () { console.log('getLastError'); }
		},
		
		/** @id droid.beep */
		beep : {
			droid : function () { _base.beep(); },
			cross : function () { console.log('beep'); }
		},
		
		/** @id droid.vibrate */
		vibrate : {
			droid : function (msec) {
				if (typeof(msec) != 'number') msec = 500;
				_base.vibrate(msec);
			},
			cross : function (msec) { console.log('vibrate'); }
		},
		
		/** @id droid.ring */
		ring : {
			droid : function () { _base.ring(); },
			cross : function () { console.log('ring'); }
		},
		/** @id droid.ring_stop */
		ring_stop : {
			droid : function () { _base.ring_stop(); },
			cross : function () { console.log('ring_stop'); }
		},
		
		/** @id droid.makeToast */
		makeToast : {
			droid : function (message) { _base.makeToast(message); },
			cross : function (message) { console.log('makeToast:' + message); }
		},
		
		/** @id droid.addEventListener */
		addEventListener : {
			droid : function (eventName, listener) {
				if (typeof(self.droid._addEventListener) != "function") {
					self.droid._addEventListener = function (tag, args) {
						var f = func_bank.getItem(tag);
						if (typeof(f) == "function") {
							f(args);
						}
					};
					self.droid._addEventListener_items = [];
				}
				var tag = func_bank.registerItem(listener);
				_base.addEventListener(eventName, "droid._addEventListener", tag);
				self.droid._addEventListener_items.push({"tag":tag, "listener": listener});
			},
			cross : function (eventName, listener) {
				console.log('addEventListener:' + eventName);
			}
		},
		
		/** @id droid.removeEventListener */
		removeEventListener : {
			droid : function (eventName, listener) {
				if (self.droid._addEventListener_items == null) return;
				for (var i = 0; i < self.droid._addEventListener_items.length; i++) {
					var n = self.droid._addEventListener_items[i];
					if (n.listener == listener) {
						_base.removeEventListener(eventName, n.tag);
						break;
					}
				}
			},
			cross : function (eventName, listener) {
				console.log('removeEventListener:' + eventName);
			}
		},
		
		/** @id droid.setMenuItem */
		setMenuItem : {
			droid : function (itemNo, visible, title, iconName, callback) {
				_base.setMenuItemCallback("droid._setMenuItem");
				_base.setMenuItem(itemNo, visible, title, iconName);
				if (!self.droid._setMenuItem) {
					self.droid._setMenuItem_items = [];
					self.droid._setMenuItem = function (itemNo) {
						var f = self.droid._setMenuItem_items[itemNo];
						if (typeof(f) == "function") f();
					};
				}
				self.droid._setMenuItem_items[itemNo] = callback;
			},
			cross : function (itemNo, visible, title, iconName, callback) {
				console.log('setMenuItem:' + [itemNo, visible, title, iconName].join(','));
			}
		},
		
		/** @id droid.dialogYesNo */
		dialogYesNo : {
			droid : function (title, message) {
				_base.setPromptType(0x10, title);
				var r = prompt(message);
				_base.setPromptType(0, "prompt");
				return (r == "true");
			},
			cross : function (title, message) {
				return confirm(title + ":" + message);
			}
		},
		
		/** @id droid.dialogList */
		dialogList : {
			droid : function (title, items) {
				if (typeof(items) == "string") { items = items.split("\n"); }
				if (!(items instanceof Array)) { items = []; }
				_base.setPromptType(0x11, title);
				var r = prompt("", items.join(";;;"));
				_base.setPromptType(0, "prompt");
				if (r == null) {
					return null;
				} else {
					return String(r);
				}
			},
			cross : function (title, items) {
				return prompt(title + ':select(' + items.join(",")+')');
			}
		},
		
		/** @id droid.dialogCheckboxList */
		dialogCheckboxList : {
			droid : function (title, items) {
				_base.setPromptType(0x12, title);
				var r = prompt("", items.join(";;;"));
				_base.setPromptType(0, "prompt");
				r = String(r);
				if (r != null) {
					return r.split(";;;");
				}
				return null;
			},
			cross : function (title, items) {
				return prompt(title + ':select(' + items.join(",")+')');
			}
		},
		
		/** @id dialogDatePicker */
		dialogDatePicker : {
			droid : function (defaultDate) {
				_base.setPromptType(0x13, "");
				if (typeof(defaultDate) != "object") {
					defaultDate = new Date();
				}
				var d = defaultDate;
				var r = prompt("", new Array(d.getFullYear(), d.getMonth(), d.getDate()).join(","));
				var a = String(r).split(",");
				d = new Date(a[0], a[1], a[2]);
				_base.setPromptType(0, "prompt");
				return d;
			},
			cross : function (defaultDate) {
				var d = prompt("Please input Date", defaultDate);
				return new Date(d);
			}
		},
		
		/** @id dialogTimePicker */
		dialogTimePicker : {
			droid : function (hours, minutes) {
				if (hours == undefined || minutes == undefined) {
					var d = new Date();
					hours = d.getHours();
					minutes = d.getMinutes();
				}
				_base.setPromptType(0x14, "");
				var r = prompt("", hours + ":" + minutes);
				_base.setPromptType(0, "prompt");
				return String(r);
			},
			cross : function (hours, minutes) {
				return prompt("Please input Time (hh:nn)");
			}
		},
		
		/** @id droid.dialogSeekbar */
		dialogSeekbar : {
			droid : function (title,min,max,defaltValue) {
				if (typeof(min) != "number") min = 0;
				if (typeof(max) != "number") max = 100;
				if (typeof(defaltValue) != "number") defaltValue = Math.floor((max - min) / 2);
				_base.setPromptType(0x15, title);
				var r = prompt("", min + "," + max + "," + defaltValue);
				_base.setPromptType(0, "prompt");
				return parseInt(r);
			},
			cross : function (title, min, max, defaultValue) {
				var s = prompt(title + ": (" + min + "-" + max + ")", defaultValue);
				return parseInt(s);
			}
		},
		
		/** @id droid.getResString */
		getResString : {
			droid : function (id) { return String(_base.getResString(id)); },
			cross : function (id) { return "[" + id + "]" }
		},
		 
		
		/** @id droid.playSound */
		playSound : {
			droid : function (filename, loopMode, audioType) {
				if (loopMode == undefined || loopMode == null) loopMode = false;
				if (audioType == undefined) audioType = "ring";
				var i = _base.createPlayer(filename, loopMode ? 1 : 0, audioType);
				if (i != null) {
					_base.playPlayer(i);
				}
				return i;
			},
			cross : function (filename) { console.log("playSound:" + filename); }
		},
		
		/** @id droid.stopSound */
		stopSound : {
			droid : function (playerObj) {
				_base.stopPlayer(playerObj); /* or playerObj.stop() */ 
				_base.unloadPlayer(playerObj);
			},
			cross : function () {}
		},
		
		/** @id droid.isPlayingSound */
		isPlayingSound : {
			droid : function (playerObj) { return _base.isPlayingSound(playerObj); },
			cross : function () { return false; }
		},
		
		/** @id droid.loadSoundPool */
		loadSoundPool : {
			droid : function (filename) { return _base.loadSoundPool(filename); },
			cross : function (filename) { console.log("loadSoundPool:" + filename); return -1; }
		},
		/** @id droid.playSoundPool */
		playSoundPool : {
			droid : function (soundId, loopCound) {
				if (soundId < 0) return;
				if (loopCound == undefined) loopCound = 0;
				_base.playSoundPool(soundId, loopCound);
			},
			cross : function (soundId, loopCound) { console.log("playSoundPool:" + soundId); }
		},
		/** @id droid.stopSoundPool */
		stopSoundPool : {
			droid : function (soundId) { _base.stopSoundPool(soundId); },
			cross : function (soundId) { console.log("stopSoundPool:" + soundId); }
		},
		/** @id droid.unloadSoundPool */
		unloadSoundPool : {
			droid : function (soundId) { _base.unloadSoundPool(soundId); },
			cross : function (soundId) { console.log("unloadSoundPool:" + soundId); }
		},
		
		
		/** @id droid.quit */
		quit : {
			droid : function() { _base.finish(); },
			cross : function() { console.log("quit"); window.close(); }
		},
		
		/** @id droid.snapshotToFile */
		snapshotToFile : {
			droid : function (filename, format) {
				if (format == undefined) { format = "png"; }
				return _base.snapshotToFile(filename, format);
			},
			cross : function (filename, format) {
				console.log('droid.snapshotToFile : ' + filename);
			}
		},
		
		/** @id droid.httpGet */
		httpGet : {
			droid : function (url, callback_ok, callback_ng) {
				var tag = func_bank.registerItem({ok:callback_ok,ng:callback_ng});
				self.droid._httpGet_ok = function (str, tag) {
					var f = func_bank.getItem(tag);
					str = decodeURIComponent(str);
					if (typeof(f.ok) == "function") f.ok(str);
				};
				self.droid._httpGet_ng = function (err, tag) {
					var f = func_bank.getItem(tag);
					if (typeof(f.ng) == "function") f.ng(err);
				};
				return String(_base.httpGet(url, "droid._httpGet_ok", "droid._httpGet_ng", tag));
			},
			cross : function (url,callback_ok, callback_ng) {
				console.log('httpGet : ' + url);
				var xmlhttp = new XMLHttpRequest();
				xmlhttp.open("GET", url);
				xmlhttp.onreadystatechange = function () {
					if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
						callback_ok(xmlhttp.responseText);
					} else {
						callback_ng(xmlhttp.status);
					}
				};
				xmlhttp.send(null);
			}
		},
		
		/** @id droid.httpDownload */
		httpDownload : {
			droid : function (url, filename, callback) {
				var tag = func_bank.getTag();
				func_bank.setItem(tag, callback);
				self.droid._httpDownload = function(ok, tag) {
					var callback = func_bank.getItem(tag);
					if(typeof(callback) == "function") {
						callback(ok);
					}
				};
				_base.httpDownload(url, filename, "droid._httpDownload", tag);
			},
			cross : function (url, filename, callback) {
				console.log('[dummy] httpDownload : ' + url);
				setTimeout(callback, 1000);
			}
		},
		
		/** @id droid.httpPost */
		httpPost : {
			droid : function (url, post_obj, callback) {
				var json = self.droid.stringify(post_obj);
				var tag = func_bank.registerItem(callback);
				self.droid._httpPost = function (result, tag) {
					var f = func_bank.getItem(tag);
					if (typeof(f) == "function") {
						f(String(result));
					}
				};
				var res = _base.httpPostJSON(url, json, "droid._httpPost", tag);
				return res;
			},
			cross : function (url, post_obj, callback) {
				console.log('[dummy] httpPost : ' + url);
				setTimeout(callback, 1000);
			}
		},
		
		/** @id droid.clipboardSetText */
		clipboardSetText : {
			droid : function (text) {
				_base.clipboardSetText(text);
			},
			cross : function (text) {
				console.log("[dummy] clipboardSetText : " + text);
				droid._clipboard = text;
			}
		},
		
		/** @id droid.clipboardGetText */
		clipboardGetText : {
			droid : function () {
				return String(_base.clipboardGetText());
			},
			cross : function () {
				console.log("[dummy] clipboardSetText : " + text);
				return droid._clipboard;
			}
		},
		
		___ : end_of_define_method
	});

plugin_defineDroidMethod(
	{ pluginName:'_accel', className:'AccelPlugin' },
	{
		/** @id droid.watchAccel */
		watchAccel : {
			droid : function (callback) {
				var tag = func_bank.getTag();
				func_bank.setItem(tag, callback);
				if (!self.droid._watchAccel) {
					self.droid._watchAccel = function (x,y,z,tag) {
						var f = func_bank.getItem(tag);
						f(x,y,z);
					};
				}
				return _accel.setAccelCallback("droid._watchAccel", tag);
			},
			cross : function (callback) {
				var watchId = setInterval(function(){
					callback(Math.random(), Math.random(), Math.random());
				}, 500);
				if (!self.droid._watchAccel_items) { self.droid._watchAccel_items = []; }
				self.droid._watchAccel_items.push(watchId);
				return watchId;
			}
		},
		
		/** @id droid.clearAccel */
		clearAccel : {
			droid : function (watchId) {
				if (watchId == undefined) {
					_accel.clearAccelAll();
				} else {
					_accel.clearAccel(watchId);
				}
			},
			cross : function (watchId) {
				if (watchId == undefined) {
					for (var i = 0; i < self.droid._watchAccel_items.length; i++) {
						clearInterval(self.droid._watchAccel_items[i]);
					}
				} else {
					clearInterval(watchId);
				}
			}
		},
		
		/** @id droid.watchShake */
		watchShake : {
			droid : function (shake_begin_callback_fn, shake_end_callback_fn, shake_begin_freq, shake_end_freq) {
				// set default value
				if (shake_begin_freq == undefined) shake_begin_freq = 20;
				if (shake_end_freq == undefined) shake_end_freq = 8;
				if (shake_begin_callback_fn == null || shake_begin_callback_fn == undefined || shake_begin_callback_fn == '') {
					_accel.clearAccelAll();
					return;
				}
				// register callback
				var tag = func_bank.registerItem({
					begin_f : shake_begin_callback_fn,
					end_f   : shake_end_callback_fn
				});
				self.droid._watchShake_begin = function (tag) {
					var f = func_bank.getItem(tag);
					if (typeof(f.begin_f) == "function") f.begin_f();
				};
				self.droid._watchShake_end = function (tag) {
					var f = func_bank.getItem(tag);
					if (typeof(f.end_f) == "function") f.end_f();
				};
				return _accel.setShakeCallback(
						"droid._watchShake_begin", "droid._watchShake_end", 
						shake_begin_freq, shake_end_freq, tag);
			},
			cross : function (shake_begin_callback_fn, shake_end_callback_fn) {
				var watchId = setInterval(function(){
					shake_begin_callback_fn();
					setTimeout(function(){
						if (typeof(shake_end_callback_fn) == "function") shake_end_callback_fn();
					},300);
				}, 5000);
				if (!self.droid._watchAccel_items) { self.droid._watchAccel_items = []; }
				self.droid._watchAccel_items.push(watchId);
				return watchId;
			}
		},
		
		___ : end_of_define_method
	});

plugin_defineDroidMethod(
	{ pluginName:'_db', className:'DatabasePlugin' },
	{
		
		/** @id droid.openDatabase */
		openDatabase : {
			droid : function (dbname) {
				var db = _db.openDatabase(dbname);
				return db;
			},
			cross : function (dbname) {
				var db = window.openDatabase(dbname, "1.0", "", 8 * 1024 * 1024);
				return db;
			}
		},
		
		/** @id droid.executeSql */
		executeSql : {
			droid : function (db, sql, fn_ok, fn_ng) {
				var tag = func_bank.getTag();
				func_bank.setItem(tag, { ok: fn_ok, ng : fn_ng });
				self.droid._executeSql_ok = function (list, tag) {
					var o = func_bank.getItem(tag);
					if (typeof(o.ok) == 'function') o.ok(list);
				};
				self.droid._executeSql_ng = function (err) {
					var o = func_bank.getItem(tag);
					if (typeof(o.ng) == 'function') o.ng(err);
				};
				_db.executeSql(db, sql, "droid._executeSql_ok", "droid._executeSql_ng", tag);
			},
			cross : function (db, sql, fn_ok, fn_ng) {
				db.transaction(function(tx) {
					tx.executeSql(sql,[],
						function(tx, rs){
							var result = [];
							if (rs.rows) {
								for (var i = 0; i < rs.rows.length; i++) {
									result.push(rs.rows.item(i));
								}
							}
							fn_ok(result);
						},
						function(err){ fn_ng(err); }
					);
				});
			}
		},
		
		/** @id droid.executeSqlSync */
		executeSqlSync : {
			droid : function (db, sql) {
				var r = _db.executeSqlSync(db, sql);
				if (r != null) {
					return eval("("+r+")");
				}
				return r;
			},
			cross : function (db, sql) {
				console.log("[dummy] executeSqlSync : " + sql);
			}
		},
		
		getDatabaseError : {
			droid : function(db){
				var r = _db.getDatabaseError(db, sql);
			},
			cross : function (db) {
				console.log("[dummy] getDatabaseError : ");
			}
		},
		
		___ : end_of_define_method
	}
);


plugin_defineDroidMethod(
	{ pluginName:'_gps', className:'GPSPlugin' },
	{
		/** @id droid.getCurrentPosition */
		getCurrentPosition : {
			droid : function (onSuccess, onError, option) {
				var tag = func_bank.registerItem({
					'ok': onSuccess,
					'ng': onError
				});
				self.droid._geolocation_fn_ok = function (obj, tag) {
					var o = func_bank.getItem(tag);
					if (typeof(o.ok) == "function") o.ok(obj);
				};
				self.droid._geolocation_fn_ng = function (err, tag) {
					var o = func_bank.getItem(tag);
					if (typeof(o.ng) == "function") o.ng(err);
				};
				// check option
				if (option == undefined || option == null) option = {};
				self.droid._geolocation_check_option(option);
				
				// register callback function
				return _gps.getCurrentPosition(
					"droid._geolocation_fn_ok",
					"droid._geolocation_fn_ng",
					option.enableHighAccuracy,
					option.timeout,
					option.maximumAge,
					tag
				);
			},
			cross : function (onSuccess, onError, accuracy_fine) {
				navigator.geolocation.getCurrentPosition(onSuccess, onError);
			}
		},
		
		/** @id droid.watchPosition */
		watchPosition : {
			droid : function (onSuccess, onError, option) {
				var tag = func_bank.registerItem({
					'ok': onSuccess,
					'ng': onError
				});
				self.droid._watchPosition_ok = function (obj, tag) {
					var o = func_bank.getItem(tag);
					if (typeof(o.ok) == "function") o.ok(obj);
				};
				self.droid._watchPosition_ng = function (err, tag) {
					var o = func_bank.getItem(tag);
					if (typeof(o.ng) == "function") o.ng(err);
				};
				// check option
				if (option == undefined || option == null) option = {};
				self.droid._geolocation_check_option(option);
				return _gps.watchPosition(
					"droid._watchPosition_ok",
					"droid._watchPosition_ng",
					option.enableHighAccuracy,
					option.timeout,
					option.maximumAge,
					tag
				);
			},
			cross : function (onSuccess, onError, option) {
				navigator.geolocation.watchPosition(onSuccess, onError);
			}
		},
		
		/** @id droid.clearWatchPosition */
		clearWatchPosition : {
			droid : function (watchId) {
				_gps.clearWatch(watchId);
			},
			cross : function (watchId) {
				navigator.geolocation.clearWatch(watchId);
			}
		},
		
		___ : end_of_define_method
		
	}
);
self.droid._geolocation_check_option = function (option) {
	var def_values = {timeout:0, maximumAge:5000, enableHighAccuracy:true};
	for (var key in def_values) {
		if (typeof(option[key]) == "undefined") option[key] = def_values[key];
	}
};

// emulate HTML5 geolocation (for Android 1.6)
if (typeof(navigator.geolocation) == "undefined") {
	navigator.geolocation = {
		getCurrentPosition : droid.getCurrentPosition,
		watchPosition : droid.watchPosition,
		clearWatch : droid.clearWatchPosition
	};
}

plugin_defineDroidMethod(
	{ pluginName:'_storage', className:'StoragePlugin' },
	{
		pref_set : {
			droid : function (key, value) { _storage.localStorage_put(key, value); },
			cross : function (key, value) { window.localStorage.setItem(key, value); }
		},
		pref_get : {
			droid : function (key, defValue) { 
				if (defValue == undefined) defValue = null;
				return String(_storage.localStorage_get(key, defValue));
			},
			cross : function (key, defValue) { return window.localStorage.getItem(key, defValue); }
		},
		pref_remove : {
			droid : function (key) { return _storage.localStorage_remove(key); },
			cross : function (key) { return window.localStorage.removeItem(key); }
		},
		pref_clear : {
			droid : function () { _storage.localStorage_clear(); },
			cross : function () { window.localStorage.clear(); }
		},
		/** @id droid.saveText */
		saveText : {
			droid : function (filename, text) {
				_storage.saveText(filename, text);
			},
			cross : function (filename, text) {
				localStorage.setItem(filename, text);
			}
		},
		
		/** @id droid.loadText */
		loadText : {
			droid : function (filename) {
				return String(_storage.loadText(filename));
			},
			cross : function (filename) {
				return localStorage.getItem(filename);
			}
		},
		
		/** @id droid.fileList */
		fileList : {
			droid : function (path) {
				var s = String(_storage.fileList(path));
				var a = s.split(";");
				return a;
			},
			cross : function () { return []; }
		},
		
		/** @id droid.fileExists */
		fileExists : {
			droid : function (path) {
				return _storage.fileExists(path);
			},
			cross : function () { return false; }
		},
		
		/** @id droid.mkdir */
		mkdir : {
			droid : function (path) {
				return _storage.mkdir(path);
			},
			cross : function () {}
		},
		
		/** @id droid.deleteFile */
		deleteFile : {
			droid : function (path) {
				return _storage.deleteFile(path);
			},
			cross : function () {}
		},
		
		/** @id droid.fileSize */
		fileSize : {
			droid : function (path) { return _storage.fileSize(path); },
			cross : function (path) { return 0; }
		},
		
		/** @id droid.copyAssetsFile */
		copyAssetsFile : {
			droid : function (assetsName, savepath) { return _storage.copyAssetsFile(assetsName, savepath); },
			cross : function () {}
		},
		
		/** @id droid.mergeSeparatedAssetsFile */
		mergeSeparatedAssetsFile : {
			droid : function (assetsName, savepath) {
				return _storage.mergeSeparatedAssetsFile(assetsName, savepath);
			},
			cross : function () {}
		},
		
		/** @id droid.fileCopy */
		fileCopy : {
			droid : function (src, des) {
				return _storage.fileCopy(src, des);
			},
			cross : function (src, des) {
				return false;
			}
		},
		
		/** @id droid.audiorecStart */
		audiorecStart : {
			droid : function (fname) {
				return _storage.audiorecStart(fname);
			},
			cross : function (fname) {
				return false;
			}
		},
		/** @id droid.audiorecStop */
		audiorecStop : {
			droid : function (fname) {
				return _storage.audiorecStop(fname);
			},
			cross : function (fname) {
				return false;
			}
		},
		___ : end_of_define_method
	}
);

// emurate localStorage for Android 1.6
if (typeof(window.localStorage) == "undefined") {
	window.localStorage = {
		getItem    : function (key, defvalue) {
			if (defvalue == undefined) defvalue = null;
			return String(_storage.localStorage_get(key, defvalue));
		},
		setItem    : function (key, value) { return _storage.localStorage_put(key, value); },
		removeItem : function (key) { return _storage.localStorage_remove(key, defValue); },
		clear      : function () { _storage.localStorage_clear(); },
		___ : 0
	};
}


plugin_defineDroidMethod(
	{ pluginName:'_dev', className:'DevInfoPlugin' },
	{
		/** @id droid.getDisplayInfo */
		getDisplayInfo : {
			droid : function () { var str = _dev.getDisplayInfo(); return eval("("+str+")"); },
			cross : function () { return {'width':document.width, 'height':document.height}; }
		},
		/** @id droid.getSystemMemory */
		getSystemMemory : {
			droid : function () { return _dev.getSystemMemory(); },
			cross : function () { return -1; }
		},
		/** @id droid.getMemoryInfo */
		getMemoryInfo : {
			droid : function () { return droid.json_parse(_dev.getMemoryInfo());    },
			cross : function () { return {availMem:0, lowMemory:false, threshold:0} }
		},
		/** @id droid.hasSDCard */
		hasSDCard : {
			droid : function () { return _dev.hasSDCard(); },
			cross : function () { return false; }
		},
		/** @id droid.getSDCardPath */
		getSDCardPath : {
			droid : function () { return String(_dev.getSDCardPath()); },
			cross : function () { return "/sdcard"; }
		},
		/** @id droid.getAndroidVersionInt */
		getAndroidVersionInt : {
			droid : function () { return _dev.getAndroidVersionInt(); },
			cross : function () { return 4; }
		},
		
		/** @id droid.getAndroidVersionRelease */
		getAndroidVersionRelease : {
			droid : function () { return _dev.getAndroidVersionRelease(); },
			cross : function () { return "?"; }
		},
		
		/** @id droid.getAndroidId */
		getAndroidId : {
			droid : function() { return _dev.getAndroidId() ; },
			cross : function() { return "hoge"; }
		},
		
		___ : end_of_define_method
	}
);


plugin_defineDroidMethod(
	{ pluginName:'_contact', className:'ContactPlugin' },
	{
		/** @id droid.pickupContact */
		pickupContact : {
			droid : function (callback) {
				var tag = func_bank.registerItem(callback);
				self.droid._pickupContact = function (tag, obj) {
					var f = func_bank.getItem(tag);
					f(obj);
				};
				var str = _contact.pickupContact("droid._pickupContact", tag);
			},
			cross : function (callback) { callback(null); }
		},
		___ : end_of_define_method
	}
);



plugin_defineDroidMethod(
	{ pluginName:'_intent', className:'IntentPlugin' },
	{
		/** @id droid.startIntent */
		startIntent : {
			droid : function (url) { return _intent.startIntent(url); },
			cross : function (url) { console.log('startIntent:' + url); }
		},
		
		/** @id droid.startIntentForResult */
		startIntentForResult : {
			droid : function (url, callback) {
				// register user function
				var tag = func_bank.getTag();
				func_bank.setItem(tag, callback);
				// set proxy function
				self.droid._startIntentForResult_callback = function (request_code, result_code) {
					var f = func_bank.getItem(request_code);
					if (typeof(f) == "function") {
						f(result_code);
					}
				};
				return _intent.startIntentForResult(url, "droid._startIntentForResult_callback", tag);
			},
			cross : function (url) { console.log('startIntentForResult:' + url); }
		},
		
		/** @id droid.startIntentFullScreen */
		startIntentFullScreen : {
			droid :  function (url) {
				return _intent.startIntentFullScreen(url);
			},
			cross : function (url) { console.log('startIntentFullScreen:' + url); }
		},
		
		/** @id droid.intent_new */
		intent_new : {
			droid : function (action, uri) {
				return _intent.intent_new(action, uri);
			},
			cross : function (action, uri) {
				console.log('intent_new:' + action + ',' + uri);
				return {};
			}
		},
		
		/** @id droid.intent_putExtra */
		intent_putExtra : {
			droid : function (intent, name, value) {
				_intent.intent_putExtra(intent, name, value);
			},
			cross : function (action, uri) {
				console.log('intent_putExtra:' + action + ',' + uri);
			}
		},
		
		/** @id droid.intent_getExtra */
		intent_getExtra : {
			droid : function (intent, name) {
				return _intent.intent_getExtra(intent, name);
			},
			cross : function (action, uri) {
				return "";
			}
		},
		
		/** @id droid.intent_startActivity */
		intent_startActivity : {
			droid : function (intent) {
				return _intent.intent_startActivity(intent);
			},
			cross : function (intent) {
				console.log('intent_putExtra:' + action + ',' + uri);
				return true;
			}
		},
		
		/** @id droid.intent_exists */
		intent_exists : {
			droid : function (intentName) { return _intent.intent_exists(intentName); },
			cross : function (intentName) { return false; }
		},
		
		/** @id droid.scanBarcode */
		scanBarcode : {
			droid : function (callback_fn, mode, show_help) {
				// params
				self.droid._scanBarcode_callback = callback_fn;
				self.droid._scanBarcode = function (contents, format) {
					var f = self.droid._scanBarcode_callback;
					if (typeof(f) == "function") {
						contents = decodeURIComponent(contents);
						f(String(contents), String(format));
					}
				};
				if (typeof(mode) != "string") { mode = "AUTO"; }
				// execute
				var b = _intent.scanBarcode("droid._scanBarcode", mode);
				if (b) return true;
				// show help
				if (!show_help) return false;
				var show_link = confirm("You need Barcode Scanner. Download?");
				if (show_link) {
					droid.startIntent("market://search?q=pname:com.google.zxing.client.android");
				}
				return false;
			},
			cross : function (callback_fn, mode, show_help) {
				console.log("scanBarcode");
				return true;
			}
		},
		
		/** @id droid.scanQRCode */
		scanQRCode : {
			droid : function (callback_fn, show_help) {
				return this.scanBarcode(callback_fn, "QR_CODE_MODE", show_help);
			},
			cross : function (callback_fn, show_help) {
				console.log("scanQRCode");
				return true;
			}
		},
		
		/** @id droid.pickupImageFromGallery */
		pickupImageFromGallery : {
			droid : function (callback_fn) {
				self.droid._pickupImageFromGallery_callback = callback_fn;
				self.droid._pickupImageFromGallery = function (url) {
					self.droid._pickupImageFromGallery_callback(url);
				};
				return _intent.pickupImageFromGallery("droid._pickupImageFromGallery");
			},
			cross : function (callback_fn) {
				console.log("pickupImageFromGallery");
				return false;
			}
		},
		
		/** @id droid.recognizeSpeech */
		recognizeSpeech : {
			droid : function (callback_fn, lang) {
				self.droid._recognizeSpeech_callback = callback_fn;
				self.droid._recognizeSpeech = function (str) {
					self.droid._recognizeSpeech_callback(str);
				};
				if (lang == undefined) lang = "";
				return _intent.recognizeSpeech("droid._recognizeSpeech", lang);
			},
			cross : function (callback_fn) {
				console.log("recognizeSpeech");
				return true;
			}
		},

		
		/** @id droid.showRoute */
		showRoute : {
			droid : function (start, end) {
				var p = ["f=d",
				         "saddr=" + escape(start),
				         "daddr=" + escape(end),
				         "hl=ja"];
				var intent = _intent.intent_new(
					"android.intent.action.VIEW", 
					"http://maps.google.com/maps?" + p.join("&"));
				_intent.intent_setClassName(intent, "com.google.android.apps.maps","com.google.android.maps.MapsActivity");
				droid.intent_startActivity(intent);
			},
			cross : function () {
			}
		},
		___ : end_of_define_method
	}
);


//JSON.stringfy for Android 1.6
var escapeString = function (str) {
	str = str.replace(/\\/g, '\\\\');
	str = str.replace(/"/g, '\\"');
	return str;
};
var stringify = function(obj) {
	//if (typeof(JSON) != 'undefined') {
	//	return JSON.stringify(obj);
	//}
	var type = typeof(obj);
	switch (type) {
	case 'number'   : return "" + obj;
	case 'string'   : return '"' + escapeString(obj) + '"';
	case 'boolean'  : return (obj) ? 'true' : 'false';
	case 'function' : return 'null';
	case 'null'     : return 'null';
	case 'undefined': return 'undefined';
	case 'object':
		if (obj instanceof Array) {
			var ary = [];
			for (var i = 0; i < obj.length; i++) {
				ary[i] = stringify(obj[i]);
			}
			return "[" + ary.join(',') + "]";
		}
		else if (obj instanceof Object) {
			var hash = [];
			for (var key in obj) {
				var line = '"' + escapeString(key)  + '":' +
				           stringify(obj[key]);
				hash.push(line);
			}
			return "{" + hash.join(',') + "}";
		}
		else {
			return "undefined";
		}
	default:
		return "undefined";
	}
};
/** @id droid.stringify */
self.droid.stringify = stringify;
/** @id droid.json_parse */
self.droid.json_parse = function (str) {
	// Many android OS is not support JSON object (1.x/2.2)
	//if (typeof(JSON) != 'undefined') {
	//	return JSON.parse(str);
	//}
	try {
		return eval('(' + str + ')');
	} catch (e) {
		droid.log("json_parse.error : " + str);
		return null;	
	}
};

})(this);
