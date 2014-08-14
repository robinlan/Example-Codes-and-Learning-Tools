// 車子類別
class Car
{
   int num;
   double gas;

   public Car()
   {
      num = 0;
      gas = 0.0;
      System.out.println("生產了車子。");
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

class Sample9
{
   public static void main(String[] args)
   {
      Car[] cars;
      cars = new Car[3];

      for(int i=0; i<cars.length; i++){
         cars[i] = new Car();
      }

      cars[0].setCar(1234, 20.5);
      cars[1].setCar(2345, 30.5);
      cars[2].setCar(3456, 40.5);

      for(int i=0; i< cars.length; i++){
         cars[i].show();
      }
   }
}
