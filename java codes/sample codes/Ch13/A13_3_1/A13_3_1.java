import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
//import java.io.*;
import java.applet.Applet;             // 載入Applet套件 
import java.applet.AudioClip;          // 載入AudioClip介面

public class A13_3_1 extends JApplet implements ActionListener {
  JButton btnPlay, btnLoop, btnStop;
  AudioClip sound;          // 宣告 AudioClip 介面型態物件sound

  public void init() {
    setLayout(new FlowLayout());   
    btnPlay = new JButton("播放");
    btnPlay.addActionListener(this);
    add(btnPlay);
    btnLoop = new JButton("循環播放");
    btnLoop.addActionListener(this);
    add(btnLoop);
    btnStop = new JButton("停止");
    btnStop.addActionListener(this);
    btnStop.setEnabled(false);
    add(btnStop);
    
    // 取得AudioClip輸入串流
    sound = getAudioClip(getCodeBase(),"happy.wav");
  }  
    
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == btnPlay) {    
      sound.play();                    // 一次播放一遍
      btnPlay.setEnabled(false);
      btnStop.setEnabled(true);
    } 
    if (e.getSource() == btnLoop) { 
      sound.loop();                   // 循環播放
      btnLoop.setEnabled(false);
      btnPlay.setEnabled(false);
      btnStop.setEnabled(true);
    }
    if (e.getSource() == btnStop) {
      sound.stop();                   // 停止播放
      btnLoop.setEnabled(true);
      btnPlay.setEnabled(true);
      btnStop.setEnabled(false);
    }
  }
}