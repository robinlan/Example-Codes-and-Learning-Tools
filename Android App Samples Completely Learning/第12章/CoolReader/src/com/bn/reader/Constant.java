package com.bn.reader;

public class Constant
{
	//directions設置
	public static String DIRECTIONSNAME="directions.txt"; //APK中自帶說明的名字
	//下載用到URL
	public static final String ADD_PRE="http://192.168.1.100:8080/txt/";//存放文本文件的URL
	//翻頁用到的常量值
	public static int CURRENT_LEFT_START;//記錄當前讀到處LeftStart的值
	public static int CURRENT_PAGE;//記錄當前讀到處的page的值
	public static int nextPageStart;//下一頁的起始字符在字符串中的位置
	public static int nextPageNo;//下一頁的頁碼
	public static int CONTENTCOUNT;//文本的總字符數
	//廣告條用到的常量值
	public static int AD_WIDTH=120;//廣告條實際長度
	public static int NUM;//廣告圖的數量
	//初始背景和字體顏色
	public static int BITMAP=R.drawable.bg_sjzx;//初始的背景圖片
	public static int COLOR=0xffffff00;//初始的字體顏色
	//屏幕的大小
	public static int SCREEN_WIDTH;//屏幕的寬度
	public static int SCREEN_HEIGHT;//屏幕的高度
	//左右兩側圖片的位置
	public static int LEFT_OR_RIGHT_X=0;//左側圖片左頂點的x坐標
	public static int RIGHT_OR_LEFT_x;//右側圖片左頂點的x坐標
	//文字的設置
	public static int PAGE_LENGTH;//每次讀取文字個數
	public static int TEXT_SPACE_BETWEEN_EN=8;//文本間距-英文
	public static int TEXT_SPACE_BETWEEN_CN=16;//文本間距-中文
	public static int TEXT_SIZE=16;//文本大小
	public static int TITLE_SIZE=25;//標頭文本的字體大小
	//虛擬頁的設置
	public static int ROWS;//虛擬頁的行數
	public static int PAGE_WIDTH;//虛擬頁的寬度
	public static int PAGE_HEIGHT;//虛擬頁的高度
	//存放文本的路徑
	public static String FILE_PATH; //存放文本的路徑
	public static String TEXTNAME;//當前閱讀的文件的名字
	//上方留白
	public static int BLANK=30;//上方留白
	//中間分割線
	public static int CENTER_WIDTH=4;//中間分割條寬度
	public static int CENTER_LEFT_Y=BLANK;//中間分割線左上角y坐標
	public static int CENTER_LEFT_X;//中間分割線左上角x坐標	
	public static int CENTER_RIGHT_X;//中間分割線 右下角x坐標
	public static int CENTER_RIGHT_Y;//中間分割線 右下角y坐標
	//背景音樂的播放
	public static int I;//背景音樂的R值
	
	//根據屏幕分辨率來更改常量值
	public static void changeRatio()
	{
		//分割線自適應
		CENTER_LEFT_X=(SCREEN_WIDTH-CENTER_WIDTH)/2;//給中間分割線左上角x坐標
		CENTER_RIGHT_X=CENTER_LEFT_X+CENTER_WIDTH-1;//給中間分割線 右下角x坐標
		CENTER_RIGHT_Y=SCREEN_HEIGHT;//給中間分割線 右下角y坐標
		//右側圖片左定點X坐標自適應
		RIGHT_OR_LEFT_x=CENTER_RIGHT_X+1;//給右側圖片左定點的x坐標
		//虛擬頁高寬自適應
		PAGE_WIDTH=(SCREEN_WIDTH-CENTER_WIDTH)/2;//虛擬頁的寬度
		PAGE_HEIGHT=SCREEN_HEIGHT-BLANK;//虛擬頁的高度
		//虛擬頁文本行數自適應
		ROWS=PAGE_HEIGHT/TEXT_SIZE;//每個虛擬頁上文本行數
		//每次讀取文本中文字的個數
		PAGE_LENGTH=(PAGE_WIDTH/TEXT_SPACE_BETWEEN_EN+1)*(PAGE_HEIGHT/TEXT_SIZE+1);//每次讀取文字個數賦值
	}
}
