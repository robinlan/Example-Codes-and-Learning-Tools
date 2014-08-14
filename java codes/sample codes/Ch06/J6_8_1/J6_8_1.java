import java.io.*;
public class J6_8_1 {
  public static void main(String[] args) throws IOException {
    BufferedReader keyin = new BufferedReader(
                           new InputStreamReader(System.in));
    System.out.print("請輸入學期成績：");
    int s = Integer.parseInt(keyin.readLine());
               
    CCollage pass1 = new CCollage();
    pass1.score = s;
    pass1.Pass();
    System.out.println(pass1.title + pass1.passOK); 
                
    CGraduate pass2 = new CGraduate();
    pass2.score = s;
    pass2.Pass();
    System.out.println(pass2.title + pass2.passOK);   
  }
}

// 定義成績通過的IPass介面
interface IPass {
  int s1 = 60;  // 大學及格成績
  int s2 = 70;  // 研究所及格成績
  void Pass();
}

// 定義CCollage類別實作IPass介面
class CCollage implements IPass {
  int score;
  String title = "大學成績，";
  String passOK;
  public void Pass() {
    if (score >= s1)
      passOK = "及格";
    else
      passOK = "不及格";
  }
}

//定義CGraduate類別實作IPass介面
class CGraduate implements IPass {
  int score;
  String title = "研究所成績，";
  String passOK;
  public void Pass() {
    if (score >= s2)
      passOK = "及格";
    else
      passOK = "不及格";
  }
}

