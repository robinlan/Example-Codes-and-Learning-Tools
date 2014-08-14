import javax.swing.*;

class cDialog {
  cDialog() {
    String s_wide = JOptionPane.showInputDialog("請輸入矩形寬度：", "10");
    String s_high = JOptionPane.showInputDialog("請輸入矩形高度：", "5");
    String unit = JOptionPane.showInputDialog("請輸入長度單位：", "公分");
    int area = Integer.parseInt(s_wide) * Integer.parseInt(s_high);
    String result = "矩形面積為： " + area + " 平方" + unit;
    JOptionPane.showMessageDialog(null, result, "換算結果",
                                  JOptionPane.INFORMATION_MESSAGE);
    System.exit(0);
  }
}

public class J11_2_2 {
  public static void main(String[] args) {
    cDialog myDialog = new cDialog();
  }
}