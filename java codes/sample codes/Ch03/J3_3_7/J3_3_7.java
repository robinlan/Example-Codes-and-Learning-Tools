public class J3_3_7 {
  public static void main(String[] args) {
    int factor, n = 1;
    CalFactor:
    while (true) {
      factor = 1;
      for(int i = n; i > 0; i--){
        factor *= i;
        if (n > 8) break CalFactor;
      }
      System.out.println(n + "! = " + factor);
      n++;
     }
     System.out.println("-- over --");
  }
}