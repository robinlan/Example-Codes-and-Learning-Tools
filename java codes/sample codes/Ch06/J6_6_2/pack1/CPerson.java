package pack1;
public class CPerson {
  private String name, sex;
  public void SetName(String st1) {
    name = st1;
  }
  public void SetSex(String st2) {
    sex = st2;
  }
  public void GetWelcome() {
    System.out.print(name);
    if(sex.equals("女"))
      System.out.println("小姐，歡迎光臨！"); 
    else
      System.out.println("先生，歡迎光臨！");
  }
}
