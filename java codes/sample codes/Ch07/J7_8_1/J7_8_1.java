import javax.swing.*;
import java.awt.event.*;

class CTimer extends JFrame implements ActionListener {
  ImageIcon icon1 = new ImageIcon("fig_0.jpg");
  JLabel lblPic = new JLabel(icon1);
  JButton btnL = new JButton("左移");
  JButton btnEnd = new JButton("結束");
  JButton btnR = new JButton("右移");
  int pos_x = 70, pos_y = 20, dx;

  CTimer() {
    lblPic.setBounds(pos_x, pos_y, 150, 160);
    add(lblPic);
    
    btnL.setBounds(40, 200, 60, 20);
    add(btnL);
    btnL.addActionListener(this); 
    btnEnd.setBounds(115, 200, 60, 20);
    add(btnEnd);
    btnEnd.addActionListener(this); 
    btnR.setBounds(190, 200, 60, 20);
    add(btnR);
    btnR.addActionListener(this); 
    
    Timer timer = new Timer(10, this);
    timer.start();     // 啟動計時器
           
    setTitle("Timer應用");
    setLayout(null);
    setBounds(100, 100, 300, 260);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }  
     
  public void actionPerformed(ActionEvent e) {
  	if(e.getSource()==btnEnd) System.exit(0);
  	if(e.getSource()==btnR) dx = 1;
  	if(e.getSource()==btnL) dx = -1;
    pos_x += dx;
    lblPic.setLocation(pos_x, pos_y);
  }   
}

public class J7_8_1 {
  public static void main(String[] aegs) {
    CTimer frame = new CTimer();
  }  
}