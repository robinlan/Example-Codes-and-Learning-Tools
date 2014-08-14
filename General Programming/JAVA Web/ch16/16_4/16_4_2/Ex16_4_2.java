import java.util.*;

class Ex16_4_2 {
   public static void main(String[] args) {
      Hashtable ht = new Hashtable();
      ht.put("Server", new String("163.15.40.242"));
      ht.put("Client1", new String("163.15.40.243"));
      ht.put("Client2", new String("163.15.40.244"));

      System.out.println("ht.isEmpty() : " + ht.isEmpty());
      System.out.println("ht.size() : " + ht.size());
      System.out.println("ht.contains() : " + ht.contains(new String("163.15.40.242")));
      System.out.println("ht.containsKey() : " + ht.containsKey("Server"));
      System.out.println("ht.get() : " + ht.get("Server"));
   }
}
