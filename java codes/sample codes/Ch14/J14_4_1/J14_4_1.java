import java.io.*;

public class J14_4_1 {
  public static void main(String[] args) {
    String fname = "mountain.jpg";   // 多媒體檔案名稱
    try {
      // 取得輸入串流	
      FileInputStream fIn = new FileInputStream (fname);
      byte data[] = new byte[fIn.available()]; // 取得檔案的大小
	  fIn.read(data);              // 輸入串流存放陣列
	  fIn.close();                 // 關閉輸入串流 
	  
	  // 取得輸出串流
	  FileOutputStream fOut = new FileOutputStream ("複製-" + fname);
	  fOut.write(data);            // 輸出串流取得陣列資料
	  System.out.println("檔案已複製....");
	  fOut.close();                // 關閉輸出串流 
	}
	catch (IOException e) {
      System.out.println("檔案處理有誤!!");
	}
  }
}