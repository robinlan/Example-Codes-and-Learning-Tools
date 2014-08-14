import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.sound.sampled.*;        // 載入Java Sound API套件

class SoundFrame extends JFrame implements ActionListener {
  JButton btnPlay, btnLoop, btnStop;
  File file = new File("happy.wav");
  // 取得音效檔的輸入串流
  AudioInputStream sound = AudioSystem.getAudioInputStream(file);
  // 將取得的輸入串流載入記憶體Clip
  DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
  // 取得指定的Clip播放器
  Clip player = (Clip)AudioSystem.getLine(info);
  
  SoundFrame() throws Exception {
    btnPlay = new JButton("播放");
    btnPlay.addActionListener(this);
    add(btnPlay);
    btnLoop = new JButton("重複播放");
    btnLoop.addActionListener(this);
    add(btnLoop);
    btnStop = new JButton("暫停");
    btnStop.addActionListener(this);
    btnStop.setEnabled(false);
    add(btnStop);
     
    player.open(sound);              // 啟動player功能
    
    setTitle("用Clip物件播放音樂");
    setLayout(new FlowLayout());
    setBounds(100, 100, 300, 80);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);          
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == btnPlay) {      
      player.start();                 // 播放一遍
      //player.loop(1);                   
      btnPlay.setEnabled(false);
      btnStop.setEnabled(true);
    } 
    if (e.getSource() == btnLoop) {     
      player.loop(-1);                  // 循環播放   
      btnLoop.setEnabled(false);
      btnPlay.setEnabled(false);
      btnStop.setEnabled(true);
    }
    if (e.getSource() == btnStop) {     
      player.stop();                   // 暫停播放
      btnLoop.setEnabled(true);
      btnPlay.setEnabled(true);
      btnStop.setEnabled(false);
    }
  }
}   

public class J13_2_1 {
  public static void main(String[] args) throws Exception {
    SoundFrame frame = new SoundFrame();
  }
}