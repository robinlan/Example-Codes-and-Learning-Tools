import java.util.*;
import java.io.*;
import javax.swing.*;

public class J14_5_2 {
  public static void main(String[] args) {
    String data, fName = "";
    StringTokenizer str;
    String name;
    int chi, eng;
    double avg;

    // 由鍵盤輸入資料寫入於檔案中 
    try {
      fName = JOptionPane.showInputDialog("輸入存檔路徑");
      BufferedWriter fOut = new BufferedWriter(new FileWriter(fName, true));
      while(true){
        data = JOptionPane.showInputDialog("輸入學生的資料\n姓名,國文,英文\n(結束直接按<<Enter>>)");
        if(data.equals("")) break;
        fOut.write(data);
        fOut.newLine();
      }
      fOut.close();
    }
    catch (Exception e) { 
      JOptionPane.showMessageDialog(null, "存檔路徑有誤!!");
      System.exit(0);
    }
  
    // 由檔案讀出資料顯示螢幕上幕
    try {
      BufferedReader fIn = new BufferedReader(new FileReader(fName));
      System.out.println("姓名\t國文\t英文\t平均");
      do {
        data = fIn.readLine();
        if(data == null || data.equals("")) break;
        str = new StringTokenizer(data, ",");   // 以","分解出子字串
        name = str.nextToken( );
        chi = Integer.parseInt(str.nextToken( ));
        eng = Integer.parseInt(str.nextToken( ));
        avg = (chi + eng) / 2.0;
        System.out.println(name + "\t" + chi + "\t" + eng + "\t" + avg );
      } while (true);
      fIn.close();
    }
    catch (IOException e) {
      JOptionPane.showMessageDialog(null,"檔案處理有誤!!");
    }
  }
}