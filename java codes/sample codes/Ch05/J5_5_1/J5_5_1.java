import java.util.*;
import java.io.*;
public class J5_5_1 {
  public static void main(String[] argv) throws IOException {
    long t1, t2, tot_t;
    Date date_time1 = new Date();
    Calendar date_time2 = Calendar.getInstance();
    t1 = System.currentTimeMillis();
    System.out.println("\n開始 : " + t1 + "毫秒\n");
    System.out.println(date_time1);
    System.out.println(date_time2.get(Calendar.YEAR) + "年");
    System.out.println(date_time2.get(Calendar.MONTH)+1 + "月");
    System.out.println(date_time2.get(Calendar.DAY_OF_MONTH) + "日");
    System.out.println(date_time2.get(Calendar.HOUR_OF_DAY) + "時");
    System.out.println(date_time2.get(Calendar.MINUTE) + "分");
    System.out.println(date_time2.get(Calendar.SECOND) + "秒");
    System.out.println(date_time2.get(Calendar.MILLISECOND) + "毫秒");

    System.out.println("\n按 << Enter >> 結束測試時間"); 
    int p = System.in.read();
    t2 = System.currentTimeMillis();
    System.out.println("\n結束 : " + t2 + "毫秒");
    tot_t = t2 - t1;
    System.out.println("\n全部 : " + (double)tot_t / 1000 + "秒");
  }
}
