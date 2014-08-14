class Ex04_9_1_3 {
      int i,j;
      private MyMember memb;

      class MyMember {
            int m,n,o;
            MyMember() { 
                i = 2;  
                Ex04_9_1_3.this.j = 4; 

                m = 12;
                this.n = 14; 
                MyMember.this.o = 16;  
             }
             public int getO() { return o; }
       }

       public Ex04_9_1_3() {
              memb = new MyMember();
              System.out.println("m= "+memb.m + ","+
                                 "n= "+memb.n + ","+
                                 "o= "+memb.getO());
       }

       public int getJ() { return j; }

       public static void main(String[] args) {
              Ex04_9_1_3 a = new Ex04_9_1_3();
              System.out.println("i= "+a.i +","+
                                 "j= "+a.getJ());
       }
}
