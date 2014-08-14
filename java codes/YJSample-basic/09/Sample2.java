// ¨®¤lÃþ§O
class Car
{
   private int num;
   private double gas;
   public void setNumGas(int n, double g)
   {
      if(g > 0 && g < 1000){
         num = n;
         gas = g;
         System.out.println("±N¨®¸¹³]¬°" + num + "¡A¨Tªo¶q³]¬°" + gas + "¡C");
       }
       else{
         System.out.println(g + "¤£¬O¥¿½Tªº¨Tªo¶q¡C");
         System.out.println("µLªkÅÜ§ó¨Tªo¶q¡C");
       }
   }
   public void show()
   {
      System.out.println("¨®¸¹¬O" + num + "¡C");
      System.out.println("¨Tªo¶q¬O" + gas + "¡C");
   }
}

class Sample2
{
   public static void main(String[] args)
   {
      Car car1 = new Car();

      // µLªk¶i¦æ³o¼Ëªº¦s¨ú‚È‚è‚Ü‚·B
      //car1.num = 1234;
      //car1.gas = -10.0;

      car1.setNumGas(1234, 20.5);
      car1.show();

      System.out.println("«ü©w¤£¥¿½Tªº¨Tªo¶q(-10.0)¬Ý¬Ý...¡C");

      car1.setNumGas(1234, -10.0);
      car1.show();
   }
}
