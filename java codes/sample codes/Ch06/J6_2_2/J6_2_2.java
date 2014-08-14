class CMath2 {
  int GetMax(int num1,int num2) {
    if( num2 > num1 )
      return num2;
    else
      return num1;
  }
  int GetMax(int[] num) {
    int max = num[0];
    for(int i = 1; i < num.length; i++) {
      if(num[i] > max) max = num[i];
    }
    return max;
  }
}

public class J6_2_2 {
  public static void main(String[] args) {
    CMath2 data = new CMath2();
    int n1 = -50, n2 = 20;     
    System.out.println(n1 + " , " + n2 + " 程计 : " + data.GetMax(n1,n2)); 
    int[] num = {1,5,3};
    System.out.println("1 , 5 , 3 程计 : " + data.GetMax(num));    
  }
}
