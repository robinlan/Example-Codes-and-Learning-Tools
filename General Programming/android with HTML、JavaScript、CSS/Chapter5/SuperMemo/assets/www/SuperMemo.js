/** 筆記儲存的路徑 */
var savedir = "/sdcard/memo/";
/** 表示筆記的種類 */
var MEMOTYPE_TEXT  = "txt";
var MEMOTYPE_DRAW  = "png";
var MEMOTYPE_PHOTO = "jpg";
/** 各種圖示 */
var iconFiles = {"txt":"text.png","png":"draw.png",
	"jpg":"photo.png","add":"add.png"};
/** 筆記的種類 */
var memoTypes = {"文字":"txt","手寫":"png","照片":"jpg"};
/** 解析並取得附在URL中的參數
 * @return {Object}
 */
function parseUrlParams() {
	var param_str = window.location.href.split("?")[1];
	var values = {};
	var param_array = param_str.split("&");
	for (var i = 0; i < param_array.length; i++) {
		var p = param_array[i].split("=");
		var key = p[0], value = decodeURI(p[1]);
		values[key] = value;
	}
	return values;
}

