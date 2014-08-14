class adder{
      int add(int a,int b){return a+b;}
      double add(double i,double j){return i+j;}
      int add(int p, int q, int r) {return p+q+r;}
}

class Ex04_6_1{
      public static void main(String[] args){
         int c;
         double k;
         int s;

         adder ad= new adder();

         c=ad.add(1, 2);
         System.out.println("int c = " + c);

         k=ad.add(3.3, 4.4);
         System.out.println("double k = " + k);

         s=ad.add(5, 6, 7);
         System.out.println("int s = " + s);
       }
}      