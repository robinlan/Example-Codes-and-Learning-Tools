// 車子類別
class Car
{
   protected int num;
   protected double gas;
   
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
   public String toString()
   {
      String str = "車號：" + num + ";汽油量：" + gas;
      return str;
   }
}

class Sample7
{
   public static void main(String[] args)
   {
      Car car1 = new Car();
      car1.setCar(1234, 20.5);

      System.out.println(car1); 
   }
}
