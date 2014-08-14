import java.io.*;
public class J3_4_1 {
  public static void main(String[] args) throws IOException {
    BufferedReader keyin = new BufferedReader(
                           new InputStreamReader(System.in));
    int num = 38;
    boolean guessOK=false;
    System.out.println("*****  猜數字遊戲  *****\n");
    do {
      System.out.print("請從 0~99 中猜一個數字： ");
      int guess = Integer.parseInt(keyin.readLine());
      if (guess == num) {
        guessOK = true;
        System.out.println("哇！好棒啊，你終於猜到了！");
      } else {
        if (guess > num) {
          System.out.println("太大了！再猜小一點！\n");
        } else {
          System.out.println("太小了！再猜大一點！\n");
        }    
      }   
    } while(!guessOK);
  }
}        
