public class J8_2_2 {
  public static void main (String[] args) {
    int n1 = 12, n2 = 2, n3;
    try {
      n3 = n1 / n2;
      System.out.println("相除結果 : " + n3);
    }
    catch(Exception e) {
      System.out.println("錯誤類型 : ");
      System.out.println(e);
    }
    finally {
      System.out.println("執行 finally 敘述");
    }
  }
}