import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.applet.Applet;
import java.applet.AudioClip;

class SoundFrame extends JFrame implements ActionListener {
  JButton btnPlay, btnLoop, btnStop;
  File file = new File("music.wav");
  // 利用Applet的newAudioClip方法取得AudioClip物件
  AudioClip sound = Applet.newAudioClip(file.toURL());

  SoundFrame() throws Exception {
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
    
    setTitle("用new AudioClip()播放音樂");
    setLayout(new FlowLayout());
    setBounds(100, 100, 300, 100);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);      
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

public class J13_4_1 {
  public static void main(String[] args) throws Exception {
    SoundFrame frame = new SoundFrame();
  }
}