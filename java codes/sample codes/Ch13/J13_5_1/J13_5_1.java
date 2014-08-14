import java.awt.*;
import javax.swing.*;
import java.net.*;
import javax.media.*;

class JMFrame extends JFrame implements ControllerListener {
  Player mplayer;               // 宣告Player播放器物件mplayer   
  Component visual, control;    // 宣告視頻及音頻Component物件
  int video_w = 0;              // 視頻寬度  
  int video_h = 0;              // 視頻高度
  int inset_w = 8;              // 視窗外框寬度，左右各4
  int inset_h = 34;             // 視窗外框高度，上30、下4
  int control_h = 30;           // 控制器高度
    
  public JMFrame() throws Exception {
    setTitle("Java多媒體播放器");
    setLocation(100, 100);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    URL url = new URL("file:man.avi");   // 取得輸入串流 
    mplayer = Manager.createPlayer(url);   // 取得播放器物件mplayer
	mplayer.addControllerListener(this);   // mplayer播放器的傾聽事件
	mplayer.prefetch();                   // 啟動播放功能
  }
 
  public void controllerUpdate(ControllerEvent e) {
 	if (e instanceof PrefetchCompleteEvent) {
	  visual = mplayer.getVisualComponent();   // 取得視頻物件
	  if (visual != null) {
	    Dimension size = visual.getPreferredSize();  //取得視頻物件尺寸
		video_w = size.width;                 // 取得視頻寬度
		video_h = size.height;                // 取得視頻高度
		add("Center", visual);            // 視窗容器加入視頻物件(中央)
	  } else video_w = 300;               // 預設音頻寬度  
	  control = mplayer.getControlPanelComponent();	 // 取得音頻物件 	 
	  control_h = control.getPreferredSize().height;  // 取得音頻高度
	  add("South", control);              // 視窗容器加入音頻物件(底部)
	  setSize(video_w + inset_w, video_h + control_h + inset_h); // 設定視窗大小
      setVisible(true);                        // 顯示視窗 
	  mplayer.start();                         // 啟用播放鈕
    } 
    if (e instanceof EndOfMediaEvent) {
	  mplayer.setMediaTime(new Time(0));       // 循環再播
	  mplayer.start();                         // 啟用播放鈕
	}
  }
}

public class J13_5_1 {
  public static void main(String[] args) throws Exception {
    JMFrame frame = new JMFrame();
  }
}