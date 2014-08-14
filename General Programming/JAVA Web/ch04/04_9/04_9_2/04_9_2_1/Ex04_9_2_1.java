class Ex04_9_2_1 {
      int i;
	
      public Ex04_9_2_1() { 
             class MyLocal { 
                   int j;
                   public MyLocal(){
                          i = 5; 
                          j= 10;
                   }
             }
             MyLocal L = new MyLocal();
				
      }

      public static void main(String[] args) {
             Ex04_9_2_1 M = new Ex04_9_2_1();
             System.out.println("j of Local: "+L.j);
             System.out.println("i of Ex04_9_2_1: "+ M.i);
      }
}
