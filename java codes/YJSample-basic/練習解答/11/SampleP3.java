class A
{
   A()
   {
      System.out.println("硂琌A礚把计篶Α");
   }
   A(int a)
   {
      System.out.println("硂琌A虫把计篶Α");
   }
}
class B extends A
{
   B()
   {
      System.out.println("硂琌B礚把计篶Α");
   }
   B(int b)
   {
      super(b);
      System.out.println("硂琌B虫把计篶Α");
   }
}

class SampleP3
{
   public static void main(String[] args)
   {
      B b1 = new B();
      B b2 = new B(10);
   }
}