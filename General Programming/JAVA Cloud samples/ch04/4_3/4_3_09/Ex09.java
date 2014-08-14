class Mynumber { 
      int i; 
} 
 
class Ex09 { 
      public static void main(String[] args) { 
             int j;
             Mynumber a;

             j=5;   
                  
             a = new Mynumber();  
             a.i=10;

             System.out.println("j = "+j);
             System.out.println("a.i = "+a.i);
       }
}