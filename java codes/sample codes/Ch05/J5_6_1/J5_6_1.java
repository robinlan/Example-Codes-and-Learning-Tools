import java.text.*;
public class J5_6_1 {
  public static void main (String[] args) {
    double d1 = 1234567890.12345;
    NumberFormat nf1 = NumberFormat.getInstance();
    System.out.println(nf1.format(d1)); 
    System.out.println(NumberFormat.getIntegerInstance().format(d1)); 
    System.out.println(NumberFormat.getPercentInstance().format(d1)); 
    System.out.println(NumberFormat.getCurrencyInstance().format(d1)); 
  }
}