import java.io.*;
public class J2_4_2 {
  public static void main(String[] args) throws IOException {
    char ch;
    System.out.print("請按一個字母： ");
    ch = (char)System.in.read();
    System.out.println("所按的字母為： " + ch);
  }
}