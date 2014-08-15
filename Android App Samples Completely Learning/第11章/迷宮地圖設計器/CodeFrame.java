package wyf;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class CodeFrame extends JFrame//生成二維數組窗體
{
	JTextArea jta=new JTextArea();//創建JTextArea
	JScrollPane jsp=new JScrollPane(jta);//創建JScrollPane
	
	public CodeFrame(String codeStr,String title)
	{
		this.setTitle(title);//設置標題
		
		this.add(jsp);//添加JScrollPane
		
		jta.setText(codeStr);//向JTextArea中添加內容
		
		this.setBounds(100,100,400,300);//設置大小
		this.setVisible(true);//設置可見
	}
	public static void main(String args[])
	{
		new MapColRowDialog();
	}
}
