import java.text.*;
public class J5_6_2 {
  public static void main (String[] args) {
    double d1 = 12345678901d, d2=3.045,d3;
    d3 = d1 / d2;
    NumberFormat nf1 = NumberFormat.getInstance();
    nf1.setMaximumFractionDigits(2);
    nf1.setMinimumFractionDigits(1);
    System.out.println("2 : " + nf1.format(d1)); 
    System.out.println("3.045 : " + nf1.format(d2)); 
    System.out.println("12345678901 / 3.045 = " + d3);  
    System.out.println("12345678901 / 3.045 : " + nf1.format(d3));  
  }
}