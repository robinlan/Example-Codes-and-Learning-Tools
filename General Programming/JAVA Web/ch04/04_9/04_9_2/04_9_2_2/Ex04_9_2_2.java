class Ex04_9_2_2 {
      int i;
	
      public Ex04_9_2_2() { 
             class MyLocal { 
                   int j;
                   public MyLocal(){
                          i = 5; 
                          j= 10;
                   }
             }
             MyLocal L = new MyLocal();
             System.out.println("j of Local: "+L.j);				
      }

      public static void main(String[] args) {
             Ex04_9_2_2 M = new Ex04_9_2_2();
             System.out.println("i of Ex04_9_2_2: "+ M.i);
      }
}
