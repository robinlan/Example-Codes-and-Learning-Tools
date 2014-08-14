import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.sound.midi.*;    // 載入套件 

class MidiFrame extends JFrame implements ActionListener {
  JButton btnPlay, btnStop;
  Sequencer player;           // 宣告Sequencer格式播放器物件
   
  MidiFrame() {
    btnPlay = new JButton("播放");
    btnPlay.addActionListener(this);
    add(btnPlay);
    btnStop = new JButton("暫停");
    btnStop.addActionListener(this);
    add(btnStop);
        
    setTitle("播放MIDI音樂");
    setLayout(new FlowLayout());
    setBounds(100, 100, 200, 80);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
     
    try {
      player = MidiSystem.getSequencer();             // 建立播放器
      File file = new File("song.midi");              // 取得音樂檔
      Sequence sound = MidiSystem.getSequence(file);  // 取得輸入串流
      player.setSequence(sound);                      // 設定播放輸入串流
      player.open();	                              // 啟動播放功能
    } 
    catch(Exception exce) {
      System.out.println(exce);
    }
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == btnPlay) player.start();     // 播放
    if (e.getSource() == btnStop) player.stop();      // 暫停
  }
}   

public class J13_1_1 {
  public static void main(String[] args) {
    MidiFrame frame = new MidiFrame();
  }
}