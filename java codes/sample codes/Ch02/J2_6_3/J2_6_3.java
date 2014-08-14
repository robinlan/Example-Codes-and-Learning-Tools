import java.io.*;
public class J2_6_3 {
  public static void main(String[] args) throws IOException {
    BufferedReader keyin = new BufferedReader(
                           new InputStreamReader(System.in));
    System.out.print("輸入貨品名稱： ");        
    String goods_name = keyin.readLine();
    System.out.print("輸入單價： ");
    float price = Float.parseFloat(keyin.readLine());
    System.out.print("輸入數量： ");
    int num = Integer.parseInt(keyin.readLine());
    float money = price * num;
    
    System.out.println("\n貨品名稱\t單價\t數量\t金額");
    System.out.println("========================================");
    System.out.println(goods_name + "\t\t" + price + "\t" + num + "\t" + money);
  }
}        
