import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.*;
import java.util.Date;
import java.text.DateFormat;

class mysqlFrame extends JFrame {
  Connection connection;
  Statement statement;
  ResultSet result;
  String id_get, password_get, dateNow, comm_data;
  int act;       // 記錄user按鈕，1 = 註冊， 2 = 登入
  JLabel itemq1, itemq2;
  JTextField id;
  JPasswordField password;
  JButton qb11, qb12;
  JButton qb31, qb32, qb33;
  JPanel panel1, panel2;
  JLabel item7, item8;
   
  Container cont;   
  JLabel item1, item2, item3, item4, item5, item6;
  ButtonGroup btn_group;
  JCheckBox cb1, cb2, cb3, cb4, cb5;
  JRadioButton rb1, rb2;
  JComboBox c_box;
  JTextField text0;
  JSpinner spin;
  JList list;
  String[] c_label = { "博士", "碩士", "大學", "高中", "國中", "國小" };
  String[] l_label = { "台北", "桃園", "新竹", "苗栗", "台中", "彰化", "雲林",
                      "嘉義", "台南", "高雄", "屏東", "花蓮", "台東", "澎湖" };
   	                    
  public mysqlFrame() {
    // 資料庫前置作業
    try {
      Class.forName("com.mysql.jdbc.Driver");
    } catch (Exception e) {
      errorMessage("MySQL驅動程式安裝失敗！");
    }
    try {
      connection = DriverManager.getConnection("jdbc:mysql://localhost/members"
                   +"?user=root&password=mysql");
      statement = connection.createStatement();
    } catch (SQLException e) { errorMessage("MySQL無法啟動！");
    } catch (Exception e) { errorMessage("發生其他錯誤！");
    }
    // 使用者介面建立
    cont = getContentPane();  //取得容器
    // 取得日期字串供加入日期使用
    Date date = new Date();
    dateNow = DateFormat.getDateInstance().format(date);
    // 配置帳號密碼輸入欄位及註冊、登入按鈕與事件傾聽
    panel1 = new JPanel();  panel1.setBounds( 0, 0, 500, 40);
    panel1.setBackground(Color.LIGHT_GRAY);
    itemq1 = new JLabel("帳號：");
    itemq2 = new JLabel("密碼：");
    id = new JTextField(10);
    password = new JPasswordField(10);
    qb11 = new JButton("註冊");
    qb12 = new JButton("登入");
    qb11.addActionListener(check);
    qb12.addActionListener(check);
    panel1.add(itemq1);  panel1.add(id);
    panel1.add(itemq2);  panel1.add(password);  
    panel1.add(qb11);  panel1.add(qb12); 
    cont.add(panel1);
    // 新增面版配置個人資料項目及內容
    panel2 = new JPanel();  panel2.setBounds( 0, 40, 500, 200); 
    panel2.setLayout(null);
    item1 = new JLabel("姓名："); item1.setBounds( 40, 60, 40, 20);
    item2 = new JLabel("年齡："); item2.setBounds(200, 60, 40, 20);
    item3 = new JLabel("性別："); item3.setBounds( 40, 80, 40, 20);
    item4 = new JLabel("興趣："); item4.setBounds( 40, 100, 50, 20);
    item5 = new JLabel("學歷："); item5.setBounds( 40, 130, 50, 20);
    item6 = new JLabel("居住地區："); item6.setBounds(200, 130, 70, 20);
    item7 = new JLabel("加入日期："); item7.setBounds(40, 230, 70, 20);
    item8 = new JLabel(); item8.setBounds(110, 230, 80, 20);
    panel2.add(item1);  panel2.add(item2);  panel2.add(item3);
    panel2.add(item4);  panel2.add(item5);  panel2.add(item6);
    panel2.add(item7);  panel2.add(item8);
    // 姓名
    text0 = new JTextField(10);  text0.setBounds(80,  60, 80, 20);
    panel2.add(text0);
    // 年齡
    spin = new JSpinner(new SpinnerNumberModel(20, 1, 100, 1));
    spin.setBounds(240, 60, 80, 20);
    panel2.add(spin);
    // 性別
    btn_group = new ButtonGroup();
    rb1 = new JRadioButton("帥哥", false); rb1.setBounds( 80, 80, 60, 20);
    rb2 = new JRadioButton("美女", false); rb2.setBounds(140, 80, 60, 20);
    btn_group.add(rb1); btn_group.add(rb2);
    panel2.add(rb1); panel2.add(rb2);
    // 興趣
    cb1 = new JCheckBox("電腦"); cb1.setBounds( 80, 100, 60, 20);
    cb2 = new JCheckBox("唱歌"); cb2.setBounds(140, 100, 60, 20);
    cb3 = new JCheckBox("電影"); cb3.setBounds(200, 100, 60, 20);
    cb4 = new JCheckBox("繪圖"); cb4.setBounds(260, 100, 60, 20);
    cb5 = new JCheckBox("旅遊"); cb5.setBounds(320, 100, 60, 20);
    panel2.add(cb1); panel2.add(cb2); panel2.add(cb3); panel2.add(cb4); panel2.add(cb5);
    // 學歷
    c_box = new JComboBox(c_label);
    c_box.setBounds(80, 130, 100, 20);
    panel2.add(c_box);
    // 居住地區
    list = new JList(l_label);
    JScrollPane s_pane = new JScrollPane(list);
    s_pane.setBounds(270, 130, 80, 80); 
    panel2.add(s_pane);
    // 新增下方工作按鈕
    qb31 = new JButton("取消");  qb31.setBounds( 270, 230, 60, 30);
    qb32 = new JButton("確認");  qb32.setBounds( 340, 230, 60, 30);
    qb33 = new JButton("刪除");  qb33.setBounds( 410, 230, 60, 30);
    qb31.addActionListener(reset);
    qb32.addActionListener(submit);
    qb33.addActionListener(delete);
    panel2.add(qb31);  panel2.add(qb32);  panel2.add(qb33);
    cont.add(panel2);
    panel2.setVisible(false);  // 未註冊或登入前將個人資料面版隱藏

    this.setTitle("會員註冊登入系統");
    this.setSize(500,300);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.show();
  }
  
