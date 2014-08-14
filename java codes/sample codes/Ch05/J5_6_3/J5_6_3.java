import java.text.*;
public class J5_6_3 {
  public static void main (String[] args) {
    double d1=0, d2=12345.6789;
    Format nf1 = new DecimalFormat("##.00");
    System.out.println(nf1.format(d1)); 
    System.out.println(nf1.format(d2)); 
    Format nf2 = new DecimalFormat("$,##0.0#");
    System.out.println(nf2.format(d1)); 
    System.out.println(nf2.format(d2));
    Format nf3 = new DecimalFormat("#0%"); 
    System.out.println(nf3.format(d2)); 
  }
}