class MyClass {
      class memb1 {
            int i;
            void seti() {i = 10;}
            int geti() {return i;}
      }
      class memb2 {
            int i;
            void seti() {i = 20;}
            int geti() {return i;}
      }
}

class Ex04_9_1_1 {
      public static void main(String[] args) {
             MyClass M = new MyClass();

             MyClass.memb1 m1 = M.new memb1();
             m1.seti( );
             System.out.println("m1.geti : "+ m1.geti());

             MyClass.memb2 m2 = M.new memb2(); 
             m2.seti( );          
             System.out.println("m2.geti : "+ m2.geti());
      }
}
