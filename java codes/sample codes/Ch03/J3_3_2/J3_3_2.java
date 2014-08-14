import java.io.*;
public class J3_3_2 {
  public static void main(String[] args) throws IOException {
    BufferedReader keyin = new BufferedReader(
                           new InputStreamReader(System.in));
    System.out.print("請輸入計程車行駛的公里數：");
    float km = Float.parseFloat(keyin.readLine());
    int money = 75;
    km -= 1.5; 
    while (km > 0){
      money += 5;
      km -= 0.3;
    }
    System.out.println("車資(元)：" + money);
  }
}