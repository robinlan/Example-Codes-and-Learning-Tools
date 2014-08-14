public class J8_2_3 {
  public static void main (String[] args) {
    int[] arr = new int[10];
    try {
      arr[9] = 900;
        System.out.println("arr[9] = " + arr[9]);
        arr[10] = 1000;
        System.out.println("arr[10] = " + arr[10]);
      }
      catch(ArithmeticException e) {
        System.out.println("錯誤類型 : 算術運算錯誤");
      }
      catch(ArrayIndexOutOfBoundsException e) {
        System.out.println("錯誤類型 : 陣列索引超出範圍");
      }
   }
}