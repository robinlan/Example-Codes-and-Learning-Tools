public class J5_4_1 {
  public static void main (String[] args) {
    for(double deg=0; deg<=360;deg+=45) {            
      System.out.println("sin(" + deg + ") = " + Math.sin(Math.toRadians(deg))); 
    }  
  }
}