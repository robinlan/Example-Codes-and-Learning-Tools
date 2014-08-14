public class J5_7_1 {
  static int x = 0;
  static void Method1() {
    System.out.println("Method1()  ---  x = " + x); 
  }  
        
  static void Method2() {
    int x = 2;
    System.out.println("Method2()  ---  x = " + x); 
  }  

  public static void main (String[] args) {
    Method1();  
    Method2();
    int x = 3;
    System.out.println("main()     ---  x = " + x);
    for(int y = 11; y <= 11; y++)  
      System.out.println("for{}   ---   y = " + y ); 
     
    int y = 12;
    System.out.println("main()  ---  y = " + y );   
  }
}