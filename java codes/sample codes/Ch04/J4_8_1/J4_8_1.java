import java.io.*;
public class J4_8_1 {
  public static void main (String[] args) throws IOException {
    BufferedReader keyin = new BufferedReader(
                           new InputStreamReader(System.in));
    String[] prob = {"2 + 6 = ( )","_ you a girl?",
                     "床前 _ _ 光，疑是地上霜。"};
    String[] right = {"8", "Are", "明月"};
    String ans; 
    int i, j, count = 0;
    for(i = 0; i < prob.length; i++) {
      System.out.println("\n第" + (i+1) + "題" );
      System.out.println(prob[i]);
      System.out.print("輸入答案 : ");
      ans = keyin.readLine();
      if(ans.equals(right[i])) {
        System.out.println("答對了！");
        count++;
      } else {
        System.out.println("答錯了 ！");
        System.out.println("正確答案 : " + right[i]);                 
      }
    }
               
    switch(count) {
      case 0 :
      case 1 : System.out.println("\n加油！");
               break;
      case 2 : System.out.println("\n過關！");
               break;
      case 3 : System.out.println("\n全對，一級棒！");
    }         
  }
}