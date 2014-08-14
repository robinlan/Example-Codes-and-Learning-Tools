import java.io.*;

public class J14_3_1 {
  public static void main(String[] args) {
    try {
      String filename = "student.txt";
      FileWriter fWrite = new FileWriter(filename);
      BufferedWriter fOut = new BufferedWriter(fWrite);
      fOut.write("王一, 85, 90");   // 寫入一筆資料
      fOut.newLine();               // 換行字元
      fOut.write("張三, 65, 67");  
      fOut.newLine();
      fOut.flush();                 // 清理緩衝區
      fWrite.close();               // 關閉檔案
    }
    catch (IOException e) {
      System.out.println("檔案處理有誤!!");
    } 
  }
}