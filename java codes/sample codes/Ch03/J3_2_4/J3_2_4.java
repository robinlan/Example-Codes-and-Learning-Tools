import java.io.*;
public class J3_2_4 {
  public static void main(String[] args) throws IOException {
    BufferedReader keyin = new BufferedReader(
                           new InputStreamReader(System.in));
    System.out.print("請輸入分數：");
   	int score = Integer.parseInt(keyin.readLine());
    if (score >= 80) {
      System.out.println("成績很好，請繼續保持！");
    } else if (score >= 60) {
      System.out.println("成績普通，要多多加油！");
    } else { 
      System.out.println("成績不行，應反省振作！");
    }
  }
}