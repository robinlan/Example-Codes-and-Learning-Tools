import javax.swing.*;

class cDialog extends JFrame {
  cDialog() {
    ImageIcon icon = new ImageIcon("pic.jpg");
    String[] options = {"皇后", "巫婆", "白雪公主"};
    int op = JOptionPane.showOptionDialog(null,
             "魔鏡！魔鏡！您認為世界上誰最漂亮？", "問題",
             0, 0, icon, options, options[0]);
    int answer = 2;
    JLabel lblAns;
    if (op == answer) 
      lblAns = new JLabel("答對了，您好神哦！");
    else 
      lblAns = new JLabel("答錯了！答案是 " + options[answer]);
    add(lblAns);
    
    setTitle("選項對話框");
    setBounds(50, 50, 250, 100);   
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
}

public class J11_2_3 {
  public static void main(String[] args) {
    cDialog frame = new cDialog();
  }
}