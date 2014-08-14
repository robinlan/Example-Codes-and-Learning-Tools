class adder{
      int c;
      double k;

      adder(int a, int b) {
          c= a+b;
          System.out.println("int c = "+ c);
      }

      adder(double i, double j){
           k= i+j;
           System.out.println("double k = "+ k);
      }
 }

class Ex04_6_2{
      public static void main(String[] args){
           adder add_int = new adder(2, 3);
           adder add_double = new adder(2.2, 3.3);
      }
 }