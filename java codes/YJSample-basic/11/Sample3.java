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
   public void show()
   {
      System.out.println("車號是" + num + "。");
      System.out.println("汽油量是" + gas + "。");
   }
}

// 賽車類別
class RacingCar extends Car
{
   private int course;
   
   public RacingCar()
   {
      course = 0;
      System.out.println("生產了賽車。");
   }
   public void setCourse(int c)
   {
      course = c;
      System.out.println("將賽車編號設為" + course + "。");
   }
   public void newShow()
   {
      System.out.println("賽車的車號是" + num + "。");
      System.out.println("汽油量是" + gas + "。");
      System.out.println("賽車編號是" + course + "。");
   }
}

class Sample3
{
   public static void main(String[] args)
   {
      RacingCar rccar1;
      rccar1 = new RacingCar();

      rccar1.newShow();
   }
}
