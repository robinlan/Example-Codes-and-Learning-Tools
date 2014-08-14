import java.io.*;

public class J14_3_2 {
  public static void main(String[] args) {
    String data;
    try {
      FileReader fRead = new FileReader("..\\J14_3_1\\student.txt");
      BufferedReader fIn = new BufferedReader(fRead);
	  do {
        data = fIn.readLine();    // 讀取一行字串資料
		if(data == null) break;     // 若資料讀取完畢，跳離迴圈
		System.out.println(data);   // 顯示所讀取的資料
	  } while (true);
	  fRead.close();               // 關閉檔案
	}
	catch (IOException e) {
      System.out.println("檔案處理有誤!!");
	}
  }
}