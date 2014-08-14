class Mynumber {
      int n;

      n = 3;  
      System.out.println("Here is Constructor and n = " + n); 

      int getnumber() { return n; }	
}

class Ex04_5_2 {
       public static void main(String[] args) {
            Mynumber a = new Mynumber();
            System.out.println("Here is main and a.getnumber() = "+a.getnumber());
       }
}
