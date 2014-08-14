import javax.swing.*;

public class A12_3_1 extends JApplet {
  public void init() {
    setLayout(null);
    
    String pic_name = getParameter("paramPic");
    ImageIcon icon;
    icon = new ImageIcon(getImage(getCodeBase(), pic_name));
    JLabel lblPic = new JLabel(icon);
    lblPic.setBounds(10, 10, 150, 160);
    add(lblPic);
    
    JLabel lblName = new JLabel();
    lblName.setText("吉祥物： " + getParameter("paramTxt"));
    lblName.setBounds(20, 180, 150, 20);
    add(lblName);
    
    int love = Integer.parseInt(getParameter("paramNum"));
    String star = "";
    for (int i = 1; i <= love; i++) star += "◎";	
    JLabel lblLove = new JLabel();
    lblLove.setText("受歡迎程度： " + star);
    lblLove.setBounds(20, 200, 150, 20);
    add(lblLove);
  }  
}