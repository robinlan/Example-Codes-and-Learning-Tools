class A
{
   A()
   {
      System.out.println("這是沒有參數的建構式。");
   }
   A(int a)
   {
      this();
      System.out.println("這是一個參數的建構式。");
   }
}
class SampleP4
{
   public static void main(String[] args)
   {
      A a1 = new A();
      A a2 = new A(10);
   }
}