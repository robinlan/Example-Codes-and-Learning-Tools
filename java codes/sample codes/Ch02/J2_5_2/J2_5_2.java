public class J2_5_2 {
  public static void main (String[] args) {
    int int_a = 100, int_b = 100000;
    float float_a = 12345.56f, float_b = -98765.4321f;
    byte byte_a, byte_b;
    float_a = int_a;
    System.out.println("float_a = " + float_a); 
    int_b = (int)float_b;
    System.out.println("int_b = " + int_b); 
    byte_a = (byte)int_a;
    System.out.println("byte_a = " + byte_a); 
    byte_b = (byte)int_b;
    System.out.println("byte_b = " + byte_b); 
  }
}