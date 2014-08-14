public class J5_2_1 {
  static void Change(int x,int y){
    int t;
    t = x;
    x = y;
    y = t;
    System.out.println("Change 方法  : ");
    System.out.println("x = " + x +"      y = " + y); 
  }  
        
  public static void main (String[] args) {
    int x = 3, y = 5;
    System.out.println("main 方法 -- 呼叫 Change 方法前 : ");
    System.out.println("x = " + x +"      y = " + y);  
    Change(x,y); 
    System.out.println("main 方法 -- 呼叫 Change 方法後 : ");
    System.out.println("x = " + x +"      y = " + y);    
  }
}