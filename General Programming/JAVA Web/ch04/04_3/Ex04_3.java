class Number {
     int n = 0; 
	
     void addN(int i) { n = i; } 
     int  getN() { return n; } 
}

class Ex04_3 {	
     public static void main(String[] args) {
          Number N = new Number( );

          N.addN(10);
          System.out.println("使用方法程序 N.getN: "+ N.getN( ));

          N.n = 20;
          System.out.println("使用變數 N.n: "+ N.n);
      }
}
