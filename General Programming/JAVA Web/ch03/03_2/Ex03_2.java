class Mynumber { 
      int i; 
} 
 
class Ex03_2 { 
      public static void main(String[] args) { 
             int j;
             Mynumber a;

             j=3;   
                  
             a = new Mynumber();  
             a.i=5;

             System.out.println("j = "+j);
             System.out.println("a.i = "+a.i);
       }
}