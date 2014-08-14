import java.io.*;
import javax.swing.*;
public class J14_5_1 {
  public static void main(String[] args) {
    String data;
    try {
      // 建立檔案物件,即分別開啟檔案 poem1.txt 與 poem3.txt
      String fname1 = "poem1.txt";
      BufferedReader fIn1 = new BufferedReader(new FileReader(fname1));
      String fname3 = "poem3.txt";
      BufferedWriter fOut3= new BufferedWriter(new FileWriter(fname3));

      // 讀取 poem1.txt 檔案資料,將此資料存於 poem3.txt
      do {
        data = fIn1.readLine();
        if(data == null) break;
        fOut3.write(data);
        fOut3.newLine();
      } while (true);
      fIn1.close();      // 關閉poem1.txt檔案

      // 建立檔案物件,即開啟檔案 poem2.txt 
      String fname2 = "poem2.txt";
      BufferedReader fIn2 = new BufferedReader(new FileReader(fname2));

      // 讀取 poem2.txt 檔案資料,將此資料接續存於 poem3.txt
      do {
        data = fIn2.readLine();
	     if(data == null) break;
        fOut3.write(data);
        fOut3.newLine();
      } while (true);
      
      fIn2.close();      // 關閉poem2.txt檔案
      fOut3.close();     // 關閉poem3.txt檔案
    }
    catch (IOException e) {
      JOptionPane.showMessageDialog(null,"檔案處理有誤!!");
    }
  }
}

