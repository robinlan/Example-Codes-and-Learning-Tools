class Number {
     static int n = 0; 
	
     static void addN(int i) { n = i; } 
     static int getN() { return n; } 
}

class Ex04_4 {	
     public static void main(String[] args) {
          Number.addN(10);
          System.out.println("Number.getN: " + Number.getN( ));

          Number.n = 20;
          System.out.println("Number.n: " + Number.n);
      }
}
