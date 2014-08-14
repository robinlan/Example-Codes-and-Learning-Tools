class Mynumber {
     private int n;

     public Mynumber(int i){this.n= i;}
     public Mynumber(int j, String s){
          this(j);
          System.out.println("Here is a Overload Constructor");
     }

     public int getnumber(){return n;} 
}

class Ex04_8_2_3 {
     public static void main(String[] args) {
         Mynumber M = new Mynumber(10, "abcde");
         System.out.println("M.getnumber : " + M.getnumber());	
     }
}
