import java.io.*;
public class J3_4_2 {
  public static void main(String[] args) throws IOException {
    BufferedReader keyin = new BufferedReader(
                           new InputStreamReader(System.in));
    do {
      System.out.print("輸入2~2147483647的整數(輸入0代表結束)： ");
      int num = Integer.parseInt(keyin.readLine());
      if (num == 0) break;
      System.out.print(num + " = ");
      int order = 0;
      boolean mult = false;
      for (int i = 2; i <= num; i++) {
        for(int j = 2; j <= i; j++) {
          if(i % j == 0 && j < i) {
            break;
          } else if (j == i - 1 || i == 2){
            while (num % i == 0) {
              num = num / i;
              order++;
            }
          }   
          if (order != 0){
            if (mult) System.out.print(" * ");
            if (order == 1) {
              System.out.print(i);
            } else {
              System.out.print(i + "^" + order);
            }
            order = 0;
            mult = true;
          }
        }
      }
      System.out.println("\n");
    } while(true);   
  }
}        
