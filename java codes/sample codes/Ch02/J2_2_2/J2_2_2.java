public class J2_2_2 {
  public static void main (String[] args) {
    char ch1;
    ch1 = 'A';
    char ch2 = '\u0041';
    String st1;
    st1 = "大家來學Java2";
    String st2 = "大家來學\"Java2\"";
    
    System.out.println(ch1); 
    System.out.println(ch2);
    System.out.println('B');
    System.out.println('\u0042'); 
    System.out.println(st1); 
    System.out.println(st2); 
    System.out.println("Java2\n易學易用"); 
  }
}