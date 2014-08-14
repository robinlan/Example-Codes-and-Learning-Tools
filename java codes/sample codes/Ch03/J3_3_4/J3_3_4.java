public class J3_3_4 {
  public static void main(String[] args) {
    for (int i=1; i<=9; i++ ) {
      int j=1;
      while (j<=9) {
        System.out.print(j + "*" + i + "=");
        System.out.print(j*i + "\t");
        j++;
      }
      System.out.println();
    }
  }
}