import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

class InputFrame extends JFrame {
  // 六個JLabel標籤物件，分別姓名、年齡、性別、興趣、學歷、居住地區
  JLabel lblName, lblAge, lblSex, lblInter, lblAcad, lblPlace;
  JTextField text0 = new JTextField(10);   // 建立JTextField元件  
  String[] checkItem = {"電腦", "唱歌", "電影", "繪圖", "旅遊"}; 
  JCheckBox[] check = new JCheckBox[checkItem.length];
  JTextArea texta = new JTextArea();
 
  InputFrame() {
  	JPanel pane = new JPanel(); 
    pane.setLayout(null); 
    pane.setBackground(Color.LIGHT_GRAY);
    add(pane);
    // JTextField文字欄位元件  
    lblName = new JLabel("姓名：");
    lblName.setBounds(10, 10, 40, 20);
    pane.add(lblName);
    text0.setBounds(50, 10, 80, 20);
    text0.addActionListener(textfield);
    pane.add(text0);
    // JSpinner數位序列元件
    lblAge = new JLabel("年齡：");
    lblAge.setBounds(170, 10, 40, 20);
    pane.add(lblAge);
    JSpinner spin = new JSpinner(new SpinnerNumberModel(20, 1, 100, 1));
    spin.setBounds(210, 10, 80, 20); 
    spin.addChangeListener(spinner);
    pane.add(spin);
    // JRadioButton選項圓鈕元件  
    lblSex = new JLabel("性別：");
    lblSex.setBounds(10, 40, 40, 20);
    pane.add(lblSex);
    ButtonGroup group = new ButtonGroup();
    JRadioButton rb1 = new JRadioButton("帥哥", false); 
    rb1.setBounds(50, 40, 60, 20);
    JRadioButton rb2 = new JRadioButton("美女", false);
    rb2.setBounds(110, 40, 60, 20);
    rb1.setOpaque(false); rb2.setOpaque(false);    // 秀出底色
    rb1.addActionListener(radio); rb2.addActionListener(radio);
    group.add(rb1); group.add(rb2);
    pane.add(rb1); pane.add(rb2);
    // JCheckBox核對方塊元件
    lblInter = new JLabel("興趣：");
    lblInter.setBounds(10, 70, 50, 20);
    pane.add(lblInter);
    for(int i = 0; i < check.length; i++) {
      check[i] = new JCheckBox(checkItem[i]);
      check[i].setBounds(50 + 60 * i, 70, 60, 20);
      check[i].setOpaque(false);
      check[i].addActionListener(checkbox);
      pane.add(check[i]);
    }
    // JComboBox下拉式清單元件
    lblAcad = new JLabel("學歷：");
    lblAcad.setBounds(10, 100, 50, 20);
    pane.add(lblAcad);
    String[] items_c = {"博士", "碩士", "大學", "高中", "國中", "國小"};
    JComboBox c_box = new JComboBox(items_c);
    c_box.setBounds(50, 100, 100, 20);
    c_box.addItemListener(cbo);
    pane.add(c_box);
    // JList清單元件
    lblPlace = new JLabel("居住地區：");
    lblPlace.setBounds(170, 100, 70, 20);
    pane.add(lblPlace); 
    String[] items_p = {"台北", "桃園", "新竹", "苗栗", "台中", "彰化", "雲林",
                    "嘉義", "台南", "高雄", "屏東", "花蓮", "台東", "澎湖"};
    JList list = new JList(items_p);
    list.setVisibleRowCount(4);
    list.addListSelectionListener(list_p);
    JScrollPane scroll = new JScrollPane(list);
    scroll.setBounds(240, 100, 80, 80); 
    pane.add(scroll);
    // JTextArea文字區域元件
    texta.setBounds(10, 190, 330, 40);
    texta.setEditable(false);
    pane.add(texta);
   
    setTitle("輸入元件綜合應用");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
    setBounds(50, 50, 360, 280);
    setVisible(true);   
  }

  // JTextField事件傾聽
  public ActionListener textfield = new ActionListener() {
    public void actionPerformed(ActionEvent e) {
      texta.setText(lblName.getText() + text0.getText());
    }
  };
  // JSpinner事件傾聽
  public ChangeListener spinner = new ChangeListener() {
    public void stateChanged(ChangeEvent e) {
      texta.setText(lblName.getText() + text0.getText()+ '\n'
                  + lblAge.getText() + ((JSpinner)e.getSource()).getValue());
    }
  };
  // JRadioButton事件傾聽
  public ActionListener radio = new ActionListener() {
    public void actionPerformed(ActionEvent e) {
      texta.setText(lblName.getText() + text0.getText()+ '\n'
                  + lblSex.getText() + ((JRadioButton)e.getSource()).getText());
    }
  };
  // JCheckBox事件傾聽
  public ActionListener checkbox = new ActionListener() {
    public void actionPerformed(ActionEvent e) {
      String txt = lblInter.getText();
      for (int i = 0; i < check.length; i++) 
      	if(check[i].isSelected()) txt += check[i].getText();
      texta.setText(lblName.getText() + text0.getText()+ '\n' + txt);
    }
  };
  // JComboBox事件傾聽
  public ItemListener cbo = new ItemListener() {
    public void itemStateChanged(ItemEvent e) {
      texta.setText(lblName.getText() + text0.getText()+ '\n' 
                 + lblAcad.getText() + e.getItem());
    }
  }; 
  // JList事件傾聽
  public ListSelectionListener list_p = new ListSelectionListener() {
    public void valueChanged(ListSelectionEvent e) {
      Object[] values = ((JList)e.getSource()).getSelectedValues();
      String txtItem = "";
      for(int i = 0; i < values.length; i++)
        txtItem += values[i].toString();
      texta.setText(lblName.getText() + text0.getText()+ '\n'
                  + lblPlace.getText() + txtItem);
    }
  };
}

public class J10_9_1 {
  public static void main(String[] args) {
    InputFrame frame = new InputFrame();
  }
}