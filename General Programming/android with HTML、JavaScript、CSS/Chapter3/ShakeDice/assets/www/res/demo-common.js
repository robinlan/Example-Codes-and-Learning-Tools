/** demo commons */

function setMyMenuItem() {
	// info
	droid.setMenuItem(
		0, true, "About","ic_menu_info_details",
		function(){
			alert("jsWaffle for Android");
		}
	);
	// quit
	droid.setMenuItem(
		1, true, "Quit", "ic_menu_close_clear_cancel",
		function() {
			var b = confirm("Quit?");
			if (b) droid.quit();
		}
	);
	// test - getResString()
	var str_test = droid.getResString("test");
	droid.setMenuItem(
		2, true, str_test, "ic_menu_info_details",
		function(){
			alert("test:" + str_test);
		}
	);
	// reload this page
	droid.setMenuItem(
		3, true, "reload", "ic_menu_info_details",
		function(){
			location.reload();
		}
	);
	// get memory info
	droid.setMenuItem(
		4, true, "MemotyInfo", "ic_menu_info_details",
		function() {
			var o = droid.getMemoryInfo();
			var s = droid.stringify(o);
			alert(s);
		}
	);
}
