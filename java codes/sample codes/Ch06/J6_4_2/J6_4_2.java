class CDogKind {
  private String kind;
  void SetKind(String k) {
    kind = k;
  }
  void Show() {
    System.out.println("寵物種類：" + kind);
  }
}

class CDog extends CDogKind {
  private String name;
  private int age;
  void SetName(String n) {
    name = n;
  }
  void SetAge(int a) {
    age = a;
  }
  void Show() {
    System.out.println("寵物名字：" + name);
    System.out.println("寵物年齡：" + age);
  }
}   
  
public class J6_4_2 {
  public static void main(String[] args) {
    CDog dog = new CDog();
    dog.SetKind("米格魯");
    dog.SetName("布丁");
    dog.SetAge(2);
    dog.Show();
  }
}