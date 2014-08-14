import java.io.*;
import java.util.*;
import java.text.*;
public class J5_8_2 {
  public static void main (String[] args) throws IOException {
    BufferedReader keyin = new BufferedReader(
                           new InputStreamReader(System.in));
    long t1, t2;
    float test_t;
    int i, score = 0;
    Date date1 = new Date();
    DateFormat df = new SimpleDateFormat("yyyy年 M月dd日 E");
    Format nf = new DecimalFormat("##.00");
 
    t1 = System.currentTimeMillis(); 
    for(i = 1; i <= 2; i++) {
      System.out.println("\n第" + i + "題" );
      int num1 = (int)(Math.random() * 11) + 10;
      int num2 = (int)(Math.random() * 11);
      int right=num1 * num2;
      System.out.print(num1 + "x" + num2 + "=");
      int ans = Integer.parseInt(keyin.readLine());
      if(ans == right) {
        System.out.println("答對了！");
        score += 50;
      } else {
        System.out.println("答錯了 ！");
        System.out.println("正確答案 : " + right);                 
      }
    }
    t2 = System.currentTimeMillis(); 
    test_t = (float)(t2 - t1) / 1000;

    System.out.println("\n" + df.format(date1));       
    System.out.println("總分 = " + score + "分");
    System.out.println("時間 = " + nf.format(test_t) + "秒");
  }
}