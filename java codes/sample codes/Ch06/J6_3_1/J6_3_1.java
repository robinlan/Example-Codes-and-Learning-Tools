class CRectArea {
  private int height, wide;
  CRectArea() {
    height = wide = 1;
  }
  CRectArea(int h) {
    SetHeight(h);
    wide = 1;
  }
  CRectArea(int h, int w) {
    SetHeight(h);
    SetWide(w); 
  }
  void SetHeight(int h) {
    height = h;
  }
  void SetWide(int w) {
    wide = w; 
  }
  int GetArea() {
    return height * wide;
  }
}

public class J6_3_1 {
  public static void main(String[] args) {
    CRectArea a1 = new CRectArea();
    System.out.println("­±¿n = " + a1.GetArea());
    CRectArea a2 = new CRectArea(5);
    System.out.println("­±¿n = " + a2.GetArea());       
    CRectArea a3 = new CRectArea(3,6);
    System.out.println("­±¿n = " + a3.GetArea());
  }
}
