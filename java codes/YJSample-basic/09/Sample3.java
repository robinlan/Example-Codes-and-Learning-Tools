// 車子類別
class Car
{
   private int num;
   private double gas;

   public void setCar(int n)
   {
      num = n;
      System.out.println("將車號設為" + num + "。");
   }
   public void setCar(double g)
   {
      gas = g;
      System.out.println("將汽油量設為" + gas + "。");
   }
   public void setCar(int n, double g)
   {
      num = n;
      gas = g;
      System.out.println("將車號設為" + num + "，汽油量設為" + gas + "。");
   }

   public void show()
   {
      System.out.println("車號是" + num + "。");
      System.out.println("汽油量是" + gas + "。");
   }
}

class Sample3
{
   public static void main(String[] args)
   {
      Car car1 = new Car();

      car1.setCar(1234, 20.5);
      car1.show();

      System.out.println("只變更車號。");
      car1.setCar(2345);
      car1.show();

      System.out.println("只變更汽油量。");
      car1.setCar(30.5);
      car1.show();
   }
}
