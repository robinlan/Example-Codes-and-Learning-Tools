import javax.swing.*;
import java.awt.*;
import java.io.File;

class cDialog extends JFrame {
  cDialog() {
    JLabel lblFile;
    JFileChooser chooser = new JFileChooser(); 
    int press = chooser.showOpenDialog(null);      //開啟檔案
    if (press == JFileChooser.APPROVE_OPTION) {
      File file = chooser.getSelectedFile();
      File path = chooser.getCurrentDirectory();
      lblFile = new JLabel("檔案為  " + path + '\\' + file.getName());
      //lblFile = new JLabel("檔案為  " + file.getPath());
    } else
      lblFile = new JLabel("取消開啟檔案");
    lblFile.setBorder(BorderFactory.createLineBorder(Color.black));
    
    add(lblFile ,BorderLayout.SOUTH);
    setTitle("檔案選擇對話框");
    setBounds(50, 50, 300, 150);   
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
}

public class J11_2_4 {
  public static void main(String[] args) {
    cDialog myFrame = new cDialog();
  }
}