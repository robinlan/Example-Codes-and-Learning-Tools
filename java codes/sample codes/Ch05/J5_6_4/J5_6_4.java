import java.util.*;
import java.text.*;
public class J5_6_4 {
  public static void main (String[] args) {
    Date date1 = new Date();
    System.out.println(DateFormat.getInstance().format(date1)); 
    System.out.println(DateFormat.getDateInstance().format(date1)); 
    System.out.println(DateFormat.getTimeInstance().format(date1)); 
    System.out.println(DateFormat.getDateTimeInstance().format(date1)); 
  }
}