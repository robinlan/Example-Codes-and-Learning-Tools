import java.util.*;
import java.text.*;
public class J5_6_5 {
  public static void main (String[] args) {
    long sum = 0;
    for(int i = 0; i <= 4; i++){
      Date date1 = new Date();
      DateFormat df = new SimpleDateFormat("yyyy年 M月dd日 E a hh點 mm分 ss秒");
      System.out.println(df.format(date1)); 
      for(long j = 0; j <= 100000000; j++){
        sum++;
      }
    }
    System.out.println("sum = " + sum); 
  }
}