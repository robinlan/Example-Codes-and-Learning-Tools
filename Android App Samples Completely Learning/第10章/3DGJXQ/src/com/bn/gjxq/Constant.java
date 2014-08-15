package com.bn.gjxq;
public class Constant
{
	public static final float MAX_S_QHC=1.0f;//S紋理坐標
	public static final float MAX_T_QHC=0.746f;//T紋理坐標	
	//START按鈕
	public static int BUTTON_START_XOFFSET=75;
	public static int BUTTON_START_YOFFSET=116;
	//HELP按鈕
	public static int BUTTON_HELP_XOFFSET=277;
	public static int BUTTON_HELP_YOFFSET=116;
	//ABOUT按鈕
	public static int BUTTON_ABOUT_XOFFSET=75;
	public static int BUTTON_ABOUT_YOFFSET=201;
	//EXIT按鈕
	public static int BUTTON_EXIT_XOFFSET=277;
	public static int BUTTON_EXIT_YOFFSET=201;
	
	//START按鈕的尺寸
	public static int BUTTON_START_WIDTH=128;
	public static int BUTTON_START_HEIGHT=64;
	//START按鈕的尺寸
	public static int BUTTON_HELP_WIDTH=128;
	public static int BUTTON_HELP_HEIGHT=64;
	//ABOUT按鈕的尺寸
	public static int BUTTON_ABOUT_WIDTH=128;
	public static int BUTTON_ABOUT_HEIGHT=64;
	//EXIT按鈕的尺寸
	public static int BUTTON_EXIT_WIDTH=128;
	public static int BUTTON_EXIT_HEIGHT=64;	
	//加載界面中圖片的相關參數
	public static final int BEIJING_XOFFSET=0;
	public static final int BEIJING_YOFFSET=0;
	//黑白方標誌位位置相關參數
	public static final float BLACK_FLAG_X=-1.6f;
	public static final float BLACK_FLAG_Y=1.5f;
	
	public static final float WHITE_FLAG_X=1.6f;
	public static final float WHITE_FLAG_Y=1.5f;
	
	//箭頭標誌板相關位置參數
	public static final float PLAYER_TYPE_X1=-1.62f;
	public static final float PLAYER_TYPE_X2=1.62f;
	public static final float PLAYER_TYPE_Y=1.8f;
	
	public static final float CURR_MOVE_PLAYER_X1=-1.62f;
	public static final float CURR_MOVE_PLAYER_X2=1.62f;
	public static final float CURR_MOVE_PLAYER_Y=1.2f;

	 //設置顏色元素參數
	 public static float ONE=65535F;
	  //棋盤顏色數組
	 public static final float[][] COLORARR=new float[][]
	  {
		  new float[]{ONE,ONE,ONE,0},//白
		  new float[]{0,0,0,0},//黑
		  new float[]{ONE,0,0,0}//紅
	  };
	  //棋盤單位格子大小
	  public static float UNIT_SIZE=1f;//每格的單位長度
	  //攝像機位置距離觀察目標點的距離  
	  public static final float DISTANCE=12f;
	  //房間大小
	  public static final float HOUSE_SIZE=34f;
}
