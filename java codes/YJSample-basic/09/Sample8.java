// 車子類別
class Car
{
   public static int sum = 0;

   private int num;
   private double gas;

   public Car()
   {
      num = 0;
      gas = 0.0;
      sum++;
      System.out.println("生產了車子。");
   }
   public void setCar(int n, double g)
   {
      num = n;
      gas = g;
      System.out.println("將車號設為" + num + "，汽油量設為" + gas + "。");
   }
   public static void showSum()
   {
      System.out.println("車子總共有" + sum + "台。");
   }
   public void show()
   {
      System.out.println("車號是" + num + "。");
      System.out.println("汽油量是" + gas + "。");
   }
}

class Sample8
{
   public static void main(String[] args)
   {
      Car.showSum();

      Car car1 = new Car();
      car1.setCar(1234, 20.5);

      Car.showSum();

      Car car2 = new Car();
      car2.setCar(4567, 30.5);

      Car.showSum();
   }
}
