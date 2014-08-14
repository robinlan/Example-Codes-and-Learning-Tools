public class J5_3_1 {
  static int Fact(int n){
    if(n == 1)
      return 1;
    else
      return n * Fact(n - 1);
  }  
        
  public static void main (String[] args) {
    int m,fact_val;
    System.out.println("5! = " + Fact(5)); 
    m = 6;
    fact_val = Fact(m);
    System.out.println(m + "! = " + fact_val);    
  }
}