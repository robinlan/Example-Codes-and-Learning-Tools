class CComputer_1 {
  private int num1, num2;
  void SetNum1(int n1, int n2) {
    num1 = n1;
    num2 = n2;
  }
  int GetAdd() {
    return num1 + num2;
  }
}

class CComputer_2 extends CComputer_1 {
  private int num1, num2;
  void SetNum2(int n1, int n2) {
    num1 = n1;
    num2 = n2;
  }
  int GetMult() {
    return num1 * num2;
  }
}

public class J6_4_1 {
  public static void main(String[] args) {
    CComputer_2 result = new CComputer_2();
    result.SetNum1(4, 3);
    result.SetNum2(5, 6);
    System.out.println("4 + 3 = " + result.GetAdd());
    System.out.println("5 * 6 = " + result.GetMult());
  }
}