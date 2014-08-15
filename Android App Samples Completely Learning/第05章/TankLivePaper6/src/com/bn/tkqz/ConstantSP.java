package com.bn.tkqz;
public class ConstantSP {
	public static int SCREEN_HEIGHT=480;//屏幕高度
	public static int SCREEN_WIDTH=320;//屏幕宽度
	public static final int UP_MARGIN=60;//屏幕上边留白宽度
	public static final int UP_BAR=5;//屏幕上边条的宽度
	
	//游戏界面xy坐标
	public static int GAME_VIEW_X=Constant.LEFT_TOP_X;
	public static int GAME_VIEW_Y=Constant.LEFT_TOP_Y+UP_MARGIN;
	public static int GAME_VIEW_WIDTH=SCREEN_WIDTH;//游戏界面宽度
	public static int GAME_VIEW_HEIGHT=SCREEN_HEIGHT-UP_MARGIN;//游戏界面高度
	//数字参数
	public static final int FIRST_NUMBER_WIDTH=10;//第一条信息的离左边的距离
	public static final int NUMBER_WIDTH=12;//每一个数字占屏幕的宽度
	public static final int NUMBER_TOTAL_WIDTH=58;//每一个数字占屏幕的宽度
	public static final int NUMBER_HEIGHT=30;//数字占屏幕的高度	
	//汉字参数
	public static final int FIRST_HANZI_WIDTH=25;//第一条信息的离左边的距离
	public static final int HANZI_WIDTH=58;//汉字占屏幕的宽度
	public static final int HANZI_HEIGHT=10;//汉字占屏幕的高度
	
	//======================================标记区域值============================begin======
	//虚拟键盘总尺寸
	public static final float BUTTON_TOTAL_WIDTH=70;
	public static final float BUTTON_TOTAL_HEIGHT=70;
	//虚拟键盘区域的左上xy坐标
	public static final float BUTTON_AREA_X=GAME_VIEW_X+GAME_VIEW_WIDTH-BUTTON_TOTAL_WIDTH-6;
	public static final float BUTTON_AREA_Y=GAME_VIEW_Y+GAME_VIEW_HEIGHT-BUTTON_TOTAL_HEIGHT-6;	
	//虚拟键盘尺寸
	public static final float BUTTON_WIDTH=BUTTON_TOTAL_WIDTH/3;
	public static final float BUTTON_HEIGHT=BUTTON_TOTAL_HEIGHT/3;
	//剩余尺寸的一半
	public static final float OTHER_WIDTH=(BUTTON_TOTAL_WIDTH-BUTTON_WIDTH)/2;
	public static final float OTHER_HEIGHT=(BUTTON_TOTAL_HEIGHT-BUTTON_HEIGHT)/2;
	//四个按钮的左上点坐标
	public static final float UP_X=BUTTON_AREA_X+OTHER_WIDTH;
	public static final float UP_Y=BUTTON_AREA_Y;
	
	public static final float DOWN_X=BUTTON_AREA_X+OTHER_WIDTH;
	public static final float DOWN_Y=BUTTON_AREA_Y+BUTTON_HEIGHT+OTHER_HEIGHT;
	
	public static final float LEFT_X=BUTTON_AREA_X;
	public static final float LEFT_Y=BUTTON_AREA_Y+OTHER_HEIGHT;
	
	public static final float RIGHT_X=BUTTON_AREA_X+BUTTON_WIDTH+OTHER_WIDTH;
	public static final float RIGHT_Y=BUTTON_AREA_Y+OTHER_HEIGHT;
	//发射子弹区域的尺寸
	public static final float FIRE_BTN_WIDTH=50;
	public static final float FIRE_BTN_HEIGHT=50;
	//发射子弹区域的左上点坐标
	public static final float FIRE_BTN_X=GAME_VIEW_X+2;
	public static final float FIRE_BTN_Y=GAME_VIEW_Y+GAME_VIEW_HEIGHT-FIRE_BTN_HEIGHT-2;	
	//======================================标记区域值============================end======
	
	//======================================图片值===================begin======
	//虚拟键盘的左上xy坐标
	public static final float BUTTON_X=BUTTON_AREA_X-7;
	public static final float BUTTON_Y=BUTTON_AREA_Y-7;
	//红点坐标xy坐标
	public static final float RED_DOT_CENTER_X=BUTTON_AREA_X+BUTTON_WIDTH-3;
	public static final float RED_DOT_CENTER_Y=BUTTON_AREA_Y+BUTTON_HEIGHT-3;
	//发射图片的xy坐标
	public static final float FIR_MAP_X=FIRE_BTN_X-2;
	public static final float FIR_MAP_Y=FIRE_BTN_Y+3;
	//======================================图片值=================== end ======
}
