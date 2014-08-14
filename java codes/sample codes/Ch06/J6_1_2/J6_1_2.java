class CRectangle {
  private int high;
  private int wide;
  private int area = 0;
  private int peri = 0;
     
  public void SetValue(int h, int w) {
    high = h;
    wide = w;
    CalArea();
    CalPeri(); 
  }
  private void CalArea() {
    area = high * wide;
  }
  private void CalPeri() {
    peri = (high + wide) * 2;
  }
  public int GetArea() {
    return area;
  }
  int GetPeri() {
    return peri;
  }
}

public class J6_1_2 {
  public static void main(String[] args) {
    CRectangle rec2 = new CRectangle();
    int h2 = 25;
    int w2 = 9;
    rec2.SetValue(h2, w2);
    System.out.println("*** 矩形二 ***");
    System.out.println("長度 = " + h2);
    System.out.println("寬度 = " + w2);
    System.out.println("面積 = " + rec2.GetArea());
    System.out.println("周長 = " + rec2.GetPeri()); 
  }
}