  // 註冊、登入按鈕事件傾聽
  public ActionListener check = new ActionListener() {
    public void actionPerformed(ActionEvent a) {
      id_get = id.getText().trim();
      password_get = password.getText().trim();
      // 如果帳號或密碼欄位空白則不處理
      if (id_get.equals("") | password_get.equals("")) return;
      // 如果帳號或密碼欄位超過10字則顯示警告並不再處理
   	  if (id_get.length() >10 | password_get.length() >10) {
   	    warnMessage("帳號及密碼最多10字！");
   	    return;
   	  }
   	  try {
   	    // 根據輸入的帳號密碼查詢資料庫
   	    String comm_data = "SELECT * FROM personal_data WHERE " + 
   	  	       "acc_id='" + id_get + "'";
   	    statement = connection.createStatement(); 
   	    result = statement.executeQuery(comm_data);
   	    if (result.next()) {
   	      // 找到符合條件資料
   	      if (a.getSource() == qb11) {
   	        // 如果user按鈕是註冊，不能繼續處理
            warnMessage("帳號重複！");
            return;
          } else {
            if (password_get.equals(result.getString("password").trim())) {
              // 如果user按鈕是登入且密碼正確，可查詢、更正、刪除資料
              act = 2;   // 登入代號
              initialProcess();  // 介面初值處理
              // 取得資料庫內存檔記錄
              item8.setText(result.getString("date_join")); // 加入日期
   	          text0.setText(result.getString("name"));      // 姓名
   	          spin.setValue(result.getInt("age"));          // 年齡
   	          int msex = result.getInt("sex");              // 性別, 1=男, 2=女
   	          if (msex == 1) rb1.setSelected(true);
   	          else rb2.setSelected(true);
              cb1.setSelected(result.getBoolean("habbit1"));      // 興趣1
              cb2.setSelected(result.getBoolean("habbit2"));      // 興趣2
              cb3.setSelected(result.getBoolean("habbit3"));      // 興趣3
              cb4.setSelected(result.getBoolean("habbit4"));      // 興趣4
              cb5.setSelected(result.getBoolean("habbit5"));      // 興趣5
   	          c_box.setSelectedIndex(result.getInt("education")); // 學歷代號0-5
   	          list.setSelectedIndex(result.getInt("home"));       // 居住地區代號0-13               	  	
            } else {
               warnMessage("密碼錯誤！");
               return;
            }
          }
        } else {
          // 找不到符合條件資料
   	      if ( a.getSource() == qb11 ) {
   	        // 如果user按鈕是註冊，可查詢、更正資料
   	        act = 1;  // 註冊代號
   	        initialProcess();  // 介面初值處理
            item8.setText(dateNow); // 加入日期由系統提供
            // 個人資料內容初值由系統提供
            text0.setText("");
            spin.setValue(20);
   	        rb1.setSelected(false);  rb2.setSelected(false);
   	        cb1.setSelected(false);  cb2.setSelected(false);
   	        cb3.setSelected(false);  cb4.setSelected(false);
   	        cb5.setSelected(false);
   	        c_box.setSelectedIndex(0);
   	        list.setSelectedIndex(0);
          } else {
            // 如果user按鈕是登入，不能繼續處理
            warnMessage("帳號或密碼錯誤！");
            return;
          }
        }
      } catch (Exception e) {
         errorMessage("資料庫發生錯誤！"); 
      }
    }
  };
  
  // 介面初值處理
  public void initialProcess() {
    // 上方輸入介面無作用
    id.setEnabled(false);
    password.setEnabled(false);
    qb11.setEnabled(false);
    qb12.setEnabled(false); 
    // 顯示下方個人資料面版     
    panel2.setVisible(true);  
    if (act == 1) qb33.setEnabled(false);  // 如果註冊則刪除鈕無作用
  }
  
