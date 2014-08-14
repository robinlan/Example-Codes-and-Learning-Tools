public class J3_3_6 {
  public static void main(String[] args) {
    for (int n = 1; n <= 50; n++) {
      if ((n % 2 == 0) || (n % 3 == 0))
        continue;
      System.out.print(n + ",");
    }    
  }
}        
