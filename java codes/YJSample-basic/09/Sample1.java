// 車子類別
class Car
{
   int num;
   double gas;

   void show()
   {
      System.out.println("車號是" + num + "。");
      System.out.println("汽油量是" + gas + "。");
   }
}

class Sample1
{
   public static void main(String[] args)
   {
      Car car1 = new Car();

      car1.num = 1234;
      car1.gas = 20.5;

      car1.show();
   }
}
