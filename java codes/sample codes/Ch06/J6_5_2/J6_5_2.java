import java.io.*;
public class J6_5_2 {
  public static void main(String[] args) throws IOException {
    BufferedReader keyin = new BufferedReader(
                           new InputStreamReader(System.in));
    System.out.print("請輸入學期成績： ");
    int s = Integer.parseInt(keyin.readLine());
    System.out.println();      
        
    String stu_pass;
    String stu_lever;
    CStudent stu = new CStudent();
    stu_pass = stu.Pass(s);
    stu_lever = stu.Lever(s);
    System.out.println(stu_pass + "，" + stu_lever + "\n"); 
  }
}

// 定義IPass介面
interface IPass {
  int score = 60;
  String Pass(int s);
}

// 定義ILever介面
interface ILever {
  int high = 80;
  int low = 60;
  String Lever(int s);
}

// 定義CStudent類別實作IPass介面與ILever介面
class CStudent implements IPass, ILever {
  String passOK;
  public String Pass(int s) {
    if (s >= score)
      passOK = "及格";
    else
      passOK = "不及格";
    return passOK;
  }
  String leverN;

  public String Lever(int s) {
    if (s >= high) 
      leverN = "表現優異";
    if ((s >= low) && (s < high)) 
      leverN = "差強人意";
    if (s < low) 
      leverN = "有待加強";
    return leverN;   
  }
}

