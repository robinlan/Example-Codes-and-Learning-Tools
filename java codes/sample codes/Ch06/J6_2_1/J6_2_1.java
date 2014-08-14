class CMath1 {
  int Add(int num1, int num2) {
    return (num1 + num2);
  }
  double Add(double num1, double num2) {
    return (num1 + num2);
  }
}

public class J6_2_1 {
  public static void main(String[] args) {
    CMath1 sum = new CMath1();
    int n1 = 50, n2 = 20;     
    System.out.println(n1 + " + " + n2 + " = " + sum.Add(n1, n2)); 
    double d1 = -10.35, d2 = 3.11;
    System.out.println(d1 + " + " + d2 + " = " + sum.Add(d1, d2));     
  }
}
