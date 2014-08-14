import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
//import java.applet.*;

public class A13_6_1 extends Applet implements ActionListener {
  int total_Photo = 5;          // 共5張，第0~4張
  ImageIcon[] img = new ImageIcon[total_Photo]; // 圖片陣列
  int page = 0;                 // 目前正在展示的圖片頁碼 
  JLabel lblPhoto = new JLabel();         // 放圖片的標籤物件 
  JButton btnPgUp = new JButton("上頁");  // 向前切換圖片按鈕
  JButton btnPgDn = new JButton("下頁");  // 向後切換圖片按鈕
  JButton btnMusic;                       // 音樂聲開關切換按鈕            
  AudioClip music;               // 宣告AudioClip物件music
  boolean music_on;              // 註記目前音樂是開或關
  
  public void init() {
    for( int i = 0; i < total_Photo; i++ ) {
      img[i] = new ImageIcon(getImage(getCodeBase(), "fig/scene_" + i + ".jpg"));
    }
    lblPhoto.setBounds(10, 10, 600, 450);
    lblPhoto.setIcon(img[page]);
    add(lblPhoto);
      
    btnPgUp.setBounds(485, 465, 60, 30);
    btnPgUp.addActionListener(this);    // 傳統的傾聽方式(第7章)
    add(btnPgUp);      
    
    btnPgDn.setBounds(550, 465, 60, 30);
    btnPgDn.addActionListener(ListbtnPgDn);  // 第10章實例的傾聽方式
    add(btnPgDn);
    
    // 取得AudioClip輸入串流，指派給music物件
    music = getAudioClip(getCodeBase(), "fig/bg_music.wav");
    music.loop();                      // 音樂循環播放
    music_on = true;                   // 音樂處在開的狀態
    btnMusic = new JButton("關音樂");
    btnMusic.setBounds(10, 465, 80, 30);
    add(btnMusic);
    btnMusic.addActionListener(new ActionListener() { // 新的傾聽方式
      // btnMusic的傾聽事件
      public void actionPerformed(ActionEvent e) {
   	    if (music_on) {
          music.stop();                // 停止橎放
   	      music_on = false;            // 音樂處在關的狀態
   	      btnMusic.setText("開音樂");
   	    } else {
          music.loop();                // 音樂循環播放
   	      music_on = true;             // 音樂處在開的狀態
   	      btnMusic.setText("關音樂");
        }
      }
    } );
           
    setLayout(null);
    setBackground(Color.yellow);
    setVisible(true);
  }
  
  //  btnPgUp的傾聽事件
  public void actionPerformed(ActionEvent e) {
    page --;
   	if (page < 0) page = total_Photo-1;
    lblPhoto.setIcon(img[page]);
  }
  
  // btnPgDn的傾聽事件
  public ActionListener ListbtnPgDn = new ActionListener() {
    public void actionPerformed(ActionEvent e) {
      page ++;
   	  if ( page > total_Photo ) page = 0;
      lblPhoto.setIcon(img[page]);
    }
  };
}