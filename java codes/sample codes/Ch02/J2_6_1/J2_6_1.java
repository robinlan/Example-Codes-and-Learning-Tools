import java.io.*;
public class J2_6_1 {
  public static void main(String[] args) throws IOException {
    BufferedReader keyin;
    keyin = new BufferedReader(new InputStreamReader(System.in));
    double w1, w2, ave;
    System.out.print("輸入 1 號體重： ");
    String st = keyin.readLine(); 
    w1 = Double.parseDouble(st);
    System.out.print("輸入 2 號體重： ");
    w2 = Double.parseDouble(keyin.readLine());
    ave = (w1 + w2 ) / 2;
    String st_ave = String.valueOf(ave);
    System.out.print("兩人平均體重 ： " + st_ave);
    //System.out.print("兩人平均體重 ： " + ave);
  }
}        
