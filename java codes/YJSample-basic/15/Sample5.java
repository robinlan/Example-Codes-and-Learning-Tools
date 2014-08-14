class Car extends Thread
{
   private String name;

   public Car(String nm)
   {
      name = nm;
   }
   public void run()
   {
      for(int i=0; i<5; i++){
         System.out.println("正在進行" + name + "的處理工作。");
      } 
   }
}

class Sample5
{
   public static void main(String[] args)
   {
      Car car1 = new Car("1號車");
      car1.start();

      try{
         car1.join();
      }
      catch(InterruptedException e){}

      System.out.println("結束main()的處理工作。");
   }
}