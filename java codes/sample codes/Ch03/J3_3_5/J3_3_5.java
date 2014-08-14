import java.io.*;
public class J3_3_5 {
  public static void main(String[] args) throws IOException {
    BufferedReader keyin = new BufferedReader(
                           new InputStreamReader(System.in));
    int count = 0;
    String pw = "Java2";
    boolean pass = false;
    do {
      count++;
      System.out.print("請輸入密碼：");
      String pw_keyin = keyin.readLine();
      if (pw_keyin.equals(pw)) {
        System.out.println("密碼正確，歡迎光臨");
        pass = true;
        break;
      } else {
        System.out.println("第" + count + "次密碼輸入錯誤！\n");
      }    
    } while (count < 3);
    if (!pass) System.out.println("三次密碼輸入錯誤，拒絕使用！");
  }
}