import java.io.*;
public class J2_6_2 {
  public static void main(String[] args) throws IOException {
    String goods_name;    // 貨品名稱變數
    float price, money;   // 單價變數, 金額變數
    int num;              // 數量變數
    BufferedReader keyin; // 輸入串流物件
    keyin = new BufferedReader(new InputStreamReader(System.in));
    System.out.print("輸入貨品名稱： ");        
    goods_name = keyin.readLine();
    System.out.print("輸入單價： ");
    price = Float.parseFloat(keyin.readLine());
    System.out.print("輸入數量： ");
    num = Integer.parseInt(keyin.readLine());
    money = price * num;
    System.out.println("");
    System.out.println("貨品名稱： " + goods_name);
    System.out.println("單價： " + price + "元");
    System.out.println("數量： " + num);
    System.out.println("金額： " + money + "元");
  }
}        