  // 介面結束處理
  public void endProcess() {
    // 恢復上方輸入介面作用
    id.setEnabled(true);        id.setText("");
    password.setEnabled(true);  password.setText("");
    panel2.setVisible(false);
    qb11.setEnabled(true);
    qb12.setEnabled(true);
    qb33.setEnabled(true);  // 恢復刪除鈕作用
    return;
  }
  
  // 取消按鈕事件傾聽
  public ActionListener reset = new ActionListener() {
    public void actionPerformed(ActionEvent a) {
      endProcess();  // 無任何動作，直接介面結束處理
    }
  };
  
  // 確認按鈕事件傾聽
  public ActionListener submit = new ActionListener() {
    public void actionPerformed(ActionEvent a) {
      int msex;
      boolean mh1, mh2, mh3, mh4, mh5;
      // 取得性別單選圓鈕boolean值轉為數字代號
      if (rb1.isSelected()) msex = 1;
      else msex = 2;
      // 取得興趣複選方鈕boolean值
      mh1 = cb1.isSelected();
      mh2 = cb2.isSelected();
      mh3 = cb3.isSelected();
      mh4 = cb4.isSelected();
      mh5 = cb5.isSelected();
      if (act == 1) {
        // 註冊狀況：資料庫為插入新資料筆數
      	comm_data = "INSERT INTO personal_data(" +
                "acc_id,password,date_join,name,sex,age," +
                "habbit1,habbit2,habbit3,habbit4,habbit5,education,home) VALUES('" +
                id_get + "', '" + password_get + "', '" + dateNow + "', '" + 
                text0.getText().trim()+ "', " + msex + ", " + spin.getValue() + ", " +
                mh1 + ", " + mh2 + ", " + mh3 + ", " + mh4 + ", " + mh5 + ", " +
                c_box.getSelectedIndex() + ", " + list.getSelectedIndex() + ")";
      } else {
        // 登入狀況：資料庫為更新資料內容
        comm_data = "UPDATE personal_data SET name='" + text0.getText().trim()+
                "', sex=" + msex + ", age=" + spin.getValue() + 
                ", habbit1=" + mh1 + ", habbit2=" + mh2 + ", habbit3=" + mh3 +
                ", habbit4=" + mh4 + ", habbit5=" + mh5 +
                ", education=" + c_box.getSelectedIndex() +
                ", home=" + list.getSelectedIndex() +
                " WHERE acc_id='" + id_get + "'AND password='" + password_get + "'";
      }
      try {	
	    statement = connection.createStatement(); 
	    statement.execute(comm_data);
	    if (act == 1) correctMessage("新會員註冊成功！");
	    else correctMessage("資料更正成功！");
   	    endProcess();  // 介面結束處理
      } catch(SQLException sqlex){
        errorMessage("資料庫發生錯誤！");
      }
    }
  };
  
  // 刪除按鈕事件傾聽
  public ActionListener delete = new ActionListener() {
    public void actionPerformed(ActionEvent a) {
      // 刪除資料需再確認一次
      JOptionPane option = new JOptionPane("請確認刪除或取消！",
      JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
      JDialog dialog = option.createDialog(null, "刪除確認");
      dialog.show();
      Object ans = option.getValue();
      if ( ans !=null ) {
        if ( (Integer) ans == 0 ) {
      	  // 資料庫刪除該筆資料
          comm_data = "DELETE FROM personal_data WHERE " + 
   	            "acc_id='" + id_get + "'AND password='" + password_get + "'";
          try {	
	       statement = connection.createStatement(); 
	       statement.execute(comm_data);
	       correctMessage("資料刪除成功！");
   	       endProcess();
	      } catch(SQLException sqlex) {
	        errorMessage("資料庫發生錯誤！");
          }
      	}
      }
    }
  };
  
  // 資料庫異動成功對話面版
  public void correctMessage(String msg) {
    String message = msg;
    JOptionPane.showMessageDialog(null, message, "資料庫異動",     JOptionPane.INFORMATION_MESSAGE);
  } 
  
  // 資料庫嚴重錯誤對話面版
  public void errorMessage(String msg) {
    String message = msg;
    JOptionPane.showMessageDialog(null, message, "嚴重錯誤", JOptionPane.ERROR_MESSAGE);
    System.exit(0);
  }
  
  // 資料庫錯誤訊息對話面版
  public void warnMessage(String msg) {
    String message = msg;
    JOptionPane.showMessageDialog(null, message, "錯誤訊息", JOptionPane.ERROR_MESSAGE);
    id.setText("");
    password.setText("");
  } 
}

public class J15_5_1 {
  public static void main(String[] args) {
    mysqlFrame frame = new mysqlFrame();
  }
}
