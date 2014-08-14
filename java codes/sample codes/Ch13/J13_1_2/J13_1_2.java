import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.sound.midi.*;     // 載入套件

class MidiFrame extends JFrame implements ActionListener {
  JButton btnPlay, btnLoop, btnStop;
  Sequencer player;           // 宣告Sequencer格式播放器物件
  
  MidiFrame() throws Exception {           // 指定類別方法丟出例外
    btnPlay = new JButton("播放");
    btnPlay.addActionListener(this);
    add(btnPlay);
    btnStop = new JButton("停止");
    btnStop.addActionListener(this);
    add(btnStop);
          
    setTitle("播放MIDI音樂");
    setLayout(new FlowLayout());
    setBounds(100, 100, 200, 80);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
    
    player = MidiSystem.getSequencer();         // 建立播放器
    Sequence sound = MidiSystem.getSequence(new File("song.midi"));
    player.setSequence(sound);                  // 設定播放輸入串流
    player.open();                              // 啟動播放功能     
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == btnPlay) player.start();     // 播放
    if (e.getSource() == btnStop) player.stop();      // 暫停
  }
}   

public class J13_1_2 {
  public static void main(String[] args) {
    try {
      MidiFrame frame = new MidiFrame();
    }
    catch(Exception exce) {
      System.out.println(exce);
    }
  }
  // public static void main(String[] args) throws Exception {
  //  MidiFrame frame = new MidiFrame();
  // }
}