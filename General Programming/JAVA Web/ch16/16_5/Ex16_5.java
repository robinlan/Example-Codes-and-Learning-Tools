import java.util.*;

class Ex16_5 {
   public static void main(String[] args) {
      Hashtable ht = new Hashtable();
      ht.put("Server", new String("163.15.40.242"));
      ht.put("Client1", new String("163.15.40.243"));
      ht.put("Client2", new String("163.15.40.244"));

      Enumeration enValue = ht.elements();
      while(enValue.hasMoreElements()) {
         String sValue = (String) enValue.nextElement();
         System.out.println("enValue = " + sValue);
      }

      Enumeration enKey = ht.keys();
      while(enKey.hasMoreElements()) {
         String sKey = (String) enKey.nextElement();
         System.out.println("enKey = " + sKey);
      }
   }
}
