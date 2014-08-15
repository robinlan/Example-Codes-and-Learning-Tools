package wyf;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class MapColRowDialog extends JFrame//生成行數和列數對話框
implements ActionListener
{
	JLabel jlRow=new JLabel("地圖行數");//地圖行數
	JLabel jlCol=new JLabel("地圖列數");//地圖列數
	JTextField jtfRow=new JTextField("20");//創建行數JTextField
	JTextField jtfCol=new JTextField("20");//創建行數JTextField
	
	JButton jbOk=new JButton("確定");//創建確定按鈕
	
	public MapColRowDialog()//構造器
	{
		this.setTitle("3D重力球地圖設計器");
		
		this.setLayout(null);//設置佈局為空
		jlRow.setBounds(10,5,60,20);//設置位置
		this.add(jlRow);//添加jlRow
		jtfRow.setBounds(70,5,100,20);//設置位置
		this.add(jtfRow);//添加jtfRow
		
		jlCol.setBounds(10,30,60,20);//設置位置
		this.add(jlCol);//添加jlCol
		jtfCol.setBounds(70,30,100,20);//設置位置
		this.add(jtfCol);//添加jtfCol
		
		jbOk.setBounds(180,5,60,20);//設置位置
		this.add(jbOk);//添加jbOk
		jbOk.addActionListener(this);//添加監聽器
		
		this.setBounds(440,320,300,100);//設置位置
		this.setVisible(true);//設置可見
		
	}
	
	public void actionPerformed(ActionEvent e)//監聽處理代碼
	{
		int row=Integer.parseInt(jtfRow.getText().trim());//獲取行數
		int col=Integer.parseInt(jtfCol.getText().trim());//獲取列數
		
		new MapDesigner(row,col);//創建MapDesigner對像
		this.dispose();//本窗口消失
	}
	
	public static void main(String args[])
	{
		new MapColRowDialog();//創建對像
	}   
}
