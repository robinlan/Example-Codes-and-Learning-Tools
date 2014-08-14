import java.util.*;
public class J4_7_1 {
  public static void main (String[] args) {
    String str1 = "汽油,28.7,40";
    StringTokenizer str2 = new StringTokenizer(str1, ",");
    String name;
    int num;
    double price, money;
    System.out.println(str2.countTokens() + " 個欄位");
    name = str2.nextToken();
    price = Double.parseDouble(str2.nextToken());
    num = Integer.parseInt(str2.nextToken());
    money = price * num;
    System.out.println("品名 : " + name);
    System.out.println("單價 : " + price);
    System.out.println("數量 : " + num);
    System.out.println("金額 : " + money);  
  }
}
