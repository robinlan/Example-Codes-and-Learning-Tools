import javax.swing.*;

class cDialog extends JFrame {
  cDialog() {
    int ans = JOptionPane.showConfirmDialog(null,
              "您對本書內容的編排，還滿意嗎？", "請問",
              JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    JLabel lblAns;
    if (ans == JOptionPane.YES_OPTION)
      lblAns = new JLabel("感謝您的支持，我們會再接再厲！");
    else
      lblAns = new JLabel("還是要謝謝您，我們會好好改進！");
    add(lblAns);
    
    setTitle("確認對話框");
    setBounds(50, 50, 250, 100);   
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
}

public class J11_2_1 {
  public static void main(String[] args) {
    cDialog myFrame = new cDialog();
  }
}